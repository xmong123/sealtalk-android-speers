package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

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
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.UserInfoViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateNameActivity extends TitleBaseActivity {

    private ClearWriteEditText updateNameCet;
    private UserInfoViewModel userInfoViewModel;
    private String uidString;
    private String newName;
    private String baiduToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        uidString = UserInfoUtils.getAppUserId(this);
        baiduToken = UserInfoUtils.getBaiduToken(this);
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
                newName = updateNameCet.getText().toString().trim();
                if (!TextUtils.isEmpty(newName)) {
                    showLoadingDialog("");
                    baiduAiHandler.sendEmptyMessage(0);
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
                    showToast(R.string.profile_update_synchro_success);
                    finish();
                } else if (resultResource.status == Status.ERROR) {
                    //TODO 错误提示
                    showToast(R.string.profile_upload_synchro_failed);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler baiduAiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitBaiduApi().getBaiduTextCheck(newName, baiduToken),
                            new NetworkCallback<BaiduTextBean>() {
                                @Override
                                public void onSuccess(BaiduTextBean baiduTextBean) {
                                    int conclusionType = baiduTextBean.getConclusionType();
                                    if (conclusionType == 1) {
                                        baiduAiHandler.sendEmptyMessage(1);
                                    } else {
                                        dismissLoadingDialog();
                                        BaiduTextBean.BaiduTextData baiduTextData = baiduTextBean.getData().get(0);
                                        ToastUitl.showToastWithImg(baiduTextData.getMsg(), R.drawable.ic_warm);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    dismissLoadingDialog();
                                    showToast(R.string.network_error);
                                }
                            });
                    break;
                case 1:
                    String nicename = String.valueOf(updateNameCet.getText());
                    String imgurl = UserInfoUtils.getAppUserUrl(UpdateNameActivity.this);
                    UserInfoUtils.setNikeName(nicename, UpdateNameActivity.this);
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updatenickname(uidString, nicename, imgurl),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    baiduAiHandler.sendEmptyMessage(2);
                                    showToast(R.string.seal_update_name_toast_nick_name_change_success);
                                    dismissLoadingDialog();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    dismissLoadingDialog();
                                    showToast(R.string.profile_upload_synchro_failed);
                                }
                            });
                    break;
                case 2:
                    if (userInfoViewModel != null) {
                        userInfoViewModel.setName(newName);
                    }
                    break;
            }
        }
    };

}
