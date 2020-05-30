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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Appointment_details extends AppCompatActivity {

    private TextView pet = findViewById(R.id.pet);
    private TextView location = findViewById(R.id.location);

    private TextView change = findViewById(R.id.change);
    private TextView doctor = findViewById(R.id.doctor);
    private TextView description = findViewById(R.id.description);
    private TextView type = findViewById(R.id.type);
    private TextView time = findViewById(R.id.time);

    private Button back = findViewById(R.id.back);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        Intent intent = getIntent();
        String atime = intent.getStringExtra("atime");
        time.setText("Datetime:"+atime);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Appointment_details.this,Check_appointment.class);

                startActivity(intent);
            }
        });

        try {
            details();
        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(Appointment_details.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }



    }





    public void details() throws SQLException{
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

        ResultSet rs = st.executeQuery("SELECT DISTINCT\n" +
                "    name\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    appointments AS a\n" +
                "users AS u\n"+
                "WHERE\n" +
                "    p.id = a.pet_id AND  p.owner_id = u.id AND u.username = '"+username+"AND a.datetime = '"+time+"';");
       pet.setText("Pet:"+rs.toString());
        rs = st.executeQuery("SELECT \n" +
                "    loc\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    appointments AS a\n" +
                "users AS u\n"+
                "WHERE\n" +
                "    p.id = a.pet_id AND  p.owner_id = u.id AND  u.username = '"+username+"AND a.datetime = '"+time+"';");
        location.setText("Location:"+rs.toString());
        rs = st.executeQuery("SELECT \n" +
                "    changeable\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    appointments AS a\n" +
                "users AS u\n"+
                "WHERE\n" +
                "    p.id = a.pet_id AND  p.owner_id = u.id AND  u.username = '"+username+"AND a.datetime = '"+time+"';");
        change.setText(rs.toString());
        rs = st.executeQuery("SELECT \n" +
                "    preferred_doctor_id\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    appointments AS a\n" +
                "users AS u\n"+
                "WHERE\n" +
                "    p.id = a.pet_id AND  p.owner_id = u.id AND u.username = '"+username+"AND a.datetime = '"+time+"';");
       doctor.setText("Doctor:"+rs.toString());
        rs = st.executeQuery("SELECT \n" +
                "    description\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    appointments AS a\n" +
                "users AS u\n"+
                "WHERE\n" +
                "    p.id = a.pet_id AND  p.owner_id = u.id AND u.username = '"+username+"AND a.datetime = '"+time+"';");
        description.setText("Description:"+rs.toString());

        rs = st.executeQuery("SELECT \n" +
                "    is_emergency\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    appointments AS a\n" +
                "users AS u\n"+
                "WHERE\n" +
                "    p.id = a.pet_id AND  p.owner_id = u.id AND u.username = '"+username+"AND a.datetime = '"+time+"';");
        type.setText("Type:"+rs.toString());
        rs.close();
        st.close();
        conn.close();

    }

}