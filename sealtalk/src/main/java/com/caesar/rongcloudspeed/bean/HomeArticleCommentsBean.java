package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class HomeArticleCommentsBean extends CommonResonseBean {

    private String state;

    private List<ArticleCommentsBean> url;

    private List<ArticleCommentsBean> referer;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ArticleCommentsBean> getUrl() {
        return url;
    }

    public void setUrl(List<ArticleCommentsBean> url) {
        this.url = url;
    }

    public List<ArticleCommentsBean> getReferer() {
        return referer;
    }

    public void setReferer(List<ArticleCommentsBean> referer) {
        this.referer = referer;
    }
}
