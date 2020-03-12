package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.BaseData;

public class TargetNumberData extends BaseData {

    private String url;
    private String referer;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
