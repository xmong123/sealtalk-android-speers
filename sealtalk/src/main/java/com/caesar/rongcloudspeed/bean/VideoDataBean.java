package com.caesar.rongcloudspeed.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class VideoDataBean implements Parcelable {
    private String url;
    private String alt;

    protected VideoDataBean(Parcel in) {
        url = in.readString();
        alt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(alt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoDataBean> CREATOR = new Creator<VideoDataBean>() {
        @Override
        public VideoDataBean createFromParcel(Parcel in) {
            return new VideoDataBean(in);
        }

        @Override
        public VideoDataBean[] newArray(int size) {
            return new VideoDataBean[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
