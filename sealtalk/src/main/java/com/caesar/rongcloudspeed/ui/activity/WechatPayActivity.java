package com.caesar.rongcloudspeed.ui.activity;


import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.WechatPayCommonBean;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.caesar.rongcloudspeed.wx.WXManager.APP_ID;


public class WechatPayActivity extends Activity {

    private IWXAPI api;
    protected static final String TAG = "WechatPayActivity";

    @SuppressLint("HandlerLeak")
    Handler payHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    WechatPayCommonBean.WechatPayBean bean = (WechatPayCommonBean.WechatPayBean) msg.obj;
                    Toast.makeText(WechatPayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId = bean.getAppid();
                    req.partnerId = bean.getPartnerid();
                    req.prepayId = bean.getPrepayid();
                    req.nonceStr = bean.getNoncestr();
                    req.timeStamp = bean.getTimestamp();
                    req.packageValue = "Sign=WXPay";
                    req.sign = bean.getSign();
                    req.extData = "app data"; // optional
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_pay_layout);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        Button appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "https://wxpay.wxutil.com/pub_v2/app/app_pay.php";
                Button payBtn = (Button) findViewById(R.id.appay_btn);
                payBtn.setEnabled(false);
                Toast.makeText(WechatPayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(createSSLSocketFactory())
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(6, TimeUnit.SECONDS)
                        .build();
                final Request request = new Request.Builder()
                        .url(url)
                        .get()//默认就是GET请求，可以不写
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Log.d(TAG, "onResponse: " + string);
                        Gson gson = new Gson();
                        WechatPayCommonBean bean = gson.fromJson(string, WechatPayCommonBean.class);
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = bean;
                        payHandler.sendMessage(msg);

                    }
                });


                payBtn.setEnabled(true);
            }
        });
        Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
        checkPayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                Toast.makeText(WechatPayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;

    }

    private static class TrustAllCerts implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public class SSLHostnameVerifier implements HostnameVerifier {

        private static final String HOSTNAME = "119.29.115.28";

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return HOSTNAME.equals(hostname) && HOSTNAME.equals(session.getPeerHost());
        }
    }

}
