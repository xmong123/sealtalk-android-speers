package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.caesar.rongcloudspeed.ui.dialog.SelectPictureBottomDialog;
import com.caesar.rongcloudspeed.ui.view.SettingItemView;
import com.caesar.rongcloudspeed.ui.view.UserInfoItemView;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.UserInfoViewModel;
import com.caesar.rongcloudspeed.utils.log.SLog;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * 我的账号
 */
public class MyAccountActivity extends TitleBaseActivity implements View.OnClickListener {
    private UserInfoItemView userInfoUiv;
    private SettingItemView nicknameSiv;
    private SettingItemView phonenumberSiv;
    private SettingItemView useremailSiv;
    private SettingItemView sAccountSiv;
    private SettingItemView genderSiv;
    private UserInfoViewModel userInfoViewModel;
    private boolean isCanSetStAccount;
    private String uidString, nicename,emailString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        uidString = UserInfoUtils.getAppUserId(MyAccountActivity.this);
        nicename = UserInfoUtils.getNickName(MyAccountActivity.this);
        emailString = UserInfoUtils.getUserEmail(MyAccountActivity.this);
        initView();
        initViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uidString = UserInfoUtils.getAppUserId(MyAccountActivity.this);
        nicename = UserInfoUtils.getNickName(MyAccountActivity.this);
        emailString = UserInfoUtils.getUserEmail(MyAccountActivity.this);
        nicknameSiv.setValue(nicename);
        useremailSiv.setValue(emailString);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        getTitleBar().setTitle(R.string.seal_mine_my_account);
        userInfoUiv = findViewById(R.id.uiv_userinfo);
        userInfoUiv.setOnClickListener(this);
        nicknameSiv = findViewById(R.id.siv_nickname);
        nicknameSiv.setOnClickListener(this);
        sAccountSiv = findViewById(R.id.siv_saccount);
        sAccountSiv.setOnClickListener(this);
        phonenumberSiv = findViewById(R.id.siv_phonenumber);
        useremailSiv = findViewById(R.id.siv_useremail);
        useremailSiv.setOnClickListener(this);
        genderSiv = findViewById(R.id.siv_gender);
        genderSiv.setOnClickListener(this);
        nicknameSiv.setValue(nicename);
        useremailSiv.setValue(emailString);
    }


    /**
     * 初始化 viewmodel
     */
    private void initViewModel() {
        userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        // 用户信息
        userInfoViewModel.getUserInfo().observe(this, new Observer<Resource<UserInfo>>() {
            @Override
            public void onChanged(Resource<UserInfo> resource) {
                SLog.d("ss_update", "userInfo == " + resource.data);
                if (resource.data != null) {
                    // 减少图片加载次数，改为失败（获取上一次数据库中数据）或成功时加载图片
                    if (resource.status == Status.SUCCESS || resource.status == Status.ERROR) {
                        ImageLoaderUtils.displayUserPortraitImage(resource.data.getPortraitUri(), userInfoUiv.getHeaderImageView());
                    }
                    nicknameSiv.setValue(resource.data.getName());
                    String phoneNumber = TextUtils.isEmpty(resource.data.getPhoneNumber()) ? "" : resource.data.getPhoneNumber();
                    phonenumberSiv.setValue(phoneNumber);
                    isCanSetStAccount = TextUtils.isEmpty(resource.data.getStAccount());
                    if (!isCanSetStAccount) {
                        sAccountSiv.setValue(resource.data.getStAccount());
                    } else {
                        sAccountSiv.setValue(getString(R.string.seal_mine_my_account_notset));
                    }
                    String gender = resource.data.getGender();
                    if (TextUtils.isEmpty(gender) || gender.equals("male")) {
                        gender = getString(R.string.seal_gender_man);
                    } else if (gender.equals("female")) {
                        gender = getString(R.string.seal_gender_female);
                    }
                    genderSiv.setValue(gender);
                }
            }
        });

        // 头像上传结果
        userInfoViewModel.getUploadPortraitResult().observe(this, new Observer<Resource<Result>>() {
            @Override
            public void onChanged(Resource<Result> resource) {
                if (resource.status == Status.SUCCESS) {

                    String imgurl = String.valueOf(resource.data.getResult());
                    Log.d("resource.data:", imgurl);
                    UserInfoUtils.setAppUserUrl(imgurl, MyAccountActivity.this);
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updatenickname(uidString, nicename, imgurl),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    if (baseData.getCode() == CODE_SUCC) {
                                        showToast(R.string.profile_update_synchro_success);
                                    } else {
                                        showToast(baseData.getInfo());
                                    }

                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    showToast(R.string.profile_upload_synchro_failed);
                                }
                            });
                    showToast(R.string.profile_update_portrait_success);
                } else if (resource.status == Status.ERROR) {
                    showToast(R.string.profile_upload_portrait_failed);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uiv_userinfo:
                showSelectPictureDialog();
                break;
            case R.id.siv_nickname:
                Intent intent = new Intent(this, UpdateNameActivity.class);
                startActivity(intent);
                break;
            case R.id.siv_saccount:
                if (isCanSetStAccount) {
                    Intent intentSt = new Intent(this, UpdateStAccountActivity.class);
                    startActivity(intentSt);
                }
                break;
            case R.id.siv_useremail:
                Intent intentEm = new Intent(this, UpdateEmailActivity.class);
                startActivity(intentEm);
                break;
            case R.id.siv_gender:
                Intent intentGender = new Intent(this, UpdateGenderActivity.class);
                startActivity(intentGender);
                break;
            default:
                //DO nothing
                break;
        }
    }

    /**
     * 选择图片的 dialog
     */
    private void showSelectPictureDialog() {
        SelectPictureBottomDialog.Builder builder = new SelectPictureBottomDialog.Builder();
        builder.setOnSelectPictureListener(new SelectPictureBottomDialog.OnSelectPictureListener() {
            @Override
            public void onSelectPicture(Uri uri) {
                //上传图片
                uploadPortrait(uri);
            }
        });
        SelectPictureBottomDialog dialog = builder.build();
        dialog.show(getSupportFragmentManager(), "select_picture_dialog");
    }


    /**
     * 上传头像
     *
     * @param uri
     */
    private void uploadPortrait(Uri uri) {
        if (userInfoViewModel != null) {
            userInfoViewModel.uploadPortrait(uri);
        }
    }


}
