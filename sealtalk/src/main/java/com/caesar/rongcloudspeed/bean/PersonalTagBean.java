package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class PersonalTagBean extends CommonResonseBean {
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */
    private String state;
    private PersonTagList url;
    private PersonTagList referer;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PersonTagList getUrl() {
        return url;
    }

    public void setUrl(PersonTagList url) {
        this.url = url;
    }

    public PersonTagList getReferer() {
        return referer;
    }

    public void setReferer(PersonTagList referer) {
        this.referer = referer;
    }

    public static class PersonTagList {
        //父分类
        private List<PersonTagItem> user_industry;
        private List<PersonTagItem> user_profession;
        private List<PersonTagItem> user_soft;

        public List<PersonTagItem> getUser_industry() {
            return user_industry;
        }

        public void setUser_industry(List<PersonTagItem> user_industry) {
            this.user_industry = user_industry;
        }

        public List<PersonTagItem> getUser_profession() {
            return user_profession;
        }

        public void setUser_profession(List<PersonTagItem> user_profession) {
            this.user_profession = user_profession;
        }

        public List<PersonTagItem> getUser_soft() {
            return user_soft;
        }

        public void setUser_soft(List<PersonTagItem> user_soft) {
            this.user_soft = user_soft;
        }
    }

    public static class PersonTagItem {
        //父分类
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
