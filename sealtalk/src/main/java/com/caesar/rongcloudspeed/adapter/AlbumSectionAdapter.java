package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PersonalPhotoBean;
import com.caesar.rongcloudspeed.entity.AlbumSectionEntity;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AlbumSectionAdapter extends BaseSectionQuickAdapter<AlbumSectionEntity, BaseViewHolder> {
    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public AlbumSectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    public AlbumSectionAdapter(Context context, int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
        this.context = context;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final AlbumSectionEntity item) {
        helper.setText(R.id.album_header, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumSectionEntity item) {
        PersonalPhotoBean baseBean = item.t;
        helper.setText(R.id.album_photo_title, baseBean.getTitle());
        Glide.with(context).load(baseBean.getImage()).into((ImageView) helper.getView(R.id.album_photo_image));
    }
}
