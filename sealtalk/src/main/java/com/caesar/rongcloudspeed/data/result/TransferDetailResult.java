package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.TransferDetailUrl;
import com.caesar.rongcloudspeed.data.UserSumUrl;

public class TransferDetailResult extends BaseData {

    private TransferDetailUrl url;
    private String status;
    private String state;

    public TransferDetailUrl getUrl() {
        return url;
    }

    public void setUrl(TransferDetailUrl url) {
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
