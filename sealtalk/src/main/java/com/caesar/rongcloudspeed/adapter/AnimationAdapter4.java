package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.util.Utils;
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
public class AnimationAdapter4 extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Context context;
    private String type;

    public AnimationAdapter4(Context context, List data) {
        super( R.layout.fragment_thinkcmf_recyclerview_item4, data );
        this.context = context;
    }

    public AnimationAdapter4(Context context, List data, String type) {
        super( R.layout.fragment_thinkcmf_recyclerview_item4, data );
        this.context = context;
        this.type = type;
    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
//            ToastUtils.showShortToast( "事件触发了 landscapes and nedes" );
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor( Utils.getContext().getResources().getColor( R.color.clickspan_color ) );
            ds.setUnderlineText( true );
        }
    };

    @Override
    protected void convert(BaseViewHolder helper, PostsArticleBaseBean bean) {
        String avatar = bean.getPost_avatar();
        if (!avatar.startsWith( "http://" )) {
            avatar = Constant.THINKCMF_PATH + avatar;
        }
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
        Glide.with( context ).load( thumbString ).into( (ImageView) helper.getView( R.id.smetaImg ) );
        String titleString = bean.getPost_title();
        if (titleString.length() > 32) {
            titleString = titleString.substring( 0, 32 );
        }
        helper.setText( R.id.tweetName1, titleString );
        String dataString = bean.getPost_date().substring( 0, 10 );
        helper.setText( R.id.tweetText1, bean.getPost_area() + " | " + dataString );
        String contentString = "您的报价:"+bean.getContent();

        helper.setText( R.id.tweetText2, contentString );

    }
}
