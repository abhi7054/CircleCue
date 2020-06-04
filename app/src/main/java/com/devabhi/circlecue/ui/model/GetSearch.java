package com.devabhi.circlecue.ui.model;

import android.graphics.Bitmap;

public class GetSearch {

    String  id, name, country, image;

    public GetSearch(String id, String image, String name, String country) {
        this.image = image;
        this.name = name;
        this.country = country;
        this.id = id;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
