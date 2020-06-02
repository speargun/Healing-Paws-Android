package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Result;

public class Answer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        TextView Title = findViewById(R.id.title1);
        TextView Body = findViewById(R.id.body1);
        TextView Answer = findViewById(R.id.answer1);

        Button back = findViewById(R.id.an_b_back);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Answer.this,Question.class);
                intent.putExtra("login_status",1);
                startActivity(intent);
            }
        });

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
            PreparedStatement st = null;
            SharedPreferences sharedPreferences = getSharedPreferences("Username", MODE_PRIVATE);
            String username = sharedPreferences.getString("username",null);
            // 执行
            String sql = "use test;";
            st = conn.prepareStatement(sql);
            st.execute();

            sql = "SELECT \n" +
                    "    body\n" +
                    "FROM\n" +
                    "    questions\n" +
                    "WHERE\n" +
                    "    title =?;";
            st = conn.prepareStatement(sql);
            st.setString(1,title);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                Body.setText(rs.getString(1));
            }

            rs.close();

            sql = "SELECT \n" +
                    "    a.body\n" +
                    "FROM\n" +
                    "    answers AS a, questions AS q\n" +
                    "WHERE\n" +
                    "    a.question_id = q.id AND q.title = ?;";
            st = conn.prepareStatement(sql);
            st.setString(1,title);
            ResultSet rs1 = st.executeQuery();
            while(rs1.next()) {
                Answer.setText(rs1.getString(1));
            }

            rs1.close();

        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(Answer.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
