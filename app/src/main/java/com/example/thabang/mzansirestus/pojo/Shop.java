package com.example.thabang.mzansirestus.pojo;

import java.io.Serializable;

public class Shop implements Serializable{
    private String ownerID,tradingName,about,streetAddress,suburb,city,cellphone,email,sdownloadLLogol,sdownloadMenu;

    public Shop() {
    }

    public Shop(String ownerID, String tradingName, String about, String streetAddress, String suburb, String city, String cellphone, String email) {
        this.ownerID = ownerID;
        this.tradingName = tradingName;
        this.about = about;
        this.streetAddress = streetAddress;
        this.suburb = suburb;
        this.city = city;
        this.cellphone = cellphone;
        this.email = email;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdownloadLLogol() {
        return sdownloadLLogol;
    }

    public void setSdownloadLLogol(String sdownloadLLogol) {
        this.sdownloadLLogol = sdownloadLLogol;
    }

    public String getSdownloadMenu() {
        return sdownloadMenu;
    }

    public void setSdownloadMenu(String sdownloadMenu) {
        this.sdownloadMenu = sdownloadMenu;
    }
}
