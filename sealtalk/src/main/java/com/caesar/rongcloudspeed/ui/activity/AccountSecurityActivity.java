package com.caesar.rongcloudspeed.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.caesar.rongcloudspeed.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新消息提醒
 */
public class AccountSecurityActivity extends TitleBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fragment_speer_security);
        getTitleBar().setTitle(R.string.seal_mine_set_account_change_security);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.security_text1, R.id.security_text2, R.id.security_text3, R.id.security_text4, R.id.security_text5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.security_text1:
                startActivity(new Intent(this, AccountCancellationActivity.class));
                break;
            case R.id.security_text2:
                onShowServerDialog();
                break;
            case R.id.security_text3:
                onShowServerDialog();
                break;
            case R.id.security_text4:
                onShowServerDialog();
                break;
            case R.id.security_text5:
                onShowServerDialog();
                break;
            default:
                break;
        }
    }

    public void onShowServerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountSecurityActivity.this).setIcon(R.mipmap.ic_launcher).setTitle("安全中心提示")
                .setMessage("您正在申请安全操作，清联系客服（13961404086）后进行操作").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13961404086"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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
