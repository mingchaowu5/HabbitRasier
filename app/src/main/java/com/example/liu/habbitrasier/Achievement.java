package com.example.liu.habbitrasier;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class Achievement extends AppCompatActivity {
    private RadioGroup RgGroup;
    final ArrayList<Habit> achList = new ArrayList<>();
    DatabaseHelper db;
    ListView achlst;
    private CustomListView mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        db = new DatabaseHelper(Achievement.this);
//        populateListView();

        RgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.calender:
                        Intent Cal = new Intent(Achievement.this, Calender.class);
                        startActivity(Cal);
                        break;
                    case R.id.home:
                        Intent Home = new Intent(Achievement.this, MainActivity.class);
                        startActivity(Home);
                        break;
                    case R.id.pet:
                        Intent Pet = new Intent(Achievement.this, PetActivity.class);
                        startActivity(Pet);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //Populate listview
    private void populateListView() {
        achList.clear();
        Cursor data = db.getData();
        if (data == null) {
            Toast.makeText(Achievement.this, "NOTHING HERE", Toast.LENGTH_SHORT).show();
        }
        while (data.moveToNext()) {
            //Get the value from the database and add to Arraylist
            achList.add(new Habit(data.getString(data.getColumnIndex("habitName")), data.getString(data.getColumnIndex("description")),data.getString(data.getColumnIndex("priority"))));
        }

        achlst = (ListView) findViewById(R.id.achlst);
        mAdapter = new CustomListView(this, achList);
        achlst.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }
}
