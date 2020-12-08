package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.RecruitItemAdapter;
import com.caesar.rongcloudspeed.bean.RecruitJobBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.classic.common.MultipleStatusView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PostRecruitListActivity extends MultiStatusActivity implements OnRefreshListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multiple_status_view;
    @BindView(R.id.recruit_post_recyclerview)
    RecyclerView recruitsRecyclerview;
    private String titleString = "招聘启事";
    private static final String TAG = "PostRecruitListActivity";
    private String uidString;
    private RecruitItemAdapter recruitItemAdapter;
    private List<RecruitJobBean> recruitJobBeanList = new ArrayList<RecruitJobBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        initTitleBarView(titlebar, titleString);
        recruitItemAdapter = new RecruitItemAdapter(this, recruitJobBeanList);
        recruitItemAdapter.openLoadAnimation();
        recruitItemAdapter.setNotDoAnimationCount(3);
        recruitsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        recruitItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecruitJobBean recruitJobBean = recruitJobBeanList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("recruitJobBean", recruitJobBean);
                ActivityUtils.startActivity(bundle, PostRecruitListActivity.this, RecruitPostInformationActivity.class);
            }
        });
        recruitsRecyclerview.setAdapter(recruitItemAdapter);
        refreshLayout.setOnRefreshListener(this);
        loadRecruitListData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_recruit_list_layout;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadRecruitListData();
    }

    private void loadRecruitListData() {
        RetrofitManager.create().getUserRecruitList(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<RecruitJobBaseBean>bindToLifecycle())
                .subscribe(new CommonObserver<RecruitJobBaseBean>(refreshLayout) {
                    @Override
                    public void onSuccess(RecruitJobBaseBean recruitJobBaseBean) {

                        if (recruitJobBaseBean.getCode() == Constant.CODE_SUCC) {
                            recruitJobBeanList = recruitJobBaseBean.getReferer();
                            recruitItemAdapter.setNewData(recruitJobBeanList);
                        }else{
                            multiple_status_view.showEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        multiple_status_view.showError();
                    }
                });
    }
}
