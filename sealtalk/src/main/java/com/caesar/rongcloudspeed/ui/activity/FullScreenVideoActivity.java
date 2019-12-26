package com.caesar.rongcloudspeed.ui.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.utils.WebViewJavaScriptFunction;
import com.caesar.rongcloudspeed.utils.X5WebView;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * 作者：ZhouJianxing on 2017/6/7 12:40
 * email:727933147@qq.com
 */
public class FullScreenVideoActivity extends Activity {
    private String videoPath;
    /**
     * 用于演示X5webView实现视频的全屏播放功能 其中注意 X5的默认全屏方式 与 android 系统的全屏方式
     */
    private TbsVideo tbsVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_videoplayer_layout);

    }
}
