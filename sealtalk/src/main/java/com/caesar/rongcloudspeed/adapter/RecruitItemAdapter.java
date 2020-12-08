package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class RecruitItemAdapter extends BaseQuickAdapter<RecruitJobBean, BaseViewHolder> {
    private Context context;
    private String[] salaryItems = new String[]{"2000元/月以下", "2000～3000元/月", "3000～4000元/月", "4000～5000元/月", "5000～8000元/月", "8000元/月以上", "面议"};
    private String[] gradeItems = new String[]{"高职高中及以下", "大专院校", "全日制本科", "硕士研究生", "MBA", "博士及以上", "不限"};
    public RecruitItemAdapter(Context context, List data) {
        super(R.layout.work_recruits_item_layout,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecruitJobBean recruitJobBean) {
        helper.setText( R.id.recruit_name_text, recruitJobBean.getPost_title() );
        int salaryInt=Integer.valueOf(recruitJobBean.getPost_salary());
        helper.setText( R.id.recruit_salary_text,salaryItems[salaryInt]);
        int gradeInt=Integer.valueOf(recruitJobBean.getPost_grade());
        helper.setText( R.id.recruit_tag_text, gradeItems[gradeInt] );
        helper.setText( R.id.recruit_area_text, recruitJobBean.getPost_area_name() );
        helper.setText( R.id.recruit_company_text, recruitJobBean.getCompany_name() );
    }
}
