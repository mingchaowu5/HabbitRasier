package com.example.liu.habbitrasier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

public class User extends AppCompatActivity {
    private RadioGroup RgGroup;
    UserDataHelper userdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        userdb = new UserDataHelper(User.this);

        //TODO
        // access user data here

        RgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.calender:
                        Intent Cal = new Intent(User.this, Calender.class);
                        startActivity(Cal);
                        break;
                    case R.id.home:
                        Intent Home = new Intent(User.this, MainActivity.class);
                        startActivity(Home);
                        break;
                    case R.id.user:
                        Intent User = new Intent(User.this, User.class);
                        startActivity(User);
                        break;
                    case R.id.achievement:
                        Intent Ach = new Intent(User.this, Achievement.class);
                        startActivity(Ach);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
