package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int login_status = 0;//default not logged in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //translate
        Button translate = (Button) findViewById(R.id.m_b_translate);
        translate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String language = getResources().getConfiguration().locale.getLanguage();
                if(language.equals("zh")){
                    Locale.setDefault(Locale.ENGLISH);
                    Configuration configuration = getBaseContext().getResources().getConfiguration();
                    configuration.locale = Locale.ENGLISH;
                    getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    intent.putExtra("login_status",login_status);
                    startActivity(intent);
                }else{
                    Locale.setDefault(Locale.CHINESE);
                    Configuration configuration = getBaseContext().getResources().getConfiguration();
                    configuration.locale = Locale.CHINESE;
                    getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    intent.putExtra("login_status",login_status);
                    startActivity(intent);
                }
            }
        });

        //login
        Button login = findViewById(R.id.m_b_login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        //register
        Button register = findViewById(R.id.m_b_register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Profile.class);
                startActivity(intent);
            }
        });

        Button question = findViewById(R.id.question);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Question.class);
                startActivity(intent);
            }
        });

        //new_appointment
        Button new_appointment = findViewById(R.id.m_b_new_appointment);
        new_appointment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,New_Appointment.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Button login = findViewById(R.id.m_b_login);
        Button register = findViewById(R.id.m_b_register);
        Button new_appointment = findViewById(R.id.m_b_new_appointment);
        Intent intent = getIntent();
        int i = intent.getIntExtra("login_status",0);
        login_status = i;
        //set button visibility
        if(login_status==0){
            new_appointment.setVisibility(View.INVISIBLE);
            register.setVisibility(View.VISIBLE);
        }else{
            new_appointment.setVisibility(View.VISIBLE);
            String logout = this.getString(R.string.logout);
            login.setText(logout);
            login = findViewById(R.id.m_b_login);
            login.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    login_status=0;
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
            register.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    public int getlogin_status(){
        return login_status;
    }
}
