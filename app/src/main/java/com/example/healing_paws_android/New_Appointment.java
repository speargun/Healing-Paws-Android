package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class New_Appointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__appointment);

        Button back = findViewById(R.id.n_b_back);
        Button submit = findViewById(R.id.n_b_submit);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(New_Appointment.this,MainActivity.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Spinner pet = findViewById(R.id.n_s_pet);
                RadioButton beijing = findViewById(R.id.n_rb_beijing);
                RadioButton shanghai = findViewById(R.id.n_rb_shanghai);
                RadioButton chengdu = findViewById(R.id.n_rb_chengdu);
                RadioButton emergency = findViewById(R.id.n_rb_emergency);
                RadioButton standard = findViewById(R.id.n_rb_standard);
                Spinner doctor = findViewById(R.id.n_s_doctor);
                RadioButton accept = findViewById(R.id.n_rb_accept);
                RadioButton refuse = findViewById(R.id.n_rb_refuse);
                EditText description = findViewById(R.id.n_et_description);
                String pe = String.valueOf(pet.getId());
                String location = "";
                if(beijing.isChecked()){
                    location = "beijing";
                }else if(shanghai.isChecked()){
                    location = "shanghai";
                }else{
                    location = "chengdu";
                }
                String type = "";
                if(emergency.isChecked()){
                    type = "emergency";
                }else{
                    type = "standard";
                }
                String doc = String.valueOf(doctor.getId());
                String change = "";
                if(accept.isChecked()){
                    change = "accept";
                }else{
                    change = "refuse";
                }
                String des = String.valueOf(description.getText());
                String url = "http://47.98.48.168:5000/new_appointment";
                SendMessage(url,pe,location,type,doc,change,des);
            }
        });
    }

    private void SendMessage(String url, final String pet, String location, String type, String doctor, String change, String description) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("pet", pet);
        formBuilder.add("location", location);
        formBuilder.add("type", type);
        formBuilder.add("doctor", doctor);
        formBuilder.add("change", change);
        formBuilder.add("description", description);

        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(New_Appointment.this, "server error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.equals("404")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(New_Appointment.this, "failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(New_Appointment.this, "successful"+res, Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(New_Appointment.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }
//The database connection part has reference of following:
//原文链接：https://blog.csdn.net/qq_36982160/java/article/details/80681360

}