package io.rong.contactcard.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Beyond on 2016/12/5.
 */

@MessageTag(value = "RC:CardMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
@DestructionTag
public class ContactSeekInfo extends MessageContent {
    private static final String TAG = "ContactSeekInfo";

    private String seek_id;
    private String seek_user;
    private String seek_nicename;
    private String seek_title;
    private String seek_price;
    private String seek_date;
    private String photos_urls;
    private String seek_expert;

    public ContactSeekInfo() {

    }

    public ContactSeekInfo(String seek_id, String seek_title, String photos_urls, String seek_expert,String seek_user, String seek_nicename,String seek_price, String seek_date) {
        this.seek_id = seek_id;
        this.seek_title = seek_title;
        this.photos_urls = photos_urls;
        this.seek_expert = seek_expert;
        this.seek_user = seek_user;
        this.seek_nicename = seek_nicename;
        this.seek_price = seek_price;
        this.seek_date = seek_date;
    }

    public static ContactSeekInfo obtain(String seek_id, String seek_title, String photos_urls, String seek_expert, String seek_user, String seek_nicename,String seek_price, String seek_date) {
        return new ContactSeekInfo(seek_id, seek_title, photos_urls, seek_expert, seek_user, seek_nicename, seek_price, seek_date);
    }

    public static final Creator<ContactSeekInfo> CREATOR = new Creator<ContactSeekInfo>() {
        @Override
        public ContactSeekInfo createFromParcel(Parcel source) {
            return new ContactSeekInfo(source);
        }

        @Override
        public ContactSeekInfo[] newArray(int size) {
            return new ContactSeekInfo[size];
        }
    };

    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("seek_id", getSeek_id()); // 这里的id（联系人）不同于下边发送名片信息者的 sendUserId
            jsonObject.put("seek_title",getSeek_title());
            jsonObject.put("photos_urls", getPhotos_urls());
            jsonObject.put("seek_expert", getSeek_expert());
            jsonObject.put("seek_user", getSeek_user());
            jsonObject.put("seek_nicename", getSeek_nicename());
            jsonObject.put("seek_price", getSeek_price());
            jsonObject.put("seek_date", getSeek_date());

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ContactSeekInfo(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("seek_id"))
                setSeek_id(jsonObj.optString("seek_id"));
            if (jsonObj.has("seek_title"))
                setSeek_title(jsonObj.optString("seek_title"));
            if (jsonObj.has("seek_image"))
                setPhotos_urls(jsonObj.optString("photos_urls"));
            if (jsonObj.has("seek_expert"))
                setSeek_expert(jsonObj.optString("seek_expert"));
            if (jsonObj.has("seek_user"))
                setSeek_user(jsonObj.optString("seek_user"));
            if (jsonObj.has("seek_nicename"))
                setSeek_nicename(jsonObj.optString("seek_nicename"));
            if (jsonObj.has("seek_price"))
                setSeek_price(jsonObj.optString("seek_price"));
            if (jsonObj.has("seek_date"))
                setSeek_date(jsonObj.optString("seek_date"));
        } catch (JSONException e) {
            RLog.e(TAG, "JSONException " + e.getMessage());
        }
    }

    public ContactSeekInfo(Parcel in) {
        seek_id = in.readString();
        seek_title = in.readString();
        photos_urls = in.readString();
        seek_expert = in.readString();
        seek_user = in.readString();
        seek_nicename = in.readString();
        seek_price = in.readString();
        seek_date = in.readString();
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
        setDestruct(ParcelUtils.readIntFromParcel(in) == 1);
        setDestructTime(ParcelUtils.readLongFromParcel(in));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(seek_id);
        dest.writeString(seek_title);
        dest.writeString(photos_urls);
        dest.writeString(seek_expert);
        dest.writeString(seek_user);
        dest.writeString(seek_nicename);
        dest.writeString(seek_price);
        dest.writeString(seek_date);
        ParcelUtils.writeToParcel(dest, getUserInfo());
        ParcelUtils.writeToParcel(dest, isDestruct() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, getDestructTime());
    }

    public String getSeek_price() {
        return seek_price;
    }

    public void setSeek_price(String seek_price) {
        this.seek_price = seek_price;
    }

    public String getSeek_date() {
        return seek_date;
    }

    public void setSeek_date(String seek_date) {
        this.seek_date = seek_date;
    }

    public String getPhotos_urls() {
        return photos_urls;
    }

    public void setPhotos_urls(String photos_urls) {
        this.photos_urls = photos_urls;
    }

    public String getSeek_id() {
        return seek_id;
    }

    public void setSeek_id(String seek_id) {
        this.seek_id = seek_id;
    }

    public String getSeek_title() {
        return seek_title;
    }

    public void setSeek_title(String seek_title) {
        this.seek_title = seek_title;
    }

    public String getSeek_expert() {
        return seek_expert;
    }

    public void setSeek_expert(String seek_expert) {
        this.seek_expert = seek_expert;
    }

    public String getSeek_user() {
        return seek_user;
    }

    public void setSeek_user(String seek_user) {
        this.seek_user = seek_user;
    }

    public String getSeek_nicename() {
        return seek_nicename;
    }

    public void setSeek_nicename(String seek_nicename) {
        this.seek_nicename = seek_nicename;
    }
}
