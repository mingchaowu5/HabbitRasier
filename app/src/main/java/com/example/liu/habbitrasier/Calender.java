package com.example.liu.habbitrasier;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

public class Calender extends AppCompatActivity {

    CalendarView cal_cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        cal_cal = (CalendarView) findViewById( R.id.cal_calendar);

        cal_cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = year+"/"+month+"/"+day;
                Log.d("Calendar", "Date changed: " + date);
            }
        });
    }
}
