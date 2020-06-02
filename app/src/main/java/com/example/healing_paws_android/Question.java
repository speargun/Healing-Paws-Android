package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Question extends AppCompatActivity {

    public String qa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        final EditText title = findViewById(R.id.title);
        final EditText body = findViewById(R.id.body);
        Button send = findViewById(R.id.send);

        ListView listview = findViewById(R.id.list_view);
        final List<String> questionList = new ArrayList<>();

        final QuestionAdapter questionAdapter;

        Button back = findViewById(R.id.q_b_back);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Question.this,MainActivity.class);
                intent.putExtra("login_status",1);
                startActivity(intent);
            }
        });

        try {
            initQuestion(questionList);
        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(Question.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        questionAdapter = new QuestionAdapter(Question.this,R.layout.question,questionList);
        listview.setAdapter(questionAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                qa=questionList.get(i);
                Intent i2 = new Intent(Question.this,Answer.class);
                i2.putExtra("title", qa);
                startActivity(i2);

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendQuestion(title, body);
                    String send_success = getString(R.string.send_success);
                    Toast.makeText(Question.this,send_success,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Question.this, Question.class);
                    startActivity(intent);
                }catch(SQLException e){
                    String sql_error = getString(R.string.sql_error);
                    Toast.makeText(Question.this,sql_error,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });



    }


    public void initQuestion(List<String> questionList) throws SQLException{
        questionList.clear();

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
        ResultSet rs = st.executeQuery("SELECT \n" +
                "    title\n" +
                "FROM\n" +
                "    questions AS q,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    q.user_id = u.id AND u.username = '"+username+"';");
        while(rs.next()){
            questionList.add(rs.getString(1));
        }
        rs.close();
        st.close();
        conn.close();
    }

    public void sendQuestion(TextView title, TextView body) throws SQLException{
        String t = title.getText().toString();
        String b = body.getText().toString();
        if(t!=null&&b!=null){
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

            ResultSet rs1 = st.executeQuery("Select id FROM users WHERE username =  '"+username+"';");
            int user_id = -1;
            while(rs1.next()) {
                user_id = Integer.parseInt(rs1.getString(1));
            }
            rs1.close();

            RadioButton pet = findViewById(R.id.q_rb_pet);
            RadioButton employee = findViewById(R.id.q_rb_employee);
            RadioButton other = findViewById(R.id.q_rb_other);
            int type = 1;
            if(pet.isChecked()){
                type = 1;
            }else if(employee.isChecked()){
                type = 2;
            }else{
                type = 3;
            }

            RadioButton yes = findViewById(R.id.q_rb_yes);
            RadioButton no = findViewById(R.id.q_rb_no);
            int anonymity = 1;
            if(yes.isChecked()){
                anonymity = 1;
            }else{
                anonymity = 0;
            }

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

            st.execute("INSERT INTO questions (title, type, body, timestamp, anonymity, user_id, counta) VALUES ('" + t + "','" + type + "','"+ b + "','"+ time+ "','"+ anonymity + "','" + user_id + "', '0');" );
            st.close();
            conn.close();

        }
    }

}
