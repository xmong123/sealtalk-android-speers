package com.caesar.rongcloudspeed.adapter;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.LessonCategoryBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AnimationProAdapter extends BaseQuickAdapter<LessonCategoryBean, BaseViewHolder> {

    public AnimationProAdapter(List<LessonCategoryBean> dataList) {
        super(R.layout.fragment_pro_recyclerview_item, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, LessonCategoryBean proCityBean) {
        helper.setText(R.id.proTitle, proCityBean.getName());
        if(proCityBean.isFlag()){
            helper.setBackgroundColor(R.id.pro_item_root, 0xFFFFFFFF);
            helper.setTextColor(R.id.proTitle, 0xff6091f7);
        }else {
            helper.setBackgroundColor(R.id.pro_item_root, 0xffe8e8e8);
            helper.setTextColor(R.id.proTitle, 0xff515151);
        }
    }


}
