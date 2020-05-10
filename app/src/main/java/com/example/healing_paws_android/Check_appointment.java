package com.example.healing_paws_android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Check_appointment extends AppCompatActivity {
    private ListView mListView;
    private String[] names={"appointment1","appointment2","appointment3","appointment4","appointment5","appointment6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_appointment);
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
            View view=View.inflate(Check_appointment.this,R.layout.list_item,null);
            TextView mTextView=(TextView) view.findViewById(R.id.tv_list);

            mTextView.setText(names[position]);
            return view;
        }

    }
}