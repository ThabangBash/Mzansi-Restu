package com.example.thabang.mzansirestus.pojo;

public class ShopMenu {
    private String owerID,sdownloadLink;

    public ShopMenu() {
    }

    public ShopMenu(String owerID, String sdownloadLink) {
        this.owerID = owerID;
        this.sdownloadLink = sdownloadLink;
    }

    public String getOwerID() {
        return owerID;
    }

    public void setOwerID(String owerID) {
        this.owerID = owerID;
    }

    public String getSdownloadLink() {
        return sdownloadLink;
    }

    public void setSdownloadLink(String sdownloadLink) {
        this.sdownloadLink = sdownloadLink;
    }
}
