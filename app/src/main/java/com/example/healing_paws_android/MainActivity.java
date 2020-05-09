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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button translate = (Button) findViewById(R.id.m_b_translate);
        Button login = (Button) findViewById(R.id.m_b_login);
        Button register = (Button) findViewById(R.id.m_b_register);
        Button new_appointment = (Button) findViewById(R.id.m_b_new_appointment);

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
                    startActivity(intent);
                }else{
                    Locale.setDefault(Locale.CHINESE);
                    Configuration configuration = getBaseContext().getResources().getConfiguration();
                    configuration.locale = Locale.CHINESE;
                    getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        new_appointment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,New_Appointment.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }
}
