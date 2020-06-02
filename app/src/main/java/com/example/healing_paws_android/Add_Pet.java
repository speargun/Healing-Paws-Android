package com.example.healing_paws_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Add_Pet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__pet);

        Button back = findViewById(R.id.ap_b_back);
        Button add = findViewById(R.id.ap_b_add);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Add_Pet.this,Profile.class);
                startActivity(intent);
            }
        });

        final ArrayList<String> pet_name_List = new ArrayList<>();
        Intent intent = getIntent();
        int amount = intent.getIntExtra("amount",0);
        String s = "";
        for(int i = 0;i<amount;i++){
            s = "pet" + i;
            s = intent.getStringExtra(s);
            pet_name_List.add(i,s);
        }

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText name = findViewById(R.id.ap_tv_pet_name);
                EditText age = findViewById(R.id.ap_tv_pet_age);
                RadioButton cat = findViewById(R.id.ap_rb_cat);

                String pet_name = String.valueOf(name.getText());
                String pet_age = String.valueOf(age.getText());
                String category = "dog";
                if(cat.isChecked()){
                    category = "cat";
                }
                if(!Register.isUsername(pet_name)){
                    String pet_name_invalid = getString(R.string.pet_name_invalid);
                    Toast.makeText(Add_Pet.this,pet_name_invalid,Toast.LENGTH_SHORT).show();
                }else if(!isDuplicate(pet_name_List,pet_name)){
                    String pet_name_duplicate = getString(R.string.pet_name_duplicate);
                    Toast.makeText(Add_Pet.this,pet_name_duplicate,Toast.LENGTH_SHORT).show();
                } else if(!isAge(pet_age)) {
                    String age_invalid = getString(R.string.age_invalid);
                    Toast.makeText(Add_Pet.this,age_invalid,Toast.LENGTH_SHORT).show();
                }else
                    {
                    try {
                        add_pet(pet_name, pet_age, category);
                        Intent intent = new Intent(Add_Pet.this, Profile.class);
                        startActivity(intent);
                    } catch (SQLException e) {
                        String sql_error = getString(R.string.sql_error);
                        Toast.makeText(Add_Pet.this, sql_error, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void add_pet(String name,String age, String category) throws SQLException{

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
        String sql = "use test;";
        st = conn.prepareStatement(sql);
        st.execute();
        sql = "insert into pets (name,age,category,owner_id) values (?,?,?,(select id from users where username = ?));";
        st = conn.prepareStatement(sql);
        st.setString(1,name);
        st.setString(2,age);
        st.setString(3,category);
        st.setString(4,username);
        // 获得结果 集合
        // 关闭资源
        st.executeUpdate();
        st.close();
        conn.close();
    }

    public static boolean isAge(String age) {
        Pattern p = Pattern.compile("^(?:[1-9][0-9]?|1[01][0-9]|30)$");
        Matcher m = p.matcher(age);
        return m.matches();
    }

    public static boolean isDuplicate(ArrayList<String> name_list,String pet_name){
        for(int i = 0;i<name_list.size();i++){
            if(name_list.get(i).equals(pet_name)){
                return false;
            }
        }
        return true;
    }
}
