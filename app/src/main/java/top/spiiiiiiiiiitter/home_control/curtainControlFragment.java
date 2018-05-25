package top.spiiiiiiiiiitter.home_control;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class curtainControlFragment extends Fragment implements View.OnClickListener{

    private Button curtain1_on;
    private Button curtain1_off;
    private Button curtain1_stop;
    private Button curtain2_on;
    private Button curtain2_off;
    private Button curtain2_stop;

    private SharedPreferences sp;
    private String UUID;
    private InternetRequest curtainRequest=new InternetRequest();

    public curtainControlFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curtain_control, container, false);
    }

    //
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp=getActivity().getSharedPreferences("uuid&start",MODE_PRIVATE);
        UUID=sp.getString("uuid","");

        curtain1_on=getActivity().findViewById(R.id.ct1_on);
        curtain1_off=getActivity().findViewById(R.id.ct1_off);
        curtain1_stop=getActivity().findViewById(R.id.ct1_stop);
        curtain2_on=getActivity().findViewById(R.id.ct2_on);
        curtain2_off=getActivity().findViewById(R.id.ct2_off);
        curtain2_stop=getActivity().findViewById(R.id.ct2_stop);

        curtain1_on.setOnClickListener(this);
        curtain1_off.setOnClickListener(this);
        curtain1_stop.setOnClickListener(this);
        curtain2_on.setOnClickListener(this);
        curtain2_off.setOnClickListener(this);
        curtain2_stop.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ct1_on:
                curtainRequest.sendCmdThread(UUID,"2","CL","01","**on");
                break;
            case R.id.ct1_off:
                curtainRequest.sendCmdThread(UUID,"2","CL","01","*off");
                break;
            case R.id.ct1_stop:
                curtainRequest.sendCmdThread(UUID,"2","CL","01","stop");
                break;
            case R.id.ct2_on:
                curtainRequest.sendCmdThread(UUID,"3","CL","02","**on");
                break;
            case R.id.ct2_off:
                curtainRequest.sendCmdThread(UUID,"3","CL","02","*off");
                break;
            case R.id.ct2_stop:
                curtainRequest.sendCmdThread(UUID,"3","CL","02","stop");
                break;
                default:
                    break;
        }
    }
}
