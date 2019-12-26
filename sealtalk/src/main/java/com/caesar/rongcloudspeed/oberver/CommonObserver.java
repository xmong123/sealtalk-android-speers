package com.caesar.rongcloudspeed.oberver;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.exception.NoNetException;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.classic.common.MultipleStatusView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by  on 2017/8/31.
 */

public abstract class CommonObserver<T extends CommonResonseBean> implements Observer<T> {

    private final String TAG = "NetWork";
    private PromptDialog prompDialog;

    public CommonObserver(PromptDialog prompDialog) {

        this.prompDialog = prompDialog;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {
        if (statusView != null) {
            statusView.showContent();
        }
        if (prompDialog != null) {
            prompDialog.dismiss();
        }
        if (layout != null) {
            layout.finishRefresh();
            layout.finishLoadmore();
        }

        if (value.getCode() != 101) {
            if (value != null && !TextUtils.isEmpty(value.getInfo())) {
                ToastUtils.showShort(value.getInfo());
            }
        }
        onSuccess(value);
        //  LogUtil.i(TAG,"请求成功");
    }

    public CommonObserver() {
    }

    SmartRefreshLayout layout;

    public CommonObserver(SmartRefreshLayout layout) {
        this.layout = layout;
    }

    MultipleStatusView statusView;

    public CommonObserver(SmartRefreshLayout layout, MultipleStatusView statusView) {
        this.layout = layout;
        this.statusView = statusView;
    }

    public CommonObserver(MultipleStatusView statusView) {
        this.statusView = statusView;
    }

    @Override
    public void onError(Throwable e) {

        e.printStackTrace();
        if (e instanceof NoNetException) {
            ToastUtils.showShort("无法连接网络");
            if (statusView != null) {
                statusView.showNoNetwork();
            }
        }

        //如果用户自己不处理异常信息
        if (!onFailure(e)) {
            //toast显示异常信息
            String message = e.getMessage();
            if (statusView != null) {
                statusView.showError();

            }
            if (!TextUtils.isEmpty(message)) {
//                ToastUtils.showShort(message);
            }
            if (prompDialog != null) {
                prompDialog.dismiss();
            }

            if (layout != null) {
                layout.finishRefresh();
                layout.finishLoadmore();
            }


        }


    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T value);

    /**
     * false 代表子类不处理异常信息
     * true 代表子类自己处理异常信息
     *
     * @param e
     * @return
     */
    public boolean onFailure(Throwable e) {
        return false;
    }

}
