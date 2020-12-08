package com.caesar.rongcloudspeed.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.PersonnelRecruitAdapter;
import com.caesar.rongcloudspeed.adapter.RecruitItemAdapter;
import com.caesar.rongcloudspeed.bean.PersonnelRecruitsBean;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.easypop.CityPopup;
import com.caesar.rongcloudspeed.easypop.PersonnelSalaryPopup;
import com.caesar.rongcloudspeed.implement.SelectSecondItemListener;
import com.caesar.rongcloudspeed.implement.SelectTabCityListener;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.rxlife.RxFragment;
import com.caesar.rongcloudspeed.ui.activity.RecruitPostDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.classic.common.MultipleStatusView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.ProCityBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RecruitsFragment extends RxFragment implements OnLoadmoreListener, OnRefreshListener, View.OnClickListener, SelectSecondItemListener, SelectTabCityListener {
    @BindView(R.id.recruits_recyclerview)
    RecyclerView recruits_recyclerview;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.appBarLayout_01)
    LinearLayout appBarLayout_01;
    @BindView(R.id.filter_text_01)
    TextView filter_text_01;
    @BindView(R.id.filter_text_02)
    TextView filter_text_02;
    @BindView(R.id.filter_text_03)
    TextView filter_text_03;
    Unbinder unbinder;
    private View notDataView;
    private View errorView;

    private String[] salaryItems = new String[]{"全部", "2000～3000", "3000～4000", "4000～5000", "5000～8000", "8000以上", "面议"};
    private String[] gradeItems = new String[]{"全部", "大专院校", "全日制本科", "硕士研究生", "MBA", "博士及以上", "保密"};
    private RecruitItemAdapter recruitItemAdapter;

    private List<RecruitJobBean> recruitJobBeanList = new ArrayList<RecruitJobBean>();

    private PersonnelSalaryPopup mPersonnelSalaryPopup;
    private CityPopup mCityPopup;

    private void initGiftPop() {
        mPersonnelSalaryPopup = PersonnelSalaryPopup.create(this)
                .setContext(getActivity())
                .apply();
    }

    private void initCityPop() {
        mCityPopup = CityPopup.create(getActivity(), this)
                .setContext(getActivity())
                .apply();
    }

    @SuppressLint("WrongConstant")
    private void initanimationAdapter() {
        recruitItemAdapter = new RecruitItemAdapter(getActivity(), recruitJobBeanList);
        recruitItemAdapter.openLoadAnimation();
        recruitItemAdapter.setNotDoAnimationCount(3);
        recruits_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recruitItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecruitJobBean recruitJobBean = recruitJobBeanList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("recruitJobBean", recruitJobBean);
                ActivityUtils.startActivity(bundle, getActivity(), RecruitPostDetailActivity.class);
            }
        });
        recruits_recyclerview.setAdapter(recruitItemAdapter);
        notDataView = getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) recruits_recyclerview.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.custom_error_view, (ViewGroup) recruits_recyclerview.getParent(), false);
    }

    private void showGiftPop(View view, String[] newString) {
        mPersonnelSalaryPopup.setNewData(view, newString);
    }

    private void showCityPop(View view, List<ProCityBean> proCityBeans, int position) {
        mCityPopup.setNewData(view, proCityBeans, position, 0);
    }

    /**
     * 默认是已付款
     */
    private int menuPosition = 0;
    private String salaryString = "0";
    private String gradeString = "0";
    private String region = "100000";
    private DBManager dbManager;
    private List<ProCityBean> proCityBeanList = new ArrayList<>();
    private int sposition = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recruits_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.autoRefresh();
        initGiftPop();
        initCityPop();
        initanimationAdapter();
        dbManager = new DBManager(getActivity());
        proCityBeanList = dbManager.getAllProCities();
    }

    private static final String TAG = "RecruitsFragment";

    private void loadQueryData() {
        RetrofitManager.create().getRecruitPostList(salaryString, gradeString, region)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<RecruitJobBaseBean>bindToLifecycle())
                .subscribe(new CommonObserver<RecruitJobBaseBean>(refreshLayout) {
                    @Override
                    public void onSuccess(RecruitJobBaseBean recruitJobBaseBean) {
                        recruitJobBeanList = new ArrayList<RecruitJobBean>();
                        if (recruitJobBaseBean.getCode() == Constant.CODE_SUCC) {
                            Log.d(TAG, "RecruitsFragmentSUCC");
                            if (recruitJobBaseBean.getReferer().size() > 0) {
                                recruitJobBeanList = recruitJobBaseBean.getReferer();
                                recruitItemAdapter.setNewData(recruitJobBeanList);
                            } else {
                                recruitItemAdapter.setNewData(recruitJobBeanList);
                                recruitItemAdapter.setEmptyView(notDataView);
                            }
                        } else {
                            recruitItemAdapter.setNewData(recruitJobBeanList);
                            recruitItemAdapter.setEmptyView(notDataView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        recruitItemAdapter.setNewData(null);
                        recruitItemAdapter.setEmptyView(errorView);
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        loadQueryData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        menuPosition = 0;
        salaryString = "0";
        gradeString = "0";
        region = "100000";
        filter_text_01.setText("薪资");
        filter_text_02.setText("学历");
        loadQueryData();
    }

    @Override
    @OnClick({R.id.filter_layout_01, R.id.filter_layout_02, R.id.filter_layout_03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_layout_01:
                menuPosition = 0;
                showGiftPop(view, salaryItems);
                break;
            case R.id.filter_layout_02:
                menuPosition = 1;
                showGiftPop(view, gradeItems);
                break;
            case R.id.filter_layout_03:
                showCityPop(view, proCityBeanList, sposition);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSelectTabCityListener(String cityCode, int position) {
        region = cityCode;
        sposition = position;
        loadQueryData();
    }

    @Override
    public void onSelectSecondItemListener(int position) {
        switch (menuPosition) {
            case 0:
                salaryString = String.valueOf(position);
                filter_text_01.setText(salaryItems[position]);
                break;
            case 1:
                gradeString = String.valueOf(position);
                filter_text_02.setText(gradeItems[position]);
                break;
            default:
                break;
        }
        Log.d(TAG, "onSelectTabItemListener");
        loadQueryData();
    }
}
