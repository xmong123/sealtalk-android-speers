package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class RecruitApplyBean {


    private String post_id;//招聘ID
    private String post_author;//招聘作者
    private String post_title;//招聘标题
    private String post_like;//发布求职者
    private List<RecruitItemBean> apply_users;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_like() {
        return post_like;
    }

    public void setPost_like(String post_like) {
        this.post_like = post_like;
    }

    public List<RecruitItemBean> getApply_users() {
        return apply_users;
    }

    public void setApply_users(List<RecruitItemBean> apply_users) {
        this.apply_users = apply_users;
    }
}

