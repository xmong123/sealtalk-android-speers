package com.caesar.rongcloudspeed.ui.adapter.viewholders;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.db.model.FriendDetailInfo;
import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.ui.adapter.models.SearchFriendModel;
import com.caesar.rongcloudspeed.ui.interfaces.OnContactItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnLessonItemClickListener;
import com.caesar.rongcloudspeed.utils.CharacterParser;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;

public class SearchLessonViewHolder extends BaseViewHolder<PostsArticleBaseBean> {
    private TextView tvNickName;
    private TextView tvDisplayName;
    private ImageView portrait;
    private View llDescription;
    private OnLessonItemClickListener listener;
    private PostsArticleBaseBean friendShipInfo;

    public SearchLessonViewHolder(@NonNull View itemView, OnLessonItemClickListener l) {
        super(itemView);
        this.listener = l;
        portrait = itemView.findViewById(R.id.iv_portrait);
        tvDisplayName = itemView.findViewById(R.id.tv_name);
        tvNickName = itemView.findViewById(R.id.tv_detail);
        llDescription = itemView.findViewById(R.id.ll_description);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnLessonItemClicked(friendShipInfo);
                }
            }
        });
    }

    @Override
    public void update(PostsArticleBaseBean postsArticleBaseBean) {



    }


}
