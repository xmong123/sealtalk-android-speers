package com.caesar.rongcloudspeed.config;


import com.caesar.rongcloudspeed.BuildConfig;

/**
 * 一些全局配置
 * Created by 43053 on 2016/6/16.
 */
public class Config {

    //debug模式开关
    public static final boolean isDebug = BuildConfig.DEBUG;


    public static final String TOKEN = "Jd0xJK3l7iUo2g8J";

    //网络请求类型
    public static final int download = 0;
    public static final int upload = 1;
    public static final int login = 2;
    public static final int delete = 3;
    public static final int check = 4;
    public static final int cancel = 5;
    public static final int logout = 6;
    public static final int scaning = 7;
    public static final int update = 8;
    public static final int create = 9;

}
