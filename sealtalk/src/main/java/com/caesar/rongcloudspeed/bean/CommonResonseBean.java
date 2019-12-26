package com.caesar.rongcloudspeed.bean;

/**
 * Created by mac on 2018/4/4.
 */

public class CommonResonseBean {
    int code;
    String info;
    String result;
    String uid;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "CommonResonseBean{" +
                "code=" + code +
                ", msg='" + info + '\'' +
                ", result='" + result + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
