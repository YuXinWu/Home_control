package top.spiiiiiiiiiitter.home_control;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class lightControlFragment extends Fragment{

    private Switch light_switch1;
    private Switch light_switch2;

    private SharedPreferences sp;
    private String UUID;

    private InternetRequest lightRequest=new InternetRequest();

    public lightControlFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_light_control, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp=getActivity().getSharedPreferences("uuid&start",MODE_PRIVATE);
        UUID=sp.getString("uuid","");

        light_switch1=(Switch) getActivity().findViewById(R.id.light_switch1);
        light_switch2=(Switch) getActivity().findViewById(R.id.light_switch2);

        setLight_switchListener(light_switch1,light_switch2);
    }

    //控件监听函数
    public void setLight_switchListener(Switch light_switch1,Switch light_switch2){

        light_switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    //发送开命令
                    lightRequest.sendCmdThread(UUID,"0","DG","01","1111");
                }else {
                    //发送关命令
                    lightRequest.sendCmdThread(UUID,"0","DG","01","0000");
                }
            }
        });
        light_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    //发送开命令
                    lightRequest.sendCmdThread(UUID,"1","DG","02","1111");
                }else {
                    //发送关命令
                    lightRequest.sendCmdThread(UUID,"1","DG","02","0000");
                }
            }
        });
    }

}
