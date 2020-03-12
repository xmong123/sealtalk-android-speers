package com.caesar.rongcloudspeed.entity;

import com.caesar.rongcloudspeed.bean.PersonalPhotoBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AlbumSectionEntity extends SectionEntity<PersonalPhotoBean> {
    private boolean isMore;
    public AlbumSectionEntity(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public AlbumSectionEntity(PersonalPhotoBean baseBean) {
        super(baseBean);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}
