package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
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
public class PersonnelRecruitAdapter extends BaseQuickAdapter<RecruitItemBean, BaseViewHolder> {
    private Context context;
    private String[] gradeItems = new String[]{"高职高中及以下", "大专院校", "全日制本科", "硕士研究生", "MBA", "博士及以上", "保密"};
    private String[] jobItems = new String[]{"1年及以下工作经验", "2年工作经验", "3年工作经验", "4年工作经验", "5年工作经验", "6年工作经验", "7年工作经验", "8年及以上工作经验"};

    public PersonnelRecruitAdapter(Context context, List data) {
        super(R.layout.personnel_recruits_item_layout,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecruitItemBean recruitItemBean) {
        Glide.with( context ).load( recruitItemBean.getRecruit_avatar() ).into( (ImageView) helper.getView( R.id.personnel_recruit_avatar ) );
        helper.setText( R.id.personnel_recruit_name, recruitItemBean.getRecruit_name() );
        helper.setText( R.id.personnel_recruit_job, "期望职位:"+recruitItemBean.getRecruit_job() );
        int gradeInt=Integer.valueOf(recruitItemBean.getRecruit_grade());
        helper.setText( R.id.personnel_recruit_grade, gradeItems[gradeInt] );
        int jobInt=Integer.valueOf(recruitItemBean.getRecruit_workingyears());
        helper.setText( R.id.personnel_recruit_experience, jobItems[jobInt] );
    }
}
