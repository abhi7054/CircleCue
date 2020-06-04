package com.devabhi.circlecue.ui.model;

public class ViewProfileModel {

    Integer imageID;
    String url;

    public ViewProfileModel(Integer imageID, String url) {
        this.imageID = imageID;
        this.url = url;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
