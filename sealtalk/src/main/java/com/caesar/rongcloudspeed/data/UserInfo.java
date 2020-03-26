package com.caesar.rongcloudspeed.data;

import java.util.List;

public class UserInfo {

    private String id;
    private String user_login;
    private String user_nicename;
    private String avatar;
    private String birthday;
    private String mobile;
    private String user_sum;
    private String orderCount;
    private String user_industry;
    private String user_profession;
    private String user_soft;
    private String user_type;
    private List<UserOrder> user_orders;

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public List<UserOrder> getUser_orders() {
        return user_orders;
    }

    public void setUser_orders(List<UserOrder> user_orders) {
        this.user_orders = user_orders;
    }

    public String getUser_paypass() {
        return user_paypass;
    }

    public void setUser_paypass(String user_paypass) {
        this.user_paypass = user_paypass;
    }

    private String user_paypass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public String getUser_sum() {
        return user_sum;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public void setUser_sum(String user_nicename) {
        this.user_sum = user_sum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUser_industry() {
        return user_industry;
    }

    public void setUser_industry(String user_industry) {
        this.user_industry = user_industry;
    }

    public String getUser_profession() {
        return user_profession;
    }

    public void setUser_profession(String user_profession) {
        this.user_profession = user_profession;
    }

    public String getUser_soft() {
        return user_soft;
    }

    public void setUser_soft(String user_soft) {
        this.user_soft = user_soft;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
