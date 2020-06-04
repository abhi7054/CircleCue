package com.devabhi.circlecue.ui.gallery;

import android.content.Intent;

public class DrawerModel {

    Integer image;
    String title;

    public DrawerModel(Integer image, String title) {
        this.image = image;
        this.title = title;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
