package com.caesar.rongcloudspeed.data;

public class UserSumUrl {

    private String id;
    private String uuid;
    private String rongid;
    private String user_login;
    private String user_nicename;
    private String avatar;
    private String user_sum;
    private String mobile;
    private String coin;

    public String getRongid() {
        return rongid;
    }

    public void setRongid(String rongid) {
        this.rongid = rongid;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    private String order;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_sum() {
        return user_sum;
    }

    public void setUser_sum(String user_sum) {
        this.user_sum = user_sum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}
