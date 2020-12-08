package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class RecruitApplyBaseBean extends CommonResonseBean{

    private String state;

    private List<RecruitApplyBean> url;

    private List<RecruitApplyBean> referer;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<RecruitApplyBean> getUrl() {
        return url;
    }

    public void setUrl(List<RecruitApplyBean> url) {
        this.url = url;
    }

    public List<RecruitApplyBean> getReferer() {
        return referer;
    }

    public void setReferer(List<RecruitApplyBean> referer) {
        this.referer = referer;
    }

}

