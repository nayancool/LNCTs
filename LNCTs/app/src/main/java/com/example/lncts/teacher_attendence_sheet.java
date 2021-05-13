package com.example.lncts;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class teacher_attendence_sheet extends AppCompatActivity {
        ListView listView;
        String teacher_id,class_selected;
        EditText date;
        ArrayList Userlist=new ArrayList<>();
        ArrayList Studentlist=new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbAttendance;
        DatabaseReference dbStudent;
        String required_date;
        Toolbar mToolbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendence_sheet);

        listView=(ListView)findViewById(R.id.list);
        date=findViewById(R.id.date);
        mToolbar=(Toolbar)findViewById(R.id.ftoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Previous record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle1=getIntent().getExtras();
        class_selected=bundle1.getString("class_selected");
        teacher_id=bundle1.getString("tid");

    }
    public void viewlist(View view){
        Userlist.clear();
        dbStudent=ref.child("Student");
        dbStudent.orderByChild("classes").equalTo(class_selected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dsp:snapshot.getChildren()){
                    Userlist.add(dsp.child("sid").getValue().toString());
                }
                display_list(Userlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void display_list(final ArrayList userlist){
        Studentlist.clear();
        required_date=date.getText().toString();
        dbAttendance=ref.child("attendance");
        Studentlist.add("   SID    "+"Status"+"   period");
        for (Object sid : userlist){
            dbAttendance.child(required_date).child(sid.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dsp : snapshot.getChildren()){
                        String p1=dsp.getValue().toString();
                        if((p1.equals("A / "+teacher_id))||(p1.equals("P / "+teacher_id))){
                            Studentlist.add(snapshot.getKey().toString()+"        "+p1.substring(0,1)+"     "+dsp.getKey());
                        }
                    }
                    list(Studentlist);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void list(ArrayList studentlist){
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1,studentlist);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}