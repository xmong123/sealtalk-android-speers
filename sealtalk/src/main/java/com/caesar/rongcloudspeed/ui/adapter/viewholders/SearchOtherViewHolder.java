package com.caesar.rongcloudspeed.ui.adapter.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.ui.adapter.models.SearchTitleModel;

public class SearchOtherViewHolder extends BaseViewHolder<PostsArticleBaseBean> {
    private TextView textView;

    public SearchOtherViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void update(PostsArticleBaseBean postsArticleBaseBean) {
        textView.setText(postsArticleBaseBean.getPost_title());
    }
}
