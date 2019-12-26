package com.caesar.rongcloudspeed.extend;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RCCaesar:RpOpendMsg",
        flag = 1
)
public class CaesarRedPacketOpenedMessage extends MessageContent {
    private String sendPacketId;
    private String sendNickName;
    private String openPacketId;
    private String packetId;
    private String isGetDone;
    private String openNickName;
    private String targetId;
    public static final Creator<CaesarRedPacketOpenedMessage> CREATOR = new Creator<CaesarRedPacketOpenedMessage>() {
        public CaesarRedPacketOpenedMessage createFromParcel(Parcel var1) {
            return new CaesarRedPacketOpenedMessage(var1);
        }

        public CaesarRedPacketOpenedMessage[] newArray(int var1) {
            return new CaesarRedPacketOpenedMessage[var1];
        }
    };

    public CaesarRedPacketOpenedMessage() {
    }

    public static CaesarRedPacketOpenedMessage obtain(String var0, String var1, String var2, String var3, String var4, String var5, String var6) {
        CaesarRedPacketOpenedMessage var7 = new CaesarRedPacketOpenedMessage();
        var7.setPacketId(var3);
        var7.setSendPacketId(var0);
        var7.setSendNickName(var1);
        var7.setOpenPacketId(var2);
        var7.setIsGetDone(var4);
        var7.setOpenNickName(var5);
        var7.setTargetId(var6);
        return var7;
    }

    public byte[] encode() {
        JSONObject var1 = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getSendPacketId())) {
                var1.put("sendPacketId", this.sendPacketId);
            }

            if (!TextUtils.isEmpty(this.getSendNickName())) {
                var1.put("sendNickName", this.sendNickName);
            }

            if (!TextUtils.isEmpty(this.getOpenPacketId())) {
                var1.put("openPacketId", this.openPacketId);
            }

            if (!TextUtils.isEmpty(this.getPacketId())) {
                var1.put("packetId", this.packetId);
            }

            if (!TextUtils.isEmpty(this.getIsGetDone())) {
                var1.put("isGetDone", this.isGetDone);
            }

            if (!TextUtils.isEmpty(this.getOpenNickName())) {
                var1.put("openNickName", this.openNickName);
            }

            if (!TextUtils.isEmpty(this.getTargetId())) {
                var1.put("targetId", this.targetId);
            }

            if (this.getJSONUserInfo() != null) {
                var1.putOpt("bribery", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        try {
            return var1.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private String getEmotion(String var1) {
        Pattern var2 = Pattern.compile("\\[/u([0-9A-Fa-f]+)\\]");
        Matcher var3 = var2.matcher(var1);
        StringBuffer var4 = new StringBuffer();

        while(var3.find()) {
            int var5 = Integer.parseInt(var3.group(1), 16);
            var3.appendReplacement(var4, String.valueOf(Character.toChars(var5)));
        }

        var3.appendTail(var4);
        return var4.toString();
    }

    public CaesarRedPacketOpenedMessage(byte[] var1) {
        String var2 = null;

        try {
            var2 = new String(var1, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        try {
            JSONObject var3 = new JSONObject(var2);
            if (var3.has("sendPacketId")) {
                this.setSendPacketId(var3.optString("sendPacketId"));
            }

            if (var3.has("sendNickName")) {
                this.setSendNickName(var3.optString("sendNickName"));
            }

            if (var3.has("openPacketId")) {
                this.setOpenPacketId(var3.optString("openPacketId"));
            }

            if (var3.has("packetId")) {
                this.setPacketId(var3.optString("packetId"));
            }

            if (var3.has("isGetDone")) {
                this.setIsGetDone(var3.optString("isGetDone"));
            }

            if (var3.has("openNickName")) {
                this.setOpenNickName(var3.optString("openNickName"));
            }

            if (var3.has("targetId")) {
                this.setTargetId(var3.optString("targetId"));
            }

            if (var3.has("bribery")) {
                this.setUserInfo(this.parseJsonToUserInfo(var3.getJSONObject("bribery")));
            }
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        ParcelUtils.writeToParcel(var1, this.sendPacketId);
        ParcelUtils.writeToParcel(var1, this.sendNickName);
        ParcelUtils.writeToParcel(var1, this.openPacketId);
        ParcelUtils.writeToParcel(var1, this.packetId);
        ParcelUtils.writeToParcel(var1, this.isGetDone);
        ParcelUtils.writeToParcel(var1, this.openNickName);
        ParcelUtils.writeToParcel(var1, this.targetId);
        ParcelUtils.writeToParcel(var1, this.getUserInfo());
    }

    protected CaesarRedPacketOpenedMessage(Parcel var1) {
        this.setSendPacketId(ParcelUtils.readFromParcel(var1));
        this.setSendNickName(ParcelUtils.readFromParcel(var1));
        this.setOpenPacketId(ParcelUtils.readFromParcel(var1));
        this.setPacketId(ParcelUtils.readFromParcel(var1));
        this.setIsGetDone(ParcelUtils.readFromParcel(var1));
        this.setOpenNickName(ParcelUtils.readFromParcel(var1));
        this.setTargetId(ParcelUtils.readFromParcel(var1));
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(var1, UserInfo.class));
    }

    public String getSendPacketId() {
        return this.sendPacketId;
    }

    public void setSendPacketId(String var1) {
        this.sendPacketId = var1;
    }

    public void setSendNickName(String var1) {
        this.sendNickName = var1;
    }

    public String getSendNickName() {
        return this.sendNickName;
    }

    public String getOpenPacketId() {
        return this.openPacketId;
    }

    public void setOpenPacketId(String var1) {
        this.openPacketId = var1;
    }

    public String getPacketId() {
        return this.packetId;
    }

    public void setPacketId(String var1) {
        this.packetId = var1;
    }

    public String getIsGetDone() {
        return this.isGetDone;
    }

    public void setIsGetDone(String var1) {
        this.isGetDone = var1;
    }

    public String getOpenNickName() {
        return this.openNickName;
    }

    public void setOpenNickName(String var1) {
        this.openNickName = var1;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setTargetId(String var1) {
        this.targetId = var1;
    }
}

