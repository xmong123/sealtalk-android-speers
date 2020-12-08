package com.caesar.rongcloudspeed.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac on 2018/4/12.
 */

public class ArticleCommentsBean implements Parcelable {

    private String id;
    private String post_id;
    private String uid;
    private String full_name;
    private String url;
    private String content;
    private String createtime;

    protected ArticleCommentsBean(Parcel in) {
        id = in.readString();
        post_id = in.readString();
        uid = in.readString();
        full_name = in.readString();
        url = in.readString();
        content = in.readString();
        createtime = in.readString();
    }

    public static final Creator<ArticleCommentsBean> CREATOR = new Creator<ArticleCommentsBean>() {
        @Override
        public ArticleCommentsBean createFromParcel(Parcel in) {
            return new ArticleCommentsBean(in);
        }

        @Override
        public ArticleCommentsBean[] newArray(int size) {
            return new ArticleCommentsBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(post_id);
        parcel.writeString(uid);
        parcel.writeString(full_name);
        parcel.writeString(url);
        parcel.writeString(content);
        parcel.writeString(createtime);
    }
}
