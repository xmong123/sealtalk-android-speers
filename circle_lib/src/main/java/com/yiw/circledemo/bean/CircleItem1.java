package com.yiw.circledemo.bean;

import java.util.List;


public class CircleItem1 extends BaseBean {

    private String object_id;
    private String post_author;
    private String user_nicename;
    private String avatar;
    private String post_title;
    private List<String> photos_urls;
    private String post_like;

    public String getPost_like() {
        return post_like;
    }

    public void setPost_like(String post_like) {
        this.post_like = post_like;
    }


    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
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

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public List<String> getPhotos_urls() {
        return photos_urls;
    }

    public void setPhotos_urls(List<String> photos_urls) {
        this.photos_urls = photos_urls;
    }

}
