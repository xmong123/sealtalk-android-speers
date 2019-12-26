package com.caesar.rongcloudspeed.listener;

import android.app.Activity;
import android.view.View;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class DefaultOnTitleBarListener implements CommonTitleBar.OnTitleBarListener {

    Activity activity;

    public DefaultOnTitleBarListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClicked(View v, int action, String extra) {
        if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
            activity.finish();
        }
    }
}
