package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button back = findViewById(R.id.r_b_back);
        Button register = findViewById(R.id.r_b_register);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText username = findViewById(R.id.r_et_username);
                EditText password = findViewById(R.id.r_et_password);
                EditText repeat = findViewById(R.id.r_et_repeat);
                EditText email = findViewById(R.id.r_et_email);
                EditText phone = findViewById(R.id.r_et_phone);
                EditText birthday = findViewById(R.id.r_et_birthday);
                EditText address = findViewById(R.id.r_et_address);
                CheckBox rules = findViewById(R.id.r_cb_rules);
                String name = String.valueOf(username.getText());
                String pass = String.valueOf(password.getText());
                String rep = String.valueOf(repeat.getText());
                String ema = String.valueOf(email.getText());
                String pho = String.valueOf(phone.getText());
                String bir = String.valueOf(birthday.getText());
                String add = String.valueOf(address.getText());
                String rul = String.valueOf(rules.isChecked());
                String url = "http://47.98.48.168:5000/register?role=c";
                SendMessage(url,name,pass,rep,ema,pho,bir,add,rul);
            }
        });
    }

    private void SendMessage(String url, final String userName, String passWord, String repeat, String email, String phone, String birthday, String address, String rules) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", userName);
        formBuilder.add("password", passWord);
        formBuilder.add("repeat", repeat);
        formBuilder.add("email", email);
        formBuilder.add("phone", phone);
        formBuilder.add("birthday", birthday);
        formBuilder.add("address", address);
        formBuilder.add("rules", rules);

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
                                Toast.makeText(Register.this, "server error", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(Register.this, "register failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, "successful"+res, Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent = new Intent(Register.this,Login.class);
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
