package com.caesar.rongcloudspeed.adapter;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.CategoryBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AnimationComplexAdapter extends BaseQuickAdapter<CategoryBean.ChildrenBean, BaseViewHolder> {

    public AnimationComplexAdapter(List<CategoryBean.ChildrenBean> dataList) {
        super(R.layout.fragment_cate_recyclerview_item, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBean.ChildrenBean item) {
        helper.setText(R.id.cateTitle, item.getName());
    }


}
