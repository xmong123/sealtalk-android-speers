package com.caesar.rongcloudspeed.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.UserAdvertPlayerAdapter;
import com.caesar.rongcloudspeed.adapter.UserOrderListAdapter;
import com.caesar.rongcloudspeed.bean.GoodsOrderBean;
import com.caesar.rongcloudspeed.bean.HomeSeekListBean;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.caesar.rongcloudspeed.bean.UserOrderListBean;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.rxlife.RxFragment;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;


public class PersonalOrdersFragment extends RxFragment implements OnRefreshListener {
    Unbinder unbinder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.userseek_recyclerview)
    RecyclerView userSeekRecyclerview;
    private String tag = "1";
    private String uidString;
    private UserOrderListAdapter orderListAdapter;
    private List<GoodsOrderBean> personalOrderList = new ArrayList<GoodsOrderBean>();
    private View notDataView;
    private View errorView;

    public static PersonalOrdersFragment newInstance(int param1, String param2) {
        PersonalOrdersFragment fragment = new PersonalOrdersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uidString = UserInfoUtils.getAppUserId(getActivity());
        LogUtils.e("loadAnimation1Fragment");
        initAnimationPostsAdapter();
        loadSeekData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = String.valueOf(getArguments().getInt(ARG_PARAM1));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_seekhelper, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void loadSeekData() {
        RetrofitManager.create().getUserOrderJson(uidString, tag)
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
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initAnimationPostsAdapter() {
        orderListAdapter = new UserOrderListAdapter(getActivity(), personalOrderList);
        userSeekRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderListAdapter.setEmptyView(R.layout.custom_loading_view, (ViewGroup) userSeekRecyclerview.getParent());
        userSeekRecyclerview.setAdapter(orderListAdapter);
        notDataView = getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) userSeekRecyclerview.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.custom_error_view, (ViewGroup) userSeekRecyclerview.getParent(), false);
        orderListAdapter.loadMoreEnd();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadSeekData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
