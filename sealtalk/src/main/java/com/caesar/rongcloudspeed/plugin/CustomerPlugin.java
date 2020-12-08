package com.caesar.rongcloudspeed.plugin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.extend.CaesarTransferAccountsMessage;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.dialog.PayPassDialog;
import com.caesar.rongcloudspeed.ui.dialog.PayPassView;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

import io.rong.contactcard.message.ContactMessage;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.IPluginRequestPermissionResultCallback;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.recognizer.IRecognizedResult;
import io.rong.recognizer.Recognizer;

public class CustomerPlugin implements IPluginModule {

    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.border_white);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.profile_start_send_money);
    }

    @Override
    public void onClick(Fragment currentFragment, final RongExtension extension) {
        UserInfo targetUserInfo = RongUserInfoManager.getInstance().
                getUserInfo(extension.getTargetId());
        String targetUserName = targetUserInfo == null ? "" : targetUserInfo.getName();
        final EditText et = new EditText(currentFragment.getActivity());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(currentFragment.getActivity()).setTitle("请设置付款金额")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String user_sum_add=et.getText().toString();
                        String sum_foruserID= UserInfoUtils.getAppUserId(currentFragment.getActivity());
//                        ToastUtils.showToast("您当前正在与"+sendUserName+"进行交易，交易金额为："+user_sum_add+"元");
                        PayPassDialog payPassDialog=new PayPassDialog(currentFragment.getActivity());
                        payPassDialog.getPayViewPass().setPayClickListener(new PayPassView.OnPayClickListener() {
                            @Override
                            public void onPassFinish(String passContent) {
                                String payPassWord= UserInfoUtils.getPayPassWord(currentFragment.getActivity());
                                if(payPassWord.equals(passContent)){
                                    payPassDialog.dismiss();
                                    //按下确定键后的事件
                                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getCodeToUserName(user_sum_add,sum_foruserID,targetUserName),
                                            new NetworkCallback<UserSumResult>() {
                                                @Override
                                                public void onSuccess(UserSumResult userSumResult) {
                                                    UserSumUrl userSumUrl=userSumResult.getUrl();
                                                    String sumString=String.valueOf(userSumUrl.getUser_sum());
                                                    UserInfoUtils.setUserSum(sumString,currentFragment.getActivity());
                                                    Toast.makeText(currentFragment.getActivity(), "交易成功，付款金额为:"+user_sum_add+"元",Toast.LENGTH_LONG).show();
                                                    UserInfo sendUserInfo = RongUserInfoManager.getInstance().
                                                            getUserInfo(RongIMClient.getInstance().getCurrentUserId());
                                                    String sendUserName = sendUserInfo == null ? "" : sendUserInfo.getName();
                                                    String friendPortrait = targetUserInfo.getPortraitUri() == null ? "" : targetUserInfo.getPortraitUri().toString();
                                                    TextMessage mTextMessage = TextMessage.obtain("交易成功，"+sendUserName+"支付了:"+user_sum_add+"元");
                                                    String pushContent = "交易成功，"+sendUserName+"支付了:"+user_sum_add+"元";

                                                    String var2 = userSumUrl.getOrder();
                                                    String var3 = user_sum_add;
                                                    String var4 = sum_foruserID;
                                                    String var5 = extension.getTargetId();
                                                    String var6 = "转账给"+targetUserName;
                                                    String var7 = "0";
                                                    CaesarTransferAccountsMessage var8 = CaesarTransferAccountsMessage.obtain(var2, var3, var4, var5, var6, var7);
//                                                    if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
//                                                        RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, var5, var8, "您收到了一条消息", (String)null, new RongIMClient.SendMessageCallback() {
//                                                            public void onError(Integer var1, RongIMClient.ErrorCode var2) {
//                                                                LogUtil.e("发送转账消息失败:" + var2.toString());
//                                                            }
//
//                                                            public void onSuccess(Integer var1) {
//                                                            }
//                                                        });
//                                                    }
                                                    RongIM.getInstance().sendMessage(Message.obtain(extension.getTargetId(), Conversation.ConversationType.PRIVATE, var8),
                                                            pushContent, null, new IRongCallback.ISendMessageCallback() {
                                                                @Override
                                                                public void onAttached(Message message) {

                                                                }

                                                                @Override
                                                                public void onSuccess(Message message) {

                                                                }

                                                                @Override
                                                                public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                                                                }
                                                            });


                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    Toast.makeText(currentFragment.getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }else{
                                    payPassDialog.dismiss();
                                    Toast.makeText(currentFragment.getActivity(), "支付密码错误", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onPayClose() {
                                payPassDialog.dismiss();
                            }

                            @Override
                            public void onPayForget() {
                                payPassDialog.dismiss();
                            }
                        });

                    }
                }).setNegativeButton("取消",null).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
