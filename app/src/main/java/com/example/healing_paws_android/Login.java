package com.example.healing_paws_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        Button back = findViewById(R.id.l_b_back);
        Button register = findViewById(R.id.l_b_register);
        Button login = findViewById(R.id.l_b_login);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.l_et_username);
                EditText password = findViewById(R.id.l_et_password);
                CheckBox remember_me = findViewById(R.id.l_cb_remember);
                String name = String.valueOf(username.getText());
                String pass = String.valueOf(password.getText());
                if(name.equals("")) {
                    String username_empty = getString(R.string.username_empty);
                    Toast.makeText(Login.this, username_empty, Toast.LENGTH_SHORT).show();

                } else if(pass.equals("")){
                    String password_empty = getString(R.string.password_empty);
                    Toast.makeText(Login.this, password_empty, Toast.LENGTH_SHORT).show();
                }else {
                    Boolean rem = remember_me.isChecked();
                    String url = "http://47.98.48.168:5000/login_android";
                    loginWithOkHttp(url, name, pass, rem);
//                HttpUtil.getUrl(url);    //this is a connection test
                }
            }
        });
    }

    public void loginWithOkHttp(String url,String username,String password,Boolean remember_me){
        HttpUtil.loginWithOkHttp(url,username,password,remember_me, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String server_error = getString(R.string.server_error);
                        Toast.makeText(Login.this, server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                //得到服务器返回的具体内容
                final String status = parseJsonWithJsonObject(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (status.equals("200")){
                            Headers headers =response.headers();//response为okhttp请求后的响应
                            List cookies = headers.values("Set-Cookie");
                            String session = cookies.get(0).toString();
                            String sessionid = session.substring(0,session.indexOf(";"));
                            EditText username = findViewById(R.id.l_et_username);
                            String name = String.valueOf(username.getText());
                            SharedPreferences share = getSharedPreferences("Session",MODE_PRIVATE);
                            SharedPreferences username_sp = getSharedPreferences("Username",MODE_PRIVATE);
                            SharedPreferences.Editor edit = share.edit();
                            SharedPreferences.Editor user = username_sp.edit();//编辑文件
                            edit.putString("sessionid",sessionid);
                            user.putString("username",name);
                            edit.commit();
                            user.apply();
                            //reference https://www.jianshu.com/p/5e1f79c450ff
                            String login_successful = getString(R.string.login_successful);
                            Toast.makeText(Login.this,login_successful,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            intent.putExtra("login_status",1);
                            startActivity(intent);
                        }else if(status.equals("400")){
                            String login_invalid = getString(R.string.login_failed_invalid);
                            Toast.makeText(Login.this,login_invalid,Toast.LENGTH_SHORT).show();
                        }else if(status.equals("403")){
                            String login_failed = getString(R.string.login_failed_password);
                            Toast.makeText(Login.this,login_failed,Toast.LENGTH_SHORT).show();
                        }else if(status.equals("404")){
                            String login_failed = getString(R.string.login_failed_username);
                            Toast.makeText(Login.this,login_failed,Toast.LENGTH_SHORT).show();
                        }else{
                            String login_failed = getString(R.string.unknown_failure);
                            Toast.makeText(Login.this,login_failed,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    //reference https://www.cnblogs.com/HenuAJY/p/10884055.html

    private String parseJsonWithJsonObject(Response response) throws IOException {
        String responseData=response.body().string();
        responseData = responseData.replace('[','{');
        responseData = responseData.replace(',',':');
        responseData = responseData.replace(']','}');
        System.out.println(responseData);
            String status="";
        try{
            JSONObject jsonObject=new JSONObject(responseData);
            status=jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }
    //reference https://www.jianshu.com/p/d006bc55bca9
}
