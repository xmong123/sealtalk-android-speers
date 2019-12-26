package com.caesar.rongcloudspeed.extend;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.caesar.CaesarTransDetailActivity;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.id;
import com.jrmf360.rylib.R.layout;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.p;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.extend.SendUser;
import com.jrmf360.rylib.rp.ui.BaseActivity;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.ArraysDialogFragment;
import io.rong.imkit.widget.ArraysDialogFragment.OnArraysDialogItemListener;
import io.rong.imkit.widget.provider.IContainerItemProvider.MessageProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Conversation.PublicServiceType;
import io.rong.imlib.model.Message.MessageDirection;
import java.net.URLEncoder;

@ProviderTag(
        messageContent = CaesarTransferAccountsMessage.class,
        showReadState = true
)
public class CaesarTransferAccountMessageProvider extends MessageProvider<CaesarTransferAccountsMessage> {
    public CaesarTransferAccountMessageProvider() {
    }

    public void bindView(View var1, int var2, CaesarTransferAccountsMessage var3, UIMessage var4) {
        CaesarTransferAccountMessageProvider.ViewHolder var5 = (CaesarTransferAccountMessageProvider.ViewHolder)var1.getTag();
        if (var4.getMessageDirection() == MessageDirection.SEND) {
            var5.rl_trans_bg.setBackgroundResource(drawable.jrmf_trans_bg_to);
            var5.rl_trans_bg.setPadding(0, 0, 0, 0);
        } else {
            var5.rl_trans_bg.setBackgroundResource(drawable.jrmf_trans_bg_from);
            var5.rl_trans_bg.setPadding(p.a(RongContext.getInstance(), 5.0F), 0, 0, 0);
        }

        String var6 = var3.getTransferDesp();
        if ("0".equals(var3.getTransferStatus())) {
            var5.iv_trans_icon.setImageDrawable(RongContext.getInstance().getResources().getDrawable(drawable.jrmf_ic_trans));
            if (q.a(var6)) {
                if (var4.getMessageDirection() == MessageDirection.SEND) {
                    var5.tv_trans_tip.setText(String.format(RongContext.getInstance().getString(string.jrmf_transfer_to), CurrentUser.getNameById(var3.getTransferReceiveUserId())));
                } else {
                    var5.tv_trans_tip.setText(RongContext.getInstance().getString(string.jrmf_transfer_to_you));
                }
            } else {
                var5.tv_trans_tip.setText(var6);
            }
        } else if ("1".equals(var3.getTransferStatus())) {
            var5.iv_trans_icon.setImageDrawable(RongContext.getInstance().getResources().getDrawable(drawable.jrmf_ic_receipt));
            var5.tv_trans_tip.setText(RongContext.getInstance().getString(string.jrmf_had_receive_money));
        } else if ("2".equals(var3.getTransferStatus())) {
            var5.iv_trans_icon.setImageDrawable(RongContext.getInstance().getResources().getDrawable(drawable.jrmf_ic_reback));
            var5.tv_trans_tip.setText(RongContext.getInstance().getString(string.jrmf_had_back_money));
        }

        var5.tv_trans_money.setText("￥" + q.h(var3.getTransferAmount()));
        var5.tv_trans_name.setText(RongContext.getInstance().getString(R.string.seal_main_mine_translate));
    }

    public Spannable getContentSummary(CaesarTransferAccountsMessage var1) {
        if (var1 == null) {
            return null;
        } else {
            String var2 = var1.getTransferDesp();
            if ("0".equals(var1.getTransferStatus())) {
                if (CurrentUser.getUserId().equals(var1.getTransferSourceUserId())) {
                    var2 = RongContext.getInstance().getString(string.jrmf_transfer_account_sender);
                } else {
                    var2 = RongContext.getInstance().getString(string.jrmf_transfer_account_receiver);
                }
            } else if ("1".equals(var1.getTransferStatus())) {
                if (CurrentUser.getUserId().equals(var1.getTransferSourceUserId())) {
                    var2 = RongContext.getInstance().getString(string.jrmf_transfer_account_receive_sender);
                } else {
                    var2 = RongContext.getInstance().getString(string.jrmf_transfer_account_receive_receiver);
                }
            } else if ("2".equals(var1.getTransferStatus())) {
                if (CurrentUser.getUserId().equals(var1.getTransferSourceUserId())) {
                    var2 = RongContext.getInstance().getString(string.jrmf_transfer_account_refund_sender);
                } else {
                    var2 = RongContext.getInstance().getString(string.jrmf_transfer_account_refund_receiver);
                }
            } else {
                var2 = RongContext.getInstance().getString(string._transfer_account);
            }

            return new SpannableString(var2);
        }
    }

    public void onItemClick(final View var1, int var2, final CaesarTransferAccountsMessage var3, UIMessage var4) {
        SendUser.sendUserId = var4.getSenderUserId();
        SendUser.conversationType = var4.getConversationType();
        SendUser.targetId = var4.getTargetId();
        if (q.a(BaseActivity.rongCloudToken)) {
            if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                RongIMClient.getInstance().getVendorToken(new ResultCallback<String>() {
                    public void onSuccess(String var1x) {
                        try {
                            if (q.c(var1x)) {
                                BaseActivity.rongCloudToken = URLEncoder.encode(var1x, "UTF-8");
                                CaesarTransDetailActivity.intent(var1.getContext(), var3.getTransferOrder(), var3.getTransferReceiveUserId(), var3.getTransferSourceUserId());
                            }
                        } catch (Exception var3x) {
                            BaseActivity.rongCloudToken = "";
                            var3x.printStackTrace();
                        }

                    }

                    public void onError(ErrorCode var1x) {
                        LogUtil.i("获得token失败" + var1x);
                        BaseActivity.rongCloudToken = "";
                    }
                });
            } else {
                ToastUtil.showToast(RongContext.getInstance(), RongContext.getInstance().getString(string.net_error_l));
            }
        } else {
            CaesarTransDetailActivity.intent(var1.getContext(), var3.getTransferOrder(), var3.getTransferReceiveUserId(), var3.getTransferSourceUserId());
        }

    }

    public void onItemLongClick(View var1, int var2, CaesarTransferAccountsMessage var3, final UIMessage var4) {
        String var5 = null;
        if (!var4.getConversationType().getName().equals(ConversationType.APP_PUBLIC_SERVICE.getName()) && !var4.getConversationType().getName().equals(ConversationType.PUBLIC_SERVICE.getName())) {
            UserInfo var8 = RongUserInfoManager.getInstance().getUserInfo(var4.getSenderUserId());
            if (var8 != null) {
                var5 = var8.getName();
            }
        } else {
            PublicServiceType var6 = PublicServiceType.setValue(var4.getConversationType().getValue());
            PublicServiceProfile var7 = RongUserInfoManager.getInstance().getPublicServiceProfile(var6, var4.getTargetId());
            if (var7 != null) {
                var5 = var7.getName();
            }
        }

        String[] var9 = new String[]{var1.getContext().getResources().getString(string._de_dialog_item_message_delete)};
        ArraysDialogFragment.newInstance(var5, var9).setArraysDialogItemListener(new OnArraysDialogItemListener() {
            public void OnArraysDialogItemClick(DialogInterface var1, int var2) {
                if (var2 == 0) {
                    RongIM.getInstance().deleteMessages(new int[]{var4.getMessageId()}, (ResultCallback)null);
                }

            }
        }).show(((FragmentActivity)var1.getContext()).getSupportFragmentManager());
    }

    public View newView(Context var1, ViewGroup var2) {
        View var3 = LayoutInflater.from(var1).inflate(layout.jrmf_rp_trans_account_item, (ViewGroup)null);
        CaesarTransferAccountMessageProvider.ViewHolder var4 = new CaesarTransferAccountMessageProvider.ViewHolder();
        var4.rl_trans_bg = (RelativeLayout)var3.findViewById(id.rl_trans_bg);
        var4.iv_trans_icon = (ImageView)var3.findViewById(id.iv_trans_icon);
        var4.tv_trans_tip = (TextView)var3.findViewById(id.tv_trans_tip);
        var4.tv_trans_money = (TextView)var3.findViewById(id.tv_trans_money);
        var4.tv_trans_name = (TextView)var3.findViewById(id.tv_trans_name);
        var3.setTag(var4);
        return var3;
    }

    class ViewHolder {
        RelativeLayout rl_trans_bg;
        ImageView iv_trans_icon;
        TextView tv_trans_tip;
        TextView tv_trans_money;
        TextView tv_trans_name;

        ViewHolder() {
        }
    }
}


