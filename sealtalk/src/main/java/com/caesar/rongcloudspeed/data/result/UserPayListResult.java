package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.PayDataItem;
import com.caesar.rongcloudspeed.data.BaseData;

import java.util.List;

public class UserPayListResult extends BaseData {

    public List<PayDataItem> getUrl() {
        return url;
    }

    public void setUrl(List<PayDataItem> url) {
        this.url = url;
    }

    private List<PayDataItem> url;
    private String status;
    private String state;


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
