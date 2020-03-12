package com.caesar.rongcloudspeed.viewmodel;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.caesar.rongcloudspeed.common.ErrorCode;
import com.caesar.rongcloudspeed.model.RegisterResult;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.model.UserCacheInfo;
import com.caesar.rongcloudspeed.task.UserTask;
import com.caesar.rongcloudspeed.utils.SingleSourceLiveData;
import com.caesar.rongcloudspeed.utils.SingleSourceMapLiveData;

public class LoginViewModel extends AndroidViewModel {
    private SingleSourceLiveData<Resource<String>> loginResult = new SingleSourceLiveData<>();
    //TODO 示例代码，当需要转换结果类型时参考
    private SingleSourceMapLiveData<Resource<String>,String> loginResultNoResource;

    private MediatorLiveData<Resource> loadingState = new MediatorLiveData<>();

//    private SingleSourceLiveData<Resource<String>> sendCodeResult = new SingleSourceLiveData<>();

    private SingleSourceMapLiveData<Resource<String>, Resource<String>> sendCodeState;

    private SingleSourceMapLiveData<Resource<String>, Resource<String>> sendRegisterCodeState;

    private MutableLiveData<Integer> codeCountDown = new MutableLiveData<>();

    private MutableLiveData<Integer> codeRegisterCountDown = new MutableLiveData<>();

    private SingleSourceMapLiveData<Resource<RegisterResult>, Resource<RegisterResult>> registerResult ;

    private SingleSourceMapLiveData<Resource<RegisterResult>, Resource<RegisterResult>> registerLoginResult ;

    private SingleSourceLiveData<Resource<Boolean>>  checkPhoneResult= new SingleSourceLiveData<>();

    private SingleSourceMapLiveData<Resource<String>, Resource<String>> resetPasswordResult ;

    private SingleSourceMapLiveData<Resource<String>, Resource<String>> resetLoginPasswordResult ;

    private MediatorLiveData<Resource<String>> checkPhoneAndSendCodeResult = new MediatorLiveData<>();

    private MutableLiveData<UserCacheInfo> lastLoginUserCache = new MutableLiveData<>();



    private UserTask userTask;
    private CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            int count = Math.round(millisUntilFinished/ 1000);
            codeCountDown.postValue(count);
            codeRegisterCountDown.postValue(count);
        }

        @Override
        public void onFinish() {
            codeCountDown.postValue(0);
            codeRegisterCountDown.postValue(0);
        }
    };

    public LoginViewModel(@NonNull Application application) {
        super(application);

        userTask = new UserTask(application);
        loadingState.addSource(loginResult, resource -> loadingState.setValue(resource));
        loginResultNoResource = new SingleSourceMapLiveData<>(input -> input.data);

        // 返送验证码请求的结果
        sendCodeState = new SingleSourceMapLiveData<>(new Function<Resource<String>, Resource<String>>() {
            @Override
            public Resource<String> apply(Resource<String> input) {
                if (input.status == Status.SUCCESS) {
                    // 开始计时
                    startCodeCountDown();
                }
                return input;
            }
        });

        // 返送验证码请求的结果
        sendRegisterCodeState = new SingleSourceMapLiveData<>(new Function<Resource<String>, Resource<String>>() {
            @Override
            public Resource<String> apply(Resource<String> input) {
                if (input.status == Status.SUCCESS) {
                    // 开始计时
                    startCodeCountDown();
                }
                return input;
            }
        });

        // 注册结果
        registerResult = new SingleSourceMapLiveData<>(new Function<Resource<RegisterResult>, Resource<RegisterResult>>() {
            @Override
            public Resource<RegisterResult> apply(Resource<RegisterResult> input) {
                if (input.status != Status.LOADING && input.code != ErrorCode.CHECK_VERIFY_CODE_FAILED.getCode()) {
                    // 停止计时. 除了验证码错误
                    stopCodeCountDown();
                }
                return input;
            }
        });

        // 注册结果
        registerLoginResult = new SingleSourceMapLiveData<>(new Function<Resource<RegisterResult>, Resource<RegisterResult>>() {
            @Override
            public Resource<RegisterResult> apply(Resource<RegisterResult> input) {
                if (input.status != Status.LOADING && input.code != ErrorCode.CHECK_VERIFY_CODE_FAILED.getCode()) {
                    // 停止计时. 除了验证码错误
                    stopCodeCountDown();
                }
                return input;
            }
        });

        // 重设密码
        resetPasswordResult = new SingleSourceMapLiveData<>(new Function<Resource<String>, Resource<String>>() {
            @Override
            public Resource<String> apply(Resource<String> input) {
                if (input.status != Status.LOADING && input.code != ErrorCode.CHECK_VERIFY_CODE_FAILED.getCode()) {
                    // 停止计时. 除了验证码错误
                    stopCodeCountDown();
                }
                return input;
            }
        });

        // 重设密码
        resetLoginPasswordResult = new SingleSourceMapLiveData<>(new Function<Resource<String>, Resource<String>>() {
            @Override
            public Resource<String> apply(Resource<String> input) {
                if (input.status != Status.LOADING && input.code != ErrorCode.CHECK_VERIFY_CODE_FAILED.getCode()) {
                    // 停止计时. 除了验证码错误
                    stopCodeCountDown();
                }
                return input;
            }
        });

        UserCacheInfo userCache = userTask.getUserCache();
        if (userCache != null) {
            lastLoginUserCache.setValue(userCache);
        }

    }

    public void login(String region, String phone, String pwd){
        loginResult.setSource(userTask.login(region, phone, pwd));
        //TODO 示例代码，当需要转换类型时参考
        //loginResultNoResource.setSource(userTask.login(region, phone, pwd));
    }

    public LiveData<Resource<String>> getLoginResult(){
        return loginResult;
    }

    public LiveData<String> getLoginResultNoResource(){
        return loginResultNoResource;
    }

    public LiveData<Resource> getLoadingState(){
        return loadingState;
    }


//    /**
//     *验证请求发送结果
//     * @return
//     */
//    public LiveData<Resource<String>> getSendCodeResult(){
//        return sendCodeResult;
//    }

    /**
     * 验证请求发送结果
     * @return
     */
    public LiveData<Resource<String>> getSendCodeState(){
        return sendCodeState;
    }

    /**
     * 验证码接受倒计时
     * @return
     */
    public LiveData<Integer> getCodeCountDown() {
        return codeCountDown;
    }

    /**
     * 验证码接受倒计时
     * @return
     */
    public LiveData<Integer> getCodeRegisterCountDown() {
        return codeRegisterCountDown;
    }

    /**
     * 检测手机号并发送验证码
     * @return
     */
    public LiveData<Resource<String>> getCheckPhoneAndSendCode() {
        return checkPhoneAndSendCodeResult;
    }

    /**
     * 最后一次的用户信息
     * @return
     */
    public LiveData<UserCacheInfo> getLastLoginUserCache() {
        return lastLoginUserCache;
    }

    /**
     * 获取注册结果
     *
     * @return
     */
    public LiveData<Resource<RegisterResult>> getRegisterResult() {
        return registerResult;
    }

    /**
     * 获取注册结果
     *
     * @return
     */
    public LiveData<Resource<RegisterResult>> getRegisterLoginResult() {
        return registerLoginResult;
    }


    /**
     * 重置密码
     * @return
     */
    public LiveData<Resource<String>> getResetPasswordResult() {
        return resetPasswordResult;
    }

    /**
     * 重置密码
     * @return
     */
    public LiveData<Resource<String>> getResetLoginPasswordResult() {
        return resetLoginPasswordResult;
    }

    // 操作方法

    /**
     * 发送验证码
     * @param phoneCode 国家区域手机区号
     * @param phoneNumber 手机号
     */
    public void sendCode(String phoneCode, String phoneNumber) {
        sendCodeState.setSource(userTask.sendCode(phoneCode, phoneNumber));
    }

    /**
     * 注册操作
     * @param phoneCode 国家区域手机区号
     * @param phoneNumber 手机号
     * @param shortMsgCode 短信验证码
     * @param nickName 昵称
     * @param password 密码
     */
    public void register(String phoneCode, String phoneNumber, String shortMsgCode, String nickName, String password) {
        registerResult.setSource(userTask.register(phoneCode, phoneNumber, shortMsgCode, nickName, password));
    }

    /**
     * 注册操作
     * @param phoneCode 国家区域手机区号
     * @param phoneNumber 手机号
     * @param shortMsgCode 短信验证码
     * @param nickName 昵称
     * @param password 密码
     */
    public void registerLogin(String phoneCode, String phoneNumber, String shortMsgCode, String nickName, String password) {
        registerLoginResult.setSource(userTask.register(phoneCode, phoneNumber, shortMsgCode, nickName, password));
    }

    /**
     * 检测手机是否注册
     * @param phoneCode 国家区域手机区号
     * @param phoneNumber 手机号
     */
    public void checkPhoneAvailable(String phoneCode, String phoneNumber) {
        checkPhoneResult.setSource(userTask.checkPhoneAvailable(phoneCode, phoneNumber));
    }


    /**
     * 重设置密码
     * @param countryCode
     * @param phoneNumber
     * @param shortMsgCode
     * @param password
     */
    public void resetPassword(String countryCode, String phoneNumber, String shortMsgCode, String password) {
        resetPasswordResult.setSource(userTask.resetPassword(countryCode, phoneNumber, shortMsgCode, password));
    }

    /**
     * 重设置密码
     * @param countryCode
     * @param phoneNumber
     * @param shortMsgCode
     * @param password
     */
    public void resetLoginPassword(String countryCode, String phoneNumber, String shortMsgCode, String password) {
        resetLoginPasswordResult.setSource(userTask.resetPassword(countryCode, phoneNumber, shortMsgCode, password));
    }

    /**
     * 检验密码并发送验证码
     * @param phoneCode
     * @param phoneNumber
     */
    public void checkPhoneAndSendCode(String phoneCode, String phoneNumber) {
        checkPhoneAvailable(phoneCode, phoneNumber);
        checkPhoneAndSendCodeResult.addSource(checkPhoneResult, new Observer<Resource<Boolean>>() {
            @Override
            public void onChanged(Resource<Boolean> resource) {
                if (resource.status == Status.SUCCESS) {
                    checkPhoneAndSendCodeResult.removeSource(checkPhoneResult);
                    sendCode(phoneCode, phoneNumber);
                    checkPhoneAndSendCodeResult.addSource(sendCodeState, new Observer<Resource<String>>() {
                        @Override
                        public void onChanged(Resource<String> resource) {
                            if (resource.status == Status.SUCCESS) {
                                checkPhoneAndSendCodeResult.removeSource(checkPhoneAndSendCodeResult);
                                checkPhoneAndSendCodeResult.postValue(resource);
                            } else if (resource.status == Status.ERROR) {
                                checkPhoneAndSendCodeResult.postValue(resource);
                            }
                        }
                    });
                } else if (resource.status == Status.ERROR) {
                    Resource<String> resourceTmp = new Resource<>(resource.status, null, resource.code);
                    checkPhoneAndSendCodeResult.postValue(resourceTmp);
                }
            }
        });
    }

    private void startCodeCountDown() {
        countDownTimer.cancel();
        countDownTimer.start();
    }

    private void stopCodeCountDown() {
        countDownTimer.cancel();
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }


}
