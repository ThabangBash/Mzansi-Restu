package com.example.thabang.mzansirestus.pojo;

public class Person {
    private String uid,username,cellphone,email;
    private boolean isShopOwner;

    public Person() {
    }

    public Person(String uid, String username, String cellphone, String email) {
        this.uid = uid;
        this.username = username;
        this.cellphone = cellphone;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


}
