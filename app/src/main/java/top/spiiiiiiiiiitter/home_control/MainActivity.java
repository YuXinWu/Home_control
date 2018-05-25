package top.spiiiiiiiiiitter.home_control;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {

    private HomeFragment homefragment;
    private WebFragment webfragment;
    private UserFragment userfragment;
    private Fragment currentFragment;
    private FragmentManager mFm;

    //private TextView mTextMessage;
    //监听点击触发了哪个
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(homefragment);
                    return true;
                case R.id.navigation_web:
                    switchFragment(webfragment);
                    return true;
                case R.id.navigation_user:
                    switchFragment(userfragment);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this,NotificationService.class));
        fragmentInial();

        FragmentTransaction beginTransaction_home=mFm.beginTransaction();
        //添加一个Fragment
        beginTransaction_home.add(R.id.content,homefragment);
        beginTransaction_home.commit();
        currentFragment=homefragment;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /*
    public void onBackPressed(){
        super.onBackPressed();
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("退出")
                .setMessage("是否退出？")
                .setIcon(R.mipmap.makerlab)
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("取消",null)
                .create();
        dialog.show();
    }
    */

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle("注意")
                        .setMessage("确定要退出吗？")
                        .setIcon(R.mipmap.makerlab)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                finish();
                            }
                        })
                        .setNegativeButton("取消",null )
                        .show();
                break;
            default:
                break;
        }
        return false;
    }

    private void fragmentInial(){
        homefragment=new HomeFragment();
        webfragment=new WebFragment();
        userfragment=new UserFragment();
        currentFragment=null;
        mFm=getFragmentManager();
    }

    private void switchFragment(Fragment to){
        if(currentFragment!=to){
            FragmentTransaction begintransaction=mFm.beginTransaction();
            //to没有添加过，即第一次添加
            if(!to.isAdded()){
                    begintransaction.hide(currentFragment)
                            .add(R.id.content,to);
            }else{
                begintransaction.hide(currentFragment)
                        .show(to);
            }
            currentFragment=to;//将目标fragment置为当前fragment
            begintransaction.commitAllowingStateLoss();
        }
    }

}
