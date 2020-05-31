package com.example.healing_paws_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Appointment_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        TextView pet = findViewById(R.id.pet);
        TextView location = findViewById(R.id.location);

        TextView change = findViewById(R.id.change);
        TextView doctor = findViewById(R.id.doctor);
        TextView description = findViewById(R.id.description);
        TextView type = findViewById(R.id.type);
        TextView time = findViewById(R.id.time);

        Button back = findViewById(R.id.back);

        Intent intent = getIntent();
        String atime = intent.getStringExtra("atime");
        String attime = getString(R.string.time)+" "+atime;
        time.setText(attime);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Appointment_details.this,Check_appointment.class);
                intent.putExtra("login_status",1);
                startActivity(intent);
            }
        });

        try {
            details(pet, location, change, doctor, description, type, atime);
        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(Appointment_details.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void details(TextView pet, TextView location, TextView change, TextView doctor, TextView description, TextView type, String time) throws SQLException{
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

        st.executeQuery("use test");

        ResultSet rs = st.executeQuery("SELECT \n" +
                "    name\n" +
                "FROM\n" +
                "    appointments AS a,\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    a.pet_id = p.id AND p.owner_id = u.id\n" +
                "        AND u.username = '"+username+"'\n" +
                "        AND a.datetime = '"+time+"';");
        String s = "";
        while(rs.next()) {
            s = getString(R.string.pet) + " " + rs.getString(1);
        }
        pet.setText(s);
        rs.close();
        ResultSet rs1 = st.executeQuery("SELECT \n" +
                "    loc\n" +
                "FROM\n" +
                "    appointments AS a,\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    a.pet_id = p.id AND p.owner_id = u.id\n" +
                "        AND u.username = '"+username+"'\n" +
                "        AND a.datetime = '"+time+"';");
        while(rs1.next()) {
            s = rs1.getString(1);
        }
        if(s.equals("1")){
            s = getString(R.string.beijing);
        }else if(s.equals("2")){
            s = getString(R.string.shanghai);
        }else{
            s = getString(R.string.chengdu);
        }
        s = getString(R.string.loc)+" "+s;
        location.setText(s);
        rs1.close();
        ResultSet rs2 = st.executeQuery("SELECT \n" +
                "    changeable\n" +
                "FROM\n" +
                "    appointments AS a,\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    a.pet_id = p.id AND p.owner_id = u.id\n" +
                "        AND u.username = '"+username+"'\n" +
                "        AND a.datetime = '"+time+"';");
        while(rs2.next()) {
            s = rs2.getString(1);
        }
        if(s.equals("1")) {
            s = getString(R.string.accept);
        }else{
            s = getString(R.string.refuse);
        }
        s = getString(R.string.ch) +" "+ s;
        change.setText(s);
        rs2.close();
        ResultSet rs3 = st.executeQuery("SELECT \n" +
                "    username\n" +
                "FROM\n" +
                "    users\n" +
                "WHERE\n" +
                "    (SELECT \n" +
                "            preferred_doctor_id\n" +
                "        FROM\n" +
                "            appointments AS a,\n" +
                "            pets AS p,\n" +
                "            users AS u\n" +
                "        WHERE\n" +
                "            a.pet_id = p.id AND p.owner_id = u.id\n" +
                "                AND u.username = '"+username+"'\n" +
                "                AND a.datetime = '"+time+"') = id");
        while(rs3.next()) {
            s = getString(R.string.doc) + " " + rs3.getString(1);
        }
        doctor.setText(s);
        rs3.close();
        ResultSet rs4 = st.executeQuery("SELECT \n" +
                "    description\n" +
                "FROM\n" +
                "    appointments AS a,\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    a.pet_id = p.id AND p.owner_id = u.id\n" +
                "        AND u.username = '"+username+"'\n" +
                "        AND a.datetime = '"+time+"';");
        while(rs4.next()) {
            s = getString(R.string.desc) + " " + rs4.getString(1);
        }
        description.setText(s);
        rs4.close();
        ResultSet rs5 = st.executeQuery("SELECT \n" +
                "    is_emergency\n" +
                "FROM\n" +
                "    appointments AS a,\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    a.pet_id = p.id AND p.owner_id = u.id\n" +
                "        AND u.username = '"+username+"'\n" +
                "        AND a.datetime = '"+time+"';");
        while(rs5.next()) {
            s = rs5.getString(1);
        }
        if(s.equals("1")){
            s = getString(R.string.emergency);
        }else{
            s = getString(R.string.standard);
        }
        s = getString(R.string.ty)+" "+s;
        type.setText(s);
        rs5.close();
        st.close();
        conn.close();

    }
}