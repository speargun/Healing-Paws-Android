package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button translate = (Button) findViewById(R.id.button);
        translate.setText(R.string.translate);

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
    }
}
