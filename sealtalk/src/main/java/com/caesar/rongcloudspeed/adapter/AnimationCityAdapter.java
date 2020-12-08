package com.caesar.rongcloudspeed.adapter;

import com.caesar.rongcloudspeed.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zaaach.citypicker.model.City;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AnimationCityAdapter extends BaseQuickAdapter<City, BaseViewHolder> {

    public AnimationCityAdapter(List<City> dataList) {
        super(R.layout.fragment_cate_recyclerview_item, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, City city) {
        helper.setText(R.id.cateTitle, city.getName());
    }


}
