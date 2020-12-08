package com.caesar.rongcloudspeed.adapter;

import android.content.Context;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.data.UserOrder;
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
public class LessonLikeAdapter extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Context context;

    public LessonLikeAdapter(Context context, List data) {
        super(R.layout.item_lesson_video_careview,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsArticleBaseBean bean) {

        String titleString = bean.getPost_title();
        String priceString = bean.getPost_price();
        if(priceString.startsWith( "0.0" )){
            helper.setText( R.id.lessonMoney, "课程免费" );
        }else{
            helper.setText( R.id.lessonMoney, "￥"+priceString+"元" );
        }
        helper.setText( R.id.item_title, titleString );
    }
}
