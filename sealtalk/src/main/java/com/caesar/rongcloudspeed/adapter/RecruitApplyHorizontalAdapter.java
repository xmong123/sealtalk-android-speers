package com.caesar.rongcloudspeed.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.RecruitApplyBean;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.ui.activity.PersonnelDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonVideosActivity;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import io.rong.imkit.widget.AsyncImageView;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class RecruitApplyHorizontalAdapter extends BaseQuickAdapter<RecruitApplyBean, BaseViewHolder> {
    private Context context;
    private LinearLayout applyContactLayout;

    public RecruitApplyHorizontalAdapter(Context context, List data) {
        super(R.layout.item_recruits_apply_horizontal_layout, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecruitApplyBean recruitJobBean) {
        helper.setText(R.id.recruit_apply_text, recruitJobBean.getPost_title());
        applyContactLayout = (LinearLayout) helper.getView(R.id.apply_contact_layout);
        List<RecruitItemBean> apply_users = recruitJobBean.getApply_users();
        int width = (int) context.getResources().getDimension(R.dimen.seal_dialog_forward_item_option_height);
        int leftMargin = (int) context.getResources().getDimension(R.dimen.layoutMargin6dp);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        params.leftMargin = leftMargin;
        for (RecruitItemBean recruitItemBean : apply_users) {
            final AsyncImageView asyncImageView = new AsyncImageView(context);
            asyncImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            applyContactLayout.addView(asyncImageView, params);
            ImageLoaderUtils.displayGroupPortraitImage(recruitItemBean.getRecruit_avatar(), asyncImageView);
            asyncImageView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("recruitItemBean", recruitItemBean);
                ActivityUtils.startActivity(bundle, (Activity) context, PersonnelDetailActivity.class);
            });
        }
    }
}
