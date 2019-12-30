package com.caesar.rongcloudspeed.bean;

/**
 * Created by mac on 2018/4/5.
 */

public class HomeMainDataBean extends CommonResonseBean {
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */
    private String state;
    private String info;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private HomeArticleBaseBeanT url;

    public HomeArticleBaseBeanT getUrl() {
        return url;
    }

    public void setUrl(HomeArticleBaseBeanT url) {
        this.url = url;
    }

    public HomeArticleBaseBeanT getReferer() {
        return referer;
    }

    public void setReferer(HomeArticleBaseBeanT referer) {
        this.referer = referer;
    }

    private HomeArticleBaseBeanT referer;

    public class HomeArticleBaseBeanT{

        public HomeArticleBaseBean getLists3() {
            return lists3;
        }

        public void setLists3(HomeArticleBaseBean lists3) {
            this.lists3 = lists3;
        }

        public HomeArticleBaseBean getLists1() {
            return lists1;
        }

        public void setLists1(HomeArticleBaseBean lists1) {
            this.lists1 = lists1;
        }

        public HomeArticleBaseBean getLists2() {
            return lists2;
        }

        public void setLists2(HomeArticleBaseBean lists2) {
            this.lists2 = lists2;
        }

        private HomeArticleBaseBean lists1;
        private HomeArticleBaseBean lists2;
        private HomeArticleBaseBean lists3;

        public HomeArticleBaseBean getLists5() {
            return lists5;
        }

        public void setLists5(HomeArticleBaseBean lists5) {
            this.lists5 = lists5;
        }

        private HomeArticleBaseBean lists5;
        private HomeArticleBaseBean lists6;

        public HomeArticleBaseBean getLists6() {
            return lists6;
        }

        public void setLists6(HomeArticleBaseBean lists6) {
            this.lists6 = lists6;
        }
    }

}

