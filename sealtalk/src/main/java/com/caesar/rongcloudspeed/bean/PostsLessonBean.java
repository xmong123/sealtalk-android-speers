package com.caesar.rongcloudspeed.bean;

import com.caesar.rongcloudspeed.data.BaseData;

/**
 * Created by mac on 2018/4/5.
 */

public class PostsLessonBean {

    private String tid;
    private String term_id;
    private String object_id;
    private String post_title;
    private String post_excerpt;
    private String post_source;
    private String post_price;
    private String smeta;
    private String thumb_video;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }

    public String getPost_source() {
        return post_source;
    }

    public void setPost_source(String post_source) {
        this.post_source = post_source;
    }

    public String getPost_price() {
        return post_price;
    }

    public void setPost_price(String post_price) {
        this.post_price = post_price;
    }

    public String getSmeta() {
        return smeta;
    }

    public void setSmeta(String smeta) {
        this.smeta = smeta;
    }

    public String getThumb_video() {
        return thumb_video;
    }

    public void setThumb_video(String thumb_video) {
        this.thumb_video = thumb_video;
    }
}
