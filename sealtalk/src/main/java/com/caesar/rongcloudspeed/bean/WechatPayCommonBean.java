package com.caesar.rongcloudspeed.bean;

import com.caesar.rongcloudspeed.data.BaseData;

/**
 * Created by mac on 2018/4/5.
 */

public class WechatPayCommonBean extends CommonResonseBean {

    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * appid: "wx87a9e4787773477d",
     * partnerid: "1900006771",
     * package: "Sign=WXPay",
     * noncestr: "0a3ece1e653862b1042c268e2945baa8",
     * timestamp: 1584114893,
     * prepayid: "wx1323545378387440e03b684c1315099632",
     * sign: "25066459C80D1F4485742C8D0E4C3BDF"
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */

    private String state;

    private WechatPayBean url;

    private WechatPayBean referer;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public WechatPayBean getUrl() {
        return url;
    }

    public void setUrl(WechatPayBean url) {
        this.url = url;
    }

    public WechatPayBean getReferer() {
        return referer;
    }

    public void setReferer(WechatPayBean referer) {
        this.referer = referer;
    }

    public static class WechatPayBean{

        private String appid;
        private String partnerid;
        private String noncestr;
        private String timestamp;
        private String prepayid;
        private String sign;
        private String retmsg;

        public String getRetmsg() {
            return retmsg;
        }

        public void setRetmsg(String retmsg) {
            this.retmsg = retmsg;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

    }
}
