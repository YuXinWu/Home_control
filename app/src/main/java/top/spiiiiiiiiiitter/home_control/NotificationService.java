package top.spiiiiiiiiiitter.home_control;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationService extends Service {
    private String Gas_Path="https://maker.tobeh.xin/data/YW01";

    private boolean isRun;// 线程是否继续的标志
    private Handler handler1; // 显示当前时间线程消息处理器。
    //private Handler testHandler;

    public NotificationService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRun = true;
        new Thread(new Runnable() {
            @Override
            // 在Runnable中，如果要让线程自己一直跑下去，必须自己定义while结构
            // 如果这个run()方法读完了，则整个线程自然死亡
            public void run() {
                // 定义一个线程中止标志
                while (isRun) {
                    try {
                        startThread(Gas_Path);
                        Thread.sleep(10000);// Java中线程的休眠，必须在try-catch结构中，每2s秒运行一次的意思
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isRun) {
                        break;
                    }
                }
            }
        }).start();// 默认线程不启动，必须自己start()

        /*
        testHandler = new Handler(new Handler.Callback() {// 这样写，就不弹出什么泄漏的警告了
            @Override
            public boolean handleMessage(Message msg) {
                System.out.println("11111111111");
                notification();
                return false;
            }
        });
        */
        // 不停在接受线程的消息，根据消息的参数，进行处理 ，这里没有传递过来的参数
        handler1 = new Handler(new Handler.Callback() {// 这样写，就不弹出什么泄漏的警告了
            @Override
            public boolean handleMessage(Message msg) {
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
                            if(sensor_type.equals("YW")){
                                // 安卓显示当前时间的方法
                                String value = jsonObject_data.getString("value");
                                if(value.equals("0000")) {
                                    Toast.makeText(getApplicationContext(),
                                            "警告！烟雾超标！！！", Toast.LENGTH_SHORT)
                                            .show();
                                    notification();
                                }
                            }
                        }
                    } else {
                        if (jsonObject.getBoolean("AUTH")) {
                            Toast.makeText(getApplicationContext(), "命令成功发送！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "命令发送失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        return START_STICKY;// 这个返回值其实并没有什么卵用，除此以外还有START_NOT_STICKY与START_REDELIVER_INTENT

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
                        handler1.sendMessage(msg);
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


    private void notification() {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);// 注册通知管理器
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("我是有铃声+震动+呼吸灯效果的通知")
                .setContentText("我是最棒的~")
                .setDefaults(Notification.DEFAULT_ALL);
        notificationManager.notify(0, builder.build());
    }
}
