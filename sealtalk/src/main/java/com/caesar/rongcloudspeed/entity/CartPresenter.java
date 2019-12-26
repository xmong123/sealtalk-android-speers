package com.caesar.rongcloudspeed.entity;

import android.view.View;
import android.widget.Toast;

import com.caesar.rongcloudspeed.bean.GoodsDetailBean;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

/**
 * Created by luoxiongwen on 16/10/24.
 */

public class CartPresenter {
    public void buyTicket(View view, GoodsDetailBean bean) {
        String userid= UserInfoUtils.getAppUserId(view.getContext());
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().ajaxAddCart(userid,bean.getGoods_id(),"1"),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        Toast.makeText(view.getContext(), "成功加入购物车: " + bean.getGoods_name(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(view.getContext(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                });


    }
}
