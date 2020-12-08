package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.RecommendListAdapter;
import com.caesar.rongcloudspeed.bean.AppPeopleBaseBean;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

public class HomeMorePeopleListActivity extends TitleBaseActivity implements OnRefreshListener {
    private RecyclerView peopleMoreRecyclerView;
    private RecommendListAdapter recommendListAdapter;
    private List<AppPeopleBaseBean.PeopleDataBean> peopleMoreArray = new ArrayList<AppPeopleBaseBean.PeopleDataBean>();
    private String uidString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more_lesson);
        uidString = UserInfoUtils.getAppUserId(this);
        initView();
        initViewModel();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        getTitleBar().setTitle("推荐好友");
        peopleMoreRecyclerView = findViewById(R.id.lessonMoreRecyclerView);
        recommendListAdapter = new RecommendListAdapter(this, peopleMoreArray);
        recommendListAdapter.openLoadAnimation();
        recommendListAdapter.setNotDoAnimationCount(4);
        recommendListAdapter.setOnItemClickListener((adapter, view, position) -> {
            AppPeopleBaseBean.PeopleDataBean peopleDataBean = peopleMoreArray.get(position);
            RongIM.getInstance().startPrivateChat(this, peopleDataBean.getRongid(), peopleDataBean.getUser_login());
        });
        peopleMoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        peopleMoreRecyclerView.setAdapter(recommendListAdapter);
    }


    /**
     * 初始话Viewmodel
     */
    private void initViewModel() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getAppRecommendMan(uidString),
                new NetworkCallback<AppPeopleBaseBean>() {
                    @Override
                    public void onSuccess(AppPeopleBaseBean peopleBaseBean) {
                        peopleMoreArray = peopleBaseBean.getReferer();
                        recommendListAdapter.setNewData(peopleMoreArray);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(HomeMorePeopleListActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        initViewModel();
    }
}
