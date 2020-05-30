package com.example.healing_paws_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Driver;
import java.util.Calendar;
import java.util.Map;

import static android.view.View.inflate;




public class Check_appointment extends AppCompatActivity {

    private ListView mListView;

    final ArrayList<String> petsList = new ArrayList<>();

    final ArrayList<String> timeList = new ArrayList<>();


    final String[] pets = new String[petsList.size()];

    final String[] time = new String[petsList.size()];



    //  private int[] icons={R.drawable.jd,R.drawable.tmall,R.drawable.sina,R.drawable.qq_dizhu,R.drawable.qq,R.drawable.uc};
    @Override




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_appointment);





        try {
            acquire_info(petsList,timeList);
        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(Check_appointment.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        int i = 0;
        while(i<petsList.size()){
            pets[i] = petsList.get(i);

            i++;
        }

        i = 0;
        while(i<timeList.size()){
            time[i] = timeList.get(i);

            i++;
        }













        mListView=(ListView)findViewById(R.id.lv);
        mListView.setAdapter(new MyBaseAdapter());




        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                TextView atime = (TextView) view.findViewById(R.id.tv_list);

                intent.putExtra("atime",atime.getText().toString());

                intent = new Intent(Check_appointment.this,Appointment_details.class);
                startActivity(intent);
            }
        });





    }





    class MyBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return time.length;
        }
        @Override
        public Object getItem(int position) {
            return time [position];
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {//组装数据
            View view=View.inflate(Check_appointment.this,R.layout.list_item,null);//在list_item中有两个id,现在要把他们拿过来
            TextView mTextView=(TextView) view.findViewById(R.id.tv_list);
            //ImageView imageView=(ImageView)view.findViewById(R.id.image);
//组件一拿到，开始组装
            mTextView.setText(time[position]);
            //imageView.setBackgroundResource(icons[position]);
//组装玩开始返回
            return view;

        }



    }







    public void acquire_info(ArrayList<String> pets,ArrayList<String> time) throws SQLException {

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
                "    name\n" +
                "FROM\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    p.owner_id = u.id AND has datetime AND u.username = '"+username+"';");
        while (rs.next()) {
            pets.add(rs.getString(1));
        }
        rs.close();

        ResultSet rs1 = st.executeQuery("SELECT \n" +
                "    datetime\n" +
                "FROM\n" +
               "appointments As a\n"+
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                " a.pet_id = p.id AND p.owner_id = u.id AND u.username = '"+username+"';");
        while (rs1.next()) {
//            System.out.println(rs1.getString(1));
            time.add(rs1.getString(1));
        }
        // 关闭资源
        rs1.close();



        st.close();
        conn.close();

    }


}