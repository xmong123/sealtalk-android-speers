package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.GoodsDetailBean;
import com.caesar.rongcloudspeed.bean.MemberSpeerBean;
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
public class MemberSpeerAdapter extends BaseQuickAdapter<MemberSpeerBean, BaseViewHolder> {
    private Context context;

    public MemberSpeerAdapter(Context context, List data) {
        super(R.layout.shop_item_member,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberSpeerBean bean) {
        String member_title=bean.getMember_title();
        String member_price="¥ "+bean.getMember_price();
        Spannable sp = new SpannableString(member_price);
        sp.setSpan(new AbsoluteSizeSpan(18, true), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(24, true), 2, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(18, true), 5, member_price.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        helper.setText( R.id.memberTitle, member_title );
        helper.setText( R.id.member_price, sp );
        if(bean.isFlag()){
            helper.setBackgroundColor(R.id.memberLayout,0xfffaebd7);
        }else {
            helper.setBackgroundColor(R.id.memberLayout,0xffffffff);
        }
    }
}
