package com.example.lncts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin_attendence_sheet extends AppCompatActivity {
ListView listView;
Spinner class_name;
String classes;
EditText date;
ArrayList Userlist = new ArrayList<>();
ArrayList Studentlist=new ArrayList<>();


DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
DatabaseReference dbAttendance;
DatabaseReference dbStudent;
String required_date;
Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendence_sheet);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Attendance records");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView=(ListView)findViewById(R.id.list);
        class_name=(Spinner)findViewById(R.id.spinner5);
        date=(EditText)findViewById(R.id.date);
        classes=class_name.getSelectedItem().toString();
    }
    public void display_list(final ArrayList userlist){
        Studentlist.clear();
        required_date=date.getText().toString();
        dbAttendance=ref.child("Attendence");
        dbAttendance.child(required_date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Result will be holded here

                Studentlist.add("      SID        "+"p1  "+"p2    "+"p3    "+"p4    "+"p5     "+"p6     "+"p7     "+"p8    ");
                for(Object sid : userlist){
                    String p1=snapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p2=snapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p3=snapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p4=snapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p5=snapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p6=snapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p7=snapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    String p8=snapshot.child(sid.toString()).child("p1").getValue().toString().substring(0,1);
                    Studentlist.add(snapshot.child(sid.toString()).getKey().toString()+"     "+p1+"    "+p2+"   "+p3+"   "+p4+"   "+p5+"   "+p6+"   "+p7+"   "+p8);

                }
                list(Studentlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void viewList(View v){
        Userlist.clear();
        dbStudent=ref.child("Student");
        dbStudent.orderByChild("Classes").equalTo(class_name.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot dsp : snapshot.getChildren()){
                    Userlist.add(dsp.child("SID").getValue().toString());
                }
                display_list(Userlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void list(ArrayList studentlist){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,studentlist);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}