package com.caesar.rongcloudspeed.extend;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jrmf360.rylib.R.color;
import com.jrmf360.rylib.R.id;
import com.jrmf360.rylib.R.layout;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.common.b.b;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.j;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.extend.SendUser;
import com.jrmf360.rylib.rp.ui.BaseActivity;
import com.jrmf360.rylib.rp.ui.RpDetailActivity;
import io.rong.imkit.RongContext;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.provider.IContainerItemProvider.MessageProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.Conversation.ConversationType;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;

@ProviderTag(
        messageContent = CaesarRedPacketOpenedMessage.class,
        showPortrait = false,
        centerInHorizontal = true,
        showSummaryWithName = false
)
public class CaesarRedPacketOpenMessageProvider extends MessageProvider<CaesarRedPacketOpenedMessage> {
    public CaesarRedPacketOpenMessageProvider() {
    }

    public void bindView(View var1, int var2, CaesarRedPacketOpenedMessage var3, UIMessage var4) {
        CaesarRedPacketOpenMessageProvider.ViewHolder var5 = (CaesarRedPacketOpenMessageProvider.ViewHolder)var1.getTag();
        ConversationType var6 = var4.getConversationType();
        String var7 = var4.getTargetId();
        if (var3 != null) {
            String var8 = CurrentUser.getUserId();
            String var9 = var3.getOpenPacketId();
            String var10 = var3.getSendPacketId();
            String var11 = var3.getPacketId();
            if (TextUtils.isEmpty(var8) || TextUtils.isEmpty(var9) || TextUtils.isEmpty(var10)) {
                LogUtil.i("CaesarRedPacketOpenedMessage:", "id不能为空!!!");
                return;
            }

            String var12;
            String var13;
            GroupUserInfo var14;
            if (var8.equals(var9)) {
                if (var8.equals(var10)) {
                    if ("1".equals(var3.getIsGetDone())) {
                        var12 = RongContext.getInstance().getString(string.jrmf_get_self_rp_and_done);
                    } else {
                        var12 = RongContext.getInstance().getString(string.jrmf_get_self_rp);
                    }
                } else {
                    var13 = "";
                    if (var6.equals(ConversationType.GROUP)) {
                        var14 = RongUserInfoManager.getInstance().getGroupUserInfo(var7, var10);
                        if (var14 != null && !TextUtils.isEmpty(var14.getNickname())) {
                            var13 = var14.getNickname();
                        }
                    }

                    if (q.a(var13)) {
                        var13 = var3.getSendNickName();
                    }

                    if (var13 == null || q.a(var13)) {
                        var13 = CurrentUser.getNameById(var10);
                    }

                    var12 = String.format(RongContext.getInstance().getString(string.jrmf_get_other_rp), var13);
                }

                SpannableString var17 = new SpannableString(var12);
                int var16;
                if (j.b()) {
                    var16 = var12.indexOf("Red Packet");
                    var17.setSpan(new CaesarRedPacketOpenMessageProvider.ClickPacketSpan(var11, var10, var1.getContext()), var16, var16 + 10, 33);
                    var17.setSpan(new ForegroundColorSpan(Color.parseColor("#fa9d3b")), var16, var16 + 10, 33);
                } else {
                    var16 = var12.indexOf("红包");
                    var17.setSpan(new CaesarRedPacketOpenMessageProvider.ClickPacketSpan(var11, var10, var1.getContext()), var16, var16 + 2, 33);
                    var17.setSpan(new ForegroundColorSpan(Color.parseColor("#fa9d3b")), var16, var16 + 2, 33);
                }

                var5.packet_message.setText(var17);
                var5.packet_message.setMovementMethod(LinkMovementMethod.getInstance());
                var5.packet_message.setHighlightColor(0);
            } else if (var8.equals(var10)) {
                var13 = "";
                if (var6.equals(ConversationType.GROUP)) {
                    var14 = RongUserInfoManager.getInstance().getGroupUserInfo(var7, var9);
                    if (var14 != null && !TextUtils.isEmpty(var14.getNickname())) {
                        var13 = var14.getNickname();
                    }
                }

                if (q.a(var13)) {
                    var13 = var3.getOpenNickName();
                }

                if (var13 == null || q.a(var13)) {
                    var13 = CurrentUser.getNameById(var9);
                }

                if ("1".equals(var3.getIsGetDone())) {
                    var12 = String.format(RongContext.getInstance().getString(string.jrmf_other_get_self_rp_done), var13);
                } else {
                    var12 = String.format(RongContext.getInstance().getString(string.jrmf_other_get_self_rp), var13);
                }

                SpannableString var18 = new SpannableString(var12);
                int var15;
                if (j.b()) {
                    var15 = var12.indexOf("Red Packet");
                    var18.setSpan(new CaesarRedPacketOpenMessageProvider.ClickPacketSpan(var11, var10, var1.getContext()), var15, var15 + 10, 33);
                    var18.setSpan(new ForegroundColorSpan(Color.parseColor("#fa9d3b")), var15, var15 + 10, 33);
                } else {
                    var15 = var12.indexOf("红包");
                    var18.setSpan(new CaesarRedPacketOpenMessageProvider.ClickPacketSpan(var11, var10, var1.getContext()), var15, var15 + 2, 33);
                    var18.setSpan(new ForegroundColorSpan(Color.parseColor("#fa9d3b")), var15, var15 + 2, 33);
                }

                var5.packet_message.setText(var18);
                var5.packet_message.setMovementMethod(LinkMovementMethod.getInstance());
                var5.packet_message.setHighlightColor(0);
            }
        }

    }

    public Spannable getSummary(UIMessage var1) {
        CaesarRedPacketOpenedMessage var2 = (CaesarRedPacketOpenedMessage)var1.getContent();
        ConversationType var3 = var1.getConversationType();
        String var4 = var1.getTargetId();
        if (var2 != null) {
            String var5 = CurrentUser.getUserId();
            String var6 = var2.getOpenPacketId();
            String var7 = var2.getSendPacketId();
            if (TextUtils.isEmpty(var5) || TextUtils.isEmpty(var6) || TextUtils.isEmpty(var7)) {
                LogUtil.i("CaesarRedPacketOpenedMessage:", "id不能为空!!!");
                return null;
            }

            String var8;
            String var9;
            GroupUserInfo var10;
            if (var5.equals(var6)) {
                if (var5.equals(var7)) {
                    if ("1".equals(var2.getIsGetDone())) {
                        var8 = RongContext.getInstance().getString(string.jrmf_get_self_rp_and_done);
                    } else {
                        var8 = RongContext.getInstance().getString(string.jrmf_get_self_rp);
                    }
                } else {
                    var9 = "";
                    if (var3.equals(ConversationType.GROUP)) {
                        var10 = RongUserInfoManager.getInstance().getGroupUserInfo(var4, var7);
                        if (var10 != null && !TextUtils.isEmpty(var10.getNickname())) {
                            var9 = var10.getNickname();
                        }
                    }

                    if (q.a(var9)) {
                        var9 = var2.getSendNickName();
                    }

                    if (var9 == null || q.a(var9)) {
                        var9 = CurrentUser.getNameById(var7);
                    }

                    var8 = String.format(RongContext.getInstance().getString(string.jrmf_get_other_rp), var9);
                }

                return new SpannableString(var8);
            }

            if (var5.equals(var7)) {
                var9 = "";
                if (var3.equals(ConversationType.GROUP)) {
                    var10 = RongUserInfoManager.getInstance().getGroupUserInfo(var4, var6);
                    if (var10 != null && !TextUtils.isEmpty(var10.getNickname())) {
                        var9 = var10.getNickname();
                    }
                }

                if (q.a(var9)) {
                    var9 = var2.getOpenNickName();
                }

                if (var9 == null || q.a(var9)) {
                    var9 = CurrentUser.getNameById(var6);
                }

                if ("1".equals(var2.getIsGetDone())) {
                    var8 = String.format(RongContext.getInstance().getString(string.jrmf_other_get_self_rp_done), var9);
                } else {
                    var8 = String.format(RongContext.getInstance().getString(string.jrmf_other_get_self_rp), var9);
                }

                return new SpannableString(var8);
            }
        }

        return null;
    }

    public Spannable getContentSummary(CaesarRedPacketOpenedMessage var1) {
        return null;
    }

    public void onItemClick(View var1, int var2, CaesarRedPacketOpenedMessage var3, UIMessage var4) {
    }

    public void onItemLongClick(View var1, int var2, CaesarRedPacketOpenedMessage var3, UIMessage var4) {
    }

    public View newView(Context var1, ViewGroup var2) {
        View var3 = LayoutInflater.from(var1).inflate(layout._open_packet, (ViewGroup)null);
        CaesarRedPacketOpenMessageProvider.ViewHolder var4 = new CaesarRedPacketOpenMessageProvider.ViewHolder();
        var4.packet_message = (TextView)var3.findViewById(id.packet_message);
        var3.setTag(var4);
        return var3;
    }

    static class ClickPacketSpan extends ClickableSpan {
        private WeakReference<Context> reference;
        String packetId;
        String sendPacketId;

        public ClickPacketSpan(String var1, String var2, Context var3) {
            this.reference = new WeakReference(var3);
            this.packetId = var1;
            this.sendPacketId = var2;
        }

        public void updateDrawState(TextPaint var1) {
            super.updateDrawState(var1);
            var1.setColor(RongContext.getInstance().getResources().getColor(color.red_trans));
            var1.setUnderlineText(false);
        }

        public void onClick(View var1) {
            if (q.a(BaseActivity.rongCloudToken)) {
                if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                    RongIMClient.getInstance().getVendorToken(new ResultCallback<String>() {
                        public void onSuccess(String var1) {
                            LogUtil.e(Thread.currentThread().getName());

                            try {
                                if (q.c(var1)) {
                                    BaseActivity.rongCloudToken = URLEncoder.encode(var1, "UTF-8");
                                    if (ClickPacketSpan.this.reference.get() != null) {
                                        b.a(new Runnable() {
                                            public void run() {
                                                LogUtil.e(Thread.currentThread().getName());
                                                SendUser.sendUserId = ClickPacketSpan.this.sendPacketId;
                                                RpDetailActivity.intent((Context)ClickPacketSpan.this.reference.get(), 1, CurrentUser.getUserId(), BaseActivity.rongCloudToken, ClickPacketSpan.this.packetId, CurrentUser.getName(), "");
                                            }
                                        });
                                    }
                                }
                            } catch (Exception var3) {
                                BaseActivity.rongCloudToken = "";
                                var3.printStackTrace();
                            }

                        }

                        public void onError(ErrorCode var1) {
                            LogUtil.i("获得token失败" + var1);
                        }
                    });
                } else if (this.reference.get() != null) {
                    ToastUtil.showToast((Context)this.reference.get(), RongContext.getInstance().getString(string.net_error_l));
                }
            } else if (this.reference.get() != null) {
                SendUser.sendUserId = this.sendPacketId;
                RpDetailActivity.intent((Context)this.reference.get(), 1, CurrentUser.getUserId(), BaseActivity.rongCloudToken, this.packetId, CurrentUser.getName(), "");
            }

        }
    }

    class ViewHolder {
        TextView packet_message;

        ViewHolder() {
        }
    }
}
