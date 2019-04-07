package com.example.liu.habbitrasier;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
        //click listener for ListView - view detail
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

        // Initialize notification.
        createNotificationChannel();    // Creating channel is required.
        String CHANNEL_ID = getString(R.string.notif_id);

        // Set a tapping handler.
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle("Habit Raiser")
                .setContentText("It's time to do the task.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Send a notification.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(R.string.notif_id, builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notif_name);
            String description = getString(R.string.notif_des);
            String CHANNEL_ID = getString(R.string.notif_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
