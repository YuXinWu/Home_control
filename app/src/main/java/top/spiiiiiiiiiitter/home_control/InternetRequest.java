package top.spiiiiiiiiiitter.home_control;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 15173 on 2018/5/12.
 */

public class InternetRequest {

    private String cmd_path;
    private String cmd_back_path;

    InternetRequest(){
        cmd_path="https://maker.tobeh.xin/cmd";
        cmd_back_path="https://maker.tobeh.xin/cmdback";
    }

    //该函数用来发送命令
    /**
     * @param uuid UUID(设备唯一识别号)
     * @param serial 命令序列
     * @param stype 设备类型
     * @param num 设备编号
     * @param cmd_state 设备状态
     */
    public void sendCmdThread(final String uuid,
                              final String serial,
                              final String stype,
                              final String num,
                              final String cmd_state) {
        new Thread(){
            private String UUID=uuid;
            private String Serial=serial;
            private String Stype=stype;
            private String Num=num;
            private String Cmd_state=cmd_state;

            public void run(){
                //写请求网络的代码
                try {
                    URL ur2=new URL(cmd_path);
                    System.out.println(cmd_path);
                    //封装JSON数据
                    JSONObject Data_Package=new JSONObject();
                    JSONObject CMD_Package=new JSONObject();
                    CMD_Package.put("head","*");
                    CMD_Package.put("netAddr","");
                    CMD_Package.put("ftype","C");
                    CMD_Package.put("mac",UUID);
                    CMD_Package.put("serial",Serial);
                    CMD_Package.put("reserve","*");
                    CMD_Package.put("stype",Stype);
                    CMD_Package.put("num",Num);
                    CMD_Package.put("value",Cmd_state);

                    Data_Package.put("TAG","cmd");
                    Data_Package.put("USERNAME","sjzb");
                    Data_Package.put("PASSWORD","1024");
                    Data_Package.put("CMD",CMD_Package);
                    //转换成String类型使用输出流向服务器写
                    String content=String.valueOf(Data_Package);
                    System.out.println(content);
                    HttpURLConnection conn_cmd = (HttpURLConnection)ur2.openConnection();
                    conn_cmd.setConnectTimeout(30000);
                    conn_cmd.setReadTimeout(30000);
                    //设置允许输出输入
                    conn_cmd.setDoOutput(true);
                    conn_cmd.setDoInput(true);
                    //设置POST方式
                    conn_cmd.setRequestMethod("POST");
                    // 设置contentType
                    conn_cmd.setRequestProperty("Content-Type","application/json");
                    //链接conn
                    conn_cmd.connect();
                    OutputStream os = conn_cmd.getOutputStream();
                    os.write(content.getBytes());
                    os.flush();
                    //获得接受码如果是200，表明成功链接
                    int code = conn_cmd.getResponseCode();
                    System.out.println(code);
                    if (code == 200){
                        //获得输入流，即服务器端的json
                        InputStream is = conn_cmd.getInputStream();
                        String json_get=readTextFromSDcard(is);
                        System.out.println(json_get);
                        //然后我们把json转换成JSONObject类型
                        JSONObject jsonObject = new JSONObject(json_get);
                        //如果此处返回auth的值为真，执行cmdback线程
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("在这里错误！");
                }
            }
        }.start();
    }

    //该函数用来发送命令返回
    public void sendCmdBackThread(final String cmd_state,final String cmd_Path){
        new Thread(){
            private String Cmd_state=cmd_state;
            private String Cmd_Path=cmd_Path;
            public void run(){
                //写请求网络的代码
                try {
                    URL ur2=new URL(Cmd_Path);
                    System.out.println(Cmd_Path);
                    //封装JSON数据
                    JSONObject Data_Package=new JSONObject();
                    Data_Package.put("TAG","cmd");
                    Data_Package.put("USERNAME","sjzb");
                    Data_Package.put("PASSWORD","1024");
                    Data_Package.put("CMD","KL1"+Cmd_state);
                    //转换成String类型使用输出流向服务器写
                    String content=String.valueOf(Data_Package);
                    System.out.println(content);
                    HttpURLConnection conn_cmd = (HttpURLConnection)ur2.openConnection();
                    conn_cmd.setConnectTimeout(30000);
                    conn_cmd.setReadTimeout(30000);
                    //设置允许输出输入
                    conn_cmd.setDoOutput(true);
                    conn_cmd.setDoInput(true);
                    //设置POST方式
                    conn_cmd.setRequestMethod("POST");
                    // 设置contentType
                    conn_cmd.setRequestProperty("Content-Type","application/json");
                    //链接conn
                    conn_cmd.connect();
                    OutputStream os = conn_cmd.getOutputStream();
                    os.write(content.getBytes());
                    os.flush();
                    //获得接受码如果是200，表明成功链接
                    int code = conn_cmd.getResponseCode();
                    System.out.println(code);
                    if (code == 200){
                        //获得输入流，即服务器端的json
                        InputStream is = conn_cmd.getInputStream();
                        String json_get=readTextFromSDcard(is);
                        System.out.println(json_get);
                        //然后我们把json转换成JSONObject类型
                        JSONObject jsonObject = new JSONObject(json_get);
                        //如果此处返回auth的值为真，执行cmdback线程
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is,"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();//把读取的数据返回
    }

}
