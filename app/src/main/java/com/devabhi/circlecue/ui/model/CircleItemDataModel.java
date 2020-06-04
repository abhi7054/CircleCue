package com.devabhi.circlecue.ui.model;

public class CircleItemDataModel {

    String fromID, toID, status, rowId, username, country, pic, id;

    public CircleItemDataModel(String fromID, String toID, String status, String rowId, String username, String country, String pic, String id) {
        this.fromID = fromID;
        this.toID = toID;
        this.status = status;
        this.rowId = rowId;
        this.username = username;
        this.country = country;
        this.pic = pic;
        this.id = id;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
