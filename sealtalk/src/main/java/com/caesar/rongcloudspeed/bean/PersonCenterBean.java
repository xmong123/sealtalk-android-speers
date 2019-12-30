package com.caesar.rongcloudspeed.bean;

/**
 * Created by mac on 2018/4/6.
 */

public class PersonCenterBean extends CommonResonseBean {
    /**
     * photo : Public/photo.png
     * user_name : 15552563295
     * money : 0.00
     * bond_money : 0.00
     * count : 0
     */

    private String photo;
    private String user_name;
    private String money;
    private String bond_money;
    private String count;
    private String poundage;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBond_money() {
        return bond_money;
    }

    public String getPoundage() {
        return poundage;
    }

    public void setPoundage(String poundage) {
        this.poundage = poundage;
    }

    public void setBond_money(String bond_money) {
        this.bond_money = bond_money;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
