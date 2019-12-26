package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AbsRecycleAdapter;
import com.caesar.rongcloudspeed.adapter.AdminIndustryAdapter;
import com.caesar.rongcloudspeed.bean.AdminInBean;
import com.caesar.rongcloudspeed.bean.AdminIndustryBean;
import com.caesar.rongcloudspeed.listener.RegisterNextStepListener;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.adapter.BrandAdapter;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class AdminIndustryFragment extends BaseFragment implements OnRefreshListener {
    private RecyclerView adminIndustryRecyclerView;
    private BrandAdapter adapter;
    private List<AdminIndustryBean> adminIndustryArray = new ArrayList<AdminIndustryBean>();
    private SmartRefreshLayout refreshLayout;
    private static final String TAG = "AdminIndustryFragment";
    private RegisterNextStepListener listener;

    @Override
    protected int getLayoutResId() {
        return R.layout.admin_fragment_industry;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        findView(R.id.nextIndustryStep, true);
        refreshLayout = getActivity().findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        adminIndustryRecyclerView = getActivity().findViewById(R.id.adminindustry_recyclerView);
        adminIndustryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adminIndustryRecyclerView.setHasFixedSize(true);
        //设置adapter的单选模式
        adapter = new BrandAdapter();
        adapter.setChoiceMode(AbsRecycleAdapter.CHOICE_MODE_MULTIPLE);
        loadAdminIndustryData();
    }

    private void loadAdminIndustryData() {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().AdminIndustryBeanDatas(),
                new NetworkCallback<AdminInBean>() {
                    @Override
                    public void onSuccess(AdminInBean adminInBean) {
                        adminIndustryArray = adminInBean.getReferer();
                        adapter.setData(adminIndustryArray);
                        adminIndustryRecyclerView.setAdapter(adapter);
                        refreshLayout.finishRefresh(true);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d(TAG, String.valueOf(t));
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setOnNextStepListener(RegisterNextStepListener listener) {
        this.listener = listener;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadAdminIndustryData();
    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.nextIndustryStep:
                SparseBooleanArray mCheckStates=adapter.getCheckedItemPositions();
                if(mCheckStates.size()>0){
                    StringBuffer stringBuffer = new StringBuffer();
                    for(int i=0;i<mCheckStates.size();i++){
                        int key = mCheckStates.keyAt(i);
                        String industryID=adminIndustryArray.get(key).getId();
                        stringBuffer.append(industryID+",");
                    }
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    String industryString = stringBuffer.toString();
                    if(listener!=null){
                        listener.onNextStep(industryString);
                    }
                }else{
                    Toast.makeText(getActivity(), "请选择相关行业", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
