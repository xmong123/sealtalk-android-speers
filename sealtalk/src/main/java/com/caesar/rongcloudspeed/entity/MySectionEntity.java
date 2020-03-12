package com.caesar.rongcloudspeed.entity;

import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MySectionEntity extends SectionEntity<PostsArticleBaseBean> {
    private boolean isMore;
    public MySectionEntity(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public MySectionEntity(PostsArticleBaseBean baseBean) {
        super(baseBean);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}
