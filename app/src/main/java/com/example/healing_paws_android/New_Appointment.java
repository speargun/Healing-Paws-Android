package com.example.healing_paws_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Driver;


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
            e.printStackTrace();
        }
        String[] pets = new String[petsList.size()];
        String[] doctors = new String[doctorsList.size()];
        int i = 0;
        while(i<petsList.size()){
            pets[i] = petsList.iterator().next();
            System.out.println(pets[i]);
            i++;
        }
        i = 0;
        while(i<doctorsList.size()){
            doctors[i] = doctorsList.iterator().next();
            System.out.println(doctors[i]);
            i++;
        }
        ArrayAdapter adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_template,pets);
//        System.out.println("pets: "+pets[0]);
        pet.setAdapter(adapter);
        adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_template,doctors);
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
                if(pe.isEmpty()){
                    String pet_empty = getString(R.string.pet_empty);
                    Toast.makeText(New_Appointment.this,pet_empty,Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        new_Appointment(pe, location, type, doc, change, des);
                        Intent intent = new Intent(New_Appointment.this,MainActivity.class);
                        intent.putExtra("login_status",1);
                        startActivity(intent);
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void new_Appointment(String pet, String location, String type, String doctor, String change, String description) throws SQLException {

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
        ResultSet rs = st.executeQuery("INSERT INTO\n" +
                "    appointments\n" +
                "(description,loc,is_emergency,changable,pet_id,preferred_doctor_id)\n" +
                "VALUES\n" +
                "('"+description+"','"+location+"','"+type+"','"+change+"','"+
                "(SELECT id from Pets\n" +
                "WHERE name = '"+pet+ "'),'\n"+
                "(SELECT id from User\n" +
                "WHERE u.username = '"+doctor+"');");
        // 获得结果 集合
        // 关闭资源
        rs.close();
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
                "    users AS u,\n" +
                "WHERE\n" +
                "    p.owner_id = u.id AND u.username = '"+username+"';");
        ResultSet rs1 = st.executeQuery("SELECT DISTINCT\n" +
                "    username\n" +
                "FROM\n" +
                "    users AS u,\n" +
                "    employees AS e\n" +
                "WHERE\n" +
                "    u.id = e.id AND u.username = '"+username+"';");
        // 获得结果 集合
        while (rs.next()) {
            pets.add(rs.getString(1));
        }
        while (rs1.next()) {
            doctors.add(rs.getString(1));
        }
        // 关闭资源
        rs.close();
        st.close();
        conn.close();
    }
}