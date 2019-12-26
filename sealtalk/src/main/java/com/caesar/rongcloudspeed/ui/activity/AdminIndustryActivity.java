package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AdminIndustryAdapter;
import com.caesar.rongcloudspeed.adapter.AnimationProAdapter;
import com.caesar.rongcloudspeed.bean.AdminInBean;
import com.caesar.rongcloudspeed.bean.AdminIndustryBean;
import com.caesar.rongcloudspeed.bean.LessonCateBean;
import com.caesar.rongcloudspeed.bean.LessonCategoryBean;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.dialog.ClearCacheDialog;
import com.caesar.rongcloudspeed.ui.dialog.CommonDialog;
import com.caesar.rongcloudspeed.viewmodel.UserInfoViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 账号设置
 */
public class AdminIndustryActivity extends TitleBaseActivity implements View.OnClickListener {
    private RecyclerView adminIndustryRecyclerView;
    private AdminIndustryAdapter adminIndustryAdapter;
    private List<AdminIndustryBean> adminIndustryArray=new ArrayList<AdminIndustryBean>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_industry);
        initView();
    }

    private void initView() {
        getTitleBar().setTitle("行业分类");
        adminIndustryRecyclerView = findViewById(R.id.adminindustry_recyclerView);
        adminIndustryAdapter = new AdminIndustryAdapter(adminIndustryArray);
        adminIndustryRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adminIndustryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        adminIndustryRecyclerView.setAdapter(adminIndustryAdapter);
        loadAdminIndustryData();
    }

    private void loadAdminIndustryData() {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().AdminIndustryBeanDatas(),
                new NetworkCallback<AdminInBean>() {
                    @Override
                    public void onSuccess(AdminInBean adminInBean) {
                        adminIndustryArray=adminInBean.getReferer();
                        adminIndustryAdapter.setNewData(adminIndustryArray);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(AdminIndustryActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                //DO nothing
                break;

        }
    }


}
