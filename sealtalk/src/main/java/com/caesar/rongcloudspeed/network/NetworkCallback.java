package com.caesar.rongcloudspeed.network;

import com.caesar.rongcloudspeed.data.BaseData;

/**
 * 网络请求回调
 * <p>
 * Created by 43053 on 2017/1/3.
 */

public interface NetworkCallback<T extends BaseData> {

    void onSuccess(T t);

    void onFailure(Throwable t);

}
