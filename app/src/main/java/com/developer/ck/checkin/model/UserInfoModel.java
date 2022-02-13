package com.developer.ck.checkin.model;

public class UserInfoModel {
    String name;
    String secondName;
    String email;
    String phone;
    int id;
    String updateDate;
    boolean isLeader;


    public UserInfoModel(String name, String secondName, String email, String phone, String updateDate, boolean isLeader, int id) {
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.phone = phone;
        this.updateDate = updateDate;
        this.isLeader = isLeader;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setLeader(boolean leader) {
        isLeader = leader;
    }
}
