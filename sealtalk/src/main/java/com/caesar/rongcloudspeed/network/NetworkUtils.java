package com.caesar.rongcloudspeed.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.util.L;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 43053 on 2017/1/3.
 */

public class NetworkUtils {

    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.isConnected();
        }
        return false;
    }

    public static void fetchInfo(Call call, NetworkCallback networkCallback) {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                BaseData baseData = (BaseData) response.body();

                networkCallback.onSuccess(baseData);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                L.l("reponse", "onFailure");
                L.l("reponse", t.getMessage());
                L.l("reponse", call.toString());
                L.l("reponse", t.getLocalizedMessage());
                t.printStackTrace();
                networkCallback.onFailure(t);
            }
        });
    }

    public static void fetchInfoWithToken(Call call, NetworkCallbackWithToken networkCallback) {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                BaseData baseData = (BaseData) response.body();
                if (NetworkResultUtils.isTokenOut(baseData)) {
                    //token过期
                    networkCallback.onTokenOut(baseData);
                } else {
                    networkCallback.onSuccess((BaseData) response.body());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                L.l("reponse", "onFailure");
                L.l("reponse", t.getMessage());
                L.l("reponse", call.toString());
                L.l("reponse", t.getLocalizedMessage());
                t.printStackTrace();
                networkCallback.onFailure(t);
            }
        });
    }

    //生成api
    public static <T> T initRetrofitApi(String baseUrl, Class<T> api) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        //if (Config.isDebug) {
        builder.addInterceptor(logging);
        //}
        builder.addInterceptor(chain -> {
            Request.Builder builder1 = chain.request().newBuilder();
            Request request = builder1.build();
            return chain.proceed(request);
        });

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(api);
    }

    //生成包含Header的api
    public static <T> T initHeadersRetrofitApi(String baseUrl, Class<T> api, Map<String, String> headerMap) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        //if (Config.isDebug) {
        builder.addInterceptor(logging);
        //}
        builder.addInterceptor(chain -> {
            Request.Builder builder1 = chain.request().newBuilder();

            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    builder1.addHeader(entry.getKey(), entry.getValue());
                }
            }
            Request request = builder1.build();
            return chain.proceed(request);
        });

        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client).addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(api);

    }
}
