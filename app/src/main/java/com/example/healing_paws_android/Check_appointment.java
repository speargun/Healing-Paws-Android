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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    //  private int[] icons={R.drawable.jd,R.drawable.tmall,R.drawable.sina,R.drawable.qq_dizhu,R.drawable.qq,R.drawable.uc};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_appointment);

        final ArrayList<String> petsList = new ArrayList<>();
        final ArrayList<String> timeList = new ArrayList<>();

        Button back = findViewById(R.id.ca_b_back);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Check_appointment.this,MainActivity.class);
                intent.putExtra("login_status",1);
                startActivity(intent);
            }
        });

        try {
            acquire_info(petsList,timeList);
        }catch(SQLException e){
            String sql_error = getString(R.string.sql_error);
            Toast.makeText(Check_appointment.this,sql_error,Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        final ArrayList<Appointment> appointmentList = new ArrayList<>();
        for(int i = 0;i<petsList.size();i++){
            Appointment a = new Appointment(petsList.get(i),timeList.get(i));
            appointmentList.add(i,a);
        }

        Adapter adapter = new Adapter(Check_appointment.this, R.layout.list_item, appointmentList);
        ListView mListView=(ListView)findViewById(R.id.lv);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Check_appointment.this,Appointment_details.class);
                TextView atime = view.findViewById(R.id.tv_list2);
                intent.putExtra("atime",atime.getText().toString());
                startActivity(intent);
            }
        });
    }

    class Appointment {
        private String pet_name;
        private String datetime;
        Appointment(String pn, String dt){
            pet_name=pn;
            datetime=dt;
        }

        public String getDatetime() {
            return datetime;
        }

        public String getPet_name() {
            return pet_name;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public void setPet_name(String pet_name) {
            this.pet_name = pet_name;
        }
    }

    class Adapter extends ArrayAdapter<Appointment> {
        private int layoutId;
        Adapter(Context c, int id, List<Appointment> f){
            super(c,id,f);
            layoutId = id;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            Appointment appointment = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(layoutId,parent,false);
            if(appointment != null) {
                TextView tv1 = view.findViewById(R.id.tv_list1);
                tv1.setText(appointment.pet_name);
                TextView tv2 = view.findViewById(R.id.tv_list2);
                tv2.setText(appointment.datetime);
            }
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
                "    p.owner_id = u.id AND u.username = '"+username+"';");
        while (rs.next()) {
            pets.add(rs.getString(1));
        }
        rs.close();

        ResultSet rs1 = st.executeQuery("SELECT \n" +
                "    datetime\n" +
                "FROM\n" +
                "    appointments AS a,\n" +
                "    pets AS p,\n" +
                "    users AS u\n" +
                "WHERE\n" +
                "    a.pet_id = p.id AND p.owner_id = u.id AND u.username = '"+username+"';");
        while (rs1.next()) {
//            System.out.println(rs1.getString(1));
            String s = rs1.getString(1);
            s = s.substring(0, s.length()-2);
            time.add(s);
        }
        // 关闭资源
        rs1.close();
        st.close();
        conn.close();
    }
}