package com.caesar.rongcloudspeed.data;

/**
 * model的基类
 * <p>
 * Created by mathum on 2017/1/5.
 */

public class BaseData {


    /**
     * total
     * OPERATE
     * STATE
     */

    private int code;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
