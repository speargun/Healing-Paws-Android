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
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Question extends AppCompatActivity {

    private EditText title = findViewById(R.id.title);
    private EditText body = findViewById(R.id.body);
    private Button send = findViewById(R.id.send);

    private ListView listview = findViewById(R.id.list_view);
    private List<String> questionList = new ArrayList();

    private QuestionAdapter questionAdapter;

    public String qa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        try {
            initQuestion();
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
                i2.putExtra("question", qa);

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendQuestion();
                }catch(SQLException e){
                    String sql_error = getString(R.string.sql_error);
                    Toast.makeText(Question.this,sql_error,Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });



    }


    public void initQuestion() throws SQLException{
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
        for(int id=0; id<=100;id++){
            //无法根据用户名确定用户提过的所有问题
           // ResultSet rs = st.executeQuery("SELECT title FROM questions LEFT JOIN  users ON users.id WHERE id ="+ id +"username= '"+username+"';");
           // String q = rs.toString();
         //   if(q !=null){
           //     questionList.add(q);
         //   }
          //  rs.close();
        }
        st.close();
        conn.close();





    }

    public void sendQuestion() throws SQLException{
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

            ResultSet rs1 = st.executeQuery("Select ");

        }
    }

}
