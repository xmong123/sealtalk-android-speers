package com.caesar.rongcloudspeed.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.ui.BaseActivity;
import com.caesar.rongcloudspeed.utils.SPConfirmDialog;
import com.classic.common.MultipleStatusView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public abstract class MultiStatusActivity extends BaseShopActivity {

    protected CommonTitleBar titlebar;
    protected MultipleStatusView multipleStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_status);
        multipleStatusView = (MultipleStatusView) findViewById(R.id.multiple_status_view);
        titlebar = (CommonTitleBar) findViewById(R.id.titlebar);
        getLayoutInflater().inflate(getContentView(), multipleStatusView,true);
    }
    public abstract int getContentView();

    public void showConfirmDialog(String message , String title , final SPConfirmDialog.ConfirmDialogListener confirmDialogListener , final int actionType){
        SPConfirmDialog.Builder builder = new SPConfirmDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                if(confirmDialogListener!=null)confirmDialogListener.clickOk(actionType);
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private AlertDialog.Builder builder;
    public void onShowReviewDialog() {
        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("认证会员提示")
                .setMessage("您还不是认证同行快线会员，请联系客服（15033370607）后进行操作").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:15033370607"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
//                        Toast.makeText(getActivity(), "确定按钮", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
//                        Toast.makeText(getActivity(), "关闭按钮", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }
}
