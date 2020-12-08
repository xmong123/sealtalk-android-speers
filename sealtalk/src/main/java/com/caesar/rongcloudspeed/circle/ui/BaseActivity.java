package com.caesar.rongcloudspeed.circle.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.config.Config;
import com.caesar.rongcloudspeed.util.ActivityStack;
import com.caesar.rongcloudspeed.util.ToastUtils;


/**
 * Created by 43053 on 2016/6/16.
 */
public abstract class BaseActivity extends Activity {
    private ToastUtils mToastUtils;
    /*private LogUtils mLogUtils;*/

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentView());
        /*        initUtils();*/
        ActivityStack.push(this);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);

        mToastUtils = new ToastUtils(this);

    }
    /* private void initUtils() {
        mToastUtils = new ToastUtils(this);
        mLogUtils = new LogUtils(this);
    }

    protected void get(){
        NetWorkUtils.get();
    }*/

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    //提供布局Id
    protected abstract int provideContentView();

    @Override
    protected void onResume() {
        super.onResume();
        //  ScreenManager.getScreenManager().pushActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        // ScreenManager.getScreenManager().popActivity(this);
        ActivityStack.pop(this);
        super.onDestroy();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    protected void showProgressBar(boolean show, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    protected void showProgressBar(boolean show, int message) {
        String s = getString(message);
        showProgressBar(show, s);
    }

    public void showProgressBar(int type) {
        switch (type) {
            case Config.download:
                showProgressBar(true, R.string.searching);
                break;
            case Config.upload:
                showProgressBar(true, R.string.upload);
                break;
            case Config.login:
                showProgressBar(true, R.string.logining);
                break;
            case Config.delete:
                showProgressBar(true, R.string.deleting);
                break;
            case Config.check:
                showProgressBar(true, R.string.checking);
                break;
            case Config.cancel:
                showProgressBar(true, R.string.canceling);
                break;
            case Config.logout:
                showProgressBar(true, R.string.logout_account);
                break;
            case Config.scaning:
                showProgressBar(true, R.string.scaning);
                break;
            case Config.update:
                showProgressBar(true, R.string.update);
                break;
            case Config.create:
                showProgressBar(true, R.string.create);
                break;
            default:
                break;
        }
    }

    public void showInfo(int messageId) {
        mToastUtils.show(getResources().getString(messageId));
    }

    public void showInfo(String message) {
        mToastUtils.show(message);
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }


}
