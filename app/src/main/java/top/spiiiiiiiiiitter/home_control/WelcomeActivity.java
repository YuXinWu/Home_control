package top.spiiiiiiiiiitter.home_control;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {

    private String UUID;
    private SharedPreferences sp;
    private boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome);

        //设置为无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置为全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        //获取组件
        RelativeLayout r1_splash = (RelativeLayout)findViewById(R.id.r1_splash);

        //背景透明度变化3秒内从0.1变到1.0
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(3000);
        r1_splash.startAnimation(aa);

        //创建Timer对象
        Timer timer = new Timer();
        //创建TimerTask对象
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        //使用timer.schedule（）方法调用timerTask，定时3秒后执行run
        timer.schedule(timerTask, 3000);
        System.out.println("11111111111111111111111");
        //第一次开始
        sp=getSharedPreferences("uuid&start",MODE_PRIVATE);
        first=sp.getBoolean("firstStart",true);
        if(first){
            UUID=java.util.UUID.randomUUID().toString();
            UUID=UUID.replace("-","");
            System.out.println(UUID);
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("firstStart",false);
            editor.putString("uuid",UUID);
            editor.commit();
        }

    }
}
