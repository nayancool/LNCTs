package com.example.lncts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class adminLogin extends AppCompatActivity {
    DatabaseReference ref;
    DatabaseReference dbStudent;
    DatabaseReference dbAttendance;
    DatabaseReference dbAdmin;

    Toolbar mToolbar;
    private static long back_pressed;

    ArrayList studentlist=new ArrayList<>();
    String date = new SimpleDateFormat("DD-MM-YYYY").format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        mToolbar.setTitle("Admin Dashboard : "+"("+date+")");
        ref= FirebaseDatabase.getInstance().getReference();
        dbStudent=ref.child("Student");

    }
    public void AddTeacherButton(View v){
        Intent intent=new Intent(this,addteacher.class);
        startActivity(intent);
    }
    public void AddStudentButton(View v){
        Intent intent=new Intent(this,addstudent.class);
        startActivity(intent);
    }
    public void attendanceRecord(View v){
        Intent intent=new Intent(this,admin_attendence_sheet.class);
        startActivity(intent);
    }
    public void CreateAttendance(View v){
        dbStudent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sid,P1="-",P2="-",P3="-",P4="-",P5="-",P6="-",P7="-",P8="-";
                Attendence_sheet a=new Attendence_sheet(P1,P2,P3,P4,P5,P6,P7,P8);

                for(DataSnapshot dsp : snapshot.getChildren()){
                    sid=dsp.child("sid").getValue().toString();
                    dbAttendance.child(date).child(sid).setValue(a);
                }
                Toast.makeText(getApplicationContext(),"Succesfully create",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something wrong",Toast.LENGTH_SHORT).show();

            }
        });
    }
    public  void logout(View v){
        Intent intent=new Intent(adminLogin.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void changepassword(View view){
        dbAdmin=ref.child("Admin");

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("Set your new password");
        final LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.change_password,null);
        final EditText password=(EditText)add_menu_layout.findViewById(R.id.newpassword);
        alertDialog.setView(add_menu_layout);
        alertDialog.setPositiveButton("Yes",((dialog, which) -> {
            if(!TextUtils.isEmpty(password.getText().toString()))
            {
                dbAdmin.child("Admin").setValue(password.getText().toString());
                Toast.makeText(adminLogin.this,"Succesfully changed",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(adminLogin.this,"Please enter new password",Toast.LENGTH_SHORT).show();
            }
        }));
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
        else
        {
            Toast.makeText(getBaseContext(),"Press once again to exit",Toast.LENGTH_SHORT).show();
            back_pressed=System.currentTimeMillis();

        }
    }
}