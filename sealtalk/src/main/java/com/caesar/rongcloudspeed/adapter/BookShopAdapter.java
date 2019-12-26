package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.GoodsDetailBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class BookShopAdapter extends BaseQuickAdapter<GoodsDetailBean, BaseViewHolder> {
    private Context context;

    public BookShopAdapter(Context context, List data) {
        super(R.layout.item_book_store_view,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsDetailBean bean) {
        String shop_price=bean.getShop_price();
        String goods_img=bean.getGoods_img();
        String priceString = "¥ " + shop_price + "元";
        Spannable sp = new SpannableString(priceString);
        if (!goods_img.startsWith( "http://" )) {
            goods_img = Constant.THINKCMF_PATH + goods_img;
        }
        if(goods_img!=null&&goods_img.length()>32){
            Glide.with( context ).load( goods_img ).into( (ImageView) helper.getView( R.id.item_smeta ) );
        }else{
            helper.setImageResource( R.id.item_smeta, R.drawable.votebook );
        }
        sp.setSpan(new AbsoluteSizeSpan(12, true), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(16, true), 2, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(12, true), 4, shop_price.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        helper.setText( R.id.item_price, sp );

    }
}
