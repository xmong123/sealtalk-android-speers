package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pili.pldroid.player.widget.PLVideoView;

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
public class UserAdvertPlayerAdapter extends BaseQuickAdapter<PostsSeekBaseBean, BaseViewHolder> {
    private Context context;
    private String type;
    private String userAvatarString;

    public UserAdvertPlayerAdapter(Context context, List data, String type) {
        super(type.equals("1") ? R.layout.advert_img_text_item : R.layout.advert_player_text_item, data);
        this.context = context;
        this.type = type;
        userAvatarString = UserInfoUtils.getAppUserUrl(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostsSeekBaseBean bean) {
        if (type.equals("1")) {
            List<String> photosArray = bean.getPhotos_array();
            if (photosArray != null && photosArray.size() > 0) {
                String photoString = photosArray.get(0);
                if (!photoString.startsWith("http://")) {
                    photoString = Constant.THINKCMF_PATH + photoString;
                }
                Glide.with(context).load(photoString).into((ImageView) helper.getView(R.id.advert_item_image));
            }
        } else {
            String thumbString = bean.getThumb_video();
            JCVideoPlayerStandard jcVideoPlayerStandard = helper.getView(R.id.advert_item_jcdplayer);
            jcVideoPlayerStandard.setUp(
                    thumbString, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    bean.getPost_title());
            Glide.with(context).load(thumbString + "?vframe/jpg/offset/1").into(jcVideoPlayerStandard.thumbImageView);
        }
        helper.setText(R.id.advert_item_title, bean.getPost_title());
        String contentString = bean.getPost_excerpt();
        if (TextUtils.isEmpty(contentString)) {
            helper.setGone(R.id.advert_item_message, false);
        } else {
            helper.setGone(R.id.advert_item_message, true);
            helper.setText(R.id.advert_item_message, bean.getPost_excerpt());
        }
        Glide.with(context).load(userAvatarString).into((ImageView) helper.getView(R.id.advert_item_header));
    }
}
