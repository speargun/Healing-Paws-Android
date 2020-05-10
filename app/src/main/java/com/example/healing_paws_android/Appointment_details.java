package com.example.healing_paws_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Appointment_details extends AppCompatActivity {
    private ListView mListView;
    private String[] names={"Customer Name:","Pet Name:","Appointment location:","Appointment Date:","Appointment Time:","Preferred Doctor:","Pet Status:","Description:"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        mListView=(ListView)findViewById(R.id.lv);
        mListView.setAdapter(new MyBaseAdapter());

    }
    class MyBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return names.length;
        }
        @Override
        public Object getItem(int position) {
            return names [position];
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(Appointment_details.this,R.layout.list_item,null);
            TextView mTextView=(TextView) view.findViewById(R.id.tv_list);

            mTextView.setText(names[position]);

            return view;
        }
    }
}