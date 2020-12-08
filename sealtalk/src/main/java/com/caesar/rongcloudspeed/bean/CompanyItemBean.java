package com.caesar.rongcloudspeed.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac on 2018/4/5.
 * `company_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT COMMENT '表id',
 *   `user_id` mediumint(8) unsigned NOT NULL COMMENT '用户ID',
 *   `company_name` varchar(64) NOT NULL COMMENT '公司名字',
 *   `company_nature` varchar(64) NOT NULL COMMENT '公司性质',
 *   `company_size` int(11) unsigned NOT NULL COMMENT '公司规模',
 *   `company_licence` varchar(255) NOT NULL COMMENT '营业执照',
 *   `company_address` varchar(128) NOT NULL COMMENT '公司地址',
 *   `company_contact` varchar(64) NOT NULL COMMENT '公司联系人',
 *   `company_phone` varchar(20) NOT NULL COMMENT '公司电话',
 *   `company_level` int(8) unsigned NOT NULL DEFAULT '0' COMMENT '公司级别',
 *   `company_show` smallint(2) NOT NULL DEFAULT '0' COMMENT '公开显示',
 *   `user_status` smallint(2) unsigned NOT NULL DEFAULT '1' COMMENT '公司状态',
 */

public class CompanyItemBean implements Parcelable {

    private String company_id;
    private String user_id;
    private String company_name;
    private String company_nature;
    private String company_size;
    private String company_licence;
    private String company_address;
    private String company_contact;
    private String company_phone;
    private String company_level;

    protected CompanyItemBean(Parcel in) {
        company_id = in.readString();
        user_id = in.readString();
        company_name = in.readString();
        company_nature = in.readString();
        company_size = in.readString();
        company_licence = in.readString();
        company_address = in.readString();
        company_contact = in.readString();
        company_phone = in.readString();
        company_level = in.readString();
    }

    public static final Creator<CompanyItemBean> CREATOR = new Creator<CompanyItemBean>() {
        @Override
        public CompanyItemBean createFromParcel(Parcel in) {
            return new CompanyItemBean(in);
        }

        @Override
        public CompanyItemBean[] newArray(int size) {
            return new CompanyItemBean[size];
        }
    };

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_nature() {
        return company_nature;
    }

    public void setCompany_nature(String company_nature) {
        this.company_nature = company_nature;
    }

    public String getCompany_size() {
        return company_size;
    }

    public void setCompany_size(String company_size) {
        this.company_size = company_size;
    }

    public String getCompany_licence() {
        return company_licence;
    }

    public void setCompany_licence(String company_licence) {
        this.company_licence = company_licence;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_contact() {
        return company_contact;
    }

    public void setCompany_contact(String company_contact) {
        this.company_contact = company_contact;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public String getCompany_level() {
        return company_level;
    }

    public void setCompany_level(String company_level) {
        this.company_level = company_level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(company_id);
        parcel.writeString(user_id);
        parcel.writeString(company_name);
        parcel.writeString(company_nature);
        parcel.writeString(company_size);
        parcel.writeString(company_licence);
        parcel.writeString(company_address);
        parcel.writeString(company_contact);
        parcel.writeString(company_phone);
        parcel.writeString(company_level);
    }
}

