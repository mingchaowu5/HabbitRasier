package com.example.liu.habbitrasier;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.MULTIPLE;

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
        mMinDate = today;
        mMaxDate = nextYear.getTime();

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
                    case R.id.pet:
                        Intent Pet = new Intent(Calender.this, PetActivity.class);
                        startActivity(Pet);
                        break;
                    default:
                        break;
                }
            }
        });

        // Get all the tasks from the db.
        populateTaskList();

        // Populate the list.
        taskListView = (ListView) findViewById(R.id.list_task_cal);
        mAdapter = new CustomListView(this, taskList);
        taskListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        //Long click listener for ListView - Delete
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = taskList.get(i).getHabitName();
                Intent Det = new Intent(Calender.this, HabitDetail.class);
                Det.putExtra("name", name);
                startActivity(Det);
            }
        });

        // For each task, display the dates on calendar.
        ArrayList<Date> datesColor1 = new ArrayList<>();
        ArrayList<Date> datesColor2 = new ArrayList<>();
        boolean goToColor1 = true;
        for (Habit h : taskList) {
            boolean startDateOk = (h.getStartDate() != null) &&
                    !(h.getStartDate().after(mMaxDate) || h.getStartDate().before(mMinDate));
            if (startDateOk) {
                addIntoColorList(h.getStartDate(), goToColor1, datesColor1, datesColor2);
            }
            if (h.getEndDate() != null) {
                if (h.getEndDate().after(mMaxDate) || h.getEndDate().before(mMinDate)) ;
                else {
                    if (startDateOk) {
                        // Add everyday between.
                        ArrayList<Date> dates = getDatesBetween(h.getStartDate(), h.getEndDate());
                        addAllIntoColorList(dates, goToColor1, datesColor1, datesColor2);
                    } else
                        addIntoColorList(h.getEndDate(), goToColor1, datesColor1, datesColor2);
                }
            }

            // switch color.
            goToColor1 = !goToColor1;
        }

        // Finally, display the calendar.
        calendar.init(mMinDate, mMaxDate)
                .inMode(MULTIPLE)
                .withSelectedDates(datesColor1)
                .withHighlightedDates(datesColor2)
                .displayOnly()
        ;
    }

    void addIntoColorList(Date value, boolean goToColor1, ArrayList<Date> datesColor1, ArrayList<Date> datesColor2) {
        if (goToColor1) datesColor1.add(value);
        else datesColor2.add(value);
    }

    void addAllIntoColorList(Collection<Date> value, boolean goToColor1, ArrayList<Date> datesColor1, ArrayList<Date> datesColor2) {
        if (goToColor1) datesColor1.addAll(value);
        else datesColor2.addAll(value);
    }

    public void populateTaskList() {
        taskList.clear();
        Cursor data = db.getData();
        if (data == null) {
            Toast.makeText(Calender.this, "No tasks to show.", Toast.LENGTH_SHORT).show();
        }
        while (data.moveToNext()) {
            //Get the value from the database and add to Arraylist
            String strStartDate = data.getString(data.getColumnIndex(DatabaseHelper.ColStartDate));
            String strEndDate = data.getString(data.getColumnIndex(DatabaseHelper.ColEndDate));
            Habit h = new Habit
                    (
                            data.getString(data.getColumnIndex(DatabaseHelper.ColHabitName)),
                            data.getString(data.getColumnIndex(DatabaseHelper.ColDescription))
                    );

            // dd/mm/yyyy
            // Try to parse the date.
            ArrayList<Integer> startDate = parse(strStartDate), endDate = parse(strEndDate);
            if (startDate == null || endDate == null) {
                // Leave them null.
            } else {
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
    }

    public ArrayList<Integer> parse(String strDate) {
        String[] strs = strDate.split("/");
        ArrayList<Integer> res = new ArrayList<>();
        if (strs.length != 3) return null;

        try {
            for (String s : strs)
                res.add(Integer.parseInt(s));
        } catch (Exception e) {
            return null;
        }
        return res;
    }

    public static ArrayList<Date> getDatesBetween(
            Date startDate, Date endDate) {
        ArrayList<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }
}