package com.caesar.rongcloudspeed.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.constants.Constant;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class AnimationAdapter3 extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Context context;
    private String typeString="4";

    public AnimationAdapter3(Context context, List data) {
        super(R.layout.item_img_text_view1,  data);
        this.context=context;
    }

    public AnimationAdapter3(Context context, List data, String type) {
        super(R.layout.item_img_text_view1,  data);
        this.context=context;
        this.typeString=type;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsArticleBaseBean bean) {
        String thumbString = null;
        try {
            JSONObject jsonSmeta = new JSONObject( bean.getSmeta() );
            thumbString = jsonSmeta.getString( "thumb" );
            if (!thumbString.startsWith( "http://" )) {
                thumbString = Constant.THINKCMF_PATH + jsonSmeta.get( "thumb" );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(thumbString!=null&&thumbString.length()>12){
            if(this.typeString.equals("4")){
                Glide.with( context ).load( thumbString+"?vframe/jpg/offset/1" ).into( (ImageView) helper.getView( R.id.item_smeta ) );
            }else{
                Glide.with( context ).load( thumbString ).into( (ImageView) helper.getView( R.id.item_smeta ) );
            }
        }else{
            helper.setImageResource( R.id.item_smeta, R.drawable.votebase );
        }
        String titleString = bean.getPost_title();
        if(this.typeString.equals("4")){
            titleString = titleString+"\n  投票数："+bean.getPost_like()+"票";
        }
        helper.setText( R.id.item_title, titleString );
    }
}
