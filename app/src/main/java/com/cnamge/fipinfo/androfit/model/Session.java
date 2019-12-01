package com.cnamge.fipinfo.androfit.model;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Session extends SugarRecord<Session> {
    private String name;
    private String location;
    private long beginDate;
    private long endDate;
    private String description;
    private long calendarEventId;

    public Session() {}

    public Session(String name, String location, long beginDate, long endDate, String description) throws Exception {
        if (beginDate > endDate) {
            throw new Exception("endDate should be greater than beginDate");
        }

        this.name            = name;
        this.location        = location;
        this.beginDate       = beginDate;
        this.endDate         = endDate;
        this.description     = description;
        this.calendarEventId = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/YYYY");
        return df2.format(date); // Format 09/10/2019
    }

    public String getBeginDateHourString(){
        Date date = new Date(this.beginDate);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/YYYY - hh:mm");
        return df2.format(date); // Format 09/10/2019
    }

    public String getDurationString(){
        long diff = this.endDate - this.beginDate;
        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm");
        String res = df2.format(diff) + "h"; // Format 01:00
        return res;
    }
}
