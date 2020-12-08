package com.caesar.rongcloudspeed.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mac on 2018/4/5.
 *  `post_id` mediumint(8) unsigned NOT NULL COMMENT '职位ID',
 *   `post_author` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '职位者关联ID',
 *   `post_title` text CHARACTER SET utf8 NOT NULL COMMENT '求职标题',
 *   `post_date` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '职位发布日期',
 *   `post_area_code` int(11) unsigned NOT NULL COMMENT '职位发布地区编码',
 *   `post_area_name` varchar(64) NOT NULL COMMENT '职位发布地区名称',
 *   `post_salary` smallint(2) NOT NULL COMMENT '职位发布薪资',
 *   `post_grade` smallint(2) NOT NULL COMMENT '职位发布学历',
 *   `post_mobile` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '职位发布手机号',
 *   `post_tag` smallint(2) unsigned NOT NULL COMMENT '职位发布手机号',
 *   `post_level` smallint(2) NOT NULL COMMENT '职位级别',
 *   `post_status` smallint(2) NOT NULL COMMENT '职位状态'
 */

public class RecruitJobBean implements Parcelable {

    private String post_id;//招聘ID
    private String post_author;//招聘作者
    private String rongid;//招聘通讯ID
    private String post_title;//招聘标题
    private String post_date;//发布日期
    private String post_area_code;//发布地区编码
    private String post_area_name;//发布地区名称
    private String post_salary;//招聘薪资
    private String post_grade;//招聘学历
    private String post_mobile;//招聘号码
    private String post_tag;//招聘标签
    private String post_level;//招聘工作年限
    private String company_name;//发布公司名称
    private String post_excerpt;//发布位置详情
    private String post_hits;//发布浏览数
    private String post_like;//发布求职者

    protected RecruitJobBean(Parcel in) {
        post_id = in.readString();
        post_author = in.readString();
        post_title = in.readString();
        post_date = in.readString();
        post_area_code = in.readString();
        post_area_name = in.readString();
        post_salary = in.readString();
        post_grade = in.readString();
        post_mobile = in.readString();
        post_tag = in.readString();
        post_level = in.readString();
        company_name = in.readString();
        post_hits = in.readString();
        post_like = in.readString();
        post_excerpt = in.readString();
        rongid = in.readString();
    }

    public static final Creator<RecruitJobBean> CREATOR = new Creator<RecruitJobBean>() {
        @Override
        public RecruitJobBean createFromParcel(Parcel in) {
            return new RecruitJobBean(in);
        }

        @Override
        public RecruitJobBean[] newArray(int size) {
            return new RecruitJobBean[size];
        }
    };

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
    }

    public String getPost_title() {
        return post_title;
    }

    public String getRongid() {
        return rongid;
    }

    public void setRongid(String rongid) {
        this.rongid = rongid;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_area_code() {
        return post_area_code;
    }

    public void setPost_area_code(String post_area_code) {
        this.post_area_code = post_area_code;
    }

    public String getPost_area_name() {
        return post_area_name;
    }

    public void setPost_area_name(String post_area_name) {
        this.post_area_name = post_area_name;
    }

    public String getPost_salary() {
        return post_salary;
    }

    public void setPost_salary(String post_salary) {
        this.post_salary = post_salary;
    }

    public String getPost_grade() {
        return post_grade;
    }

    public void setPost_grade(String post_grade) {
        this.post_grade = post_grade;
    }

    public String getPost_mobile() {
        return post_mobile;
    }

    public void setPost_mobile(String post_mobile) {
        this.post_mobile = post_mobile;
    }

    public String getPost_tag() {
        return post_tag;
    }

    public void setPost_tag(String post_tag) {
        this.post_tag = post_tag;
    }

    public String getPost_level() {
        return post_level;
    }

    public void setPost_level(String post_level) {
        this.post_level = post_level;
    }

    public String getPost_hits() {
        return post_hits;
    }

    public void setPost_hits(String post_hits) {
        this.post_hits = post_hits;
    }

    public String getPost_like() {
        return post_like;
    }

    public void setPost_like(String post_like) {
        this.post_like = post_like;
    }

    public String getPost_excerpt() {
        return post_excerpt;
    }

    public void setPost_excerpt(String post_excerpt) {
        this.post_excerpt = post_excerpt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(post_id);
        parcel.writeString(post_author);
        parcel.writeString(post_title);
        parcel.writeString(post_date);
        parcel.writeString(post_area_code);
        parcel.writeString(post_area_name);
        parcel.writeString(post_salary);
        parcel.writeString(post_grade);
        parcel.writeString(post_mobile);
        parcel.writeString(post_tag);
        parcel.writeString(post_level);
        parcel.writeString(company_name);
        parcel.writeString(post_hits);
        parcel.writeString(post_like);
        parcel.writeString(post_excerpt);
        parcel.writeString(rongid);
    }
}
