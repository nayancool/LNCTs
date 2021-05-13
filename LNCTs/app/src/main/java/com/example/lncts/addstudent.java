package com.example.lncts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addstudent extends AppCompatActivity {
    EditText Sname;
    EditText Sid,spassword;
    String sname,sid,classname,spass;
    Spinner classes;
    DatabaseReference databaseStudent;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);

        databaseStudent= FirebaseDatabase.getInstance().getReference("Student");

        Sname=(EditText)findViewById(R.id.editText1);
        Sid=(EditText)findViewById(R.id.editText3);
        classes=(Spinner)findViewById(R.id.spinner3);
        spassword=(EditText)findViewById(R.id.editText4);
        mToolbar=(Toolbar) findViewById(R.id.ftoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add/Remove Student");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    public  void addStudent(View v){
        if(!(TextUtils.isEmpty(Sid.getText().toString()))){
            sname=Sname.getText().toString();
            sid=Sid.getText().toString();
            classname=classes.getSelectedItem().toString();
            spass=spassword.getText().toString();

            student student=new student(sname,sid,classname,spass);
            databaseStudent.child(sid).setValue(student);
            Toast.makeText(getApplicationContext(),"Student added succesfully",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
        }
    }
    public void removeStudent(View v){
        if(!TextUtils.isEmpty(Sid.getText().toString())){
            sid=Sid.getText().toString();
            databaseStudent.child(sid).setValue(null);
            Toast.makeText(getApplicationContext(),"Student removed succesfully",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"Id cannot be empty",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}