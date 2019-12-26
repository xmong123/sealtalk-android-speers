package com.caesar.rongcloudspeed.extend;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RCCaesar:TrAccountMsg",
        flag = 3
)
public class CaesarTransferAccountsMessage extends MessageContent {
    private String transferOrder;
    private String transferAmount;
    private String transferSourceUserId;
    private String transferReceiveUserId;
    private String transferDesp;
    private String transferStatus;
    public static final Creator<CaesarTransferAccountsMessage> CREATOR = new Creator<CaesarTransferAccountsMessage>() {
        public CaesarTransferAccountsMessage createFromParcel(Parcel var1) {
            return new CaesarTransferAccountsMessage(var1);
        }

        public CaesarTransferAccountsMessage[] newArray(int var1) {
            return new CaesarTransferAccountsMessage[var1];
        }
    };

    public byte[] encode() {
        JSONObject var1 = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getTransferOrder())) {
                var1.put("transferOrder", this.transferOrder);
            }

            if (!TextUtils.isEmpty(this.getTransferAmount())) {
                var1.put("transferAmount", this.transferAmount);
            }

            if (!TextUtils.isEmpty(this.getTransferSourceUserId())) {
                var1.put("transferSourceUserId", this.transferSourceUserId);
            }

            if (!TextUtils.isEmpty(this.getTransferReceiveUserId())) {
                var1.put("transferReceiveUserId", this.transferReceiveUserId);
            }

            if (!TextUtils.isEmpty(this.getTransferDesp())) {
                var1.put("transferDesp", this.transferDesp);
            }

            if (!TextUtils.isEmpty(this.getTransferStatus())) {
                var1.put("transferStatus", this.transferStatus);
            }

            if (this.getJSONUserInfo() != null) {
                var1.putOpt("transAccount", this.getJSONUserInfo());
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

    public CaesarTransferAccountsMessage() {
    }

    public static CaesarTransferAccountsMessage obtain(String var0, String var1, String var2, String var3, String var4, String var5) {
        CaesarTransferAccountsMessage var6 = new CaesarTransferAccountsMessage();
        var6.setTransferOrder(var0);
        var6.setTransferAmount(var1);
        var6.setTransferSourceUserId(var2);
        var6.setTransferReceiveUserId(var3);
        var6.setTransferDesp(var4);
        var6.setTransferStatus(var5);
        return var6;
    }

    public CaesarTransferAccountsMessage(byte[] var1) {
        String var2 = null;

        try {
            var2 = new String(var1, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
        }

        try {
            JSONObject var3 = new JSONObject(var2);
            if (var3.has("transferOrder")) {
                this.setTransferOrder(var3.optString("transferOrder"));
            }

            if (var3.has("transferAmount")) {
                this.setTransferAmount(var3.optString("transferAmount"));
            }

            if (var3.has("transferSourceUserId")) {
                this.setTransferSourceUserId(var3.optString("transferSourceUserId"));
            }

            if (var3.has("transferReceiveUserId")) {
                this.setTransferReceiveUserId(var3.optString("transferReceiveUserId"));
            }

            if (var3.has("transferDesp")) {
                this.setTransferDesp(var3.optString("transferDesp"));
            }

            if (var3.has("transferStatus")) {
                this.setTransferStatus(var3.optString("transferStatus"));
            }

            if (var3.has("transAccount")) {
                this.setUserInfo(this.parseJsonToUserInfo(var3.getJSONObject("transAccount")));
            }
        } catch (JSONException var4) {
        }

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel var1, int var2) {
        var1.writeString(this.transferOrder);
        var1.writeString(this.transferAmount);
        var1.writeString(this.transferSourceUserId);
        var1.writeString(this.transferReceiveUserId);
        var1.writeString(this.transferDesp);
        var1.writeString(this.transferStatus);
    }

    protected CaesarTransferAccountsMessage(Parcel var1) {
        this.transferOrder = var1.readString();
        this.transferAmount = var1.readString();
        this.transferSourceUserId = var1.readString();
        this.transferReceiveUserId = var1.readString();
        this.transferDesp = var1.readString();
        this.transferStatus = var1.readString();
    }

    public String getTransferOrder() {
        return this.transferOrder;
    }

    public void setTransferOrder(String var1) {
        this.transferOrder = var1;
    }

    public String getTransferAmount() {
        return this.transferAmount;
    }

    public void setTransferAmount(String var1) {
        this.transferAmount = var1;
    }

    public String getTransferSourceUserId() {
        return this.transferSourceUserId;
    }

    public void setTransferSourceUserId(String var1) {
        this.transferSourceUserId = var1;
    }

    public String getTransferReceiveUserId() {
        return this.transferReceiveUserId;
    }

    public void setTransferReceiveUserId(String var1) {
        this.transferReceiveUserId = var1;
    }

    public String getTransferDesp() {
        return this.transferDesp;
    }

    public void setTransferDesp(String var1) {
        this.transferDesp = var1;
    }

    public String getTransferStatus() {
        return this.transferStatus;
    }

    public void setTransferStatus(String var1) {
        this.transferStatus = var1;
    }
}

