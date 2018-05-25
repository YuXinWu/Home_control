package top.spiiiiiiiiiitter.home_control;


import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    //Fragment初始化
    private lightControlFragment lightcontrolfragment;
    private curtainControlFragment curtaincontrolfragment;
    private projectorControlFragment projectorcontrolfragment;
    private ACFragment acFragment;
    private SocketFragment socketFragment;
    private WaterDispensersFragment waterdispensersfragment;
    private Fragment currentFragment;
    private FragmentManager mFm;

    private Button Btn_livingroom;
    private UserDataHandle userDataHandle;
    private Dialog dialog;

    private ListView mListView;
    private String[] names={"电灯","窗帘","投影仪","空调","插座","饮水机"};
    //private int[] icons={R.drawable.light,R.drawable.curtain,R.drawable.projector};

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    //inflater是对应的布局资源文件
    //container是存放布局的viewgroup
    //savedInstanceState是一个布尔值，表示创建的布局附加到viewgroup上
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment;
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //控件初始化放到这里
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //初始化fragment
        lightcontrolfragment=new lightControlFragment();
        curtaincontrolfragment=new curtainControlFragment();
        projectorcontrolfragment=new projectorControlFragment();
        acFragment=new ACFragment();
        socketFragment=new SocketFragment();
        waterdispensersfragment=new WaterDispensersFragment();
        currentFragment=null;
        mFm=getFragmentManager();


        //初始化数据库处理类
        userDataHandle=new UserDataHandle(getActivity());

        /* 初始化天气组件 */
        weatherGet();

        //初始化ListView控件
        mListView =(ListView) getActivity().findViewById(R.id.lv);
        MyBaseAdapter myBaseAdapter=new MyBaseAdapter();
        mListView.setAdapter(myBaseAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0:
                        switchFragment(lightcontrolfragment);
                        break;
                    case 1:
                        switchFragment(curtaincontrolfragment);
                        break;
                    case 2:
                        switchFragment(projectorcontrolfragment);
                        break;
                    case 3:
                        switchFragment(acFragment);
                        break;
                    case 4:
                        switchFragment(socketFragment);
                        break;
                    case 5:
                        switchFragment(waterdispensersfragment);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void switchFragment(Fragment to){
        if(currentFragment!=to){
            FragmentTransaction begintransaction=mFm.beginTransaction();
            //to没有添加过，即第一次添加
            if(!to.isAdded()){
                if (currentFragment == null) {
                    begintransaction.add(R.id.subfragment_layout,to);
                }else{
                    begintransaction.hide(currentFragment)
                                    .add(R.id.subfragment_layout,to);
                }
            }else{
                begintransaction.hide(currentFragment)
                                .show(to);
            }
            currentFragment=to;//将目标fragment置为当前fragment
            begintransaction.commitAllowingStateLoss();
        }
    }

    public void weatherGet(){
        try {
            InputStream is =this.getResources().openRawResource(R.raw.weather);//此处为要加载的json文件名称

            String text = readTextFromSDcard(is);

            weatherExtract(text);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("readFromAssets",e.toString());
        }
    }

    //将传入的is一行一行解析读取出来出来
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is,"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();//把读取的数据返回
    }

    //把读取出来的json数据进行解析
    public boolean weatherExtract(String response) {


        //初始化几个TextView
        TextView city = (TextView) getActivity().findViewById(R.id.city);
        TextView temperature = (TextView) getActivity().findViewById(R.id.temperature);
        TextView weather = (TextView) getActivity().findViewById(R.id.weatherStatus);
        TextView PM_25 = (TextView) getActivity().findViewById(R.id.PMvalue);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String ct=jsonObject.getString("city_name");
            int tp=jsonObject.getInt("temp");
            String wt=jsonObject.getString("weather");
            int pm=jsonObject.getInt("PM值");
            city.setText(ct);
            temperature.setText(String.valueOf(tp)+"℃");
            weather.setText(wt);
            PM_25.setText("PM2.5:"+String.valueOf(pm));

            return true;

        }catch (Exception e) {
            Log.d("handleCitiesResponse", e.toString());
            System.out.println("---------------------");
        }
        return false;
    }


    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getActivity(),
                    R.layout.list_item,null);

            /*
            new Handler().postDelayed(new Runnable(){
                public void run() {

                }
            }, 500);
            */

            TextView mTextView=(TextView)view.findViewById(R.id.item_tv);
            mTextView.setText(names[position]);
            //ImageView imageView=(ImageView)view.findViewById(R.id.item_image);
            //imageView.setBackgroundResource(icons[position]);
            return view;
        }
    }

}
