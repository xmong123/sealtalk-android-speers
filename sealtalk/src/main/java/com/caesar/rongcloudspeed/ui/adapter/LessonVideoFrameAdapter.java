package com.caesar.rongcloudspeed.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
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
public class LessonVideoFrameAdapter extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Context context;

    public LessonVideoFrameAdapter(Context context, List data) {
        super(R.layout.item_lesson_video_view_frame,  data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsArticleBaseBean bean) {
        String thumbVideoString = bean.getThumb_video();
        if (!thumbVideoString.startsWith( "http://" )) {
            thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
        }
        if(thumbVideoString!=null&&thumbVideoString.length()>32){
            Glide.with( context ).load( thumbVideoString+"?vframe/jpg/offset/1" ).into( (ImageView) helper.getView( R.id.item_smeta ) );
        }else{
            helper.setImageResource( R.id.item_smeta, R.drawable.votebase );
        }
        String titleString = bean.getPost_title();

        helper.setText( R.id.item_title, titleString );
    }
}
