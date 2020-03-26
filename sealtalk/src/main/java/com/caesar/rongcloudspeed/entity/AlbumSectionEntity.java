package com.caesar.rongcloudspeed.entity;

import com.caesar.rongcloudspeed.bean.PersonalPhotoBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AlbumSectionEntity extends SectionEntity<PersonalPhotoBean> {
    private boolean isMore;
    private List<String> imagesList;
    private int sectionNO;

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public int getSectionNO() {
        return sectionNO;
    }

    public void setSectionNO(int sectionNO) {
        this.sectionNO = sectionNO;
    }

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
