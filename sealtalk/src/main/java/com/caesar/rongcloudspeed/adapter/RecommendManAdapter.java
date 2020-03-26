package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.AppPeopleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
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
public class RecommendManAdapter extends BaseQuickAdapter<AppPeopleBaseBean.PeopleDataBean, BaseViewHolder> {
    private Context context;

    public RecommendManAdapter(Context context, List data) {
        super(R.layout.fragment_recommend_recyclerview_people,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AppPeopleBaseBean.PeopleDataBean bean) {
        String avatarString=bean.getAvatar();
        if (!avatarString.startsWith( "http://" )) {
            avatarString = Constant.THINKCMF_PATH + avatarString;
        }
        helper.setText(R.id.recommend_nicename,bean.getUser_login());
        Glide.with( context ).load( avatarString ).into( (ImageView) helper.getView( R.id.recommend_avatar ) );
    }
}
