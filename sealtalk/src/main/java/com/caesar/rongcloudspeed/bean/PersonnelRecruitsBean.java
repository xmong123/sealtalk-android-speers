package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5..
 */

public class PersonnelRecruitsBean extends CommonResonseBean {
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */
    private String state;

    private List<RecruitItemBean> url;

    private List<RecruitItemBean> referer;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<RecruitItemBean> getUrl() {
        return url;
    }

    public void setUrl(List<RecruitItemBean> url) {
        this.url = url;
    }

    public List<RecruitItemBean> getReferer() {
        return referer;
    }

    public void setReferer(List<RecruitItemBean> referer) {
        this.referer = referer;
    }
}
