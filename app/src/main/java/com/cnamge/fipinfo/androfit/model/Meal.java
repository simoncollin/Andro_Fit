package com.cnamge.fipinfo.androfit.model;

import androidx.annotation.NonNull;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Meal extends SugarRecord<Meal> {
    private String name;
    private long date;
    private long time;
    private String description;
    private String image_url;
    private long calendarEventId;

    public Meal() {}

    public Meal(String name, long date, long time, String description, String image_url, long calendarEventId) {
        this.name        = name;
        this.date        = date;
        this.time        = time;
        this.description = description;
        this.image_url   = image_url;
        this.calendarEventId = calendarEventId;
    }

    public Meal(String s, long l, String desc, String s1) {
        this.name = s;
        this.date = l;
        this.description = desc;
        this.image_url = s1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public long getTime() {
        return time;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getimage_url() {
        return image_url;
    }

    public void setimage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setTime (long time){
        this.time = time;
    }

    public long getCalendarEventId() {
        return calendarEventId;
    }

    public void setCalendarEventId(long calendarEventId) {
        this.calendarEventId = calendarEventId;
    }


    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", id=" + id +
                '}';
    }

    public String getDateString(){
        Date date = new Date(this.date);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/YYYY", Locale.FRANCE);
        return df2.format(date); // Format 09/10/2019
    }

    public String getTimeString(){
        long diff = this.time;
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        return df2.format(diff); // Format 01:00
    }
}
