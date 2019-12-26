package com.caesar.rongcloudspeed.extend;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.extension.CaesarRedPacketClient;
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
        messageContent = CaesarRedPacketMessage.class,
        showReadState = false
)
public class CaesarRedPacketMessageProvider extends MessageProvider<CaesarRedPacketMessage> {
    public CaesarRedPacketMessageProvider() {
    }

    public void bindView(View var1, int var2, CaesarRedPacketMessage var3, UIMessage var4) {
        CaesarRedPacketMessageProvider.ViewHolder var5 = (CaesarRedPacketMessageProvider.ViewHolder)var1.getTag();
        if (var4.getMessageDirection() == MessageDirection.SEND) {
            var5.bri_bg.setBackgroundResource(drawable._bg_from_hongbao2);
            var5.tv_bri_target.setPadding(p.a(RongContext.getInstance(), 60.0F), 0, 0, 0);
            var5.tv_bri_target.setText(RongContext.getInstance().getString(string.jrmf_rp_look_rp));
            var5.tv_bri_name.setPadding(28, 0, 0, 0);
            var5.tv_bri_mess.setPadding(p.a(RongContext.getInstance(), 60.0F), 0, p.a(RongContext.getInstance(), 10.0F), 0);
        } else {
            var5.bri_bg.setBackgroundResource(drawable._bg_to_hongbao2);
            var5.tv_bri_target.setPadding(p.a(RongContext.getInstance(), 65.0F), 0, 0, 0);
            var5.tv_bri_target.setText(RongContext.getInstance().getString(string.jrmf_rp_receive_rp));
            var5.tv_bri_name.setPadding(48, 0, 0, 0);
            var5.tv_bri_mess.setPadding(p.a(RongContext.getInstance(), 65.0F), 0, p.a(RongContext.getInstance(), 5.0F), 0);
        }

        var5.tv_bri_mess.setText(var3.getBribery_Message());
        var5.tv_bri_name.setText(R.string.profile_start_send_redpacket);
    }

    public Spannable getContentSummary(CaesarRedPacketMessage var1) {
        return var1 != null && !q.a(var1.getContent().trim()) ? new SpannableString(var1.getContent()) : null;
    }

    public void onItemClick(final View var1, int var2, final CaesarRedPacketMessage var3, final UIMessage var4) {
        SendUser.sendUserId = var4.getSenderUserId();
        SendUser.conversationType = var4.getConversationType();
        SendUser.targetId = var4.getTargetId();
        if (var4.getConversationType().getValue() == ConversationType.PRIVATE.getValue()) {
            CaesarRedPacketClient.openSingleRp((Activity)var1.getContext(), CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon(), var3.getBribery_ID());
        } else {
            CaesarRedPacketClient.openGroupRp((Activity)var1.getContext(), CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon(), var3.getBribery_ID());
        }
//        if (q.a(BaseActivity.rongCloudToken)) {
//            if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
//                RongIMClient.getInstance().getVendorToken(new ResultCallback<String>() {
//                    public void onSuccess(String var1x) {
//                        try {
//                            if (q.c(var1x)) {
//                                BaseActivity.rongCloudToken = URLEncoder.encode(var1x, "UTF-8");
//                                if (var4.getConversationType().getValue() == ConversationType.PRIVATE.getValue()) {
//                                    CaesarRedPacketClient.openSingleRp((Activity)var1.getContext(), CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon(), var3.getBribery_ID());
//                                } else if (var4.getConversationType().equals(ConversationType.CHATROOM)) {
//                                    CaesarRedPacketClient.openGroupRp((Activity)var1.getContext(), CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon(), var3.getBribery_ID());
//                                } else {
//                                    CaesarRedPacketClient.openGroupRp((Activity)var1.getContext(), CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon(), var3.getBribery_ID());
//                                }
//                            }
//                        } catch (Exception var3x) {
//                            BaseActivity.rongCloudToken = "";
//                            var3x.printStackTrace();
//                        }
//
//                    }
//
//                    public void onError(ErrorCode var1x) {
//                        LogUtil.i("获得token失败" + var1x);
//                        BaseActivity.rongCloudToken = "";
//                    }
//                });
//            } else {
//                ToastUtil.showToast(RongContext.getInstance(), RongContext.getInstance().getString(string.net_error_l));
//            }
//        } else if (var4.getConversationType().getValue() == ConversationType.PRIVATE.getValue()) {
//            CaesarRedPacketClient.openSingleRp((Activity)var1.getContext(), CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon(), var3.getBribery_ID());
//        } else if (var4.getConversationType().equals(ConversationType.CHATROOM)) {
//            CaesarRedPacketClient.openGroupRp((Activity)var1.getContext(), CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon(), var3.getBribery_ID());
//        } else {
//            CaesarRedPacketClient.openGroupRp((Activity)var1.getContext(), CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon(), var3.getBribery_ID());
//        }

    }

    public void onItemLongClick(View var1, int var2, CaesarRedPacketMessage var3, final UIMessage var4) {
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
                    RongIM.getInstance().getRongIMClient().deleteMessages(new int[]{var4.getMessageId()}, (ResultCallback)null);
                }

            }
        }).show(((FragmentActivity)var1.getContext()).getSupportFragmentManager());
    }

    public View newView(Context var1, ViewGroup var2) {
        View var3 = LayoutInflater.from(var1).inflate(layout._bribery_item, (ViewGroup)null);
        CaesarRedPacketMessageProvider.ViewHolder var4 = new CaesarRedPacketMessageProvider.ViewHolder();
        var4.layout = (RelativeLayout)var3.findViewById(id.layout);
        var4.tv_bri_mess = (TextView)var3.findViewById(id.tv_bri_mess);
        var4.tv_bri_target = (TextView)var3.findViewById(id.tv_bri_target);
        var4.tv_bri_name = (TextView)var3.findViewById(id.tv_bri_name);
        var4.bri_bg = (RelativeLayout)var3.findViewById(id.bri_bg);
        var3.setTag(var4);
        return var3;
    }

    class ViewHolder {
        RelativeLayout layout;
        RelativeLayout bri_bg;
        TextView tv_bri_mess;
        TextView tv_bri_target;
        TextView tv_bri_name;

        ViewHolder() {
        }
    }
}

