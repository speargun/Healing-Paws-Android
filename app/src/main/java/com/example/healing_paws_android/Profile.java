package com.example.healing_paws_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView name = findViewById(R.id.username);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        TextView dob = findViewById(R.id.dob);
        TextView address = findViewById(R.id.address);
        Button back = findViewById(R.id.back);
        Button add = findViewById(R.id.add_pet);

        final ArrayList<String> pet_name_List = new ArrayList<>();
        final ArrayList<String> pet_age_list = new ArrayList<>();
        final ArrayList<String> pet_category_List = new ArrayList<>();

        try {
            profile(name, phone, email, dob, address,pet_name_List,pet_age_list,pet_category_List);
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

        final ArrayList<Pet> petList = new ArrayList<>();
        for(int i = 0;i<pet_name_List.size();i++){
            Pet a = new Pet(pet_name_List.get(i),pet_age_list.get(i),pet_category_List.get(i));
            petList.add(i,a);
        }

        Adapter adapter = new Adapter(Profile.this, R.layout.pet_list, petList);
        ListView mListView=findViewById(R.id.list_view);
        mListView.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this,Add_Pet.class);
                String s = "";
                intent.putExtra("amount",pet_name_List.size());
                for(int i = 0;i<pet_name_List.size();i++){
                    s = "pet" + i;
                    intent.putExtra(s,pet_name_List.get(i));
                }
                startActivity(intent);
            }
        });

    }

    class Pet {
        private String pet_name;
        private String pet_age;
        private String category;
        Pet(String pn, String pa, String c){
            pet_name=pn;
            pet_age=pa;
            category=c;
        }

        public String getPet_name() {
            return pet_name;
        }

        public String getCategory() {
            return category;
        }

        public String getPet_age() {
            return pet_age;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setPet_age(String pet_age) {
            this.pet_age = pet_age;
        }

        public void setPet_name(String pet_name) {
            this.pet_name = pet_name;
        }
    }

    class Adapter extends ArrayAdapter<Pet> {
        private int layoutId;
        Adapter(Context c, int id, List<Pet> f){
            super(c,id,f);
            layoutId = id;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            final Pet pet = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(layoutId,parent,false);
            if(pet != null) {
                final TextView pet_name = view.findViewById(R.id.pet_name);
                String s = getString(R.string.pet_name)+"  "+pet.pet_name;
                pet_name.setText(s);
                TextView pet_age = view.findViewById(R.id.pet_age);
                s = getString(R.string.pet_age)+"  "+pet.pet_age;
                pet_age.setText(s);
                TextView pet_category = view.findViewById(R.id.pet_category);
                s = getString(R.string.pet_category)+"  "+pet.category;
                pet_category.setText(s);
                Button remove_pet = view.findViewById(R.id.remove_pet);

                remove_pet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            remove_pet(pet.pet_name);
                            String remove_success = getString(R.string.remove_success);
                            Toast.makeText(Profile.this,remove_success,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Profile.this,Profile.class);
                            startActivity(intent);
                        }catch(SQLException e){
                            String sql_error = getString(R.string.sql_error);
                            Toast.makeText(Profile.this,sql_error,Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
            return view;
        }
    }

    public void profile(TextView name, TextView phone, TextView email, TextView dob, TextView address, ArrayList<String> pet_name_List, ArrayList<String> pet_age_List, ArrayList<String> pet_category_List) throws SQLException{
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

        String sql = "use test";
        st = conn.prepareStatement(sql);
        st.execute();

        name.setText(username);
        sql = "SELECT phone, email, dob, address FROM customers LEFT JOIN  users ON users.id WHERE username= ?;";
        st = conn.prepareStatement(sql);
        st.setString(1,username);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            phone.setText(rs.getString(1));
            email.setText(rs.getString(2));
            dob.setText(rs.getString(3));
            address.setText(rs.getString(4));
        }

        rs.close();

        sql = "SELECT \n" +
                "    name, age, category\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    p.owner_id = u.id AND u.username = ?;";
        st = conn.prepareStatement(sql);
        st.setString(1,username);
        ResultSet rs1 = st.executeQuery();
        while (rs1.next()) {
            pet_name_List.add(rs1.getString(1));
            pet_age_List.add(rs1.getString(2));
            pet_category_List.add(rs1.getString(3));
        }

        rs1.close();
        st.close();
        conn.close();

    }

    public void remove_pet(String pet_name)throws SQLException{
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

        String sql = "use test";
        st = conn.prepareStatement(sql);
        st.execute();

        sql = "DELETE FROM appointments \n" +
                "WHERE\n" +
                "    pet_id = (SELECT \n" +
                "        p.id\n" +
                "    FROM\n" +
                "        pets AS p,\n" +
                "        users AS u\n" +
                "    \n" +
                "    WHERE\n" +
                "        name = ? AND u.id = p.owner_id\n" +
                "        AND u.username = ?);\n";

        st = conn.prepareStatement(sql);
        st.setString(1,pet_name);
        st.setString(2,username);
        st.executeUpdate();

         sql = "DELETE FROM pets \n" +
                "WHERE\n" +
                "    name = ?\n" +
                "    AND owner_id = (SELECT \n" +
                "        id\n" +
                "    FROM\n" +
                "        users\n" +
                "    \n" +
                "    WHERE\n" +
                "        username = ?);";

        st = conn.prepareStatement(sql);
        st.setString(1,pet_name);
        st.setString(2,username);
        st.executeUpdate();
        st.close();
        conn.close();

    }
}
