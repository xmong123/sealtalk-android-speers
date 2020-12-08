package com.caesar.rongcloudspeed.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.rxlife.RxFragment;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PersonalRecruitDetailFragment extends RxFragment implements OnRefreshListener {
    Unbinder unbinder;
    @BindView(R.id.recruit_base_supert)
    SuperTextView recruit_base_supert;
    @BindView(R.id.recruit_base_supert1)
    SuperTextView recruit_base_supert1;
    @BindView(R.id.recruit_base_supert2)
    SuperTextView recruit_base_supert2;
    @BindView(R.id.recruit_base_supert3)
    SuperTextView recruit_base_supert3;
    @BindView(R.id.recruit_base_supert4)
    SuperTextView recruit_base_supert4;
    @BindView(R.id.recruit_base_supert5)
    SuperTextView recruit_base_supert5;
    @BindView(R.id.recruit_base_supert6)
    SuperTextView recruit_base_supert6;
    @BindView(R.id.recruit_base_supert7)
    SuperTextView recruit_base_supert7;
    @BindView(R.id.recruit_base_supert8)
    SuperTextView recruit_base_supert8;
    private String uidString;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uidString = UserInfoUtils.getAppUserId(getActivity());
        LogUtils.e("PersonalRecruitTwoFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_recruit_basemessage, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
