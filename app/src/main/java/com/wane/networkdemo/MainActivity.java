package com.wane.networkdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wane.networkdemo.utils.KsoapServiceUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button1,button2,button3;
    private TextView textView;
    private EditText editText;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.Get_city);
        button2 = findViewById(R.id.Get_Weather);
        textView = findViewById(R.id.place_text);
        button3 = findViewById(R.id.Get_Place_of_belonging);
        editText = findViewById(R.id.city_text);
        listView = findViewById(R.id.list_item);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetCityTask().execute(editText.getText().toString());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetWeatherTask().execute(editText.getText().toString());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPlaceTask().execute(editText.getText().toString());
            }
        });



    }

    class GetCityTask extends AsyncTask<String,Void,List<String>> {

        private List<String> resultlist = new ArrayList<>();

        @Override
        protected List<String> doInBackground(String... result) {
            Log.e("得到数据",""+KsoapServiceUtils.getInstance().getInformation(result[0]));
            resultlist.addAll(KsoapServiceUtils.getInstance().getInformation(result[0]));
            return resultlist;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            //创建数组适配器对象，并且通过参数设置类item项的布局样式和数据源
            arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, strings);
            //把数组适配器加载到ListView控件中
            listView.setAdapter(arrayAdapter);
        }
    }


    class GetWeatherTask extends AsyncTask<String,Void,List<String>> {

        private List<String> resultlist = new ArrayList<>();

        @Override
        protected List<String> doInBackground(String... result) {
            Log.e("xxx",result[0]+"=="+KsoapServiceUtils.getInstance().getWeather(result[0]));
//            resultlist.addAll(KsoapServiceUtils.getInstance().getWeather(result[0]));
            return resultlist;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            //创建数组适配器对象，并且通过参数设置类item项的布局样式和数据源
            arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, strings);
            //把数组适配器加载到ListView控件中
            listView.setAdapter(arrayAdapter);
        }
    }


    class GetPlaceTask extends AsyncTask<String,Void,String> {

        private String response= null;

        @Override
        protected String doInBackground(String... result) {
            response = KsoapServiceUtils.getInstance().getPlace(result[0]);
            return response;
        }

        @Override
        protected void onPostExecute(String strings) {
            textView.setText(strings);
        }
    }

}
