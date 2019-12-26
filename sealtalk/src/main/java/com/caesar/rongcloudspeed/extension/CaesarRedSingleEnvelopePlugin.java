package com.caesar.rongcloudspeed.extension;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.fragment.app.Fragment;

import com.caesar.rongcloudspeed.caesar.CaesarSendSingleEnvelopesActivity;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.extend.JrmfRedPacketMessage;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.SendMessageCallback;
import io.rong.imlib.model.Conversation.ConversationType;

public class CaesarRedSingleEnvelopePlugin implements IPluginModule {
    private String targetId;

    public CaesarRedSingleEnvelopePlugin() {
    }

    public Drawable obtainDrawable(Context var1) {
        return var1.getResources().getDrawable(drawable.selector_hongbao);
    }

    public String obtainTitle(Context var1) {
        return var1.getString(string._s_bribery);
    }

    public void onClick(Fragment var1, RongExtension var2) {
        this.targetId = var2.getTargetId();
        Intent var3 = new Intent(var2.getContext(), CaesarSendSingleEnvelopesActivity.class);
        var3.putExtra("TargetId", var2.getTargetId());
        var3.putExtra("user_id", CurrentUser.getUserId());
        var3.putExtra("user_name", CurrentUser.getName());
        var3.putExtra("user_icon", CurrentUser.getUserIcon());
        var2.startActivityForPluginResult(var3, 52, this);
    }

    public void onActivityResult(int var1, int var2, Intent var3) {
        if (var2 == -1) {
            this.sendRpMessage(var3);
        }
    }

    private void sendRpMessage(Intent var1) {
        String var2 = var1.getStringExtra("envelopesID");
        String var3 = var1.getStringExtra("envelopeMessage");
        String var4 = var1.getStringExtra("envelopeName");
        JrmfRedPacketMessage var5 = JrmfRedPacketMessage.obtain(var2, var4, var3, "[" + var4 + "] " + var3);
        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance().getRongIMClient().sendMessage(ConversationType.PRIVATE, this.targetId, var5, "您收到了一条消息", (String)null, new SendMessageCallback() {
                public void onError(Integer var1, ErrorCode var2) {
                }

                public void onSuccess(Integer var1) {
                }
            });
        }

    }
}

