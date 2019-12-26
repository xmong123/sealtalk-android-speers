package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.circle.ui.PayCardCircleActivity;
import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.TranslateFriendActivity;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.jrmf360.rylib.JrmfClient;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.db.model.UserInfo;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.model.VersionInfo;
import com.caesar.rongcloudspeed.model.qrcode.QrCodeDisplayType;
import com.caesar.rongcloudspeed.ui.activity.AboutSealTalkActivity;
import com.caesar.rongcloudspeed.ui.activity.AccountSettingActivity;
import com.caesar.rongcloudspeed.ui.activity.ChangeLanguageActivity;
import com.caesar.rongcloudspeed.ui.activity.MyAccountActivity;
import com.caesar.rongcloudspeed.ui.activity.QrCodeDisplayActivity;
import com.caesar.rongcloudspeed.ui.view.SettingItemView;
import com.caesar.rongcloudspeed.ui.view.UserInfoItemView;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.viewmodel.AppViewModel;
import com.caesar.rongcloudspeed.viewmodel.UserInfoViewModel;
import io.rong.imkit.RongIM;
import io.rong.imkit.utilities.LangUtils;
import io.rong.imlib.model.CSCustomServiceInfo;

public class MainMeFragment extends BaseFragment {

    private SettingItemView sivAbout;
    private UserInfoItemView uivUserInfo;
    private AppViewModel appViewModel;
    private SettingItemView sivLanguage;

    @Override
    protected int getLayoutResId() {
        return R.layout.main_fragment_me;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {

        uivUserInfo = findView(R.id.uiv_userinfo, true);
        findView(R.id.siv_setting_qrcode, true);
        findView(R.id.siv_setting_translate, true);
        findView(R.id.siv_setting_paylist, true);
        findView(R.id.siv_setting_account, true);
        sivLanguage = findView(R.id.siv_language, true);
        findView(R.id.siv_my_wallet, true);
        findView(R.id.siv_feedback, true);
        sivAbout = findView(R.id.siv_about, true);
    }

    @Override
    protected void onInitViewModel() {
        UserInfoViewModel userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);

        userInfoViewModel.getUserInfo().observe(this, new Observer<Resource<UserInfo>>() {
            @Override
            public void onChanged(Resource<UserInfo> resource) {
                if (resource.data != null) {
                    UserInfo info = resource.data;
                    uivUserInfo.setName(info.getName());
                    ImageLoaderUtils.displayUserPortraitImage(info.getPortraitUri(), uivUserInfo.getHeaderImageView());
                }

            }
        });

        appViewModel.getHasNewVersion().observe(this, new Observer<Resource<VersionInfo.AndroidVersion>>() {
            @Override
            public void onChanged(Resource<VersionInfo.AndroidVersion> resource) {
                if (resource.status == Status.SUCCESS && resource.data != null) {
                    sivAbout.setTagImageVisibility(View.VISIBLE);
                }
            }
        });

        appViewModel.getLanguageLocal().observe(this, new Observer<LangUtils.RCLocale>() {
            @Override
            public void onChanged(LangUtils.RCLocale rcLocale) {
                if (rcLocale == LangUtils.RCLocale.LOCALE_US) {
                    sivLanguage.setValue(R.string.lang_english);
                } else {
                    sivLanguage.setValue(R.string.lang_chs);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //Toast.makeText(getActivity(), "提示：onStart", Toast.LENGTH_SHORT).show();
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getSumUser(UserInfoUtils.getAppUserId(getActivity())),
                new NetworkCallback<UserSumResult>() {
                    @Override
                    public void onSuccess(UserSumResult userSumResult) {
                        UserSumUrl userSumUrl=userSumResult.getUrl();
                        String sumString=String.valueOf(userSumUrl.getUser_sum());
                        UserInfoUtils.setUserSum(sumString,getActivity());
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity(), "网络异常,请稍后再试", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.siv_setting_qrcode:
                Intent qrCodeIntent = new Intent(getActivity(), QrCodeDisplayActivity.class);
                qrCodeIntent.putExtra(IntentExtra.STR_TARGET_ID, RongIM.getInstance().getCurrentUserId());
                qrCodeIntent.putExtra(IntentExtra.SERIA_QRCODE_DISPLAY_TYPE, QrCodeDisplayType.PRIVATE);
                startActivity(qrCodeIntent);
                break;
            case R.id.siv_setting_translate:
                Intent transIntent = new Intent(getActivity(), TranslateFriendActivity.class);
                startActivity(transIntent);
                break;
            case R.id.siv_setting_paylist:
                Intent paylistIntent = new Intent(getActivity(), PayCardCircleActivity.class);
                startActivity(paylistIntent);
                break;
            case R.id.uiv_userinfo:
                Intent intentUserInfo = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intentUserInfo);
                break;
            case R.id.siv_setting_account:
                startActivity(new Intent(getActivity(), AccountSettingActivity.class));

                break;
            case R.id.siv_language:
                startActivity(new Intent(getActivity(), ChangeLanguageActivity.class));

                break;
            case R.id.siv_my_wallet:
                JrmfClient.intentWallet(getActivity());
                break;
            case R.id.siv_feedback:
                CSCustomServiceInfo.Builder builder = new CSCustomServiceInfo.Builder();
                builder.province(getString(R.string.beijing));
                builder.city(getString(R.string.beijing));
                RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU146001495753714", getString(R.string.seal_main_mine_online_custom_service), builder.build());

                break;
            case R.id.siv_about:
                sivAbout.setTagImageVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), AboutSealTalkActivity.class);
                VersionInfo.AndroidVersion data = appViewModel.getHasNewVersion().getValue().data;
                if (data != null && !TextUtils.isEmpty(data.getUrl())) {
                    intent.putExtra(IntentExtra.URL, data.getUrl());
                }
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
