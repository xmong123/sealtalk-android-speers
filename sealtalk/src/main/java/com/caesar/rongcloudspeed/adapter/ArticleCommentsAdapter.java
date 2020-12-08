package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.data.CommentsItemData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class ArticleCommentsAdapter extends BaseQuickAdapter<CommentsItemData, BaseViewHolder> {
    private Context context;

    public ArticleCommentsAdapter(List<CommentsItemData> dataList) {
        super(R.layout.fragment_comments_recyclerview_item, dataList);
    }

    public ArticleCommentsAdapter(Context context, List<CommentsItemData> dataList) {
        super(R.layout.fragment_comments_recyclerview_item, dataList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentsItemData commentsItemData) {
        helper.setText(R.id.comment_nicename, commentsItemData.getUser_nicename());
        helper.setText(R.id.comment_content, commentsItemData.getContent());
        helper.setText(R.id.comment_time, commentsItemData.getCreatetime());
        String avatarString=commentsItemData.getAvatar();
        if (avatarString != null && !avatarString.startsWith("http")) {
            avatarString = Constant.THINKCMF_PATH + avatarString;
        }
        Glide.with(context).load(avatarString).into((ImageView) helper.getView(R.id.comment_avatar));
    }


}
