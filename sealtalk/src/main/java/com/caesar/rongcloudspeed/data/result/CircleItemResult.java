package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.CircleUrl;
import com.caesar.rongcloudspeed.data.BaseData;

public class CircleItemResult extends BaseData {

    private CircleUrl url;

    public CircleUrl getReferer() {
        return referer;
    }

    public void setReferer(CircleUrl referer) {
        this.referer = referer;
    }

    private CircleUrl referer;

    public CircleUrl getUrl() {
        return url;
    }

    public void setUrl(CircleUrl url) {
        this.url = url;
    }
}
