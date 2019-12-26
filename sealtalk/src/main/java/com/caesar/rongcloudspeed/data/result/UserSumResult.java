package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.BaseData;

public class UserSumResult extends BaseData {

    private UserSumUrl url;
    private String status;
    private String state;

    public UserSumUrl getUrl() {
        return url;
    }

    public void setUrl(UserSumUrl url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
