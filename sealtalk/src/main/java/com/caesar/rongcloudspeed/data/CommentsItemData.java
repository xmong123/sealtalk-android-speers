package com.caesar.rongcloudspeed.data;

import com.yiw.circledemo.bean.CircleItem;

import java.util.List;

public class CommentsItemData {

    private String id;
    private String uid;
    private String rongid;
    private String user_nicename;
    private String avatar;
    private String content;
    private String createtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRongid() {
        return rongid;
    }

    public void setRongid(String rongid) {
        this.rongid = rongid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
