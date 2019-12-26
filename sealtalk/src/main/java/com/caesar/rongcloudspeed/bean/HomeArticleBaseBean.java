package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class HomeArticleBaseBean {
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<PostsArticleBaseBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostsArticleBaseBean> posts) {
        this.posts = posts;
    }

    private List<PostsArticleBaseBean> posts;
}
