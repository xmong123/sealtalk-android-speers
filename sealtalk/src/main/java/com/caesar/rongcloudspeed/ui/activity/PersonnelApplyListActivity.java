package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.PersonnelRecruitAdapter;
import com.caesar.rongcloudspeed.adapter.RecruitApplyHorizontalAdapter;
import com.caesar.rongcloudspeed.adapter.RecruitItemAdapter;
import com.caesar.rongcloudspeed.bean.PersonnelRecruitsBean;
import com.caesar.rongcloudspeed.bean.RecruitApplyBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitApplyBean;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
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

public class PersonnelApplyListActivity extends MultiStatusActivity implements OnRefreshListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multiple_status_view;
    @BindView(R.id.personnel_apply_recyclerView)
    RecyclerView applyRecyclerview;
    private String titleString = "求职简历";
    private static final String TAG = "PersonnelApplyListActivity";
    private String uidString;
    private RecruitApplyHorizontalAdapter personnelRecruitAdapter;
    private List<RecruitApplyBean> recruitApplyList = new ArrayList<RecruitApplyBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        initTitleBarView(titlebar, titleString);
        personnelRecruitAdapter = new RecruitApplyHorizontalAdapter(this, recruitApplyList);
        personnelRecruitAdapter.openLoadAnimation();
        personnelRecruitAdapter.setNotDoAnimationCount(3);
        applyRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        personnelRecruitAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        applyRecyclerview.setAdapter(personnelRecruitAdapter);
        refreshLayout.setOnRefreshListener(this);
        loadApplyListData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personnel_apply_list;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadApplyListData();
    }

    private void loadApplyListData() {
        RetrofitManager.create().getApplyRecruitList(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<RecruitApplyBaseBean>bindToLifecycle())
                .subscribe(new CommonObserver<RecruitApplyBaseBean>(refreshLayout) {
                    @Override
                    public void onSuccess(RecruitApplyBaseBean recruitApplyBaseBean) {

                        if (recruitApplyBaseBean.getCode() == Constant.CODE_SUCC) {
                            recruitApplyList = recruitApplyBaseBean.getReferer();
                            personnelRecruitAdapter.setNewData(recruitApplyList);
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
