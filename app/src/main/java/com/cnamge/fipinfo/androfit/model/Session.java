package com.cnamge.fipinfo.androfit.model;

import com.orm.SugarRecord;

public class Session extends SugarRecord<Session> {
    private String name;
    private String location;
    private long beginDate;
    private long endDate;
    private String description;

    public Session() {}

    public Session(String name, String location, long beginDate, long endDate, String description) throws Exception {
        if (beginDate > endDate) {
            throw new Exception("endDate should be greater than beginDate");
        }

        this.name        = name;
        this.location    = location;
        this.beginDate   = beginDate;
        this.endDate     = endDate;
        this.description = description;
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
        // TODO team back
        return "09/10/2019"; // Il faut bien respecter ce format
    }

    public String getDurationString(){
        // TODO team back
        return "1:00h"; // Il faut bien respecter ce format
    }
}
