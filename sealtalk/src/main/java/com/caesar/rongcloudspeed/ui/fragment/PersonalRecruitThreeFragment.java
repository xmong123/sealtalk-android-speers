package com.caesar.rongcloudspeed.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.UserSeekHelperAdapter;
import com.caesar.rongcloudspeed.rxlife.RxFragment;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PersonalRecruitThreeFragment extends RxFragment implements OnRefreshListener {
    Unbinder unbinder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.userseek_recyclerview)
    RecyclerView userSeekRecyclerview;
    private String cid = "48";
    private String tag = "neq";
    private String uidString;
    private UserSeekHelperAdapter userSeekHelperAdapter;

    public static PersonalRecruitThreeFragment newInstance(int param1, String param2) {
        PersonalRecruitThreeFragment fragment = new PersonalRecruitThreeFragment();
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_seekhelper, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }



    private void initAnimationPostsAdapter() {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
