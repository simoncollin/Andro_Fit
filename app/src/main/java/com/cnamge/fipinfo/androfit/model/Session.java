package com.cnamge.fipinfo.androfit.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Session extends SugarRecord<Session> {
    private String name;
    private String location;
    private long beginDate;
    private long endDate;
    private String description;
    private long calendarEventId;

    public Session() {}

    public Session(String name, String location, long beginDate, long endDate, String description, long calendarEventId) throws Exception {
        if (beginDate > endDate) {
            throw new Exception("End date should be greater than begin date");
        }

        this.name            = name;
        this.location        = location;
        this.beginDate       = beginDate;
        this.endDate         = endDate;
        this.description     = description;
        this.calendarEventId = calendarEventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception{
        if (name.length() > 16)
            throw new Exception("Session name mustn't exceed 16 characters");
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) throws Exception{
        if (beginDate > endDate) {
            throw new Exception("Begin date should not be greater than end date");
        }
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) throws Exception{
        if (beginDate > endDate) {
            throw new Exception("End date should be greater than begin date");
        }
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public long getCalendarEventId() {
        return calendarEventId;
    }

    public void setCalendarEventId(long calendarEventId) {
        this.calendarEventId = calendarEventId;
    }

    @Override
    public String toString() {
        return "Session{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }

    public String getBeginDateString(){
        Date date = new Date(this.beginDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/YYYY", Locale.FRANCE);
        return df2.format(date); // Format 09/10/2019
    }

    public String getBeginDateHourString(){
        Date date = new Date(this.beginDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/YYYY - HH:mm", Locale.FRANCE);
        return df2.format(date); // Format 09/10/2019
    }

    public String getEndDateHourString(){
        Date date = new Date(this.endDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/YYYY - HH:mm", Locale.FRANCE);
        return df2.format(date); // Format 09/10/2019
    }

    public String getDurationString(){
        Date beg = new Date(this.beginDate);
        Date end = new Date(this.endDate + 60000);
        beg.setSeconds(0);
        end.setSeconds(0);

        long diff = end.getTime() - beg.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        String hours = String.format("%02d", diffHours);
        String min = String.format("%02d", diffMinutes);

        return hours + ":" + min + "h";
    }
}
