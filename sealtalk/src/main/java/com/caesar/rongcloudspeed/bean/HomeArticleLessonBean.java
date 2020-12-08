package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class HomeArticleLessonBean {
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<PostsArticleVideoBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostsArticleVideoBean> posts) {
        this.posts = posts;
    }

    private List<PostsArticleVideoBean> posts;
}
