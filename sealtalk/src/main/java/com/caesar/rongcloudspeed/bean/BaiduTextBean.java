package com.caesar.rongcloudspeed.bean;

import com.caesar.rongcloudspeed.data.BaseData;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class BaiduTextBean extends BaseData {
    
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */
    private String conclusion;
    private int conclusionType;

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public int getConclusionType() {
        return conclusionType;
    }

    public void setConclusionType(int conclusionType) {
        this.conclusionType = conclusionType;
    }

    private List<BaiduTextData> data;

    public List<BaiduTextData> getData() {
        return data;
    }

    public void setData(List<BaiduTextData> data) {
        this.data = data;
    }

    public class BaiduTextData{
        private String msg;
        private String conclusion;
        private String subType;
        private String type;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getConclusion() {
            return conclusion;
        }

        public void setConclusion(String conclusion) {
            this.conclusion = conclusion;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

