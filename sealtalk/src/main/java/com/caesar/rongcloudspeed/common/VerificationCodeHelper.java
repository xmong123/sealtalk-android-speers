package com.caesar.rongcloudspeed.common;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mac on 2018/4/26.
 */

public abstract class VerificationCodeHelper implements View.OnClickListener {

    private TextView mTv;

    public VerificationCodeHelper(long timeCount, TextView tv) {
        this.timeCount = timeCount;
        this.mTv = tv;
        attach(mTv);

    }


    public VerificationCodeHelper(TextView mTv) {
        this.mTv = mTv;
        attach(mTv);

    }

    private long timeCount = 60 * 1000;

    private void attach(final TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("正在获取");
                tv.setEnabled(false);
                VerificationCodeHelper.this.onClick(tv);
            }
        });
    }


    private void beginCountDown(final TextView tv) {
        CountDownTimer countDownTimer = new CountDownTimer(timeCount, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int i = (int) (millisUntilFinished + 500) / 1000;
                tv.setText(i + "s");
            }

            @Override
            public void onFinish() {
                tv.setText("重新获取");
                tv.setEnabled(true);
            }
        }.start();
    }


    public void onSuccess() {
        beginCountDown(mTv);
    }


    public void onFailure() {
        mTv.setText("获取验证码");
        mTv.setEnabled(true);
    }
}
