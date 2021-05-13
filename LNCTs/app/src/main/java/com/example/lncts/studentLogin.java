package com.example.lncts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class studentLogin extends AppCompatActivity {
        String message;
        String date=new SimpleDateFormat("DD-MM-YYYY").format(new Date());
        Toolbar mToolbar;
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Teacher");
        private static long back_pressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        Bundle bundle=getIntent().getExtras();
        message=bundle.getString("message");
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        mToolbar.setTitle(message+"S Dashboard"+"("+date+")");
        TextView textView=(TextView)findViewById(R.id.textView1);

        textView.setText("Welcome :"+message);

    }
    public void viewAttendance(View v){
        Bundle basket=new Bundle();
        basket.putString("sid",message);

        Intent intent=new Intent(this,student_attendence_sheet.class);
        intent.putExtras(basket);
        startActivity(intent);
    }
    public void logoutStudent(View view){
        Intent logout=new Intent(studentLogin.this,LoginActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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