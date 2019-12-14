package com.cnamge.fipinfo.androfit.model;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Meal extends SugarRecord<Meal> {
    private String name;
    private long date;
    private String description;
    private String image_url;

    public Meal() {}

    public Meal(String name, long date, String description, String image_url) {
        this.name        = name;
        this.date        = date;
        this.description = description;
        this.image_url   = image_url;
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
}
