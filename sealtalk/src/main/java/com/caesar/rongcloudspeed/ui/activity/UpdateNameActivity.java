package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.db.model.UserInfo;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Result;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.widget.ClearWriteEditText;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.UserInfoViewModel;

public class UpdateNameActivity extends TitleBaseActivity {

    private ClearWriteEditText updateNameCet;
    private UserInfoViewModel userInfoViewModel;
    private String uidString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        uidString = UserInfoUtils.getAppUserId(this);
        initView();
        initViewModel();
    }

    /**
     * 初始化布局
     */
    private void initView() {

        getTitleBar().setTitle(R.string.seal_update_name);
        getTitleBar().setOnBtnRightClickListener(getString(R.string.seal_update_name_save_update), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = updateNameCet.getText().toString().trim();
                if (!TextUtils.isEmpty(newName)) {
                    updateName(newName);
                } else {
                    showToast(R.string.seal_update_name_toast_nick_name_can_not_empty);
                    updateNameCet.setShakeAnimation();
                }
            }
        });

        updateNameCet = findViewById(R.id.cet_update_name);
    }

    /**
     * 初始化ViewModel
     */
    private void initViewModel() {

        userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        // 用户信息
        userInfoViewModel.getUserInfo().observe(this, new Observer<Resource<UserInfo>>() {
            @Override
            public void onChanged(Resource<UserInfo> resource) {
                if (resource.data != null) {
                    String name = TextUtils.isEmpty(resource.data.getName()) ? "" : resource.data.getName();
                    updateNameCet.setText(name);
                    updateNameCet.setSelection(name.length());
                }
            }
        });

        // name 修改结果
        userInfoViewModel.getSetNameResult().observe(this, new Observer<Resource<Result>>() {
            @Override
            public void onChanged(Resource<Result> resultResource) {
                if (resultResource.status == Status.SUCCESS) {
                    showToast(R.string.seal_update_name_toast_nick_name_change_success);

                    String nicename = String.valueOf(updateNameCet.getText());
                    String imgurl = UserInfoUtils.getAppUserUrl(UpdateNameActivity.this);
                    UserInfoUtils.setNikeName(nicename, UpdateNameActivity.this);
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updatenickname(uidString, nicename, imgurl),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    showToast(R.string.profile_update_synchro_success);
                                    finish();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    showToast(R.string.profile_upload_synchro_failed);
                                    finish();
                                }
                            });
                } else if (resultResource.status == Status.ERROR) {
                    //TODO 错误提示
                }
            }
        });
    }

    /**
     * 更新name
     *
     * @param newName
     */
    private void updateName(String newName) {
        if (userInfoViewModel != null) {
            userInfoViewModel.setName(newName);
        }
    }
}
