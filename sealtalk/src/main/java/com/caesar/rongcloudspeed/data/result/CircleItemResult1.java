package com.caesar.rongcloudspeed.data.result;

import com.caesar.rongcloudspeed.data.BaseData;
import com.yiw.circledemo.bean.CircleItem1;

import java.util.List;

public class CircleItemResult1 extends BaseData {

    private List<CircleItem1> url;

    private List<CircleItem1> referer;

    public List<CircleItem1> getUrl() {
        return url;
    }

    public void setUrl(List<CircleItem1> url) {
        this.url = url;
    }

    public List<CircleItem1> getReferer() {
        return referer;
    }

    public void setReferer(List<CircleItem1> referer) {
        this.referer = referer;
    }
}
