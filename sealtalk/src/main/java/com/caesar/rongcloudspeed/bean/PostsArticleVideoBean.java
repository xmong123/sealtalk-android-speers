package com.caesar.rongcloudspeed.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.caesar.rongcloudspeed.data.BaseData;

/**
 * Created by mac on 2018/4/5.
 */

public class PostsArticleVideoBean extends BaseData implements Parcelable {
    protected PostsArticleVideoBean(Parcel in) {
        tid = in.readString();
        term_id = in.readString();
        object_id = in.readString();
        post_author = in.readString();
        post_authorname = in.readString();
        post_avatar = in.readString();
        post_keywords = in.readString();
        post_type = in.readString();
        avatar = in.readString();
        user_nicename = in.readString();
        rongid = in.readString();
        post_source = in.readString();
        post_date = in.readString();
        post_title = in.readString();
        post_content = in.readString();
        post_excerpt = in.readString();
        post_area = in.readString();
        post_region = in.readString();
        post_price = in.readString();
        mobile = in.readString();
        photos_urls = in.readString();
        post_mobile = in.readString();
        post_hits = in.readString();
        post_like = in.readString();
        smeta = in.readString();
        thumb_video = in.readString();
        content = in.readString();
        imageResource = in.readInt();
        store_count = in.readString();
        post_delivery = in.readString();
        cat_id = in.readString();
        selected = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tid);
        dest.writeString(term_id);
        dest.writeString(object_id);
        dest.writeString(post_author);
        dest.writeString(post_authorname);
        dest.writeString(post_avatar);
        dest.writeString(post_keywords);
        dest.writeString(post_type);
        dest.writeString(avatar);
        dest.writeString(user_nicename);
        dest.writeString(rongid);
        dest.writeString(post_source);
        dest.writeString(post_date);
        dest.writeString(post_title);
        dest.writeString(post_content);
        dest.writeString(post_excerpt);
        dest.writeString(post_area);
        dest.writeString(post_region);
        dest.writeString(post_price);
        dest.writeString(mobile);
        dest.writeString(photos_urls);
        dest.writeString(post_mobile);
        dest.writeString(post_hits);
        dest.writeString(post_like);
        dest.writeString(smeta);
        dest.writeString(thumb_video);
        dest.writeString(content);
        dest.writeInt(imageResource);
        dest.writeString(store_count);
        dest.writeString(post_delivery);
        dest.writeString(cat_id);
        dest.writeString(selected);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostsArticleVideoBean> CREATOR = new Creator<PostsArticleVideoBean>() {
        @Override
        public PostsArticleVideoBean createFromParcel(Parcel in) {
            return new PostsArticleVideoBean(in);
        }

        @Override
        public PostsArticleVideoBean[] newArray(int size) {
            return new PostsArticleVideoBean[size];
        }
    };

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    private String tid;
    private String term_id;
    private String object_id;
    private String post_author;
    private String post_authorname;
    private String post_avatar;
    private String post_keywords;
    private String post_type;
    private String avatar;
    private String user_nicename;
    private String rongid;
    private VideoSmetArrayBean smetarray;

    public VideoSmetArrayBean getSmetarray() {
        return smetarray;
    }

    public void setSmetarray(VideoSmetArrayBean smetarray) {
        this.smetarray = smetarray;
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

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
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

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
    }

    public String getPost_authorname() {
        return post_authorname;
    }

    public void setPost_authorname(String post_authorname) {
        this.post_authorname = post_authorname;
    }

    public String getPost_avatar() {
        return post_avatar;
    }

    public void setPost_avatar(String post_avatar) {
        this.post_avatar = post_avatar;
    }

    public String getPost_keywords() {
        return post_keywords;
    }

    public void setPost_keywords(String post_keywords) {
        this.post_keywords = post_keywords;
    }

    public String getPost_source() {
        return post_source;
    }

    public void setPost_source(String post_source) {
        this.post_source = post_source;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }

    public String getPost_area() {
        return post_area;
    }

    public void setPost_area(String post_area) {
        this.post_area = post_area;
    }

    public String getPost_region() {
        return post_region;
    }

    public void setPost_region(String post_region) {
        this.post_region = post_region;
    }

    public String getSmeta() {
        return smeta;
    }

    public void setSmeta(String smeta) {
        this.smeta = smeta;
    }

    private String post_source;
    private String post_date;
    private String post_title;
    private String post_content;
    private String post_excerpt;
    private String post_area;
    private String post_region;
    private String post_price;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;

    public String getPhotos_urls() {
        return photos_urls;
    }

    public void setPhotos_urls(String photos_urls) {
        this.photos_urls = photos_urls;
    }

    private String photos_urls;

    public String getPost_mobile() {
        return post_mobile;
    }

    public void setPost_mobile(String post_mobile) {
        this.post_mobile = post_mobile;
    }

    private String post_mobile;

    public String getPost_hits() {
        return post_hits;
    }

    public void setPost_hits(String post_hits) {
        this.post_hits = post_hits;
    }

    private String post_hits;

    public String getPost_like() {
        return post_like;
    }

    public void setPost_like(String post_like) {
        this.post_like = post_like;
    }

    private String post_like;

    public String getPost_price() {
        return post_price;
    }

    public void setPost_price(String post_price) {
        this.post_price = post_price;
    }

    private String smeta;

    private String thumb_video;

    public String getThumb_video() {
        return thumb_video;
    }

    public void setThumb_video(String thumb_video) {
        this.thumb_video = thumb_video;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    private int imageResource;

    private String store_count;

    private String post_delivery;

    public String getPost_delivery() {
        return post_delivery;
    }

    public void setPost_delivery(String post_delivery) {
        this.post_delivery = post_delivery;
    }

    public String getStore_count() {
        return store_count;
    }

    public void setStore_count(String store_count) {
        this.store_count = store_count;
    }

    private String cat_id;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    //该商品是否在购物车中选择, 如果选择该商品将会结算 , 1:选中, 0:未选中
    private String selected="0";

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
