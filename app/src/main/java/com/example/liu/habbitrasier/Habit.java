package com.example.liu.habbitrasier;

import java.util.Date;

/**
 * Created by liu on 3/10/19.
 */

public class Habit {

    private String HabitName;
    private Date StartDate;
    private String startDate;
    private Date EndDate;
    private String endDate;
    private String Frequency;
    private String Duration;
    private String Notification;
    private String Description;

    public Habit(String name, String des) {
        this.HabitName = name;
        this.Description = des;
    }


    public Habit(String name, Date startDate, Date endDate, String freq, String duration, String notif, String desc) {
        this.HabitName = name;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.Frequency = freq;
        this.Duration = duration;
        this.Notification = notif;
        this.Description = desc;
    }

    public Habit(String name, String startDate, String endDate, String freq, String duration, String notif, String desc) {
        this.HabitName = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.Frequency = freq;
        this.Duration = duration;
        this.Notification = notif;
        this.Description = desc;
    }

    public String getHabitName() {
        return HabitName;
    }

    public void setHabitName(String name) {
        this.HabitName = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getstartDate() {
        return startDate;
    }

    public void setstartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getendDate() {
        return endDate;
    }

    public void setendDate(String endDate) {
        this.endDate = endDate;
    }
    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        this.StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        this.EndDate = endDate;
    }
    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        this.Frequency = frequency;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        this.Duration = duration;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        this.Notification = notification;
    }


}
