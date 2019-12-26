package com.caesar.rongcloudspeed.interceptors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.caesar.rongcloudspeed.exception.NoNetException;
import com.caesar.rongcloudspeed.listener.NetMonitor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mac on 2018/4/4.
 */

public class InternetInterceptor implements NetMonitor, Interceptor {

    public InternetInterceptor(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Context applicationContext;

    @Override
    public boolean isConnect() {
        ConnectivityManager cm =
                (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (this.isConnect()) {
            Request request = chain.request();
            return chain.proceed(request);
        } else {
            throw new NoNetException();
        }
    }
}
