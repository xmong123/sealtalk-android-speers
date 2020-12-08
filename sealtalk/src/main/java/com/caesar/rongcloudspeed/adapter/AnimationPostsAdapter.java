package com.caesar.rongcloudspeed.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;

import java.util.List;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class AnimationPostsAdapter extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Context context;

    public AnimationPostsAdapter(Context context, List data) {
        super(R.layout.seller_post_item, data );
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsArticleBaseBean bean) {
        helper.setText( R.id.post_text01, "出售：其他机械设备");
        helper.setText( R.id.post_text02, "批次号：TXS060221" + bean.getTid() );
        helper.setText( R.id.post_text03, "数量：" + bean.getStore_count()+"吨");
        helper.setText( R.id.post_text04, "价格：" + bean.getPost_price() +"元");
        helper.setText( R.id.post_text05, "提货方式：自提" );
        helper.setText( R.id.post_text06, "规格：" + bean.getPost_title() );
        helper.setText( R.id.post_text07,  bean.getPost_date() );
        if (bean.getSelected().equals("1")) {
            helper.setBackgroundRes(R.id.post_check_btn,R.drawable.icon_checkyes);
        } else {
            helper.setBackgroundRes(R.id.post_check_btn,R.drawable.icon_checkno);
        }

        helper.addOnClickListener(R.id.post_check_btn).addOnClickListener(R.id.post_del_btn);
    }
}
