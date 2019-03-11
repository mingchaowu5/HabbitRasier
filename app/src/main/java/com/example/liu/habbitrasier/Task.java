package com.example.liu.habbitrasier;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class Task extends AppCompatActivity {
    private Button save, cancel;
    private RadioGroup RgGroup;
    DatabaseHelper db;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        db = new DatabaseHelper(Task.this);

        //TODO
        // access task data here

        RgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
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
                // add/update data here

                Intent Task = new Intent(Task.this, MainActivity.class);
                startActivity(Task);
            }
        });
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Task = new Intent(Task.this, MainActivity.class);
                startActivity(Task);
            }
        });
    }


}
