package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * $field ='object_id,post_author,user_nicename,avatar,post_title,photos_urls';
 * Created by mac on 2018/4/5.
 */

public class LessonAdvBean  {

    private String object_id;
    private String post_author;
    private String post_source;
    private String avatar;
    private String post_title;
    private String thumb;
    private String post_mime_type;
    private String post_excerpt;

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }

    public String getPost_mime_type() {
        return post_mime_type;
    }

    public void setPost_mime_type(String post_mime_type) {
        this.post_mime_type = post_mime_type;
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

    public String getPost_source() {
        return post_source;
    }

    public void setPost_source(String post_source) {
        this.post_source = post_source;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}

