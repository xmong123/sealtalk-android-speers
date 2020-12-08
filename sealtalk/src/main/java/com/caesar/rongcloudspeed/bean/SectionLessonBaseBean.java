package com.caesar.rongcloudspeed.bean;

import com.caesar.rongcloudspeed.data.BaseData;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class SectionLessonBaseBean extends BaseData {
    private String state;
    private List<SectionLessonBean> url;
    private List<SectionLessonBean> referer;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<SectionLessonBean> getReferer() {
        return referer;
    }

    public void setReferer(List<SectionLessonBean> referer) {
        this.referer = referer;
    }

    public List<SectionLessonBean> getUrl() {
        return url;
    }

    public void setUrl(List<SectionLessonBean> url) {
        this.url = url;
    }

    public class SectionLessonBean {
        private String term_id;
        private String name;
        private String parent;
        private List<PostsLessonBean> posts;

        public String getTerm_id() {
            return term_id;
        }

        public void setTerm_id(String term_id) {
            this.term_id = term_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public List<PostsLessonBean> getPosts() {
            return posts;
        }

        public void setPosts(List<PostsLessonBean> posts) {
            this.posts = posts;
        }
    }
}
