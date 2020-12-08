package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class CategoryBean extends CommonResonseBean {
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */

    public static class CateBean {
        /**
         * id : 2
         * img : Public/Uploads/5abc9216607d4.png
         * goods_name : Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机
         * money : 7788.00
         */

        private String id;
        private String name;
        private boolean isFlag;

        public boolean isFlag() {
            return isFlag;
        }

        public void setFlag(boolean flag) {
            isFlag = flag;
        }

            private List<ChildrenBean> children;

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

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

    }

    public static class ChildrenBean {
        /**
         * id : 2
         * img : Public/Uploads/5abc9216607d4.png
         * goods_name : Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机
         * money : 7788.00
         */

        private String id;
        private String name;

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

    }
}
