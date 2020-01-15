package com.caesar.rongcloudspeed.network;


import com.caesar.rongcloudspeed.BuildConfig;

/**
 * Created by mathum on 2019/3/26.
 */

public class ApiConfig {

    private static final String TESTSERVER = "http://speer.500-china.com/";
    private static final String FORMALSERVER = "http://speer.500-china.com/";
    public static int CODE_SUCC = 101;

    public String getServer() {
        return BuildConfig.DEBUG ? TESTSERVER : FORMALSERVER;
    }

}
