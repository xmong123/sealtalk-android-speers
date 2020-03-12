package com.caesar.rongcloudspeed.adapter;

import android.content.Context;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.entity.MySectionEntity;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MessageSectionAdapter extends BaseSectionQuickAdapter<MySectionEntity, BaseViewHolder> {
    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public MessageSectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    public MessageSectionAdapter(Context context, int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
        this.context = context;
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final MySectionEntity item) {
        helper.setText(R.id.header, item.header);
        helper.setVisible(R.id.more, item.isMore());
        helper.addOnClickListener(R.id.more);
    }


    @Override
    protected void convert(BaseViewHolder helper, MySectionEntity item) {
        PostsArticleBaseBean baseBean = (PostsArticleBaseBean) item.t;
        helper.setText(R.id.extension_title, "\u3000\u3000\u3000" + baseBean.getPost_title());
        if (baseBean.getPost_type().equals("2")) {
            helper.setText(R.id.extension_tag,"课程");
            helper.setBackgroundColor(R.id.extension_tag, 0xffe54141);
        } else if (baseBean.getPost_type().equals("3")) {
            helper.setText(R.id.extension_tag,"求助");
            helper.setBackgroundColor(R.id.extension_tag, 0xffe54141);
        }

    }
}
