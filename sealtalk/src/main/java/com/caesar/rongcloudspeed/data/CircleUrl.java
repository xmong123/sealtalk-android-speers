package com.caesar.rongcloudspeed.data;

import com.yiw.circledemo.bean.CircleItem;

import java.util.List;

public class CircleUrl {

    private int total_pages;
    private String count;
    private List<CircleItem> posts;

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<CircleItem> getPosts() {
        return posts;
    }

    public void setPosts(List<CircleItem> posts) {
        this.posts = posts;
    }
}
