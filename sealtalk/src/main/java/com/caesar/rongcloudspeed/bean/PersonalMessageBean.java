package com.caesar.rongcloudspeed.bean;

/**
 * Created by mac on 2018/4/6.
 */

public class PersonalMessageBean extends CommonResonseBean {
    /**
     * photo : Public/photo.png
     * user_name : 15552563295
     * money : 0.00
     * bond_money : 0.00
     * count : 0
     */
    private PersonalMessageData referer;
    private PersonalMessageData url;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PersonalMessageData getReferer() {
        return referer;
    }

    public void setReferer(PersonalMessageData referer) {
        this.referer = referer;
    }

    public PersonalMessageData getUrl() {
        return url;
    }

    public void setUrl(PersonalMessageData url) {
        this.url = url;
    }

    public class PersonalMessageData {
        private String id;
        private String rongid;
        private String user_login;
        private String user_nicename;
        private String avatar;
        private String birthday;
        private String user_sum;
        private String mobile;
        private String follow_uid;
        private String user_expert;
        private String lessonCount;
        private String orderCount;

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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getUser_sum() {
            return user_sum;
        }

        public void setUser_sum(String user_sum) {
            this.user_sum = user_sum;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFollow_uid() {
            return follow_uid;
        }

        public void setFollow_uid(String follow_uid) {
            this.follow_uid = follow_uid;
        }

        public String getUser_expert() {
            return user_expert;
        }

        public void setUser_expert(String user_expert) {
            this.user_expert = user_expert;
        }

        public String getLessonCount() {
            return lessonCount;
        }

        public void setLessonCount(String lessonCount) {
            this.lessonCount = lessonCount;
        }

        public String getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(String orderCount) {
            this.orderCount = orderCount;
        }
    }


}
