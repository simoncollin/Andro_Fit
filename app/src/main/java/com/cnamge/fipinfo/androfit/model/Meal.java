package com.cnamge.fipinfo.androfit.model;

import com.orm.SugarRecord;

public class Meal extends SugarRecord<Meal> {
    String name;
    long date;
    String description;
    String image_url;

    public Meal() {}

    public Meal(String name, long date, String description, String image_url) {
        this.name        = name;
        this.date        = date;
        this.description = description;
        this.image_url   = image_url;
    }
}
