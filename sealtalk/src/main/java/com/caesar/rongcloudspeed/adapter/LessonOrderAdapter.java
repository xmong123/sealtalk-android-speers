package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.GoodsOrderBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.data.UserOrder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class LessonOrderAdapter extends BaseQuickAdapter<UserOrder, BaseViewHolder> {
    private Context context;

    public LessonOrderAdapter(Context context, List data) {
        super(R.layout.item_lesson_video_view,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, UserOrder bean) {

        String titleString = bean.getGoods_name();
        String priceString = bean.getGoods_price();
        if(priceString.startsWith( "0.0" )){
            helper.setText( R.id.lessonMoney, "课程免费" );
        }else{
            helper.setText( R.id.lessonMoney, "￥"+priceString+"元" );
        }
        helper.setText( R.id.item_title, titleString );
    }
}
