package top.spiiiiiiiiiitter.home_control;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaterDispensersFragment extends Fragment {

    private Switch waterdispensers_switch;

    private SharedPreferences sp;
    private String UUID;

    private InternetRequest waterdispensersRequest=new InternetRequest();

    public WaterDispensersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_water_dispensers, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp=getActivity().getSharedPreferences("uuid&start",MODE_PRIVATE);
        UUID=sp.getString("uuid","");

        waterdispensers_switch=(Switch)getActivity().findViewById(R.id.waterdispensersswitch);

        waterdispensers_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    waterdispensersRequest.sendCmdThread(UUID,"8","WT","01","1111");
                }else{
                    waterdispensersRequest.sendCmdThread(UUID,"8","WT","01","0000");
                }
            }
        });
    }

}
