package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Result;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.UserInfoViewModel;

/**
 * 修改密码
 */
public class UpdatePayPasswordActivity extends TitleBaseActivity {
    private EditText oldPasswordEt;
    private EditText newPasswordEt;
    private EditText confirmPasswordEt;
    private Button updateBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_paypassword);

        initView();
    }


    /**
     * 初始化 view
     */
    private void initView() {
        getTitleBar().setTitle(R.string.seal_mine_set_account_change_password);

        oldPasswordEt = findViewById(R.id.et_old_password);
        newPasswordEt = findViewById(R.id.et_new_password);
        confirmPasswordEt = findViewById(R.id.et_confirm_password);
        updateBtn = findViewById(R.id.btn_update);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = oldPasswordEt.getText().toString();
                String newPassword = newPasswordEt.getText().toString();
                String confirmPassword = confirmPasswordEt.getText().toString();

                if (oldPassword.equals(newPassword)) {
                    showToast(R.string.seal_update_password_toast_password_old_equal_new);
                    return ;
                }

                if (!confirmPassword.equals(newPassword)) {
                    showToast(R.string.seal_update_password_toast_password_not_equal);
                    return ;
                }
                changePassword(oldPassword,newPassword);
            }
        });


        oldPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setConformButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setConformButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setConformButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 检测是否满足要求
     */
    private void setConformButtonState() {
        String oldPassword = oldPasswordEt.getText().toString().trim();
        String newPassword = newPasswordEt.getText().toString().trim();
        String confirmPassword = confirmPasswordEt.getText().toString().trim();
        if (!TextUtils.isEmpty(oldPassword)
                && !TextUtils.isEmpty(newPassword)
                && newPassword.length() >= 6
                && newPassword.length() <= 16
                && !TextUtils.isEmpty(confirmPassword)
                && confirmPassword.length() >= 6
                && confirmPassword.length() <= 16) {
            updateBtn.setEnabled(true);
        } else {
            updateBtn.setEnabled(false);
        }
    }


    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     */
    private void changePassword(String oldPassword, String newPassword) {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().resetuserpaypass(UserInfoUtils.getAppUserId(this), newPassword),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData circleItemResult) {
                        finish();
                        Toast.makeText(UpdatePayPasswordActivity.this, "支付密码修改成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(UpdatePayPasswordActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
