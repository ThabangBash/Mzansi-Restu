package com.example.thabang.mzansirestus.pojo;

public class ShopLogo {
    private String owerID,downloadLink;

    public ShopLogo() {
    }

    public ShopLogo(String owerID, String downloadLink) {
        this.owerID = owerID;
        this.downloadLink = downloadLink;
    }

    public String getOwerID() {
        return owerID;
    }

    public void setOwerID(String owerID) {
        this.owerID = owerID;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
