package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.UserDataInfo;
import com.caesar.rongcloudspeed.data.UserInfo;
import com.caesar.rongcloudspeed.data.UserInfo1;
import com.caesar.rongcloudspeed.data.BaseData;

public class UserDataInfoResult extends BaseData {

    private UserDataInfo url;
    private UserInfo1 referer;
    private String status;
    private String state;

    public UserInfo1 getReferer() {
        return referer;
    }

    public void setReferer(UserInfo1 referer) {
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

    public UserDataInfo getUrl() {
        return url;
    }

    public void setUrl(UserDataInfo url) {
        this.url = url;
    }
}
