package com.caesar.rongcloudspeed.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac on 2018/4/5.
 */

public class RecruitItemBean  implements Parcelable {

    private String rongid;//招聘通讯ID
    private String recruit_id;//简历名称
    private String recruit_name;//简历名称
    private String recruit_avatar;//简历头像
    private String recruit_sex;//性别
    private String recruit_nation;//民族
    private String recruit_show;//显示
    private String recruit_native;//籍贯
    private String recruit_place;//居住地
    private String recruit_birthday;//生日
    private String recruit_height;//身高
    private String recruit_weight;//体重
    private String recruit_marry;//婚姻
    private String recruit_health;//健康
    private String recruit_grade;//学历
    private String recruit_salary;//薪资
    private String recruit_mobile;//手机
    private String recruit_age;//年纪
    private String recruit_job;//工作职位
    private String recruit_email;//邮件
    private String recruit_state;//工作状态
    private String recruit_education;//教育培训经历
    private String recruit_experience;//职业经历
    private String recruit_workingyears;//职业工作年限
    private String recruit_major;//所学专业
    private String recruit_school;//毕业院校
    private String recruit_expert1;//自我评价

    protected RecruitItemBean(Parcel in) {
        rongid = in.readString();
        recruit_id = in.readString();
        recruit_name = in.readString();
        recruit_avatar = in.readString();
        recruit_sex = in.readString();
        recruit_nation = in.readString();
        recruit_show = in.readString();
        recruit_native = in.readString();
        recruit_place = in.readString();
        recruit_birthday = in.readString();
        recruit_height = in.readString();
        recruit_weight = in.readString();
        recruit_marry = in.readString();
        recruit_health = in.readString();
        recruit_grade = in.readString();
        recruit_salary = in.readString();
        recruit_mobile = in.readString();
        recruit_age = in.readString();
        recruit_job = in.readString();
        recruit_email = in.readString();
        recruit_state = in.readString();
        recruit_education = in.readString();
        recruit_experience = in.readString();
        recruit_workingyears = in.readString();
        recruit_major = in.readString();
        recruit_school = in.readString();
        recruit_expert1 = in.readString();
    }

    public static final Creator<RecruitItemBean> CREATOR = new Creator<RecruitItemBean>() {
        @Override
        public RecruitItemBean createFromParcel(Parcel in) {
            return new RecruitItemBean(in);
        }

        @Override
        public RecruitItemBean[] newArray(int size) {
            return new RecruitItemBean[size];
        }
    };

    public String getRecruit_major() {
        return recruit_major;
    }

    public void setRecruit_major(String recruit_major) {
        this.recruit_major = recruit_major;
    }

    public String getRecruit_school() {
        return recruit_school;
    }

    public void setRecruit_school(String recruit_school) {
        this.recruit_school = recruit_school;
    }

    public String getRecruit_expert1() {
        return recruit_expert1;
    }

    public void setRecruit_expert1(String recruit_expert1) {
        this.recruit_expert1 = recruit_expert1;
    }

    public String getRecruit_id() {
        return recruit_id;
    }

    public void setRecruit_id(String recruit_id) {
        this.recruit_id = recruit_id;
    }

    public String getRecruit_workingyears() {
        return recruit_workingyears;
    }

    public void setRecruit_workingyears(String recruit_workingyears) {
        this.recruit_workingyears = recruit_workingyears;
    }

    public String getRecruit_avatar() {
        return recruit_avatar;
    }

    public void setRecruit_avatar(String recruit_avatar) {
        this.recruit_avatar = recruit_avatar;
    }

    public String getRecruit_name() {
        return recruit_name;
    }

    public void setRecruit_name(String recruit_name) {
        this.recruit_name = recruit_name;
    }

    public String getRecruit_sex() {
        return recruit_sex;
    }

    public void setRecruit_sex(String recruit_sex) {
        this.recruit_sex = recruit_sex;
    }

    public String getRecruit_nation() {
        return recruit_nation;
    }

    public String getRecruit_show() {
        return recruit_show;
    }

    public void setRecruit_show(String recruit_show) {
        this.recruit_show = recruit_show;
    }

    public void setRecruit_nation(String recruit_nation) {
        this.recruit_nation = recruit_nation;
    }

    public String getRecruit_native() {
        return recruit_native;
    }

    public void setRecruit_native(String recruit_native) {
        this.recruit_native = recruit_native;
    }

    public String getRecruit_place() {
        return recruit_place;
    }

    public void setRecruit_place(String recruit_place) {
        this.recruit_place = recruit_place;
    }

    public String getRecruit_birthday() {
        return recruit_birthday;
    }

    public void setRecruit_birthday(String recruit_birthday) {
        this.recruit_birthday = recruit_birthday;
    }

    public String getRecruit_height() {
        return recruit_height;
    }

    public void setRecruit_height(String recruit_height) {
        this.recruit_height = recruit_height;
    }

    public String getRecruit_weight() {
        return recruit_weight;
    }

    public void setRecruit_weight(String recruit_weight) {
        this.recruit_weight = recruit_weight;
    }

    public String getRecruit_marry() {
        return recruit_marry;
    }

    public void setRecruit_marry(String recruit_marry) {
        this.recruit_marry = recruit_marry;
    }

    public String getRecruit_health() {
        return recruit_health;
    }

    public void setRecruit_health(String recruit_health) {
        this.recruit_health = recruit_health;
    }

    public String getRecruit_grade() {
        return recruit_grade;
    }

    public void setRecruit_grade(String recruit_grade) {
        this.recruit_grade = recruit_grade;
    }

    public String getRecruit_salary() {
        return recruit_salary;
    }

    public void setRecruit_salary(String recruit_salary) {
        this.recruit_salary = recruit_salary;
    }

    public String getRecruit_mobile() {
        return recruit_mobile;
    }

    public void setRecruit_mobile(String recruit_mobile) {
        this.recruit_mobile = recruit_mobile;
    }

    public String getRecruit_age() {
        return recruit_age;
    }

    public void setRecruit_age(String recruit_age) {
        this.recruit_age = recruit_age;
    }

    public String getRecruit_job() {
        return recruit_job;
    }

    public void setRecruit_job(String recruit_job) {
        this.recruit_job = recruit_job;
    }

    public String getRecruit_email() {
        return recruit_email;
    }

    public void setRecruit_email(String recruit_email) {
        this.recruit_email = recruit_email;
    }

    public String getRecruit_state() {
        return recruit_state;
    }

    public void setRecruit_state(String recruit_state) {
        this.recruit_state = recruit_state;
    }

    public String getRecruit_education() {
        return recruit_education;
    }

    public void setRecruit_education(String recruit_education) {
        this.recruit_education = recruit_education;
    }

    public String getRecruit_experience() {
        return recruit_experience;
    }

    public void setRecruit_experience(String recruit_experience) {
        this.recruit_experience = recruit_experience;
    }

    public String getRongid() {
        return rongid;
    }

    public void setRongid(String rongid) {
        this.rongid = rongid;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(rongid);
        parcel.writeString(recruit_id);
        parcel.writeString(recruit_name);
        parcel.writeString(recruit_avatar);
        parcel.writeString(recruit_sex);
        parcel.writeString(recruit_nation);
        parcel.writeString(recruit_show);
        parcel.writeString(recruit_native);
        parcel.writeString(recruit_place);
        parcel.writeString(recruit_birthday);
        parcel.writeString(recruit_height);
        parcel.writeString(recruit_weight);
        parcel.writeString(recruit_marry);
        parcel.writeString(recruit_health);
        parcel.writeString(recruit_grade);
        parcel.writeString(recruit_salary);
        parcel.writeString(recruit_mobile);
        parcel.writeString(recruit_age);
        parcel.writeString(recruit_job);
        parcel.writeString(recruit_email);
        parcel.writeString(recruit_state);
        parcel.writeString(recruit_education);
        parcel.writeString(recruit_experience);
        parcel.writeString(recruit_workingyears);
        parcel.writeString(recruit_major);
        parcel.writeString(recruit_school);
        parcel.writeString(recruit_expert1);
    }
}
