package com.example.liu.habbitrasier;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE;

public class Calender extends AppCompatActivity {

    private RadioGroup rgGroup; // Menu.

    CalendarPickerView calendar;
    final ArrayList<Habit> taskList = new ArrayList<>();
    DatabaseHelper db;
    ListView taskListView;
    private CustomListView mAdapter;
    Date mMinDate;
    Date mMaxDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar = (CalendarPickerView) findViewById(R.id.cal_calendar);
        Date today = new Date();
        calendar.init(mMinDate = today, mMaxDate = nextYear.getTime())
                .withSelectedDate(today)
                .inMode(RANGE);;

        // Get db.
        db = new DatabaseHelper(Calender.this);

        // Render menu bar.
        rgGroup = (RadioGroup) findViewById(R.id.rg_group);
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.achievement:
                        Intent Cal = new Intent(Calender.this, Achievement.class);
                        startActivity(Cal);
                        break;
                    case R.id.home:
                        Intent Home = new Intent(Calender.this, MainActivity.class);
                        startActivity(Home);
                        break;
                    case R.id.user:
                        Intent User = new Intent(Calender.this, User.class);
                        startActivity(User);
                        break;
                    default:
                        break;
                }
            }
        });

        // Get all the tasks from the db.
        render();
    }

    public void render(){
        taskList.clear();
        Cursor data = db.getData();
        if (data == null) {
            Toast.makeText(Calender.this, "No tasks to show.", Toast.LENGTH_SHORT).show();
        }
        while (data.moveToNext()) {
            //Get the value from the database and add to Arraylist
            String strStartDate =data.getString(data.getColumnIndex(DatabaseHelper.ColStartDate));
            String strEndDate=  data.getString(data.getColumnIndex(DatabaseHelper.ColEndDate));
            Habit h = new Habit
                    (
                            data.getString(data.getColumnIndex(DatabaseHelper.ColHabitName)),
                            data.getString(data.getColumnIndex(DatabaseHelper.ColDescription))
                    );

            // dd/mm/yyyy
            // Try to parse the date.
            ArrayList<Integer> startDate = parse(strStartDate), endDate = parse(strEndDate);
            if(startDate == null || endDate == null)
            {
                // Leave them null.
            }
            else{
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, startDate.get(2));
                calendar.set(Calendar.MONTH, startDate.get(1));
                calendar.set(Calendar.DAY_OF_MONTH, startDate.get(0));
                Date date = calendar.getTime();
                h.setStartDate(date);

                calendar.set(Calendar.YEAR, endDate.get(2));
                calendar.set(Calendar.MONTH, endDate.get(1));
                calendar.set(Calendar.DAY_OF_MONTH, endDate.get(0));
                date = calendar.getTime();
                h.setEndDate(date);
            }
            taskList.add(h);
        }

        // Populate the list.
        taskListView = (ListView) findViewById(R.id.list_task_cal);
        mAdapter = new CustomListView(this, taskList);
        taskListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // For each task, display the dates on calendar.
        for(Habit h : taskList){
            if(h.getStartDate() != null){
                if(h.getStartDate().after(mMaxDate) || h.getStartDate().before(mMinDate));
                else
                    calendar.selectDate(h.getStartDate(), true);
            }
            if(h.getEndDate()!=null){
                if(h.getEndDate().after(mMaxDate) || h.getEndDate().before(mMinDate));
                else
                    calendar.selectDate(h.getEndDate(), true);
            }
        }
    }

    public ArrayList<Integer> parse(String strDate) {
         String[] strs =  strDate.split("/");
         ArrayList<Integer> res = new ArrayList<>();
         if(strs.length!=3) return null;

         try{
             for (String s:strs)
            res.add(Integer.parseInt(s));
         }catch  (Exception e){
             return null;
         }
         return res;
    }
}
