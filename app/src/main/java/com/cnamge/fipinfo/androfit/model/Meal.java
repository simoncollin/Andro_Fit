package com.cnamge.fipinfo.androfit.model;

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
    private User creator;

    public Meal() {}

    public Meal(String name, long date, long time, String description, String image_url) {
        this.name        = name;
        this.date        = date;
        this.time        = time;
        this.description = description;
        this.image_url   = image_url;
    }

    public Meal(String name, long date, long time, String description, String image_url, User creator) {
        this.name        = name;
        this.date        = date;
        this.time        = time;
        this.description = description;
        this.image_url   = image_url;
        this.creator     = creator;
    }

    public Meal(String name, long date, String description, String image_url, User creator) {
        this.name        = name;
        this.date        = date;
        this.description = description;
        this.image_url   = image_url;
        this.creator     = creator;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setTime (long time){
        this.time = time;
    }


    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", image_url='" + image_url + '\'' +
                ", id=" + id +
                ", creator username=" + creator.getUsername() +
                '}';
    }

    public String getDateString(){
        Date date = new Date(this.date);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/YYYY", Locale.FRANCE);
        return df2.format(date); // Format 09/10/2019
    }


    public String getTimeString(){
        Date date = new Date(this.time);
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        return df2.format(date);
    }
}
