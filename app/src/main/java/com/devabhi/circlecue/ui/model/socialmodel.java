package com.devabhi.circlecue.ui.model;

public class socialmodel {

    Integer image;
    String text, socialClickCount, privateString;

    public socialmodel(Integer image, String text, String socialClickCount, String privateString) {
        this.image = image;
        this.text = text;
        this.socialClickCount = socialClickCount;
        this.privateString = privateString;

    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getSocialClickCount() {
        return socialClickCount;
    }

    public void setSocialClickCount(String socialClickCount) {
        this.socialClickCount = socialClickCount;
    }

    public String getPrivateString() {
        return privateString;
    }

    public void setPrivateString(String privateString) {
        this.privateString = privateString;
    }
}
