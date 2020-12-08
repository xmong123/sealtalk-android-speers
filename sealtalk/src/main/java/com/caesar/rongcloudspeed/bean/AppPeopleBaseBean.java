package com.caesar.rongcloudspeed.bean;

import com.caesar.rongcloudspeed.data.BaseData;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class AppPeopleBaseBean extends BaseData {
    /**
     * result : suc
     * msg : 获取成功 id,rongid,user_login,user_nicename,avatar,mobile
     * code : 101
     * quota_present : 0.00
     * info : [{}]
     **/

    private String state;
    private String status;
    private List<PeopleDataBean> referer;
    private List<PeopleDataBean> url;

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

    public List<PeopleDataBean> getReferer() {
        return referer;
    }

    public void setReferer(List<PeopleDataBean> referer) {
        this.referer = referer;
    }

    public List<PeopleDataBean> getUrl() {
        return url;
    }

    public void setUrl(List<PeopleDataBean> url) {
        this.url = url;
    }

    public static class PeopleDataBean {
        private String id;
        private String rongid;
        private String user_login;
        private String user_nicename;
        private String avatar;
        private String mobile;
        private String user_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRongid() {
            return rongid;
        }

        public void setRongid(String rongid) {
            this.rongid = rongid;
        }

        public String getUser_login() {
            return user_login;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public void setUser_login(String user_login) {
            this.user_login = user_login;
        }

        public String getUser_nicename() {
            return user_nicename;
        }

        public void setUser_nicename(String user_nicename) {
            this.user_nicename = user_nicename;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
