package com.caesar.rongcloudspeed.callback;

/**
 * 上传图片回调
 * <p>
 * Created by mathum on 2017/9/6.
 */

public interface UpLoadImgCallback {

    void onSuccess(String imgurl);

    void onFailure();

}
