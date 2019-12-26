package com.caesar.rongcloudspeed.bean;

import android.widget.Checkable;

/**
 * Created by mac on 2018/4/5.
 */

public class AdminIndustryBean implements Checkable {

    public AdminIndustryBean(boolean flag, String id, String name, String parent_id) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
        this.isFlag = flag;
    }

    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */
    private String id;
    private String name;
    private String parent_id;
    private boolean isFlag;

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

    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean flag) {
        isFlag = flag;
    }

    @Override
    public void setChecked(boolean checked) {
        isFlag = checked;
    }

    @Override
    public boolean isChecked() {
        return isFlag;
    }

    @Override
    public void toggle() {
        setChecked(!isFlag);
    }
}
