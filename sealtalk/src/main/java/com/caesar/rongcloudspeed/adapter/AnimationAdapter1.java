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
public class AnimationAdapter1 extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Context context;

    public AnimationAdapter1(Context context, List data) {
        super(R.layout.fragment_thinkcmf_recyclerview_item1,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsArticleBaseBean bean) {
        String titleString=bean.getPost_title();
        if(titleString.length()>24){
            titleString = titleString.substring(0, 24);
        }
        helper.setText(R.id.animation1_title, titleString);
        helper.setText(R.id.animation1_text, bean.getPost_date());
        if(bean.getTid()!=null){
            helper.setGone(R.id.animation1_image,bean.getTid().equals("38"));
        }
    }
}
