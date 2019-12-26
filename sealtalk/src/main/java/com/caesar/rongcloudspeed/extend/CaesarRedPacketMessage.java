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
        value = "RCCaesar:RpMsg",
        flag = 3
)
public class CaesarRedPacketMessage extends MessageContent {
    private String content;
    private String Bribery_ID;
    private String Bribery_Name;
    private String Bribery_Message;
    public static final Creator<CaesarRedPacketMessage> CREATOR = new Creator() {
        public CaesarRedPacketMessage createFromParcel(Parcel var1) {
            return new CaesarRedPacketMessage(var1);
        }

        public CaesarRedPacketMessage[] newArray(int var1) {
            return new CaesarRedPacketMessage[var1];
        }
    };

    public byte[] encode() {
        JSONObject var1 = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getContent())) {
                var1.put("content", this.content);
            }

            if (!TextUtils.isEmpty(this.getBribery_ID())) {
                var1.put("Bribery_ID", this.Bribery_ID);
            }

            if (!TextUtils.isEmpty(this.getBribery_Name())) {
                var1.put("Bribery_Name", this.Bribery_Name);
            }

            if (!TextUtils.isEmpty(this.getBribery_Message())) {
                var1.put("Bribery_Message", this.Bribery_Message);
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

    protected CaesarRedPacketMessage() {
    }

    public static CaesarRedPacketMessage obtain(String var0, String var1, String var2, String var3) {
        CaesarRedPacketMessage var4 = new CaesarRedPacketMessage();
        var4.setBribery_ID(var0);
        var4.setBribery_Name(var1);
        var4.setBribery_Message(var2);
        var4.setContent(var3);
        return var4;
    }

    public CaesarRedPacketMessage(byte[] var1) {
        String var2 = null;

        try {
            var2 = new String(var1, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
        }

        try {
            JSONObject var3 = new JSONObject(var2);
            if (var3.has("content")) {
                this.setContent(var3.optString("content"));
            }

            if (var3.has("Bribery_ID")) {
                this.setBribery_ID(var3.optString("Bribery_ID"));
            }

            if (var3.has("Bribery_Name")) {
                this.setBribery_Name(var3.optString("Bribery_Name"));
            }

            if (var3.has("Bribery_Message")) {
                this.setBribery_Message(var3.optString("Bribery_Message"));
            }

            if (var3.has("bribery")) {
                this.setUserInfo(this.parseJsonToUserInfo(var3.getJSONObject("bribery")));
            }
        } catch (JSONException var4) {
        }

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        ParcelUtils.writeToParcel(var1, this.Bribery_ID);
        ParcelUtils.writeToParcel(var1, this.Bribery_Name);
        ParcelUtils.writeToParcel(var1, this.Bribery_Message);
        ParcelUtils.writeToParcel(var1, this.content);
        ParcelUtils.writeToParcel(var1, this.getUserInfo());
    }

    public CaesarRedPacketMessage(Parcel var1) {
        this.setBribery_ID(ParcelUtils.readFromParcel(var1));
        this.setBribery_Name(ParcelUtils.readFromParcel(var1));
        this.setBribery_Message(ParcelUtils.readFromParcel(var1));
        this.setContent(ParcelUtils.readFromParcel(var1));
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(var1, UserInfo.class));
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String var1) {
        this.content = var1;
    }

    public String getBribery_ID() {
        return this.Bribery_ID;
    }

    public void setBribery_ID(String var1) {
        this.Bribery_ID = var1;
    }

    public String getBribery_Name() {
        return this.Bribery_Name;
    }

    public void setBribery_Name(String var1) {
        this.Bribery_Name = var1;
    }

    public String getBribery_Message() {
        return this.Bribery_Message;
    }

    public void setBribery_Message(String var1) {
        this.Bribery_Message = var1;
    }
}
