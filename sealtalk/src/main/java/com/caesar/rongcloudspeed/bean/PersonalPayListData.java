package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5..
 */

public class PersonalPayListData extends CommonResonseBean {
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */
    private String state;

    private List<PayItem> url;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private List<PayItem> referer;

    public List<PayItem> getUrl() {
        return url;
    }

    public void setUrl(List<PayItem> url) {
        this.url = url;
    }

    public List<PayItem> getReferer() {
        return referer;
    }

    public void setReferer(List<PayItem> referer) {
        this.referer = referer;
    }

    public class PayItem{
        private String payforname;
        private String paytoname;
        private String payamount;
        private String paydatatime;

        public String getPayforname() {
            return payforname;
        }

        public void setPayforname(String payforname) {
            this.payforname = payforname;
        }

        public String getPaytoname() {
            return paytoname;
        }

        public void setPaytoname(String paytoname) {
            this.paytoname = paytoname;
        }

        public String getPayamount() {
            return payamount;
        }

        public void setPayamount(String payamount) {
            this.payamount = payamount;
        }

        public String getPaydatatime() {
            return paydatatime;
        }

        public void setPaydatatime(String paydatatime) {
            this.paydatatime = paydatatime;
        }
    }

}
