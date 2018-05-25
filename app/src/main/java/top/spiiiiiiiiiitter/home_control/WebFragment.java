package top.spiiiiiiiiiitter.home_control;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {

    private TextView temperature_web_set;
    private TextView humidity_web_set;
    private TextView soil_humidity_web_set;

    private String temperature_humidity_path = "https://maker.tobeh.xin/data/WS01";
    private String soil_humidity_path = "https://maker.tobeh.xin/data/TR01";

    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        temperature_web_set = (TextView) getActivity().findViewById(R.id.soil_temperature_web);
        humidity_web_set = (TextView) getActivity().findViewById(R.id.humidity_web);
        soil_humidity_web_set = (TextView) getActivity().findViewById(R.id.soil_humidity_web);

        startThread(temperature_humidity_path);
        startThread(soil_humidity_path);
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            //Fragment隐藏时调用
            startThread(temperature_humidity_path);
            startThread(soil_humidity_path);
        }else {
            //Fragment显示时调用

        }

    }

    private void startThread(final String path) {
        new Thread() {
            private String Path = path;

            public void run() {
                //写请求网络的代码
                try {
                    URL url = new URL(Path);
                    //封装JSON数据
                    JSONObject Data_Package = new JSONObject();
                    Data_Package.put("TAG", "data");
                    Data_Package.put("USERNAME", "sjzb");
                    Data_Package.put("PASSWORD", "1024");
                    //转换成String类型使用输出流向服务器写
                    String content = String.valueOf(Data_Package);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    //设置允许输出输入
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //设置POST方式
                    conn.setRequestMethod("POST");
                    // 设置contentType
                    conn.setRequestProperty("Content-Type", "application/json");
                    //链接conn
                    conn.connect();
                    OutputStream os = conn.getOutputStream();
                    os.write(content.getBytes());
                    os.flush();
                    //获得接受码如果是200，表明成功链接
                    int code = conn.getResponseCode();
                    System.out.println(code);
                    if (code == 200) {
                        //获得输入流，即服务器端的json
                        InputStream is = conn.getInputStream();
                        String json_get = readTextFromSDcard(is);
                        System.out.println(json_get);
                        //然后我们把json转换成JSONObject类型得到{"Person"://{"username":"zhangsan","age":"12"}}
                        JSONObject jsonObject = new JSONObject(json_get);
                        Message msg = new Message();
                        //将json格式传给obj
                        msg.obj = jsonObject;
                        //传给handler类
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();//把读取的数据返回
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //将obj类型强制转换
            JSONObject jsonObject = (JSONObject) msg.obj;
            // TODO
            // UI界面的更新等相关操作
            //UI实现部分给主线程做
            try {
                //tag为TAG的判据
                String tag = jsonObject.getString("TAG");
                //只有收到tag:data，auth:true才执行
                //如下思路，switch语句作为选择，里面是解析的json数据的类型字段，根据不同类型调用不同函数
                //返回tag是data才解析数据
                if (tag.equals("data")) {
                    if (jsonObject.getBoolean("AUTH")) {
                        //得到数据里面的DATA的包
                        JSONObject jsonObject_data = jsonObject.getJSONObject("DATA");
                        String sensor_type = jsonObject_data.getString("stype");
                        switch (sensor_type) {
                            case "WS": //温湿度
                                String Temperature_Humidity_Value = jsonObject_data.getString("value");
                                setTemperature_Humidity(Temperature_Humidity_Value);
                                break;
                            case "TR": //土壤
                                String Soil_Humidity_Value = jsonObject_data.getString("value");
                                setSoilHumidity(Soil_Humidity_Value);
                            default:
                                break;
                        }
                    }
                } else {
                    if (jsonObject.getBoolean("AUTH")) {
                        Toast.makeText(getActivity().getApplicationContext(), "命令成功发送！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "命令发送失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private void setTemperature_Humidity(String Temperature_Humidity_Value){
        //获取子字符串
        String temperatureValue=Temperature_Humidity_Value.substring(0,2);
        String humidityValue=Temperature_Humidity_Value.substring(2,4);
        temperature_web_set.setText(temperatureValue);
        humidity_web_set.setText(humidityValue);
    }

    private void setSoilHumidity(String Soil_Humidity_Value){
        //获取子字符串
        String zhengshu_value=Soil_Humidity_Value.substring(1,3);
        String xiaoshu_value=Soil_Humidity_Value.substring(3,4);
        String value=zhengshu_value+"."+xiaoshu_value;
        soil_humidity_web_set.setText(value);
    }
}