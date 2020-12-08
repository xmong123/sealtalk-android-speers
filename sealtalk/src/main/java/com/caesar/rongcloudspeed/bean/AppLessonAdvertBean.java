package com.caesar.rongcloudspeed.bean;

import com.caesar.rongcloudspeed.data.BaseData;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class AppLessonAdvertBean extends BaseData {
    /**
     *
     */
    private String state;
    private String status;

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

    private List<LessonAdvBean> url;

    private List<LessonAdvBean> referer;

    public List<LessonAdvBean> getUrl() {
        return url;
    }

    public void setUrl(List<LessonAdvBean> url) {
        this.url = url;
    }

    public List<LessonAdvBean> getReferer() {
        return referer;
    }

    public void setReferer(List<LessonAdvBean> referer) {
        this.referer = referer;
    }

}
