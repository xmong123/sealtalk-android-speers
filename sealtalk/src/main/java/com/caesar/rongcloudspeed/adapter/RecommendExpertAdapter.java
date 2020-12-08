package com.caesar.rongcloudspeed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.AppPeopleBaseBean;
import com.caesar.rongcloudspeed.bean.PeopleItemBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 文 件 名: RecommendExpertAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class RecommendExpertAdapter extends BaseQuickAdapter<PeopleItemBean, BaseViewHolder> {
    private Context context;

    public RecommendExpertAdapter(Context context, List data) {
        super(R.layout.fragment_recomuser_recyclerview_item,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PeopleItemBean peopleItemBean) {
        String avatarString=peopleItemBean.getAvatar();
        String usertypeString=peopleItemBean.getUser_type();
        if (!avatarString.startsWith( "http" )) {
            avatarString = Constant.THINKCMF_PATH + avatarString;
        }
        if(usertypeString.equals("4")){
            helper.setImageResource(R.id.recommend_user_icon,R.mipmap.recommend_service);
            helper.setText(R.id.recommend_user_add,"客服");
            helper.setBackgroundRes(R.id.recommend_user_add, R.drawable.btn_shape_normal);
        }
        helper.setText(R.id.recommend_user_nicename,peopleItemBean.getUser_login());
        Glide.with( context ).load( avatarString ).into( (ImageView) helper.getView( R.id.recommend_user_avatar ) );
    }
}
