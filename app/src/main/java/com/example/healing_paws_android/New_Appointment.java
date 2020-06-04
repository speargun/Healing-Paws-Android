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

import java.sql.PreparedStatement;
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
        final ArrayList<String> petsList = new ArrayList<>();
        final Spinner doctor = findViewById(R.id.n_s_doctor);
        final ArrayList<String> doctorsList = new ArrayList<>();
        final ArrayList<String> locsList = new ArrayList<>();
        try {
            acquire_info(petsList,doctorsList,locsList);
        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(New_Appointment.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        final String[] pets = new String[petsList.size()];
        final String[] locs = new String[locsList.size()];
        int i = 0;
        while(i<petsList.size()){
            pets[i] = petsList.get(i);
//            System.out.println(pets[i]);
            i++;
        }

        i = 0;
        while(i<locsList.size()){
            locs[i] = locsList.get(i);
//            System.out.println(locs[i]);
            i++;
        }
        i = 0;
        int j = 0;
        int[] i_set = new int[doctorsList.size()];
        while(i<doctorsList.size()){
            if(locs[i].equals("1")) {
                i_set[j] = i;
                j++;
            }
            i++;
        }
        i = 0;
        final String[] doctors1 = new String[j];
        while(i<doctors1.length) {
            doctors1[i] = doctorsList.get(i_set[i]);
            i++;
        }
        RadioButton beijing = findViewById(R.id.n_rb_beijing);
        RadioButton shanghai = findViewById(R.id.n_rb_shanghai);
        RadioButton chengdu = findViewById(R.id.n_rb_chengdu);
        beijing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = 0;
                int j = 0;
                int[] i_set = new int[doctorsList.size()];
                while(i<doctorsList.size()){
                    if(locs[i].equals("1")) {
                        i_set[j] = i;
                        j++;
                    }
                    i++;
                }
                i = 0;
                final String[] doctors = new String[j];
                while(i<doctors.length) {
                    doctors[i] = doctorsList.get(i_set[i]);
                    i++;
                }
                ArrayAdapter adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_template,doctors){
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
            }
        });
        shanghai.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = 0;
                int j = 0;
                int[] i_set = new int[doctorsList.size()];
                while(i<doctorsList.size()){
                    if(locs[i].equals("2")) {
                        i_set[j] = i;
                        j++;
                    }
                    i++;
                }
                i = 0;
                final String[] doctors = new String[j];
                while(i<doctors.length) {
                    doctors[i] = doctorsList.get(i_set[i]);
                    i++;
                }
                ArrayAdapter adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_template,doctors){
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
            }
        });
        chengdu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int i = 0;
                int j = 0;
                int[] i_set = new int[doctorsList.size()];
                while(i<doctorsList.size()){
                    if(locs[i].equals("3")) {
                        i_set[j] = i;
                        j++;
                    }
                    i++;
                }
                i = 0;
                final String[] doctors = new String[j];
                while(i<doctors.length) {
                    doctors[i] = doctorsList.get(i_set[i]);
                    i++;
                }
                ArrayAdapter adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_template,doctors){
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
            }
        });



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
        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_template,doctors1){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = inflate(getContext(), R.layout.spinner_dropdown, null);
                TextView label = view.findViewById(R.id.spinner_item_label);
                label.setText(doctors1[position]);
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
                String pe = null;
                if(pet.getSelectedItem()!=null) {
                    pe = pet.getSelectedItem().toString();
                }
                String doct = null;
                if(doctor.getSelectedItem()!=null) {
                    doct = doctor.getSelectedItem().toString();
                }
//                System.out.println("the selected pet is: "+pe);
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
                String doc = null;
                if(doctor.getSelectedItem()!=null) {
                    doc = doctor.getSelectedItem().toString();
                }
//                System.out.println("the selected doctor is: "+doc);
                int change = 1;
                if(accept.isChecked()){
                    change = 1;
                }else{
                    change = 0;
                }
                String des = String.valueOf(description.getText());
                if(pe==null){
                    String pet_empty = getString(R.string.pet_empty);
                    Toast.makeText(New_Appointment.this,pet_empty,Toast.LENGTH_SHORT).show();
                }else if(doct==null) {
                    String doctor_empty = getString(R.string.doctor_empty);
                    Toast.makeText(New_Appointment.this,doctor_empty,Toast.LENGTH_SHORT).show();
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
        PreparedStatement st = null;
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
        String sql = "use test;";
        st = conn.prepareStatement(sql);
        st.execute();
        if(type==0) {
            sql = "INSERT INTO\n" +
                    "                    appointments\n" +
                    "                (description,loc,is_emergency,changeable,pet_id,preferred_doctor_id,datetime,status)\n" +
                    "                VALUES\n" +
                    "                (?,?,?,?,\n" +
                    "                (SELECT p.id from Pets AS p, users as u\n" +
                    "                WHERE name = ? AND p.owner_id = u.id AND u.username = ? LIMIT 1),\n" +
                    "                (SELECT id from Users\n" +
                    "                WHERE username = ?),?,\n" +
                    "                'Pending');";
        }else{
            sql = "INSERT INTO\n" +
                    "                    appointments\n" +
                    "                (description,loc,is_emergency,changeable,pet_id,preferred_doctor_id,datetime,status,pet_status)\n" +
                    "                VALUES\n" +
                    "                (?,?,?,?,\n" +
                    "                (SELECT p.id from Pets AS p, users as u\n" +
                    "                WHERE name = ? AND p.owner_id = u.id AND u.username = ? LIMIT 1),\n" +
                    "                (SELECT id from Users\n" +
                    "                WHERE username = ?),?,\n" +
                    "                'Pending', 'Checked');";
        }
        st = conn.prepareStatement(sql);
        st.setString(1,description);
        st.setInt(2,location);
        st.setInt(3,type);
        st.setInt(4,change);
        st.setString(5,pet);
        st.setString(6,username);
        st.setString(7,doctor);
        st.setString(8,time);
        // 获得结果 集合
        // 关闭资源
        st.executeUpdate();
        st.close();
        conn.close();
    }

    public void acquire_info(ArrayList<String> pets,  ArrayList<String> doctors, ArrayList<String> locs) throws SQLException {

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
        PreparedStatement st = null;
        SharedPreferences sharedPreferences = getSharedPreferences("Username", MODE_PRIVATE);
        String username = sharedPreferences.getString("username",null);
        // 执行
        String sql = "use test; ";
        st = conn.prepareStatement(sql);
        st.execute();
        sql = "SELECT DISTINCT\n" +
                "    name\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    p.owner_id = u.id AND u.username = ?;";
        st = conn.prepareStatement(sql);
        st.setString(1,username);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            pets.add(rs.getString(1));
        }
        rs.close();
        sql = "SELECT DISTINCT\n" +
                "    username, loc\n" +
                "FROM\n" +
                "    users AS u,\n" +
                "    employees AS e\n" +
                "WHERE\n" +
                "    is_customer = 0 AND u.ref_id = e.id;";
        st = conn.prepareStatement(sql);
        ResultSet rs1 = st.executeQuery();
        while (rs1.next()) {
//            System.out.println(rs1.getString(1));
            doctors.add(rs1.getString(1));
            locs.add(rs1.getString(2));
        }
        // 关闭资源
        rs1.close();
        st.close();
        conn.close();

    }

}