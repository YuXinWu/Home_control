package top.spiiiiiiiiiitter.home_control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {

    //获取账号密码的String
    private String usernameGet;
    private String passwordGet;

    private EditText Username;
    private Button Btn_Login;
    private EditText Password;

    private UserDataHandle userDataHandle;//定义数据库操作类

    private String Login_Path="http://maker.tobeh.xin/signin";

    @Override
    protected void onDestroy(){
        super.onDestroy();
        userDataHandle.closeDatabase();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        //初始化控件
        Btn_Login=(Button) findViewById(R.id.loginButton);
        Username=(EditText) findViewById(R.id.usernameEditview);
        Password=(EditText) findViewById(R.id.passwordEditview);

        //初始化数据库操作函数
        userDataHandle=new UserDataHandle(this);

        //添加按键事件
        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    public void run() {
                        // TODO
                        // 在这里进行 http request.网络请求相关操作
                        usernameGet=Username.getText().toString();
                        passwordGet=Password.getText().toString();
                        //向服务器发送登陆信息
                        try {
                            URL url = new URL(Login_Path);
                            //封装JSON数据
                            JSONObject Login_Package = new JSONObject();
                            Login_Package.put("TAG", "signin");
                            Login_Package.put("USERNAME", usernameGet);
                            Login_Package.put("PASSWORD", passwordGet);
                            //转换成String类型使用输出流向服务器写
                            String content = String.valueOf(Login_Package);
                            System.out.println(content);
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
//                            os.close();
                            //获得接受码，即服务器接收到了数据
                            int code = conn.getResponseCode();
                            System.out.println(code);
                            if (code == 200) {
                                //获得输入流，即服务器端的json
                                InputStream is = conn.getInputStream();
                                //下面的json就已经是{"TAG":"cmd","AUTH":"true/false",}这个形式了,只不过是String类型
                                String json_get = readTextFromSDcard(is);
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

                        ////////////////////////////////////////////
                        //Message msg = new Message();
                        //Bundle data = new Bundle();
                        //data.putString("value", "请求结果");
                        //msg.setData(data);
                        //handler.sendMessage(msg);
                    }
                }.start();
            }
        });

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //将obj类型强制转换
            JSONObject jsonObject=(JSONObject)msg.obj;
            // TODO
            // UI界面的更新等相关操作
            //UI实现部分给主线程做
            try {
                if(userDataHandle.find("sjzb")){
                    System.out.println("---------------------");
                    Toast.makeText(getApplicationContext(), "您已经登陆过了", Toast.LENGTH_SHORT).show();
                }else{
                    if(jsonObject.getBoolean("AUTH")){
                        //将当前的账户密码存入数据库之中
                        userDataHandle.insert(usernameGet,passwordGet);
                        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "登陆失败，用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    private String readTextFromSDcard(InputStream is) throws Exception {
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



