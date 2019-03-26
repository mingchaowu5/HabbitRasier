package com.example.liu.habbitrasier;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class HabitDetail extends AppCompatActivity {
    private RadioGroup RgGroup;
    private Button delete;
    private Button edit;
    DatabaseHelper db;
    private String name;
    private TextView infor;
    private int back = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        Intent Det = getIntent();
        name = Det.getStringExtra("name");
        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        delete = (Button)findViewById(R.id.delete);
        edit = (Button)findViewById(R.id.edit);
        infor = (TextView) findViewById(R.id.information);

        //TODO
        infor.setText(name);

        RgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.home:
                        Intent Home = new Intent(HabitDetail.this, MainActivity.class);
                        startActivity(Home);
                        break;
                    case R.id.calender:
                        Intent Cal = new Intent(HabitDetail.this, Calender.class);
                        startActivity(Cal);
                        break;
                    case R.id.achievement:
                        Intent Ach = new Intent(HabitDetail.this, Achievement.class);
                        startActivity(Ach);
                        break;
                    case R.id.user:
                        Intent User = new Intent(HabitDetail.this, User.class);
                        startActivity(User);
                        break;
                    default:
                        break;
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Task = new Intent(HabitDetail.this, Task.class);
                Task.putExtra("name", name);
                startActivity(Task);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(HabitDetail.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(HabitDetail.this);
                }
                builder.setTitle("Never Give Up")
                        .setMessage("You can persevere to do it for a little bit longer! Don't give up so early please!")
                        .setPositiveButton("Sorry but delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor data = db.getItemID(name);
                                int itemID = -1;
                                while (data.moveToNext()) {
                                    itemID = data.getInt(0);
                                }
                                if (itemID > -1) {
                                    db.deleteHabit(itemID, name);
                                }
                                back = 1;

                            }
                        })
                        .setNegativeButton("I'll try more", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing
                                back = 0;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                if (back == 1){
                    Intent Home = new Intent(HabitDetail.this, MainActivity.class);
                    startActivity(Home);
                }
                else {}
            }
        });
    }
}
