package com.example.lncts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.DragStartHelper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.DescriptorProtos;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jxl.Cell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class take_attendence extends AppCompatActivity {
    String teacher_id,class_selected,peroidno;
    Spinner period;
    ArrayList<String> selecteditems;
    ArrayList<String> nonselecteditems;
    Toolbar mToolbar;

    ArrayList<String> ul;
    ListView listView;
    private ArrayAdapter adapter;
    ArrayList Userlist = new ArrayList<>();
    ArrayList Username= new ArrayList<>();
     DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
     DatabaseReference dbAttendance;
     String date=new SimpleDateFormat("DD=MM=YYYY").format(new Date());

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);

        mToolbar=(Toolbar)findViewById(R.id.takeattendancebar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        period=(Spinner)findViewById(R.id.spinner4);

        selecteditems=new ArrayList<String>();
        TextView classname=(TextView)findViewById(R.id.textView);
        classname.setText("CSE");

        Bundle bundle1=getIntent().getExtras();
        class_selected=bundle1.getString("class_selected");
        teacher_id=bundle1.getString("tid");

        classname.setText("class_selected");

        DatabaseReference dbuser=ref.child("Student");
        dbuser.orderByChild("classes").equalTo(class_selected).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dsp : snapshot.getChildren()){
                    Userlist.add(dsp.child("sid").getValue().toString());
                    Username.add(dsp.child("sname").getValue().toString());
                }
                OnStart(Userlist);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public  void OnStart(ArrayList<String>userlist){
        nonselecteditems=userlist;
        ListView chl=(ListView)findViewById(R.id.checkable_list);
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter<String> aa=new ArrayAdapter<>(this,R.layout.checkable_list_layout,R.id.txt_title,userlist);
        chl.setAdapter(aa);
        chl.setOnItemClickListener(((parent, view, position, id) -> {
            String selectedItem =((TextView) view).getText().toString();
            if(selecteditems.contains(selectedItem))
                selecteditems.remove(selectedItem);
            else
                selecteditems.add(selectedItem);
        }));
    }
    public void showSelectedItems(View view){
        String selItems="";
        peroidno = period.getSelectedItem().toString();
        if(peroidno.equals("Selected Period")){
            Toast.makeText(this,"Select a class",Toast.LENGTH_SHORT).show();
        }
        else
        {
            ref=FirebaseDatabase.getInstance().getReference();

            dbAttendance=ref.child("Attendance").child(date);
            for(String item : selecteditems){
                Toast.makeText(this,"Attendance created succesfully",Toast.LENGTH_SHORT).show();
                nonselecteditems.remove(item);
                dbAttendance.child(item).child(peroidno).setValue("P"+ " / " + teacher_id);
                if (selItems =="")
                    selItems=item;

                else
                    selItems+= "/" +item;




            }
            for(String item : nonselecteditems){
                Toast.makeText(this,"Attendance created succesfuly",Toast.LENGTH_SHORT).show();
                dbAttendance.child(item).child(peroidno).setValue("A" + " / " + teacher_id);
            }
        }
    }
    public void addtoreport(View view) throws IOException, BiffException {

        Workbook workbook = null;
        WritableWorkbook wb = null;
        WritableSheet s = null;
        try {
            workbook = Workbook.getWorkbook(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/online_Attendance/" + class_selected));
            wb = createWorkbook(class_selected + "_month_" + date.substring(3, 5), workbook);
            s = wb.getSheet(0);
        } catch (Exception e) {
            File wbfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/online_attendance/" + class_selected);
            wb = createWorkbook(class_selected + "_month_" + date.substring(3, 5));
            s = createSheet(wb, "_month_", 0);
        }
        int i = s.getColumns();
        if (i == 0) {
            try {
                Label newCell = new Label(0, 0, "Student_id");
                Label newCell2 = new Label(1, 0, "Student_name");
                WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                WritableCellFormat headerFormat = new WritableCellFormat(headerFont);

                headerFormat.setAlignment(Alignment.CENTRE);
                newCell.setCellFormat(headerFormat);
                newCell2.setCellFormat(headerFormat);
                s.addCell(newCell);
                s.addCell(newCell2);
            } catch (WriteException e) {
                e.printStackTrace();
            }
            for (Object item : Userlist) {
                int j = s.getRows();
                String name = Username.get(j - 1).toString();

                Label label = new Label(0, j, item.toString());
                Label label2 = new Label(1, j, name);

                try {
                    s.addCell(label);
                    s.addCell(label2);
                } catch (WriteException e) {
                    e.printStackTrace();

                }
            }

        }

        i = s.getColumns();

        int j = 1;
        try {
            Label newCell = new Label(i, 0, date);
            WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headerFormat = new WritableCellFormat(headerFont);

            headerFormat.setAlignment(Alignment.CENTRE);
            newCell.setCellFormat(headerFormat);
            s.addCell(newCell);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        for (Object item : Userlist) {
            Label label2;

            if (selecteditems.contains(item)) {
                label2 = new Label(i, j, "P");
            } else {
                label2 = new Label(i, j, "A");
            }
            j++;
            try {
                s.addCell(label2);
            } catch (Exception e) {
                Toast.makeText(this, "Unable to create Sheet", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        Date today = new Date();
        String tommorow = new SimpleDateFormat("DD-MM-YYYY").format(new Date(today.getTime() + (1000 * 60 * 60 * 24)));
        if (tommorow.substring(0, 2).equals("01")) {
            int row = s.getRows();
            int col = s.getColumns();
            String xx = "";
            int nop, tc;

            for (i = 0; i < row; i++) {
                nop = 0;
                tc = -2;
                for (int c = 0; c < col; c++) {
                    Cell z = s.getCell(c, i);
                    xx = z.getContents();
                    if (xx.equals("P"))
                        nop++;
                    if (!xx.isEmpty() || !xx.equals("")) {
                        tc++;
                    }
                    xx = xx + "\n";
                    Label label = new Label(col, i, "" + nop);
                    Label label2 = new Label(col + 1, i, nop * 100 / tc + "%");
                    try {
                        if (i == 0) {
                            label = new Label(col, i, "Total=" + tc);
                            label2 = new Label(col + 1, i, "percentage");
                        }
                        s.addCell(label);
                        s.addCell(label2);
                    } catch (WriteException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                wb.write();
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "sheet created succesfully", Toast.LENGTH_SHORT).show();
        }
    }
        public WritableWorkbook createWorkbook(String fileName ,Workbook workbook){
            WorkbookSettings wbSetting = new WorkbookSettings();
            wbSetting.setUseTemporaryFileDuringWrite(true);
            File sdcard=Environment.getExternalStorageDirectory();
            File dir=new File(sdcard.getAbsolutePath()+"/online_attendance");
            dir.mkdir();

            File wbfile=new File(dir,fileName+".xls");
            WritableWorkbook wb=null;
            try{
                wb=Workbook.createWorkbook(wbfile,workbook);

            }
            catch (IOException e){
                e.printStackTrace();
            }
            return wb;
        }
        public WritableWorkbook createWorkbook(String fileName){
            WorkbookSettings wbSetting = new WorkbookSettings();
            wbSetting.setUseTemporaryFileDuringWrite(true);
            File sdcard=Environment.getExternalStorageDirectory();
            File dir=new File(sdcard.getAbsolutePath()+"/online_attendance");
            dir.mkdir();
            File wbfile=new File(dir,fileName+".xls");
            WritableWorkbook wb=null;
            try{
                wb=Workbook.createWorkbook(wbfile,wbSetting);

            }
            catch (IOException e){
                e.printStackTrace();
            }
            return wb;
        }
            public WritableSheet createSheet(WritableWorkbook wb,String sheetName,int sheetIndex){
            return wb.createSheet(sheetName,sheetIndex);
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}