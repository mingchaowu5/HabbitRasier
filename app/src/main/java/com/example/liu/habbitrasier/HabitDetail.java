package com.example.liu.habbitrasier;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class HabitDetail extends AppCompatActivity {
    private RadioGroup RgGroup;
    private Button delete;
    private Button edit;
    private Button checkIn;
    private Button checkOut;
    DatabaseHelper db;
    String name;
    private TextView HabitName;
    private TextView StartD;
    private TextView EndD;
    private TextView Repeat;
    private TextView Duration;
    private TextView Notification;
    private TextView Priority;
    private TextView Description;
    Habit Data;
    Thread timerThread = null;

    boolean m_checkedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        Intent Det = getIntent();
        name = Det.getStringExtra("name");
        RgGroup = (RadioGroup) findViewById(R.id.rg_group);
        delete = (Button) findViewById(R.id.delete);
        edit = (Button) findViewById(R.id.edit);
        checkIn = (Button) findViewById(R.id.checkin);
        checkOut = (Button) findViewById(R.id.checkout);
        HabitName = (TextView) findViewById(R.id.name);
        StartD = (TextView) findViewById(R.id.start);
        EndD = (TextView) findViewById(R.id.end);
        Repeat = (TextView) findViewById(R.id.repeat);
        Duration = (TextView) findViewById(R.id.duration);
        Notification = (TextView) findViewById(R.id.notif);
        Priority = (TextView)findViewById(R.id.prior);
        Description = (TextView) findViewById(R.id.desc);
        db = new DatabaseHelper(HabitDetail.this);

        Data = db.getHabit(name);
        HabitName.setText("Habit Name: " + Data.getHabitName());
        StartD.setText("Start Date: " + Data.getstartDate());
        EndD.setText("End Date: " + Data.getendDate());
        Repeat.setText("Repeat: " + Data.getFrequency());
        Duration.setText("Duration: " + Data.getDuration() + " hours/day");
        Notification.setText("Notification: " + Data.getNotification());
        Priority.setText("Priority: "+Data.getPriority());
        Description.setText("Description: " + Data.getDescription());

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
                    case R.id.pet:
                        Intent Pet = new Intent(HabitDetail.this, PetActivity.class);
                        startActivity(Pet);
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

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check in.
                if(!m_checkedIn){
                    // Get the habit's timer.
                    float overallSeconds = 9;
                    try{
                        if(Data!=null){
                            // it's a string representing how many hours.
                            String strHowManyHours = Data.getDuration();
                            float howManyHours = Float.parseFloat(strHowManyHours);
                            overallSeconds = howManyHours * 3600;
                        }
                    }
                    catch (Exception e){

                    }
                    // Start the timer.
                    timerThread = new Thread(new HabitDoingTimer(overallSeconds, HabitDetail.this));
                    timerThread.start();

                    // give ui feedback.
                    Toast.makeText(getApplicationContext(),"You checked in. Let's do this task!",Toast.LENGTH_SHORT).show();

                    // flag checkedIn.
                    m_checkedIn = true;
                }
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check out.
                if(m_checkedIn){
                    // no need to interrupt the timer thread because it will stop automatically.
                    // give food and go to the activity.
                    PetDataHelper petdb = new PetDataHelper(HabitDetail.this);
                    petdb.changeFood(true,1);

                    // give ui feedback.
                    Toast.makeText(getApplicationContext(),"Task completed! You get 1 food.",Toast.LENGTH_SHORT).show();

                    // flag checkedIn.
                    m_checkedIn = false;
                }
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
                                int itemID = -1;
                                Cursor data = db.getItemID(name);
                                while (data.moveToNext()) {
                                    itemID = data.getInt(0);
                                }
                                if (itemID > -1) {
                                    db.deleteHabit(itemID, name);
                                }
                                Intent Home = new Intent(HabitDetail.this, MainActivity.class);
                                startActivity(Home);
                            }
                        })
                        .setNegativeButton("I'll try more", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    protected class HabitDoingTimer implements Runnable{

        AppCompatActivity m_activity = null;
        float m_overallSeconds = 0;
        NotificationCompat.Builder m_notifBuilder = null;
        NotificationManagerCompat m_notifManager = null;

        HabitDoingTimer(float overallSeconds, AppCompatActivity activity){
            this.m_overallSeconds = overallSeconds;
// /* For Debug */            this.m_overallSeconds = 3;
            this.m_activity = activity;

            // Set up notification stuff.
            // Set a tapping handler.
            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(m_activity, HabitDetail.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(m_activity, 0, intent, 0);

            // Build a notification.
            String CHANNEL_ID = getString(R.string.notif_id);
            Uri ringtoneNotif = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification2);
            m_notifBuilder = new NotificationCompat.Builder(m_activity, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Habit Raiser")
                    .setContentText("Read paper!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSound(ringtoneNotif)
                    .setVibrate(new long[] { 1000, 1000, 1000})
                    .setColor(Color.BLUE)
                    .setAutoCancel(true);
            m_notifManager = NotificationManagerCompat.from(m_activity);
        }

        void popNotification(){
            // notificationId is a unique int for each notification that you must define
            m_notifManager.notify(R.string.notif_id, m_notifBuilder.build());

            // Sound
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Uri ringtoneNotif = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification2);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ringtoneNotif);
//            r.play();
        }

        @Override
        public void run() {
            float interval = m_overallSeconds /3;
            float progress=interval;
            while(progress< m_overallSeconds){
                try {
                    Thread.sleep((long)(interval * 1000));
                    // Pop up a notification.
                    popNotification();

                    // Bump up the progress
                    progress+=interval;
                }
                catch (Exception e){

                }
            }
        }
    }

}

