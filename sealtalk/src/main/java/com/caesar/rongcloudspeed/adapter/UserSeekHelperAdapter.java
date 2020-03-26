package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
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
public class UserSeekHelperAdapter extends BaseQuickAdapter<PostsSeekBaseBean, BaseViewHolder> {
    private Context context;
    private String type;

    public UserSeekHelperAdapter(Context context, List data, String type) {
        super( R.layout.fragment_seekhelper_recyclerview_item , data );
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsSeekBaseBean bean) {
        List<String> photosArray = bean.getPhotos_array() ;
        if(photosArray!=null&&photosArray.size()>0){
            String photoString=photosArray.get(0);
            if (!photoString.startsWith( "http://" )) {
                photoString = Constant.THINKCMF_PATH + photoString;
            }
            Glide.with( context ).load( photoString ).into( (ImageView) helper.getView( R.id.seekHelperImg ) );
        }
        String titleString = "【求助】"+bean.getPost_title();
        if(type.equals("eq")){
            titleString = "【我的】"+bean.getPost_title();
        }
        Spannable sp = new SpannableString(titleString);
        ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#6091f7"));
        if(type.equals("eq")){
            foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#f15353"));
        }
        sp.setSpan(foregroundColorSpan,0,4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText( R.id.seekHelperName, sp );
        String dataString = bean.getPost_date();
        helper.setText( R.id.seekHelperText,  "全国-所有地区 | " + dataString );
        String priceString = bean.getPost_price();
        if (priceString.startsWith( "0.0" )) {
            priceString = "面议";
        }
        helper.setText( R.id.seekHelperSub, priceString );
        if(type.equals("eq")){
            helper.setTextColor(R.id.seekHelperSub,Color.parseColor("#f15353"));
        }
    }
}
