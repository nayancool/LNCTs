package com.example.lncts;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class teacherLogin extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String item,message;
    Toolbar mToolbar;
    private static long back_pressed;
    String date= new SimpleDateFormat("DD-MM-YYYY").format(new Date());

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        Spinner spinner2=(Spinner)findViewById(R.id.spinner2);

        //to get username from login page

        Bundle bundle1=getIntent().getExtras();
        message=bundle1.getString("message");
        mToolbar=(Toolbar)findViewById(R.id.takeattendancebar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(message+"'s Dashboard - "+date);

        TextView textView=(TextView)findViewById(R.id.textView1);
        textView.setText("Welcome : "+message);
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        List<String> categories=new ArrayList<>();
        categories.add("CSE");
        categories.add("ME");
        categories.add("CE");
        categories.add("EE");
        categories.add("DE");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);




    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    item=parent.getItemAtPosition(position).toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> args0) {

    }
    public void takeAttendanceButton(View view){
        Bundle basket=new Bundle();
        basket.putString("class_selected", item);
        basket.putString("tid",message);

        Intent intent=new Intent(this,take_attendence.class);
        intent.putExtras(basket);
        startActivity(intent);
    }
    public void prevoius_records(View view){
        Bundle basket=new Bundle();
        basket.putString("class_selected", item);
        basket.putString("tid",message);

        Intent intent=new Intent(this,teacher_attendence_sheet.class);
        intent.putExtras(basket);
        startActivity(intent);
    }
    public void logoutTeacher(View view){
        Intent logout=new Intent(teacherLogin.this,LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logout);
    }

    @Override
    public void onBackPressed() {
        if(back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
        else {
            Toast.makeText(getBaseContext(),"Press once again to exit",Toast.LENGTH_SHORT).show();
            back_pressed=System.currentTimeMillis();
        }
    }
}