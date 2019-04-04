package com.example.liu.habbitrasier;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class Task extends AppCompatActivity {
    private Button save, cancel;
    private RadioGroup RgGroup;
    DatabaseHelper db;
//    private Habit habit;
    private TextView editStartDate;
    private TextView editEndDate;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private CheckBox everyDay;
    private CheckBox weekDay;
    private CheckBox weekEnd;
    private CheckBox Mon;
    private CheckBox Tue;
    private CheckBox Wed;
    private CheckBox Thur;
    private CheckBox Fri;
    private CheckBox Sat;
    private CheckBox Sun;
    private String old;

    private String repeated;

    private TextView mSelectedForDatePick = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        db = new DatabaseHelper(Task.this);
        everyDay = (CheckBox) findViewById(R.id.every);
        weekDay = (CheckBox)findViewById(R.id.weekD);
        weekEnd = (CheckBox)findViewById(R.id.weekE);
        Mon = (CheckBox) findViewById(R.id.mon);
        Tue = (CheckBox) findViewById(R.id.tues);
        Wed = (CheckBox) findViewById(R.id.wed);
        Thur = (CheckBox) findViewById(R.id.thur);
        Fri = (CheckBox) findViewById(R.id.fri);
        Sat = (CheckBox) findViewById(R.id.sat);
        Sun = (CheckBox) findViewById(R.id.sun);


        editStartDate = (TextView)findViewById(R.id.editStartDate);
        editEndDate = (TextView)findViewById(R.id.editEndDate);

        Intent Det = getIntent();
        old = Det.getStringExtra("name");

        if (old != null){
            editHabit();
        }

        everyDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Mon.setChecked(true);
                    Tue.setChecked(true);
                    Wed.setChecked(true);
                    Thur.setChecked(true);
                    Fri.setChecked(true);
                    Sat.setChecked(true);
                    Sun.setChecked(true);
                    weekDay.setChecked(true);
                    weekEnd.setChecked(true);
                    repeated = "EveryDay";
                }
                else{
                    Mon.setChecked(false);
                    Tue.setChecked(false);
                    Wed.setChecked(false);
                    Thur.setChecked(false);
                    Fri.setChecked(false);
                    Sat.setChecked(false);
                    Sun.setChecked(false);
                    weekDay.setChecked(false);
                    weekEnd.setChecked(false);
                    repeated = null;
                }
            }
        });
        weekDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Mon.setChecked(true);
                    Tue.setChecked(true);
                    Wed.setChecked(true);
                    Thur.setChecked(true);
                    Fri.setChecked(true);
                    repeated = "Weekdays";
                }
                else{
                    Mon.setChecked(false);
                    Tue.setChecked(false);
                    Wed.setChecked(false);
                    Thur.setChecked(false);
                    Fri.setChecked(false);
                    repeated = null;
                }
            }
        });
        weekEnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Sat.setChecked(true);
                    Sun.setChecked(true);
                    repeated = "Weekends";
                }
                else{
                    Sat.setChecked(false);
                    Sun.setChecked(false);
                    repeated = null;
                }
            }
        });
        // bind date picker.
        editStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ShowDatePicker();
                mSelectedForDatePick = editStartDate;
            }
        });
        editEndDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ShowDatePicker();
                mSelectedForDatePick = editEndDate;
            }
        });
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if(mSelectedForDatePick != null)
                    ApplyDatePicker(mSelectedForDatePick, year, month, day);
            }
        };

        //TODO
        // access task data here

        RgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.calender:
                        Intent Cal = new Intent(Task.this, Calender.class);
                        startActivity(Cal);
                        break;
                    case R.id.home:
                        Intent Home = new Intent(Task.this, MainActivity.class);
                        startActivity(Home);
                        break;
                    case R.id.user:
                        Intent User = new Intent(Task.this, User.class);
                        startActivity(User);
                        break;
                    case R.id.achievement:
                        Intent Ach = new Intent(Task.this, Achievement.class);
                        startActivity(Ach);
                        break;
                    default:
                        break;
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO
                //add/update data here
                AlertDialog.Builder builder;
                String name = ((EditText) findViewById(R.id.editTaskName)).getText().toString();
                String startDate = ((TextView) findViewById(R.id.editStartDate)).getText().toString();
                String endDate =((TextView) findViewById(R.id.editEndDate)).getText().toString();
                String repeat = repeated;
                String duration =((EditText) findViewById(R.id.editDuration)).getText().toString();
                String notification =((TextView) findViewById(R.id.textNotification)).getText().toString();
                String description = ((EditText) findViewById(R.id.editDescription)).getText().toString();
                int dur = Integer.parseInt(duration);
                System.out.println("-----------------------------" + name + description);
                if(name == null||startDate == null|| endDate == null||repeated == null|| duration == null ){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(Task.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(Task.this);
                    }
                    builder.setTitle("You did not enter enough information")
                            .setMessage("You cannot create a habit with information missing. You have to fill all the blocks with * ")
                            .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {
                    if (dur>24){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(Task.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(Task.this);
                        }
                        builder.setTitle("The number of duration is wrong")
                                .setMessage("You cannot set your duration more than 24.")
                                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                    else {
                        db.addData(name, startDate, endDate, repeat, duration, notification, description);
                        Intent Task = new Intent(Task.this, MainActivity.class);
                        startActivity(Task);
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Task = new Intent(Task.this, MainActivity.class);
                startActivity(Task);
            }
        });

    }



    private void ShowDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(
                Task.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                mOnDateSetListener,
                year,month,day
        );
        dpd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dpd.show();
    }


    private void ApplyDatePicker(TextView textView, int year, int month, int day){
        month = month +1;

        String date = month + "/" + day + "/" + year;
        textView.setText(date);
    }

    private void editHabit(){
        Habit Data = db.getHabit(old);
        ((EditText) findViewById(R.id.editTaskName)).setText(old);
        ((TextView) findViewById(R.id.editStartDate)).setText(Data.getstartDate());
        ((TextView) findViewById(R.id.editEndDate)).setText(Data.getendDate());
        ((EditText) findViewById(R.id.editDuration)).setText(Data.getDuration());
        ((EditText) findViewById(R.id.editDescription)).setText(Data.getDescription());
        ((TextView) findViewById(R.id.textNotification)).setText(Data.getNotification());
        if(Data.getFrequency() == "EveryDay"){
            everyDay.setChecked(true);
        }
        if(Data.getFrequency() == "Weekdays"){
            weekDay.setChecked(true);
        }
        if(Data.getFrequency() == "Weekends"){
            weekEnd.setChecked(true);
        }

    }
}
