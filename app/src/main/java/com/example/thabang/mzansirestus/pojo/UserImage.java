package com.example.thabang.mzansirestus.pojo;

public class UserImage {
    private String userID,downloadLink;

    public UserImage() {
    }

    public UserImage(String userID, String downloadLink) {
        this.userID = userID;
        this.downloadLink = downloadLink;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
