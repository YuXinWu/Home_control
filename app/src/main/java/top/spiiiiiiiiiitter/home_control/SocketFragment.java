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
public class SocketFragment extends Fragment {

    private Switch socket_switch;

    private SharedPreferences sp;
    private String UUID;

    private InternetRequest socketRequest=new InternetRequest();

    public SocketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_socket, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp=getActivity().getSharedPreferences("uuid&start",MODE_PRIVATE);
        UUID=sp.getString("uuid","");

        socket_switch=(Switch)getActivity().findViewById(R.id.socketswitch);

        socket_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    socketRequest.sendCmdThread(UUID,"7","CZ","01","1111");
                }else{
                    socketRequest.sendCmdThread(UUID,"7","CZ","01","0000");
                }
            }
        });
    }


}
