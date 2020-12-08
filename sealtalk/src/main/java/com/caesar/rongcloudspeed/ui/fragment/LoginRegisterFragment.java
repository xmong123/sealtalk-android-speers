package com.caesar.rongcloudspeed.ui.fragment;

import android.app.Activity;
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
import com.caesar.rongcloudspeed.model.RegisterResult;
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

public class LoginRegisterFragment extends BaseFragment {

    private static final int REQUEST_CODE_SELECT_COUNTRY = 1000;
    private static final int REQUEST_CODE_SELECT_CATEGORY = 1001;
    private ClearWriteEditText phoneEdit;
    private ClearWriteEditText passwordEdit;
    private ClearWriteEditText codeEdit;
    private ClearWriteEditText userNameEdit;
    private Button sendCodeBtn;
    private LoginViewModel loginViewModel;
    private TextView countryNameTv;
    private TextView countryCodeTv;
    private boolean isRequestVerifyCode = false; // 是否请求成功验证码
    private OnRegisterListener listener;
    private String msgCode;
    private String registerCode ;
    private String registerUserName ;
    private String registerPassword ;

    @Override
    protected int getLayoutResId() {
        return R.layout.login_fragment_register;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        userNameEdit = findView(R.id.cet_reg_username);
        passwordEdit = findView(R.id.cet_reg_password);
        countryNameTv = findView(R.id.tv_reg_country_name);
        countryCodeTv = findView(R.id.tv_reg_country_code);
        findView(R.id.ll_reg_country_select, true);
        phoneEdit = findView(R.id.cet_reg_phone);
        codeEdit = findView(R.id.cet_reg_code);
        sendCodeBtn = findView(R.id.btn_reg_send_code, true);
        Button registerBtn = findView(R.id.btn_register, true);

        sendCodeBtn.setEnabled(false);

        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && !isRequestVerifyCode) {
                    sendCodeBtn.setEnabled(true);
                } else {
                    sendCodeBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });

        codeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 6) {
//                    AMUtils.onInactive(mContext, mCodeEdit);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5) {
                    registerBtn.setEnabled(true);
                } else {
                    registerBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });
    }

    @Override
    protected void onInitViewModel() {
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);

        loginViewModel.getSendCodeState().observe(this, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                //提示
                if (resource.status == Status.SUCCESS) {
                    showToast(R.string.seal_login_toast_send_code_success);
                } else if (resource.status == Status.LOADING) {

                } else {
                    showToast(resource.message+"..");
                    sendCodeBtn.setEnabled(true);
                    //phoneEdit.setEnabled(true);
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
                    //phoneEdit.setEnabled(true);
                    sendCodeBtn.setText(R.string.seal_login_send_code);
                    isRequestVerifyCode = false;
                }
            }
        });

        loginViewModel.getRegisterResult().observe(this, new Observer<Resource<RegisterResult>>() {
            @Override
            public void onChanged(Resource<RegisterResult> resource) {
                if(resource.status == Status.SUCCESS){
                    String phone = phoneEdit.getText().toString();
                    String countryName = countryNameTv.getText().toString();
                    String countryCode = countryCodeTv.getText().toString();
                    String rongcloudid = resource.data.id;
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().register1(rongcloudid,phone, registerUserName, registerPassword, registerUserName,registerCode,""),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    if (NetworkResultUtils.isSuccess(baseData)) {

                                        if (listener != null) {

                                            listener.onRegisterSuccess(phone,  countryCode, countryName);
                                        }
//
                                    }else{
                                        dismissLoadingDialog();
                                        Toast.makeText(getActivity(), baseData.getInfo(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    dismissLoadingDialog();
                                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {
                            showToast(R.string.seal_login_register_toast_register_success);
                        }
                    });
                } else if(resource.status == Status.ERROR){
                    int code = resource.code;
                    SLog.d("ss_register", "register failed = " + code);
                    if (resource.code != ErrorCode.CHECK_VERIFY_CODE_FAILED.getCode()) {
                        sendCodeBtn.setEnabled(true);
                        //phoneEdit.setEnabled(true);
                        sendCodeBtn.setText(R.string.seal_login_send_code);
                        isRequestVerifyCode = false;
                    }
                    dismissLoadingDialog(new Runnable() {
                        @Override
                        public void run() {

                            showToast(resource.message+".");
                        }
                    });
                } else {
//                    showLoadingDialog(R.string.seal_login_register_registering);
                }
            }
        });
    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.ll_reg_country_select:
                startActivityForResult(new Intent(getActivity(), SelectCountryActivity.class), REQUEST_CODE_SELECT_COUNTRY);
                break;
            case R.id.btn_reg_send_code:
                String phoneNumber = phoneEdit.getText().toString().trim();
                String phoneCode = countryCodeTv.getText().toString().trim();
                if (TextUtils.isEmpty(phoneEdit.getText().toString().trim())) {
                    showToast(R.string.seal_login_toast_phone_number_is_null);
                    return;
                }
                if (!AccountValidatorUtil.isMobile(phoneNumber)) {
                    Toast.makeText(getActivity(), "手机格式错误", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(phoneCode)){
                    phoneCode = "86";
                }else if(phoneCode.startsWith("+")){
                    phoneCode = phoneCode.substring(1);
                }
                // 请求发送验证码时， 禁止手机号改动和获取验证码的按钮改动
                // 请求完成后在恢复原来状态
                sendCodeBtn.setEnabled(false);
                //phoneEdit.setEnabled(false);
                String finalPhoneCode = phoneCode;
                NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchSmsCode(phoneNumber),
                        new NetworkCallback<SmsCode>() {
                            @Override
                            public void onSuccess(SmsCode baseData) {
                                msgCode=baseData.getCodemsg().trim();
//                                codeEdit.setText(baseData.getCodemsg().trim());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendCode(finalPhoneCode, phoneNumber);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t) {
//                                dismissLoadingDialog();
                                Toast.makeText(getActivity(), "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                            }
                        });

                break;
            case R.id.btn_register:
                String phone = phoneEdit.getText().toString().trim();
                String phoneCodeReg = countryCodeTv.getText().toString().trim();
                registerCode = codeEdit.getText().toString().trim();
                registerUserName = userNameEdit.getText().toString().trim();
                registerPassword = passwordEdit.getText().toString().trim();

                if (TextUtils.isEmpty(registerUserName)) {
                    showToast(R.string.seal_login_toast_name_is_null);
                    userNameEdit.setShakeAnimation();
                    return;
                }
                if (registerUserName.contains(" ")) {
                    showToast(R.string.seal_login_toast_name_contain_spaces);
                    userNameEdit.setShakeAnimation();
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    showToast(R.string.seal_login_toast_phone_number_is_null);
                    phoneEdit.setShakeAnimation();
                    return;
                }
                if (!AccountValidatorUtil.isMobile(phone)) {
                    Toast.makeText(getActivity(), "手机格式错误", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(registerCode)) {
                    showToast(R.string.seal_login_toast_code_is_null);
                    codeEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(registerPassword)) {
                    showToast(R.string.seal_login_toast_password_is_null);
                    passwordEdit.setShakeAnimation();
                    return;
                }
                if (registerPassword.contains(" ")) {
                    showToast(R.string.seal_login_toast_password_cannot_contain_spaces);
                    passwordEdit.setShakeAnimation();
                    return;
                }

                if (!isRequestVerifyCode) {
//                    showToast(R.string.seal_login_toast_not_send_code);
//                    return;
                }
                if(!msgCode.equals(registerCode)){
                    showToast("验证码错误");
                    codeEdit.setShakeAnimation();
                    return;
                }

                if(TextUtils.isEmpty(phoneCodeReg)){
                    phoneCodeReg = "86";
                }else if(phoneCodeReg.startsWith("+")){
                    phoneCodeReg = phoneCodeReg.substring(1);
                }
                String finalPhoneCodeReg = phoneCodeReg;
                //注册
                register(finalPhoneCodeReg, phone, "9999", registerUserName, registerPassword);
//                NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().register1(phone, registerUserName, registerPassword, registerUserName,registerCode,""),
//                        new NetworkCallback<BaseData>() {
//                            @Override
//                            public void onSuccess(BaseData baseData) {
//                                if (NetworkResultUtils.isSuccess(baseData)) {
//
//                                    handler.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            register(finalPhoneCodeReg, phone, "9999", registerUserName, registerPassword);
//                                        }
//                                    });
//
//                                }else{
//                                    dismissLoadingDialog();
//                                    Toast.makeText(getActivity(), baseData.getInfo(), Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Throwable t) {
//                                dismissLoadingDialog();
//                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });

                break;
            default:
                break;
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 请求发送验证码
     * @param phoneCode 国家地区的手机区号
     * @param phoneNumber 手机号
     */
    private void sendCode(String phoneCode, String phoneNumber) {
        loginViewModel.sendCode(phoneCode, phoneNumber);
    }

    private void register(String phoneCode, String phoneNumber, String shortMsgCode, String  nickName, String password) {
        loginViewModel.register(phoneCode, phoneNumber, shortMsgCode, nickName, password);
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

    public void setOnOnRegisterListener(OnRegisterListener listener) {
        this.listener = listener;
    }

    public interface OnRegisterListener {
        void onRegisterSuccess(String phone, String region, String countryName);
    }
}
