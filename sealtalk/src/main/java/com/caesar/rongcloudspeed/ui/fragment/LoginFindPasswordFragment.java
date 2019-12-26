package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.ErrorCode;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.result.SmsCode;
import com.caesar.rongcloudspeed.model.CountryInfo;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.SelectCountryActivity;
import com.caesar.rongcloudspeed.ui.widget.ClearWriteEditText;
import com.caesar.rongcloudspeed.utils.AccountValidatorUtil;
import com.caesar.rongcloudspeed.viewmodel.LoginViewModel;
import com.caesar.rongcloudspeed.utils.log.SLog;

public class LoginFindPasswordFragment extends BaseFragment{
    private static final int REQUEST_CODE_SELECT_COUNTRY = 1000;
    private LoginViewModel loginViewModel;
    private Button sendCodeBtn;
    private Button confirmBtn;
    private ClearWriteEditText phoneEdit;
    private TextView countryCodeTv;
    private boolean isRequestVerifyCode;
    private ClearWriteEditText codeEdit;
    private ClearWriteEditText confirmPasswordEdit;
    private ClearWriteEditText passwordEdit;
    private TextView countryNameTv;
    private OnResetPasswordListener listener;

    @Override
    protected int getLayoutResId() {
        return R.layout.login_fragment_find_password;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        findView(R.id.ll_country_select, true);
        countryNameTv = findView(R.id.tv_country_name);
        countryCodeTv = findView(R.id.tv_country_code);
        phoneEdit  = findView(R.id.cet_phone);
        codeEdit = findView(R.id.cet_code);
        sendCodeBtn = findView(R.id.btn_send_code, true);
        passwordEdit = findView(R.id.cet_password);
        confirmPasswordEdit = findView(R.id.cet_confirm_password);
        confirmBtn = findView(R.id.btn_confirm, true);


        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    sendCodeBtn.setEnabled(true);
                } else {
                    sendCodeBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        codeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    protected void onInitViewModel() {
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);

        loginViewModel.getCheckPhoneAndSendCode().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                // 提示
                if (resource.status == Status.SUCCESS) {
                    showToast(R.string.seal_login_toast_send_code_success);
                } else if (resource.status == Status.LOADING) {

                } else {
                    showToast(resource.message+".");
                    sendCodeBtn.setEnabled(true);
                    phoneEdit.setEnabled(true);
                }
            }
        });


        // 等待接受验证码倒计时， 并刷新及时按钮的刷新
        loginViewModel.getCodeCountDown().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0) {
                    sendCodeBtn.setText(integer + "s");
                    isRequestVerifyCode = true;
                } else {
                    // 当计时结束时， 恢复按钮的状态
                    sendCodeBtn.setEnabled(true);
                    phoneEdit.setEnabled(true);
                    sendCodeBtn.setText(R.string.seal_login_send_code);
//                    isRequestVerifyCode = false;

                }
            }
        });

        loginViewModel.getResetPasswordResult().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                if (resource.status == Status.SUCCESS) {

                    if (listener != null) {
                        String phone = phoneEdit.getText().toString();
                        String countryName = countryNameTv.getText().toString();
                        String countryCode = countryCodeTv.getText().toString();
                        listener.onResetPasswordSuccess(phone,  countryCode, countryName);
                    }

                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            showToast(R.string.seal_login_reset_password_toast_reset_password_success);
                        }
                    });
                } else if (resource.status == Status.ERROR) {
                    int code = resource.code;
                    if (resource.code != ErrorCode.CHECK_VERIFY_CODE_FAILED.getCode()) {
                        sendCodeBtn.setEnabled(true);
                        phoneEdit.setEnabled(true);
                        sendCodeBtn.setText(R.string.seal_login_send_code);
//                        isRequestVerifyCode = false;
                    }

                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            showToast(resource.message+".....");
                        }
                    });
                } else {
                    showLoadingDialog(R.string.seal_login_reset_password_loading_password);
                }
            }
        });

    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private String msgCode;

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.ll_country_select:
                startActivityForResult(new Intent(getActivity(), SelectCountryActivity.class), REQUEST_CODE_SELECT_COUNTRY);
                break;
            case R.id.btn_send_code:
                String phoneStr = phoneEdit.getText().toString().trim();
                String phoneCodeStr = countryCodeTv.getText().toString().trim();
                if (TextUtils.isEmpty(phoneStr)) {
                   showToast(R.string.seal_login_toast_phone_number_is_null);
                    return;
                }
                if(TextUtils.isEmpty(phoneCodeStr)){
                    phoneCodeStr = "86";
                }else if(phoneCodeStr.startsWith("+")){
                    phoneCodeStr = phoneCodeStr.substring(1);
                }
                if (!AccountValidatorUtil.isMobile(phoneStr)) {
                    Toast.makeText(getActivity(), "手机格式错误", Toast.LENGTH_LONG).show();
                    return;
                }
                sendCodeBtn.setEnabled(false);
                phoneEdit.setEnabled(false);
                String finalPhoneCode = phoneCodeStr;
                NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchSmsCode(phoneStr),
                        new NetworkCallback<SmsCode>() {
                            @Override
                            public void onSuccess(SmsCode baseData) {
                                msgCode=baseData.getCodemsg().trim();
                                codeEdit.setText(baseData.getCodemsg().trim());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        isRequestVerifyCode=true;
                                        checkPhoneAndSendCode(finalPhoneCode, phoneStr);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getActivity(), "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                            }
                        });

                break;
            case R.id.btn_confirm:
                String fpPhoneStr = phoneEdit.getText().toString().trim();
                String fpCodeStr = codeEdit.getText().toString().trim();
                String fpPasswordStr = passwordEdit.getText().toString().trim();
                String fpConfirmPasswordStr = confirmPasswordEdit.getText().toString().trim();
                String fpCountryCodeStr = countryCodeTv.getText().toString().trim();
                if(TextUtils.isEmpty(fpCountryCodeStr)){
                    fpCountryCodeStr = "86";
                }else if(fpCountryCodeStr.startsWith("+")){
                    fpCountryCodeStr = fpCountryCodeStr.substring(1);
                }
                if (TextUtils.isEmpty(fpPhoneStr)) {
                    showToast(R.string.seal_login_toast_phone_number_is_null);
                    phoneEdit.setShakeAnimation();
                    return;
                }

                if (!AccountValidatorUtil.isMobile(fpPhoneStr)) {
                    Toast.makeText(getActivity(), "手机格式错误", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(fpCodeStr)) {
                    showToast(R.string.seal_login_toast_code_is_null);
                    codeEdit.setShakeAnimation();
                    return;
                }

                if (TextUtils.isEmpty(fpPasswordStr)) {
                    showToast(R.string.seal_login_toast_new_password_not_null);
                    passwordEdit.setShakeAnimation();
                    return;
                }

                if (passwordEdit.length() < 6 || passwordEdit.length() > 16) {
                    showToast(R.string.seal_login_toast_passwords_invalid);
                    return;
                }

                if (TextUtils.isEmpty(fpConfirmPasswordStr)) {
                   showToast(R.string.seal_login_toast_confirm_password_not_null);
                    confirmPasswordEdit.setShakeAnimation();
                    return;
                }

                if (!fpPasswordStr.equals(fpConfirmPasswordStr)) {
                    showToast(R.string.seal_login_toast_passwords_do_not_match);
                    return;
                }

                if (!isRequestVerifyCode) {
                    showToast(R.string.seal_login_toast_not_send_code);
                    return;
                }
                String finalCountryCodeStr = fpCountryCodeStr;
                NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updatePwd(fpPhoneStr, fpPasswordStr, fpCodeStr),
                        new NetworkCallback<BaseData>() {
                            @Override
                            public void onSuccess(BaseData baseData) {
                                if (NetworkResultUtils.isSuccess(baseData)) {
                                    Toast.makeText(getActivity(), baseData.getInfo(), Toast.LENGTH_LONG).show();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            resetPassword(finalCountryCodeStr, fpPhoneStr, "9999", fpPasswordStr);
                                        }
                                    });
                                }else{
                                    Toast.makeText(getActivity(), "重置密码网络异常,请稍后再试..", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getActivity(), "重置密码网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                            }
                        });

                break;
            default:
                break;
        }
    }

    /**
     * 检测手机并发送验证码
     * @param phoneCode
     * @param phoneNumber
     */
    private void checkPhoneAndSendCode(String phoneCode, String phoneNumber) {
        loginViewModel.checkPhoneAndSendCode(phoneCode, phoneNumber);
    }

    /**
     * 重写设置密码
     * @param countryCode
     * @param phoneNumber
     * @param shortMsgCode
     * @param password
     */
    private void resetPassword(String countryCode, String phoneNumber, String shortMsgCode, String password) {
        loginViewModel.resetPassword(countryCode, phoneNumber, shortMsgCode, password);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE_SELECT_COUNTRY) {
            CountryInfo info = data.getParcelableExtra(SelectCountryActivity.RESULT_PARAMS_COUNTRY_INFO);
            SLog.d("ss_country", "info = " + info);
            countryNameTv.setText(info.getCountryName());
            countryCodeTv.setText(info.getZipCode());
        }
    }


    public void setOnResetPasswordListener(OnResetPasswordListener listener) {
        this.listener = listener;
    }

    public interface OnResetPasswordListener {
        void onResetPasswordSuccess(String phone, String region, String countryName);
    }

}
