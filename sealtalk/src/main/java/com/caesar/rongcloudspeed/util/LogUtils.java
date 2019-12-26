package com.caesar.rongcloudspeed.util;

import android.content.Context;

/**
 * Created by 43053 on 2016/6/16.
 */
public class LogUtils{

    private L l;
    private Context mContext;

    public LogUtils(Context context){
        mContext = context;
    }
    //输出
    public void out(String message){
        L.l(mContext.getClass().getName(),message);
    }
}
