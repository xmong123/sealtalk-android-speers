package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.BaseData;
import com.yiw.circledemo.bean.CircleHeaderItem;
import com.yiw.circledemo.bean.CircleItem;

import java.util.List;

public class CircleHeaderResult extends BaseData {

    private List<CircleHeaderItem> referer;
    private List<CircleHeaderItem> url;

    public List<CircleHeaderItem> getReferer() {
        return referer;
    }

    public void setReferer(List<CircleHeaderItem> referer) {
        this.referer = referer;
    }

    public List<CircleHeaderItem> getUrl() {
        return url;
    }

    public void setUrl(List<CircleHeaderItem> url) {
        this.url = url;
    }
}
