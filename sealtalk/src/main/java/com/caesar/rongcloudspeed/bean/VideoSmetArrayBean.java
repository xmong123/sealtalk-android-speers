package com.caesar.rongcloudspeed.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class VideoSmetArrayBean implements Parcelable {
    private String thumb;
    private String template;
    private List<VideoDataBean>photo;

    protected VideoSmetArrayBean(Parcel in) {
        thumb = in.readString();
        template = in.readString();
        photo = in.createTypedArrayList(VideoDataBean.CREATOR);
    }

    public static final Creator<VideoSmetArrayBean> CREATOR = new Creator<VideoSmetArrayBean>() {
        @Override
        public VideoSmetArrayBean createFromParcel(Parcel in) {
            return new VideoSmetArrayBean(in);
        }

        @Override
        public VideoSmetArrayBean[] newArray(int size) {
            return new VideoSmetArrayBean[size];
        }
    };

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<VideoDataBean> getPhoto() {
        return photo;
    }

    public void setPhoto(List<VideoDataBean> photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(thumb);
        parcel.writeString(template);
        parcel.writeTypedList(photo);
    }
}
