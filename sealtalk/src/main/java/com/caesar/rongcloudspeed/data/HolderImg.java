package com.caesar.rongcloudspeed.data;

/**
 *
 * Created by mathum on 2017/9/3.
 */

public class HolderImg {

    private String url;//照片地址
    private int placeHolderResId;//展位图
    private boolean hasUpload = false;
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getPlaceHolderResId() {
        return placeHolderResId;
    }

    public void setPlaceHolderResId(int placeHolderResId) {
        this.placeHolderResId = placeHolderResId;
    }

    public boolean isHasUpload() {
        return hasUpload;
    }

    public void setHasUpload(boolean hasUpload) {
        this.hasUpload = hasUpload;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HolderImg() {

    }

    public HolderImg(String url, int placeHolderResId, boolean hasUpload) {

        this.url = url;
        this.placeHolderResId = placeHolderResId;
        this.hasUpload = hasUpload;
    }

    public HolderImg(String url, byte[] bytes, boolean hasUpload) {

        this.url = url;
        this.bytes = bytes;
        this.hasUpload = hasUpload;
    }
}
