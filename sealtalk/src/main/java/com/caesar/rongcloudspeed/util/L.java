package com.caesar.rongcloudspeed.util;

import android.util.Log;

import com.caesar.rongcloudspeed.config.Config;

/**
 * Log工具类
 * Created by 43053 on 2016/6/16.
 */
public class L {

    public static void l(String tag,String message){
        if (Config.isDebug){
            Log.i(tag,message);
        }
    }

}
