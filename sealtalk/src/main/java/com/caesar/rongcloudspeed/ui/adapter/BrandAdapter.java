package com.caesar.rongcloudspeed.ui.adapter;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AbsRecycleAdapter;
import com.caesar.rongcloudspeed.bean.AdminIndustryBean;

/**
 * Created by lannister on 2017/11/3.
 */

public class BrandAdapter extends AbsRecycleAdapter<AdminIndustryBean> {
    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_size;
    }

    @Override
    public void convert(AbsRecycleAdapter.VH holder, AdminIndustryBean data, int position) {
        holder.setText(R.id.brandName,data.getName());
        holder.setImageResid(R.id.selectedIcon, data.isFlag() ? R.mipmap.checkbox_checked : R.mipmap.checkbox_uncheck);
    }
}
