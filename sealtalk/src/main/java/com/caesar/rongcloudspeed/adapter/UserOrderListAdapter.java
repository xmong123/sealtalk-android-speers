package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.graphics.Color;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.GoodsOrderBean;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class UserOrderListAdapter extends BaseQuickAdapter<GoodsOrderBean, BaseViewHolder> {
    private Context context;

    public UserOrderListAdapter(Context context, List data) {
        super(R.layout.personal_order_action_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsOrderBean bean) {
        String orderType = bean.getOrder_type();
        if (orderType.equals("2")) {
            helper.setText(R.id.user_order_item_tag, "同 行\n课 程");
            helper.setTextColor(R.id.user_order_item_tag, Color.parseColor("#D9E7F7"));
            helper.setBackgroundRes(R.id.user_order_item_tag, R.drawable.btn_shape_normal);
        } else if (orderType.equals("3")) {
            helper.setText(R.id.user_order_item_tag, "同 行\n求 助");
            helper.setTextColor(R.id.user_order_item_tag, Color.parseColor("#000000"));
            helper.setBackgroundRes(R.id.user_order_item_tag, R.drawable.btn_shape_normal);
        } else if (orderType.equals("4")) {
            helper.setText(R.id.user_order_item_tag, "同 行\n广 告");
            helper.setTextColor(R.id.user_order_item_tag, Color.parseColor("#f15353"));
            helper.setBackgroundRes(R.id.user_order_item_tag, R.drawable.btn_shape_normal);
        } else if (orderType.equals("6")) {
            helper.setText(R.id.user_order_item_tag, "同 行\n会 员");
            helper.setTextColor(R.id.user_order_item_tag, Color.parseColor("#FFE384"));
            helper.setBackgroundRes(R.id.user_order_item_tag, R.drawable.btn_shape_black);
        }
        helper.setText(R.id.user_order_item_title, bean.getUser_note());
        helper.setText(R.id.user_order_item_price, "¥" + bean.getOrder_amount());
        helper.setText(R.id.user_order_item_sn, "订单编号:" + bean.getOrder_sn());
    }
}
