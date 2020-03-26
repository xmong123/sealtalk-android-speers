package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
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
public class UserAlbumVideoAdapter extends BaseQuickAdapter<PostsSeekBaseBean, BaseViewHolder> {
    private Context context;

    public UserAlbumVideoAdapter(Context context, List data) {
        super(R.layout.personal_album_video_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsSeekBaseBean bean) {
        String thumbString = bean.getThumb_video();
        JCVideoPlayerStandard jcVideoPlayerStandard = helper.getView(R.id.album_item_video);
        jcVideoPlayerStandard.setUp(
                thumbString, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                bean.getPost_title());
        Glide.with(context).load(thumbString + "?vframe/jpg/offset/1").into(jcVideoPlayerStandard.thumbImageView);
        helper.setText(R.id.album_item_title, bean.getPost_excerpt());
        helper.setText(R.id.album_item_time, bean.getPost_date());
    }


}
