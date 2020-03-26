package com.caesar.rongcloudspeed.bean;

import com.caesar.rongcloudspeed.data.BaseData;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class AppAdvertVideoBean extends BaseData {
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
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

    private List<AdvertVideoBean> url;

    private List<AdvertVideoBean> referer;

    public List<AdvertVideoBean> getUrl() {
        return url;
    }

    public void setUrl(List<AdvertVideoBean> url) {
        this.url = url;
    }

    public List<AdvertVideoBean> getReferer() {
        return referer;
    }

    public void setReferer(List<AdvertVideoBean> referer) {
        this.referer = referer;
    }

    public static  class AdvertVideoBean  {
        private String slide_id;
        private String slide_name;
        private String slide_pic;

        public String getSlide_id() {
            return slide_id;
        }

        public void setSlide_id(String slide_id) {
            this.slide_id = slide_id;
        }

        public String getSlide_name() {
            return slide_name;
        }

        public void setSlide_name(String slide_name) {
            this.slide_name = slide_name;
        }

        public String getSlide_pic() {
            return slide_pic;
        }

        public void setSlide_pic(String slide_pic) {
            this.slide_pic = slide_pic;
        }
    }
}
