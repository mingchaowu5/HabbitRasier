package com.example.liu.habbitrasier;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE;

public class Calender extends AppCompatActivity {

    CalendarPickerView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar = (CalendarPickerView) findViewById(R.id.cal_calendar);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today)
                .inMode(RANGE);;


        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }
}
