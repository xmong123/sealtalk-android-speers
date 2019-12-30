package com.caesar.rongcloudspeed.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.ToastUtils;
import com.classic.common.MultipleStatusView;
import com.gyf.immersionbar.ImmersionBar;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import com.caesar.rongcloudspeed.listener.DefaultOnTitleBarListener;
import com.caesar.rongcloudspeed.rxlife.RxActivity;
import com.caesar.rongcloudspeed.utils.PromptDialogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.leefeng.promptlibrary.PromptDialog;


/**
 * Created by  on 2017/7/10.
 */

public class BaseShopActivity extends RxActivity {


    protected SwipeBackLayout mSwipeBackLayout;
    public static final String CLOSE_APP = "close_app";
    private CloseAppBrocastReceiver receiver;
    public PromptDialog prompDialog;

    public static void checkNetWork() {
        ToastUtils.showShort("网络不可用请检查网络");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        //  StatusBarUtil.setTransparent(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MIUISetStatusBarLightMode(this.getWindow(), true);
        FlymeSetStatusBarLightMode(this.getWindow(), true);
        mSwipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL,EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        receiver = new CloseAppBrocastReceiver();
        prompDialog = PromptDialogUtils.getPrompDialog(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(CLOSE_APP));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void initTitleBarView(CommonTitleBar titleBar, String titleName) {
        titleBar.getCenterTextView().setText(titleName);
        titleBar.setListener(new DefaultOnTitleBarListener(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    /**
     * 适配小米的机子
     *
     * @param window
     * @param dark
     * @return
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 适配魅族的机子
     *
     * @param window
     * @param dark
     * @return
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public class CloseAppBrocastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }


    public void showError(String msg, int currentPageIndex, MultipleStatusView multipleStatusView) {

        if (currentPageIndex == 0) {
            multipleStatusView.showNoNetwork();
        } else {
            ToastUtils.showShort(msg);
        }
    }

}
