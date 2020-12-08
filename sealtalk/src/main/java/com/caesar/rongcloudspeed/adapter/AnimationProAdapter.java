package com.caesar.rongcloudspeed.adapter;

import com.caesar.rongcloudspeed.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zaaach.citypicker.model.ProCityBean;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AnimationProAdapter extends BaseQuickAdapter<ProCityBean, BaseViewHolder> {

    public AnimationProAdapter(List<ProCityBean> dataList) {
        super(R.layout.fragment_pro_recyclerview_item, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProCityBean proCityBean) {
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
