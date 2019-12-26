package com.caesar.rongcloudspeed.extension;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.extend.CaesarTransferAccountsMessage;
import com.caesar.rongcloudspeed.caesar.CaesarTransAccountActivity;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.m;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.SendMessageCallback;
import io.rong.imlib.model.Conversation.ConversationType;

public class CaesarTransferAccountPlugin implements IPluginModule {
    private String targetId;

    public CaesarTransferAccountPlugin() {
    }

    public Drawable obtainDrawable(Context var1) {
        return var1.getResources().getDrawable(drawable.jrmf_selector_transfer_account);
    }

    public String obtainTitle(Context var1) {
        return var1.getString(R.string.profile_start_send_money);
    }

    public void onClick(Fragment var1, RongExtension var2) {
        if (!m.a()) {
            this.targetId = var2.getTargetId();
            Intent var3 = new Intent(var2.getContext(), CaesarTransAccountActivity.class);
            var3.putExtra("TargetId", this.targetId);
            Bundle var4 = new Bundle();
            var4.putString("TargetId", var2.getTargetId());
            var4.putString("TargetName", CurrentUser.getNameById(this.targetId));
            var4.putString("TargetIcon", CurrentUser.getUserIconById(this.targetId));
            var3.putExtras(var4);
            var3.putExtra("user_id", CurrentUser.getUserId());
            var3.putExtra("user_name", CurrentUser.getName());
            var3.putExtra("user_icon", CurrentUser.getUserIcon());
            var2.startActivityForPluginResult(var3, 53, this);
        }

    }

    public void onActivityResult(int var1, int var2, Intent var3) {
        if (var2 == -1) {
            this.sendTransAccountMessage(var3);
        }
    }

    private void sendTransAccountMessage(Intent var1) {
        String var2 = var1.getStringExtra("transferOrder");
        String var3 = var1.getStringExtra("transferAmount");
        String var4 = CurrentUser.getUserId();
        String var5 = this.targetId;
        String var6 = var1.getStringExtra("transferDesp");
        String var7 = "0";
        CaesarTransferAccountsMessage var8 = CaesarTransferAccountsMessage.obtain(var2, var3, var4, var5, var6, var7);
        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance().getRongIMClient().sendMessage(ConversationType.PRIVATE, var5, var8, "您收到了一条消息", (String)null, new SendMessageCallback() {
                public void onError(Integer var1, ErrorCode var2) {
                    LogUtil.e("发送转账消息失败:" + var2.toString());
                }

                public void onSuccess(Integer var1) {
                }
            });
        }

    }
}

