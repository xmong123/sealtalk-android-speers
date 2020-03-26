package com.caesar.rongcloudspeed.ui.fragment;

import android.annotation.SuppressLint;
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
import com.caesar.rongcloudspeed.adapter.LessonOrderAdapter;
import com.caesar.rongcloudspeed.bean.UserLessonOrderListBean;
import com.caesar.rongcloudspeed.data.UserOrder;
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


public class PersonalLessonesFragment extends RxFragment implements OnRefreshListener {
    Unbinder unbinder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.personal_common_recyclerview)
    RecyclerView personalCommonRecyclerview;
    private String tag = "1";
    private String uidString;
    private LessonOrderAdapter lessonOrderAdapter;
    private List<UserOrder> personalOrderList = new ArrayList<>();
    private View notDataView;
    private View errorView;

    public static PersonalLessonesFragment newInstance(int param1, String param2) {
        PersonalLessonesFragment fragment = new PersonalLessonesFragment();
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
        LogUtils.e("PersonalLessonesFragment");
        initPersonalLessonesAdapter();
        loadPersonalLessonesData();
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
        View view = inflater.inflate(R.layout.fragment_personal_common_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void loadPersonalLessonesData() {
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
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initPersonalLessonesAdapter() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);

        personalCommonRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        lessonOrderAdapter = new LessonOrderAdapter(getActivity(), personalOrderList);

        lessonOrderAdapter.setEmptyView(R.layout.custom_loading_view, (ViewGroup) personalCommonRecyclerview.getParent());

        personalCommonRecyclerview.setAdapter(lessonOrderAdapter);

        notDataView = getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) personalCommonRecyclerview.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.custom_error_view, (ViewGroup) personalCommonRecyclerview.getParent(), false);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadPersonalLessonesData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
