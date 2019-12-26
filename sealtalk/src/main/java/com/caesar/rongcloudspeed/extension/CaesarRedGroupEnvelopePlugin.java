package com.caesar.rongcloudspeed.extension;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.extend.JrmfRedPacketMessage;
import com.jrmf360.rylib.rp.ui.SendGroupEnvelopesActivity;

import java.io.File;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.IPluginRequestPermissionResultCallback;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imkit.utils.RongOperationPermissionUtils;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.SendMessageCallback;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.message.SightMessage;
import io.rong.sight.record.SightRecordActivity;

public class CaesarRedGroupEnvelopePlugin implements IPluginModule {
    private ConversationType conversationType;
    private String targetId;

    public CaesarRedGroupEnvelopePlugin() {
    }

    public Drawable obtainDrawable(Context var1) {
        return ContextCompat.getDrawable(var1, drawable.selector_hongbao);
    }

    public String obtainTitle(Context var1) {
        return var1.getString(string._s_bribery);
    }

    public void onClick(Fragment var1, RongExtension var2) {
        this.conversationType = var2.getConversationType();
        this.targetId = var2.getTargetId();
        Intent var3 = new Intent(var2.getContext(), SendGroupEnvelopesActivity.class);
        var3.putExtra("TargetId", var2.getTargetId());
        var3.putExtra("user_id", CurrentUser.getUserId());
        var3.putExtra("user_name", CurrentUser.getName());
        var3.putExtra("user_icon", CurrentUser.getUserIcon());
        var2.startActivityForPluginResult(var3, 51, this);
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
            RongIM.getInstance().getRongIMClient().sendMessage(this.conversationType, this.targetId, var5, "您收到了一条消息", (String)null, new SendMessageCallback() {
                public void onError(Integer var1, ErrorCode var2) {
                    LogUtil.i("send a group rp error");
                }

                public void onSuccess(Integer var1) {
                    LogUtil.i("send a group rp success");
                }
            });
        }

    }

    public static class CaesarSightPlugin implements IPluginModule, IPluginRequestPermissionResultCallback {
        protected ConversationType conversationType;
        protected String targetId;
        protected Context context;
        private static final int REQUEST_SIGHT = 104;

        public CaesarSightPlugin() {
        }

        public Drawable obtainDrawable(Context context) {
            this.context = context;
            return ContextCompat.getDrawable(context, io.rong.sight.R.drawable.rc_ext_plugin_sight_selector);
        }

        public String obtainTitle(Context context) {
            return context.getString(io.rong.sight.R.string.rc_plugin_sight);
        }

        public void onClick(Fragment currentFragment, RongExtension extension) {
            if (RongOperationPermissionUtils.isMediaOperationPermit(currentFragment.getActivity())) {
                String[] permissions = new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
                this.conversationType = extension.getConversationType();
                this.targetId = extension.getTargetId();
                if (PermissionCheckUtil.checkPermissions(currentFragment.getActivity(), permissions)) {
                    this.startSightRecord(currentFragment, extension);
                } else {
                    extension.requestPermissionForPluginResult(permissions, 255, this);
                }

            }
        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == -1 && requestCode == 104 && data != null) {
                String fileUrl = data.getStringExtra("recordSightUrl");
                File file = new File(fileUrl);
                if (file.exists()) {
                    int recordTime = data.getIntExtra("recordSightTime", 0);
                    Log.d("recordSightUrl",fileUrl+"recordSightTime:"+recordTime);
                    SightMessage sightMessage = SightMessage.obtain(Uri.fromFile(file), recordTime);
                    Message message = Message.obtain(this.targetId, this.conversationType, sightMessage);
                    RongIM.getInstance().sendMediaMessage(message, this.context.getApplicationContext().getString(io.rong.sight.R.string.rc_message_content_sight), (String)null, (IRongCallback.ISendMediaMessageCallback)null);
                }
            }

        }

        private void startSightRecord(Fragment currentFragment, RongExtension extension) {
            File saveDir = null;
            if (ContextCompat.checkSelfPermission(currentFragment.getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                saveDir = new File(Environment.getExternalStorageDirectory(), "RongCloud/Media");
                saveDir.mkdirs();
            }

            Intent intent = new Intent(currentFragment.getActivity(), SightRecordActivity.class);
            intent.putExtra("recordSightDir", saveDir.getAbsolutePath());
            int maxRecordDuration = 10;

            try {
                maxRecordDuration = currentFragment.getActivity().getResources().getInteger(io.rong.sight.R.integer.rc_sight_max_record_duration);
            } catch (Resources.NotFoundException var7) {
                var7.printStackTrace();
            }

            intent.putExtra("maxRecordDuration", maxRecordDuration);
            extension.startActivityForPluginResult(intent, 104, this);
        }

        public boolean onRequestPermissionResult(Fragment fragment, RongExtension extension, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (PermissionCheckUtil.checkPermissions(fragment.getActivity(), permissions)) {
                this.startSightRecord(fragment, extension);
            } else {
                extension.showRequestPermissionFailedAlter(PermissionCheckUtil.getNotGrantedPermissionMsg(fragment.getActivity(), permissions, grantResults));
            }

            return true;
        }
    }
}

