package com.caesar.rongcloudspeed.network;

import com.caesar.rongcloudspeed.config.Config;
import com.caesar.rongcloudspeed.data.BaseData;

/**
 * 网络请求结果工具类
 * <p>
 * Created by mathum on 2017/1/5.
 */

public class NetworkResultUtils {

    //判断请求是否成功
    public static boolean isSuccess(BaseData resultData) {
        return resultData.getCode() == 101;
    }

    //读取请求结果的信息
    public static String getMessage(BaseData resultData) {
        return resultData == null ? (Config.isDebug ? "服务器重启==" : "网络异常") : "ERROR";
    }

    //判断请求的token是否过期
    public static boolean isTokenOut(BaseData resultData) {
        return resultData == null || resultData.getCode() == 104;
    }

}
