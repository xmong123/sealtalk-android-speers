package com.caesar.rongcloudspeed.circle.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.circle.adapter.PayListAdapter;
import com.caesar.rongcloudspeed.data.PayDataItem;
import com.caesar.rongcloudspeed.data.result.UserPayListResult;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.yiw.circledemo.listener.SwipeListViewOnScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiw
 * @ClassName: FriendCircleActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午4:21:18
 */
public class PayCardCircleActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    protected static final String TAG = PayCardCircleActivity.class.getSimpleName();
    private ListView payCircleLv;
    private SwipeRefreshLayout payRefreshLayout;
    private PayListAdapter mAdapter;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylist);
        initView();
        loadData();
    }

    private void initView() {
        payRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.payRefreshLayout);
        payCircleLv = (ListView) findViewById(R.id.payCircleLv);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });
        payCircleLv.setOnScrollListener(new SwipeListViewOnScrollListener(payRefreshLayout));
        payRefreshLayout.setOnRefreshListener(this);
        payRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mAdapter = new PayListAdapter(this);
        payCircleLv.setAdapter(mAdapter);

    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                payRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }

    private void loadData() {
        List<PayDataItem> datas = new ArrayList<PayDataItem>();
        String userID= UserInfoUtils.getAppUserId(PayCardCircleActivity.this);
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getUserPayList(userID),
                new NetworkCallback<UserPayListResult>() {
                    @Override
                    public void onSuccess(UserPayListResult payListResult) {
                        mAdapter.setDatasource(payListResult.getUrl());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(PayCardCircleActivity.this, "您还没有交易记录，请稍后再试", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
