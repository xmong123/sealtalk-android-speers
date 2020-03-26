package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.UserOrderListAdapter;
import com.caesar.rongcloudspeed.bean.GoodsOrderBean;
import com.caesar.rongcloudspeed.bean.UserOrderListBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PersonalOrderListActivity extends MultiStatusActivity implements OnRefreshListener {
    private List<GoodsOrderBean> personalOrderList = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview_album_view)
    RecyclerView recyclerview_album_view;
    private UserOrderListAdapter orderListAdapter;
    private String uidString;
    private View notDataView;
    private View errorView;

    @Override
    public int getContentView() {
        return R.layout.activity_personal_album_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitleBarView(titlebar, "您的订单");
        uidString = UserInfoUtils.getAppUserId(this);
        initView();
        loadMPersonalOrderData();
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);

        recyclerview_album_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        orderListAdapter = new UserOrderListAdapter(this, personalOrderList);

        orderListAdapter.setEmptyView(R.layout.custom_loading_view, (ViewGroup) recyclerview_album_view.getParent());

        recyclerview_album_view.setAdapter(orderListAdapter);

        notDataView = getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) recyclerview_album_view.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.custom_error_view, (ViewGroup) recyclerview_album_view.getParent(), false);
    }

    private void loadMPersonalOrderData() {
        RetrofitManager.create().getUserOrderJson(uidString,"0")
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<UserOrderListBean>bindToLifecycle())
                .subscribe(new CommonObserver<UserOrderListBean>(refreshLayout) {
                    @Override
                    public void onSuccess(UserOrderListBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            personalOrderList = value.getReferer();
                            if (personalOrderList.size() > 0) {
                                orderListAdapter.setNewData(personalOrderList);
                            } else {
                                orderListAdapter.setEmptyView(notDataView);
                            }
                        } else {
                            orderListAdapter.setEmptyView(notDataView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        orderListAdapter.setEmptyView(errorView);
                        Toast.makeText(PersonalOrderListActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadMPersonalOrderData();
    }


}
