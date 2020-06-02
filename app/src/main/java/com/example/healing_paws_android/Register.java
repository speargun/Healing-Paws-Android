package com.example.healing_paws_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        DatePicker birthday = findViewById(R.id.r_dp_birthday);

// the chosen date should not be later than system date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        birthday.init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                if (isDateAfter(view)) {
                    Calendar mCalendar = Calendar.getInstance();
                    view.init(mCalendar.get(Calendar.YEAR),
                            mCalendar.get(Calendar.MONTH),
                            mCalendar.get(Calendar.DAY_OF_MONTH), this);
                }
            }

            private boolean isDateAfter(DatePicker tempView) {
                Calendar mCalendar = Calendar.getInstance();
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.set(tempView.getYear(), tempView.getMonth(),
                        tempView.getDayOfMonth(), 0, 0, 0);
                if (tempCalendar.after(mCalendar))
                    return true;
                else
                    return false;
            }
        });
        //reference https://blog.csdn.net/csdnadcode/article/details/39555519

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
                DatePicker birthday = findViewById(R.id.r_dp_birthday);
                EditText address = findViewById(R.id.r_et_address);
                CheckBox rules = findViewById(R.id.r_cb_rules);
                String name = String.valueOf(username.getText());
                String pass = String.valueOf(password.getText());
                String rep = String.valueOf(repeat.getText());
                String ema = String.valueOf(email.getText());
                String pho = String.valueOf(phone.getText());
                int year = birthday.getYear();
                int month = birthday.getMonth() + 1;
                int day = birthday.getDayOfMonth();
                String bir = year+"-"+month+"-"+day;
                String add = String.valueOf(address.getText());
                Boolean rul = rules.isChecked();
                if(!isUsername(name)){
                    String username_invalid = getString(R.string.username_invalid);
                    Toast.makeText(Register.this,username_invalid,Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(rep)||pass.equals("")){
                    String password_invalid = getString(R.string.password_invalid);
                    Toast.makeText(Register.this,password_invalid,Toast.LENGTH_SHORT).show();
                }else if(!isEmail(ema)){
                    String email_invalid = getString(R.string.email_invalid);
                    Toast.makeText(Register.this,email_invalid,Toast.LENGTH_SHORT).show();
                }else if(!isPhone(pho)) {
                    String phone_invalid = getString(R.string.phone_invalid);
                    Toast.makeText(Register.this, phone_invalid, Toast.LENGTH_SHORT).show();
                }else if(!rul){
                    String deny_rules = getString(R.string.deny_rules);
                    Toast.makeText(Register.this, deny_rules, Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://47.98.48.168:5000/register_android";
                    RegisterWithOkHttp(url, name, pass, rep, ema, pho, bir, add);
                }
            }
        });
    }

    private void RegisterWithOkHttp(String url, String username, String password, String repeat, String email, String phone, String birthday, String address) {
        HttpUtil.registerWithOkHttp(url,username,password,repeat,email,phone,birthday,address, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String server_error = getString(R.string.server_error);
                        Toast.makeText(Register.this, server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //得到服务器返回的具体内容
                final String status = parseJsonWithJsonObject(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (status.equals("200")) {
                            String register_successful = getString(R.string.register_successful);
                            Toast.makeText(Register.this, register_successful, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        } else if(status.equals("409")){
                            String username_duplicate = getString(R.string.username_duplicate);
                            Toast.makeText(Register.this, username_duplicate, Toast.LENGTH_SHORT).show();
                        }else{
                            String login_failed = getString(R.string.register_failed);
                            Toast.makeText(Register.this,login_failed,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    //reference https://www.cnblogs.com/HenuAJY/p/10884055.html

    private String parseJsonWithJsonObject(Response response) throws IOException {
        String responseData=response.body().string();
        System.out.println(responseData);
        responseData = responseData.replace('[','{');
        responseData = responseData.replace(',',':');
        responseData = responseData.replace(']','}');
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


    public static boolean isUsername(String username) {
        Pattern p = Pattern.compile("^\\w{1,15}$");
        Matcher m = p.matcher(username);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        String emailPattern = "[a-zA-Z0-9][a-zA-Z0-9._-]{0,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
        return Pattern.matches(emailPattern, email);
    }

    public static boolean isPhone(String mobiles) {
        Pattern p = Pattern.compile("^[0-9]{7,13}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
//reference    https://blog.csdn.net/ewrfedf/article/details/21445607

}
