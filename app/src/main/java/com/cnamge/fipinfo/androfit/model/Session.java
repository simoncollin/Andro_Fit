package com.cnamge.fipinfo.androfit.model;

import com.orm.SugarRecord;

public class Session extends SugarRecord<Session> {
    String name;
    String location;
    long beginDate;
    long endDate;
    String description;

    public Session() {}

    public Session(String name, String location, long beginDate, long endDate, String description) {
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
}
