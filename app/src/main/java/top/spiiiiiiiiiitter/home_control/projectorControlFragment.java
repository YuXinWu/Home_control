package top.spiiiiiiiiiitter.home_control;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class projectorControlFragment extends Fragment implements View.OnClickListener {

    private Switch projector_power;
    private Button projector_up;
    private Button projector_down;
    private Button projector_left;
    private Button projector_right;
    private Button projector_ok;
    private Button projector_menu;
    private Button projector_zoom_in;
    private Button projector_zoom_out;
    private Button projector_signal;


    private SharedPreferences sp;
    private String UUID;

    private InternetRequest projectorRequest=new InternetRequest();

    public projectorControlFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_projector_control, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp=getActivity().getSharedPreferences("uuid&start",MODE_PRIVATE);
        UUID=sp.getString("uuid","");

        projector_power=getActivity().findViewById(R.id.projector_power);
        projector_up=getActivity().findViewById(R.id.projector_up);
        projector_down=getActivity().findViewById(R.id.projector_down);
        projector_left=getActivity().findViewById(R.id.projector_left);
        projector_right=getActivity().findViewById(R.id.projector_right);
        projector_ok=getActivity().findViewById(R.id.projector_ok);
        projector_menu=getActivity().findViewById(R.id.projector_menu);
        projector_zoom_in=getActivity().findViewById(R.id.projector_zoom_in);
        projector_zoom_out=getActivity().findViewById(R.id.projector_zoom_out);
        projector_signal=getActivity().findViewById(R.id.projector_signal);

        projector_power.setOnClickListener(this);
        projector_up.setOnClickListener(this);
        projector_down.setOnClickListener(this);
        projector_left.setOnClickListener(this);
        projector_right.setOnClickListener(this);
        projector_ok.setOnClickListener(this);
        projector_menu.setOnClickListener(this);
        projector_zoom_in.setOnClickListener(this);
        projector_zoom_out.setOnClickListener(this);
        projector_signal.setOnClickListener(this);
        projector_power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        //发送开命令
                        projectorRequest.sendCmdThread(UUID,"4","YK","01","0100");
                    } else {
                        //发送关命令
                        projectorRequest.sendCmdThread(UUID,"4","YK","01","0100");

                    }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.projector_up:
                break;
            case R.id.projector_down:
                break;
            case R.id.projector_left:
                break;
            case R.id.projector_right:
                break;
            case R.id.projector_ok:
                break;
            case R.id.projector_menu:
                break;
            case R.id.projector_zoom_out:
                break;
            case R.id.projector_zoom_in:
                break;
            case R.id.projector_signal:
                projectorRequest.sendCmdThread(UUID,"5","YK","01","0101");
                break;
            default:
                break;
        }
    }


}
