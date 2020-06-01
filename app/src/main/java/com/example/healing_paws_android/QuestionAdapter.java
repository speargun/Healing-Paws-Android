package com.example.healing_paws_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<String> {
    private int resourceId;
    public QuestionAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    public View gitView(int position, View convertView, ViewGroup parent){
        String q =(String) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView s = view.findViewById(R.id.text_dname);
        s.setText(q);
        return view;
    }

}
