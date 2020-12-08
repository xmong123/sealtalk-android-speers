package com.caesar.rongcloudspeed.adapter;

import android.content.Context;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PersonalPayListData;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
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
public class AnimationPayAdapter extends BaseQuickAdapter<PersonalPayListData.PayItem, BaseViewHolder> {
    private Context context;

    public AnimationPayAdapter(List data) {
        super(R.layout.fragment_thinkcmf_recyclerview_payitem,  data);
    }

    public AnimationPayAdapter(Context context, List data) {
        super(R.layout.fragment_thinkcmf_recyclerview_payitem,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalPayListData.PayItem payItem) {
        helper.setText(R.id.pay_title, payItem.getPayforname());
        helper.setText(R.id.pay_price, "¥"+payItem.getPayamount());
        helper.setText(R.id.pay_submit, payItem.getPaytoname());
        helper.setText(R.id.pay_time, payItem.getPaydatatime());
    }
}
