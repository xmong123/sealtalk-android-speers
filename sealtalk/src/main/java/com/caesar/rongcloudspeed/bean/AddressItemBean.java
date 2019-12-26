package com.caesar.rongcloudspeed.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac on 2018/4/5.
 */

public class AddressItemBean implements Parcelable {

    private String address_id;
    private String user_id;
    private String consignee;
    private String email;
    private String country;
    private String province;
    private String city;
    private String address;
    private String zipcode;
    private String mobile;
    private String is_default;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.address_id);
        parcel.writeString(this.consignee);
        parcel.writeString(this.mobile);
        parcel.writeString(this.province);
        parcel.writeString(this.city);
        parcel.writeString(this.address);
    }

    public AddressItemBean() {
    }

    protected AddressItemBean(Parcel in) {
        this.address_id = in.readString();
        this.consignee = in.readString();
        this.mobile = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.address = in.readString();
    }

    public static final Creator<AddressItemBean> CREATOR = new Creator<AddressItemBean>() {
        @Override
        public AddressItemBean createFromParcel(Parcel source) {
            return new AddressItemBean(source);
        }

        @Override
        public AddressItemBean[] newArray(int size) {
            return new AddressItemBean[size];
        }
    };
}
