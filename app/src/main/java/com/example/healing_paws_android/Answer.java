package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Result;

public class Answer extends AppCompatActivity {
    private TextView Title = findViewById(R.id.title1);
    private TextView Body = findViewById(R.id.body1);
    private TextView Answer = findViewById(R.id.answer1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent i2 = getIntent();
        String title =i2.getStringExtra("title");
        Title.setText(title);

        try {

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

            ResultSet rs = st.executeQuery("SELECT \n" +
                    "    body\n" +
                    "FROM\n" +
                    "    questions\n" +
                    "WHERE\n" +
                    "    title = '"+title+"';");
            Body.setText(rs.toString());

            ResultSet rs1 = st.executeQuery("SELECT \n" +
                    "    answers\n" +
                    "FROM\n" +
                    "    questions\n" +
                    "WHERE\n" +
                    "    title = '"+title+"';");
            Answer.setText(rs1.toString());


        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(Answer.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
