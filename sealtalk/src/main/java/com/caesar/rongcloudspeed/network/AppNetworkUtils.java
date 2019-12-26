package com.caesar.rongcloudspeed.network;

import android.content.Context;

import com.caesar.rongcloudspeed.network.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 43053 on 2017/1/4.
 */

public class AppNetworkUtils extends NetworkUtils {

    //生成api
    public static Api initRetrofitApi() {
        return initRetrofitApi(new ApiConfig().getServer(), Api.class);
    }

    //生成包含Headers的api
    public static Api initHeadersRetrofitApi(Context context) {
        Map<String, String> headersMap = new HashMap<>();
        try {
//            String authToken = new JSONObject().put("AuthToken", UserInfoUtils.getAuthToken(context.getApplicationContext())).toString();
//            String enToStr = "Basic " + Base64Utils.base64(authToken, "UTF-8");
            //headersMap.put("authtoken", UserInfoUtils.getAuthToken(context.getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return initHeadersRetrofitApi(new ApiConfig().getServer(), Api.class, headersMap);
    }

    //生成公共api
    public static Api initPublicRetrofitApi() {
        return initRetrofitApi(new ApiConfig().getServer(), Api.class);
    }


}
