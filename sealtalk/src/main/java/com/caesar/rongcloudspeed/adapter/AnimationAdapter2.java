package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.util.ToastUtils;
import com.caesar.rongcloudspeed.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class AnimationAdapter2 extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Context context;
    private String type;
    String user_type = SPUtils.getInstance().getString( "user_type", "0" );

    public AnimationAdapter2(Context context, List data, String type) {
        super( R.layout.fragment_thinkcmf_recyclerview_item2 , data );
        this.context = context;
        this.type = type;
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
        if(type.equals( "3" )){
            Glide.with( context ).load( thumbString ).into( (ImageView) helper.getView( R.id.smetaImg ) );
        }
        String titleString = bean.getPost_title();
        if(titleString.length()>32){
            titleString = titleString.substring( 0, 32 );
        }
        helper.setText( R.id.tweetName1, titleString );
        String dataString = bean.getPost_date().substring( 0, 10 );
        helper.setText( R.id.tweetText1, bean.getPost_area() + " | " + dataString );
        String priceString = bean.getPost_price();
        if (priceString.startsWith( "0" )) {
            priceString = "面议";
        }
        helper.setText( R.id.tweetText2, priceString );
        helper.setText( R.id.tweetWatch, bean.getPost_hits() + "人浏览" );
        helper.setOnClickListener( R.id.tweetText3, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_type.equals("2")){
                    onShowVIPDialog(context,R.mipmap.ic_launcher);
                }else{
                    Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse( "tel:" + bean.getPost_mobile() ) );
                    intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity( intent );
                }
            }
        } );
    }
}
