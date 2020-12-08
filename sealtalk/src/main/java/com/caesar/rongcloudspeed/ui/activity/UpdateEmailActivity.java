package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.BaiduTextBean;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.db.model.UserInfo;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Result;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.widget.ClearWriteEditText;
import com.caesar.rongcloudspeed.util.ToastUitl;
import com.caesar.rongcloudspeed.utils.AccountValidatorUtil;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.UserInfoViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateEmailActivity extends TitleBaseActivity {

    private ClearWriteEditText updateEmailCet;
    private String uidString;
    private String emailString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        uidString = UserInfoUtils.getAppUserId(this);
        emailString = UserInfoUtils.getUserEmail(this);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {

        getTitleBar().setTitle("邮箱设置");
        getTitleBar().setOnBtnRightClickListener(getString(R.string.seal_update_name_save_update), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailString = updateEmailCet.getText().toString().trim();
                if (!TextUtils.isEmpty(emailString)) {
                    if (!AccountValidatorUtil.isEmail(emailString)) {
                        Toast.makeText(UpdateEmailActivity.this, "邮箱格式错误", Toast.LENGTH_LONG).show();
                    }else{
                        showLoadingDialog("");
                        changeHandler.sendEmptyMessage(0);
                    }
                } else {
                    showToast("邮箱不能为空");
                    updateEmailCet.setShakeAnimation();
                }
            }
        });

        updateEmailCet = findViewById(R.id.cet_update_email);
        updateEmailCet.setText(emailString);
        updateEmailCet.setSelection(emailString.length());
    }

    @SuppressLint("HandlerLeak")
    Handler changeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateEmail(uidString, emailString),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    UserInfoUtils.setUserEmail(emailString, UpdateEmailActivity.this);
                                    showToast("邮箱信息更新成功");
                                    dismissLoadingDialog();
                                    finish();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    dismissLoadingDialog();
                                    showToast("邮箱信息更新失败");
                                }
                            });
                    break;
            }
        }
    };

}
