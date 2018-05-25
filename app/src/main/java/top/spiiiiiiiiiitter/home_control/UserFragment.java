package top.spiiiiiiiiiitter.home_control;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * 本类中是在Fragment中的两个按钮，一个是登陆按钮，会跳到另一个activity；
 * 另一个是退出按钮，跳出一个dialog，显示是否退出，退出把数据库中的信息删除，下次手机登陆
 * 就不会默认是上次登陆的账号
 */
public class UserFragment extends Fragment {

    private UserDataHandle userDataHandle;//定义数据库操作类
    private AlertDialog dialog;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //初始化数据库操作函数
        userDataHandle=new UserDataHandle(getActivity());

        Button Btn_Login=(Button) getActivity().findViewById(R.id.loginButton);
        Button Btn_Logout=(Button) getActivity().findViewById(R.id.logoutButton);

        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });

        Btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userDataHandle.find("sjzb")){
                    dialog=new AlertDialog.Builder(getActivity())
                            .setTitle("退出")
                            .setMessage("是否退出？")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    userDataHandle.delete("sjzb");
                                    Toast.makeText(getActivity().getApplicationContext(), "退出成功！", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create();
                    dialog.show();
                }else{
                    dialog=new AlertDialog.Builder(getActivity())
                            .setTitle("Tip")
                            .setMessage("您还未登录，是否登录？")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消",null)
                            .create();
                    dialog.show();
                }
            }
        });
    }

}
