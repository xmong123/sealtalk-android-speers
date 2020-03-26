package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.LessonOrderAdapter;
import com.caesar.rongcloudspeed.adapter.UserOrderListAdapter;
import com.caesar.rongcloudspeed.bean.GoodsOrderBean;
import com.caesar.rongcloudspeed.bean.UserLessonOrderListBean;
import com.caesar.rongcloudspeed.bean.UserOrderListBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.data.UserOrder;
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
public class PersonalLessonListActivity extends MultiStatusActivity implements OnRefreshListener {
    private List<UserOrder> personalOrderList = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview_album_view)
    RecyclerView recyclerview_album_view;
    private LessonOrderAdapter lessonOrderAdapter;
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
        initTitleBarView(titlebar, "您的课程");
        uidString = UserInfoUtils.getAppUserId(this);
        initView();
        loadPersonalOrderLesson();
    }

    private void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);

        recyclerview_album_view.setLayoutManager(new LinearLayoutManager(this));

        lessonOrderAdapter = new LessonOrderAdapter(this, personalOrderList);

        lessonOrderAdapter.setEmptyView(R.layout.custom_loading_view, (ViewGroup) recyclerview_album_view.getParent());

        recyclerview_album_view.setAdapter(lessonOrderAdapter);

        notDataView = getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) recyclerview_album_view.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.custom_error_view, (ViewGroup) recyclerview_album_view.getParent(), false);
    }

    private void loadPersonalOrderLesson() {
        RetrofitManager.create().getUserOrderLesson(uidString,"2")
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<UserLessonOrderListBean>bindToLifecycle())
                .subscribe(new CommonObserver<UserLessonOrderListBean>(refreshLayout) {
                    @Override
                    public void onSuccess(UserLessonOrderListBean orderListBean) {
                        if (orderListBean.getCode() == CODE_SUCC) {
                            personalOrderList = orderListBean.getReferer();
                            if (personalOrderList.size() > 0) {
                                lessonOrderAdapter.setNewData(personalOrderList);
                            } else {
                                lessonOrderAdapter.setEmptyView(notDataView);
                            }
                        } else {
                            lessonOrderAdapter.setEmptyView(notDataView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        lessonOrderAdapter.setEmptyView(errorView);
                        Toast.makeText(PersonalLessonListActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
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
        loadPersonalOrderLesson();
    }


}
