package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class SectionAlbumBean {
    private String query_date;
    private List<PersonalPhotoBean> post_array;

    public String getQuery_date() {
        return query_date;
    }

    public void setQuery_date(String query_date) {
        this.query_date = query_date;
    }

    public List<PersonalPhotoBean> getPost_array() {
        return post_array;
    }

    public void setPost_array(List<PersonalPhotoBean> post_array) {
        this.post_array = post_array;
    }
}
