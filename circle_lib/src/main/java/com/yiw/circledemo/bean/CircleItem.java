package com.yiw.circledemo.bean;

import java.util.List;

import android.text.TextUtils;


public class CircleItem extends BaseBean {

    private String object_id;
    private String post_author;
    private String user_nicename;
    private String avatar;
    private String post_title;
    private List<String> photos_urls;
    private String post_like;
    private String smeta;
    private String post_type;
    private String thumb_video;

    public String getThumb_video() {
        return thumb_video;
    }

    public void setThumb_video(String thumb_video) {
        this.thumb_video = thumb_video;
    }

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    private String term_id;

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getSmeta() {
        return smeta;
    }

    public void setSmeta(String smeta) {
        this.smeta = smeta;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    private String post_date;
    private List<FavoriteItem> post_likes;
    private List<CommentItem> last_comments;

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

    public List<FavoriteItem> getPost_likes() {
        return post_likes;
    }

    public void setPost_likes(List<FavoriteItem> post_likes) {
        this.post_likes = post_likes;
    }

    public List<CommentItem> getLast_comments() {
        return last_comments;
    }

    public void setLast_comments(List<CommentItem> last_comments) {
        this.last_comments = last_comments;
    }
}
