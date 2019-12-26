package com.caesar.rongcloudspeed.data;

import java.io.Serializable;

/**
 * RecyclerView样式弹窗的基本数据结构
 * <p>
 * Created by 43053 on 2017/1/6.
 */

public class BaseDialogData implements Serializable {

    private String itemName;
    private int id;

    public BaseDialogData() {
    }

    public BaseDialogData(String itemName, int id) {

        this.itemName = itemName;
        this.id = id;
    }

    public String getItemName() {

        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
