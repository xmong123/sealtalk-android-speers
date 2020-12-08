package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.ArticleCommentsBean;
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
public class AnimationCommentAdapter extends BaseQuickAdapter<ArticleCommentsBean, BaseViewHolder> {
    private Context context;

    public AnimationCommentAdapter(Context context, List data) {
        super(R.layout.recycle_item_news_comment,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleCommentsBean articleCommentsBean) {
        String urlString=articleCommentsBean.getUrl();
        if (!urlString.startsWith( "http" )) {
            urlString = Constant.THINKCMF_PATH + urlString;
        }
        helper.setText(R.id.tv_username, articleCommentsBean.getFull_name()+" · "+articleCommentsBean.getCreatetime());
        helper.setText(R.id.tv_text, articleCommentsBean.getContent());
        Glide.with( context ).load( urlString ).into( (ImageView) helper.getView( R.id.iv_avatar ) );
    }
}
