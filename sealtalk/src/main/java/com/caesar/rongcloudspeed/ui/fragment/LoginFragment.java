package com.caesar.rongcloudspeed.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.ErrorCode;
import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.data.UserInfo;
import com.caesar.rongcloudspeed.data.UserOrder;
import com.caesar.rongcloudspeed.data.result.UserInfoResult;
import com.caesar.rongcloudspeed.model.CountryInfo;
import com.caesar.rongcloudspeed.model.RegisterResult;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.model.UserCacheInfo;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.MainActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectCountryActivity;
import com.caesar.rongcloudspeed.ui.activity.WebViewActivity;
import com.caesar.rongcloudspeed.ui.widget.ClearWriteEditText;
import com.caesar.rongcloudspeed.utils.AccountValidatorUtil;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.utils.log.SLog;
import com.caesar.rongcloudspeed.viewmodel.LoginViewModel;
import com.caesar.rongcloudspeed.network.NetworkCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoginFragment extends BaseFragment {
    private static final int REQUEST_CODE_SELECT_COUNTRY = 1000;
    private ClearWriteEditText phoneNumberEdit;
    private ClearWriteEditText passwordEdit;
    private CheckBox agree_checkbox;
    private TextView countryNameTv;
    private TextView countryCodeTv;
    private String phoneString;
    private String passwordString;
    private String userNameString;
    private LoginViewModel loginViewModel;
    private int resourceCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.login_fragment_login;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        phoneNumberEdit = findView(R.id.cet_login_phone);
        passwordEdit = findView(R.id.cet_login_password);
        agree_checkbox = findView(R.id.agree_checkbox);
        countryNameTv = findView(R.id.tv_country_name);
        countryCodeTv = findView(R.id.tv_country_code);
        findView(R.id.btn_login, true);
        findView(R.id.btn_agree_text2, true);
        findView(R.id.btn_agree_text4, true);
        findView(R.id.ll_country_select, true);
        phoneNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    phoneNumberEdit.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phoneNumberEdit.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onInitViewModel() {
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.getLoginResult().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                if (resource.status == Status.SUCCESS) {
                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            showToast(R.string.seal_login_toast_success);
                            toMain(resource.data);
                        }
                    });

                } else if (resource.status == Status.LOADING) {
                    showLoadingDialog(R.string.seal_loading_dialog_logining);
                } else {
                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            if (resource.code == 11000) {
                                resourceCode = 11000;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendCode("86", phoneString);
                                        registerLogin("86", phoneString, "9999", userNameString, passwordString);
                                    }
                                });
                                showToast("服务器更新升级，清稍后...");
                            } else if (resource.code == 11001) {
                                resourceCode = 11001;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendCode("86", phoneString);
                                        resetLoginPassword("86", phoneString, "9999", passwordString);
                                    }
                                });
                                showToast("服务器更新升级，密码重置清稍后...");
                            } else {
                                showToast(resource.message + "....");
                            }

                        }
                    });
                }
            }
        });

        loginViewModel.getLastLoginUserCache().observe(this, new Observer<UserCacheInfo>() {
            @Override
            public void onChanged(UserCacheInfo userInfo) {
                phoneNumberEdit.setText(userInfo.getPhoneNumber());
                String region = userInfo.getRegion();
                if (!region.startsWith("+")) {
                    region = "+" + region;
                }
                countryCodeTv.setText(region);
                CountryInfo countryInfo = userInfo.getCountryInfo();
                if (countryInfo != null && !TextUtils.isEmpty(countryInfo.getCountryName())) {
                    countryNameTv.setText(countryInfo.getCountryName());
                }
                passwordEdit.setText(userInfo.getPassword());
            }
        });

        loginViewModel.getResetLoginPasswordResult().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                if (resource.status == Status.SUCCESS) {

                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    login("86", phoneString, passwordString);
                                }
                            });
                            showToast(R.string.seal_login_reset_password_toast_reset_password_success);
                        }
                    });
                } else if (resource.status == Status.ERROR) {

                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            showToast(resource.message + "..");
                        }
                    });
                } else {
                    showLoadingDialog(R.string.seal_login_reset_password_loading_password);
                }
            }
        });

        loginViewModel.getRegisterLoginResult().observe(this, new Observer<Resource<RegisterResult>>() {
            @Override
            public void onChanged(Resource<RegisterResult> resource) {
                if (resource.status == Status.SUCCESS) {

                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    login("86", phoneString, passwordString);
                                }
                            });
                            showToast(R.string.seal_login_register_toast_register_success);
                        }
                    });

                } else if (resource.status == Status.ERROR) {
                    int code = resource.code;
                    SLog.d("ss_register", "register failed = " + code);

                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            showToast(resource.message + ".");
                        }
                    });

                } else {
                    showLoadingDialog(R.string.seal_login_register_registering);
                }
            }
        });
    }


    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.btn_login:
                String phoneStr = phoneNumberEdit.getText().toString().trim();
                String passwordStr = passwordEdit.getText().toString().trim();
                String countryCodeStr = countryCodeTv.getText().toString().trim();

                if (TextUtils.isEmpty(phoneStr)) {
                    showToast(R.string.seal_login_toast_phone_number_is_null);
                    phoneNumberEdit.setShakeAnimation();
                    break;
                }

                if (TextUtils.isEmpty(passwordStr)) {
                    showToast(R.string.seal_login_toast_password_is_null);
                    passwordEdit.setShakeAnimation();
                    return;
                }

                if (passwordStr.contains(" ")) {
                    showToast(R.string.seal_login_toast_password_cannot_contain_spaces);
                    passwordEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(countryCodeStr)) {
                    countryCodeStr = "86";
                } else if (countryCodeStr.startsWith("+")) {
                    countryCodeStr = countryCodeStr.substring(1);
                }
                if (!AccountValidatorUtil.isMobile(phoneStr)) {
                    Toast.makeText(getActivity(), "手机格式错误", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!agree_checkbox.isChecked()) {
                    Toast.makeText(getActivity(), "请先阅读并同意《服务协议》和《隐私政策》", Toast.LENGTH_LONG).show();
                    return;
                }
                String finalCountryCodeStr = countryCodeStr;
                NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().login(phoneStr, passwordStr),
                        new NetworkCallback<UserInfoResult>() {
                            @Override
                            public void onSuccess(UserInfoResult baseData) {
                                if (NetworkResultUtils.isSuccess(baseData)) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            UserInfo userInfo = baseData.getUrl();
                                            UserInfoUtils.setAppUserId(userInfo.getId(), getActivity());
                                            UserInfoUtils.setUserName(userInfo.getUser_login(), getActivity());
                                            UserInfoUtils.setNikeName(userInfo.getUser_nicename(), getActivity());
                                            UserInfoUtils.setUserEmail(userInfo.getUser_email(),getActivity());
                                            UserInfoUtils.setUserSum(userInfo.getUser_sum(), getActivity());
                                            UserInfoUtils.setUserType(userInfo.getUser_type(), getActivity());
                                            UserInfoUtils.setUserIndustry(userInfo.getUser_industry(), getActivity());
                                            UserInfoUtils.setUserProfession(userInfo.getUser_profession(), getActivity());
                                            UserInfoUtils.setUserSoft(userInfo.getUser_soft(), getActivity());
                                            UserInfoUtils.setPhone(userInfo.getMobile(), getActivity());
                                            UserInfoUtils.setPayPassWord(userInfo.getUser_paypass(), getActivity());
                                            UserInfoUtils.setAppUserUrl(userInfo.getAvatar(), getActivity());
                                            UserInfoUtils.setAppUserOrderSum(userInfo.getOrderCount(), getActivity());
                                            List<UserOrder> userOrderList = userInfo.getUser_orders();
                                            if (userOrderList != null && userOrderList.size() > 0) {
                                                Set<String> set = new HashSet<>();
                                                for (UserOrder order : userOrderList) {
                                                    set.add(order.getGoods_id());
                                                }
                                                UserInfoUtils.setAppUserLessones(set, getActivity());
                                            }
                                            phoneString = phoneStr;
                                            passwordString = passwordStr;
                                            userNameString = userInfo.getUser_nicename();
                                            login(finalCountryCodeStr, phoneStr, passwordStr);
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), baseData.getInfo(), Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                            }
                        });

                break;
            case R.id.btn_agree_text2:
                // 跳转区域选择界面
                Bundle bundle = new Bundle();
                bundle.putString("url", "file:///android_asset/webpage/memaiagree.html");
                bundle.putString("title", "《服务协议》");
                ActivityUtils.startActivity(bundle, WebViewActivity.class);
                break;
            case R.id.btn_agree_text4:
                // 跳转区域选择界面
                Bundle bundle2 = new Bundle();
                bundle2.putString("url", "file:///android_asset/webpage/memaifree.html");
                bundle2.putString("title", "《隐私政策》");
                ActivityUtils.startActivity(bundle2, WebViewActivity.class);
                break;
            case R.id.ll_country_select:
                // 跳转区域选择界面
                startActivityForResult(new Intent(getActivity(), SelectCountryActivity.class), REQUEST_CODE_SELECT_COUNTRY);
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:


                    break;
                default:
                    break;
            }
        }
    };

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 登录到 业务服务器，以获得登录 融云 IM 服务器所必须的 token
     *
     * @param region 国家区号
     * @param phone  电话号/帐号
     * @param pwd    密码
     */
    private void login(String region, String phone, String pwd) {
        loginViewModel.login(region, phone, pwd);
    }

    /**
     * 请求发送验证码
     *
     * @param phoneCode   国家地区的手机区号
     * @param phoneNumber 手机号
     */
    private void sendCode(String phoneCode, String phoneNumber) {
        loginViewModel.sendCode(phoneCode, phoneNumber);
    }

    /**
     * 重写设置密码
     *
     * @param countryCode
     * @param phoneNumber
     * @param shortMsgCode
     * @param password
     */
    private void resetLoginPassword(String countryCode, String phoneNumber, String shortMsgCode, String password) {
        loginViewModel.resetLoginPassword(countryCode, phoneNumber, shortMsgCode, password);
    }

    private void registerLogin(String phoneCode, String phoneNumber, String shortMsgCode, String nickName, String password) {
        loginViewModel.registerLogin(phoneCode, phoneNumber, shortMsgCode, nickName, password);
    }

    private void toMain(String userId) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE_SELECT_COUNTRY) {
            CountryInfo info = data.getParcelableExtra(SelectCountryActivity.RESULT_PARAMS_COUNTRY_INFO);
            countryNameTv.setText(info.getCountryName());
            countryCodeTv.setText(info.getZipCode());
        }
    }

    /**
     * 设置上参数
     *
     * @param phone
     * @param region
     * @param countryName
     */
    public void setLoginParams(String phone, String region, String countryName) {
        phoneNumberEdit.setText(phone);
        countryNameTv.setText(countryName);
        countryCodeTv.setText(region);
    }
}
