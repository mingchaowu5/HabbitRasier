package com.example.liu.habbitrasier;

import java.util.Date;

/**
 * Created by liu on 3/10/19.
 */

public class Habit {

    private String HabitName;
    private Date StartDate;
    private Date EndDate;
    private String Frequency;
    private String Duration;
    private Boolean Notification;
    private String Description;

    public Habit(String name, String des) {
        this.HabitName = name;
        this.Description = des;
    }


    public Habit(String name, Date startDate, Date endDate, String freq, String duration, Boolean notif, String desc) {
        this.HabitName = name;
        this.StartDate = startDate;
        this.EndDate = endDate;
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

    public Boolean getNotification() {
        return Notification;
    }

    public void setNotification(Boolean notification) {
        this.Notification = notification;
    }


}
