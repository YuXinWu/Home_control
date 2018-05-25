package top.spiiiiiiiiiitter.home_control;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class ACFragment extends Fragment implements View.OnClickListener{

    private Button ac_power;
    private Button ac_temperature_up;
    private Button ac_temperature_down;
    private Button ac_mode;
    private Button ac_windspeed;
    private Button ac_direction;
    private Button ac_windsweep;
    private Button ac_timing;

    private SharedPreferences sp;
    private String UUID;

    private InternetRequest acRequest=new InternetRequest();

    public ACFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ac, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sp=getActivity().getSharedPreferences("uuid&start",MODE_PRIVATE);
        UUID=sp.getString("uuid","");

        ac_power=(Button)getActivity().findViewById(R.id.ac_power);
        ac_temperature_up=(Button)getActivity().findViewById(R.id.temperature_up);
        ac_temperature_down=(Button)getActivity().findViewById(R.id.temperature_down);
        ac_mode=(Button)getActivity().findViewById(R.id.ac_mode);
        ac_windspeed=(Button)getActivity().findViewById(R.id.ac_windspeed);
        ac_direction=(Button)getActivity().findViewById(R.id.ac_direction);
        ac_windsweep=(Button)getActivity().findViewById(R.id.ac_windsweep);
        ac_timing=(Button)getActivity().findViewById(R.id.ac_timing);

        ac_power.setOnClickListener(this);
        ac_temperature_up.setOnClickListener(this);
        ac_temperature_down.setOnClickListener(this);
        ac_mode.setOnClickListener(this);
        ac_windspeed.setOnClickListener(this);
        ac_direction.setOnClickListener(this);
        ac_windsweep.setOnClickListener(this);
        ac_timing.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ac_power:
                acRequest.sendCmdThread(UUID,"6","YK","01","0000");
                break;
            case R.id.temperature_up:
                acRequest.sendCmdThread(UUID,"6","YK","01","0001");
                break;
            case R.id.temperature_down:
                acRequest.sendCmdThread(UUID,"6","YK","01","0010");
                break;
            case R.id.ac_mode:
                acRequest.sendCmdThread(UUID,"6","YK","01","0011");
                break;
            case R.id.ac_windspeed:
                break;
            case R.id.ac_direction:
                break;
            case R.id.ac_windsweep:
                break;
            case R.id.ac_timing:
                break;
            default:
                break;
        }
    }

}
