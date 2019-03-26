package com.example.liu.habbitrasier;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RadioGroup RgGroup;
    private ImageButton plus;
    final ArrayList<Habit> habitList = new ArrayList<>();
    DatabaseHelper db;
    ListView lst;
    private CustomListView mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        plus = (ImageButton) findViewById(R.id.imageButton2);
        db = new DatabaseHelper(MainActivity.this);
        //TODO
        //initial fake data
        //db.addData("Study HCI","","","","",""," Read Critiques");
        populateListView();
        //Long click listener for ListView - Delete
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = habitList.get(i).getHabitName();
                Intent Det = new Intent(MainActivity.this, HabitDetail.class);
                Det.putExtra("name", name);
                startActivity(Det);
            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Task = new Intent(MainActivity.this, Task.class);
                startActivity(Task);
            }
        });

        RgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.calender:
                        Intent Cal = new Intent(MainActivity.this, Calender.class);
                        startActivity(Cal);
                        break;
                    case R.id.achievement:
                        Intent Ach = new Intent(MainActivity.this, Achievement.class);
                        startActivity(Ach);
                        break;
                    case R.id.user:
                        Intent User = new Intent(MainActivity.this, User.class);
                        startActivity(User);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    //Populate listview
    private void populateListView() {
        habitList.clear();
        Cursor data = db.getData();
        if (data == null) {
            Toast.makeText(MainActivity.this, "NOTHING HERE", Toast.LENGTH_SHORT).show();
        }
        while (data.moveToNext()) {
            //Get the value from the database and add to Arraylist
            habitList.add(new Habit(data.getString(data.getColumnIndex("habitName")), data.getString(data.getColumnIndex("description"))));
        }

        lst = (ListView) findViewById(R.id.lst);
        mAdapter = new CustomListView(this, habitList);
        lst.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}
