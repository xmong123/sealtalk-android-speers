package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class NavCategoryBean extends CommonResonseBean {
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */
    private String state;
    private String info;
    private List<ParentCateGory> url;
    private List<ParentCateGory> referer;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<ParentCateGory> getUrl() {
        return url;
    }

    public void setUrl(List<ParentCateGory> url) {
        this.url = url;
    }

    public List<ParentCateGory> getReferer() {
        return referer;
    }

    public void setReferer(List<ParentCateGory> referer) {
        this.referer = referer;
    }

    public static class ParentCateGory {
        //父分类
        private String id;
        private String name;
        private boolean isFlag;
        private String parent_id;
        private List<ChildrenCateGory> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isFlag() {
            return isFlag;
        }

        public void setFlag(boolean flag) {
            isFlag = flag;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public List<ChildrenCateGory> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenCateGory> children) {
            this.children = children;
        }
    }

    public static class ChildrenCateGory {
        //子分类
        private String id;
        private String name;
        private String parent_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }
    }

}
