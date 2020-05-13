package com.example.healing_paws_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Driver;
import java.util.Calendar;

import static android.view.View.inflate;


public class New_Appointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__appointment);

        //fill pets and doctors in spinners
        Spinner pet = findViewById(R.id.n_s_pet);
        ArrayList<String> petsList = new ArrayList<>();
        Spinner doctor = findViewById(R.id.n_s_doctor);
        ArrayList<String> doctorsList = new ArrayList<>();
        try {
            acquire_info(petsList,doctorsList);
        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(New_Appointment.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        final String[] pets = new String[petsList.size()];
        final String[] doctors = new String[doctorsList.size()];
        int i = 0;
        while(i<petsList.size()){
            pets[i] = petsList.get(i);
//            System.out.println(pets[i]);
            i++;
        }
        i = 0;
        while(i<doctorsList.size()){
            doctors[i] = doctorsList.get(i);
//            System.out.println(doctors[i]);
            i++;
        }
        ArrayAdapter adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_template,pets){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = inflate(getContext(), R.layout.spinner_dropdown, null);
                TextView label = view.findViewById(R.id.spinner_item_label);
                label.setText(pets[position]);
                return view;
            }
        };
// reference       https://www.cnblogs.com/coding-way/p/3549865.html
        pet.setAdapter(adapter);
        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_template,doctors){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = inflate(getContext(), R.layout.spinner_dropdown, null);
                TextView label = view.findViewById(R.id.spinner_item_label);
                label.setText(doctors[position]);
                return view;
            }
        };
//        System.out.println("doctors: "+doctors[0]);
        doctor.setAdapter(adapter);

        Button back = findViewById(R.id.n_b_back);
        Button submit = findViewById(R.id.n_b_submit);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(New_Appointment.this,MainActivity.class);
                intent.putExtra("login_status",1);
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
                int location = 1;
                if(beijing.isChecked()){
                    location = 1;
                }else if(shanghai.isChecked()){
                    location = 2;
                }else{
                    location = 3;
                }
                int type = 1;
                if(emergency.isChecked()){
                    type = 1;
                }else{
                    type = 0;
                }
                String doc = String.valueOf(doctor.getId());
                int change = 1;
                if(accept.isChecked()){
                    change = 1;
                }else{
                    change = 0;
                }
                String des = String.valueOf(description.getText());
                if(pe.isEmpty()){
                    String pet_empty = getString(R.string.pet_empty);
                    Toast.makeText(New_Appointment.this,pet_empty,Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        new_Appointment(pe, location, type, doc, change, des);
                        String reserve_successful = getString(R.string.reserve_successful);
                        Toast.makeText(New_Appointment.this, reserve_successful, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(New_Appointment.this, MainActivity.class);
                        intent.putExtra("login_status", 1);
                        startActivity(intent);
                    }catch(SQLException e){
                        String sql_error = getString(R.string.sql_error);
                        Toast.makeText(New_Appointment.this,sql_error,Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void new_Appointment(String pet, int location, int type, String doctor, int change, String description) throws SQLException {

        // 注册驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //注册失败
            e.printStackTrace();
        }
        // 获得链接
        Connection conn = DriverManager.getConnection("jdbc:mysql://47.98.48.168:3306", "root", "root");
        // 得到操作数据库sql语句的对象Statement
        Statement st = conn.createStatement();
        SharedPreferences sharedPreferences = getSharedPreferences("Username", MODE_PRIVATE);
        String username = sharedPreferences.getString("username",null);
        // 执行

        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
        int year = calendar.get(Calendar.YEAR);
//月
        int month = calendar.get(Calendar.MONTH)+1;
//日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
        int minute = calendar.get(Calendar.MINUTE);
//秒
        int second = calendar.get(Calendar.SECOND);
        String time = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
        st.executeQuery("use test");
        st.execute("INSERT INTO\n" +
                "                    appointments\n" +
                        "                (description,loc,is_emergency,changeable,pet_id,preferred_doctor_id,datetime)\n" +
                        "                VALUES\n" +
                        "                ('"+description+"',"+location+","+type+","+change+",\n" +
                        "                (SELECT id from Pets\n" +
                        "                WHERE name = '"+pet+"'),\n" +
                        "                (SELECT id from Users\n" +
                        "                WHERE username = '"+doctor+"'),'"+time+"');");
        // 获得结果 集合
        // 关闭资源
        st.close();
        conn.close();
    }

    public void acquire_info(ArrayList<String> pets,  ArrayList<String> doctors) throws SQLException {

        // 注册驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //注册失败
            e.printStackTrace();
        }
        // 获得链接
        Connection conn = DriverManager.getConnection("jdbc:mysql://47.98.48.168:3306", "root", "root");
        // 得到操作数据库sql语句的对象Statement
        Statement st = conn.createStatement();
        SharedPreferences sharedPreferences = getSharedPreferences("Username", MODE_PRIVATE);
        String username = sharedPreferences.getString("username",null);
        // 执行
        st.executeQuery("use test");
        ResultSet rs = st.executeQuery("SELECT DISTINCT\n" +
                "    name\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    p.owner_id = u.id AND u.username = '"+username+"';");
        while (rs.next()) {
            pets.add(rs.getString(1));
        }
        rs.close();
        ResultSet rs1 = st.executeQuery("SELECT DISTINCT\n" +
                "    username\n" +
                "FROM\n" +
                "    users AS u,\n" +
                "    employees AS e\n" +
                "WHERE\n" +
                "    u.id = e.id;");
        while (rs1.next()) {
//            System.out.println(rs1.getString(1));
            doctors.add(rs1.getString(1));
        }
        // 关闭资源
        rs1.close();
        st.close();
        conn.close();
    }
}