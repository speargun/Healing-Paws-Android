package com.example.healing_paws_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Check_appointment extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_appointment);
        mListView=(ListView)findViewById(R.id.lv);
        List<appointmentdata> mappointmentdataList=new ArrayList<>();
        for( int i = 1; i <= 5 ; i++) {
            appointmentdata mappointmentData = new appointmentdata();
            mappointmentData.setDname("doctor: " + mappointmentData.getDname());
            mappointmentData.setAddress("address:"+ mappointmentData.getAddress());

            mappointmentdataList.add(mappointmentData);
        }






        mListView.setAdapter(new MyBaseAdapter(mappointmentdataList,this));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                intent = new Intent(Check_appointment.this,Appointment_details.class);
                startActivity(intent);
            }
        });
    }
    class MyBaseAdapter extends BaseAdapter{
        private List<appointmentdata> mappointmentdataList;
        private LayoutInflater inflater;

        public  MyBaseAdapter (List<appointmentdata> mappointmentdataList, Context context) {
            this.mappointmentdataList = mappointmentdataList;
            this.inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return mappointmentdataList == null?0:mappointmentdataList.size();  //判断有说个Item
        }

        @Override
        public Object getItem(int position) {
            return mappointmentdataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //加载布局为一个视图
            View view = inflater.inflate(R.layout.activity_check_appointment,null);
            appointmentdata mappointmentdata = (appointmentdata) getItem(position);

            //在view 视图中查找 组件
            TextView tv_dname = (TextView) view.findViewById(R.id.text_dname);
            TextView tv_address= (TextView) view.findViewById(R.id.text_address);


            //为Item 里面的组件设置相应的数据
            tv_dname.setText("doctor:"+mappointmentdata.getDname());
            tv_address.setText("address: "+ mappointmentdata.getAddress());

            //返回含有数据的view
            return view;
        }

    }


}