package com.caesar.rongcloudspeed.manager;


import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import com.caesar.rongcloudspeed.network.Api;
import com.caesar.rongcloudspeed.interceptors.HttpLoggingInterceptor;
import com.caesar.rongcloudspeed.interceptors.InternetInterceptor;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 的服务管理器
 * Created by  on 2017/8/30.
 */

public class RetrofitManageres {


    /**
     * 主机地址
     */
    public static String server_address = "http://thinkcmf.yx2588.com/";
    private static OkHttpClient client;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

    public static void init(Context appContext) {
        if (client != null) {
            throw new RuntimeException("只可初始化一次");
        }
        final InternetInterceptor internetInterceptor = new InternetInterceptor(appContext);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient().newBuilder()
                .connectTimeout(16, TimeUnit.SECONDS)
                .readTimeout(16, TimeUnit.SECONDS)
                .writeTimeout(16, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(internetInterceptor)
                .build();


    }

    private static Retrofit retrofit;


    public static Retrofit getInstance() {

        if (retrofit == null) {
            if (client == null) {
                throw new RuntimeException("必须先初始化");
            }

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(server_address)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }
        return retrofit;
    }

    public static Api create() {
        return getInstance().create(Api.class);

    }


    public static String getHost(String url) {

        Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:+(\\d+)");

        Matcher m = p.matcher(url);
        if (m.find()) {
            return "http://" + m.group();
        }
        return "";
    }

}
