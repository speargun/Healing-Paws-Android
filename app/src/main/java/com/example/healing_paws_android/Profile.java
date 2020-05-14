package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Profile extends AppCompatActivity {

    private TextView name = findViewById(R.id.username);
    private TextView phone = findViewById(R.id.phone);
    private TextView email = findViewById(R.id.email);
    private TextView dob = findViewById(R.id.dob);
    private TextView address = findViewById(R.id.address);
    private Button back = findViewById(R.id.back);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {
            profile();
        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(Profile.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,MainActivity.class);
                intent.putExtra("login_status",1);
                startActivity(intent);
            }
        });

    }

    public void profile() throws SQLException{
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
        name.setText(username);
        ResultSet rs = st.executeQuery("SELECT phone FROM customers LEFT JOIN  users ON users.id WHERE username= '"+username+"';");
        phone.setText(rs.toString());
        rs = st.executeQuery("SELECT email FROM customers LEFT JOIN  users ON users.id WHERE username= '"+username+"';");
        email.setText(rs.toString());
        rs = st.executeQuery("SELECT dob FROM customers LEFT JOIN  users ON users.id WHERE username= '"+username+"';");
        dob.setText(rs.toString());
        rs = st.executeQuery("SELECT address FROM customers LEFT JOIN  users ON users.id WHERE username= '"+username+"';");
        address.setText(rs.toString());

        rs.close();
        st.close();
        conn.close();

    }

}
