package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
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
import com.caesar.rongcloudspeed.callback.OnItemClickListener;
import com.caesar.rongcloudspeed.rxlife.RxFragment;
import com.caesar.rongcloudspeed.ui.activity.AccountRecruitEduActivity;
import com.caesar.rongcloudspeed.ui.activity.AccountRecruitJobActivity;
import com.caesar.rongcloudspeed.ui.activity.AccountRecruitMessageActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonnelApplyListActivity;
import com.caesar.rongcloudspeed.ui.activity.PostRecruitListActivity;
import com.caesar.rongcloudspeed.ui.activity.RecruitCompanyMessageActivity;
import com.caesar.rongcloudspeed.ui.activity.RecruitPostMessageActivity;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class PersonalRecruitFourFragment extends RxFragment implements View.OnClickListener {
    Unbinder unbinder;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.e("PersonalRecruitFourFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_recruit_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    @OnClick({R.id.recruit_text1, R.id.recruit_text2, R.id.recruit_text3,
            R.id.recruit_text4})
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), RecruitCompanyMessageActivity.class);
        switch (view.getId()) {
            case R.id.recruit_text1:
                startActivity(intent);
                break;
            case R.id.recruit_text2:
                intent = new Intent(getActivity(), RecruitPostMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.recruit_text3:

                intent = new Intent(getActivity(), PostRecruitListActivity.class);
                startActivity(intent);
                break;
            case R.id.recruit_text4:
                intent = new Intent(getActivity(), PersonnelApplyListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
