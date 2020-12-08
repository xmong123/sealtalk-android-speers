package com.caesar.rongcloudspeed.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.SectionLessonBaseBean;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新消息提醒
 */
public class AccountCancellationActivity extends TitleBaseActivity {
    @BindView(R.id.cancellation_title)
    TextView cancellation_title;
    private String uidString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fragment_speer_cancellation);
        getTitleBar().setTitle("申请注销账户");
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
    }

    @OnClick({R.id.cancellation_btn})
    public void onViewClicked(View view) {
        onShowCancellationDialog();
    }

    public void onShowCancellationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountCancellationActivity.this).setIcon(R.mipmap.ic_launcher).setTitle("申请注册账号提示")
                .setMessage("您正在申请注册账号，请您再次确认是否进行注销当前账号（一旦操作无法复原）").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        showLoadingDialog("");
                        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().userCancellation(uidString),
                                new NetworkCallback<BaseData>() {
                                    @Override
                                    public void onSuccess(BaseData lessonBaseBean) {
                                        dismissLoadingDialog();
                                        Toast.makeText(AccountCancellationActivity.this, "您已成功提交了申请注销同行快线账户", Toast.LENGTH_LONG).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        dismissLoadingDialog();
                                        Toast.makeText(AccountCancellationActivity.this, "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }


}
