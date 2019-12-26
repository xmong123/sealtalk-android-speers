package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.UserInfo;
import com.caesar.rongcloudspeed.data.UserInfo1;
import com.caesar.rongcloudspeed.data.BaseData;

public class UserInfoResult extends BaseData {

    private UserInfo url;
    private UserInfo referer;
    private String status;
    private String state;

    public UserInfo getReferer() {
        return referer;
    }

    public void setReferer(UserInfo referer) {
        this.referer = referer;
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

    public UserInfo getUrl() {
        return url;
    }

    public void setUrl(UserInfo url) {
        this.url = url;
    }
}
