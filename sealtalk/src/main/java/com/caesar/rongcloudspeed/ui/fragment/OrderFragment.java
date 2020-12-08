package com.caesar.rongcloudspeed.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.rxlife.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

public class OrderFragment extends RxFragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private Fragment orderListsFragment;

    public OrderFragment() {
    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        registerOrderBoardcast();
//        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void refreshData(int position) {
        viewPager.setCurrentItem(position - 1);
        if (orderListsFragment != null) {
//            orderListsFragment.refreshData(position);
        }
    }

    private void initView() {
        //tab的字体选择器,默认黑色,选择时红色
        int colorprimary = getResources().getColor(R.color.colorAccent);
        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorDark), colorprimary);
        //tab的下划线颜色,默认是粉红色
        tabLayout.setSelectedTabIndicatorColor(colorprimary);
        tabLayout.setTabMode(MODE_FIXED);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(),0) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    orderListsFragment = new RecruitsFragment();
                } else if (position == 1) {
                    orderListsFragment = new PersonalRecruitTwoFragment();
                } else if (position == 2) {
                    orderListsFragment = new PersonnelFragment();
                } else {
                    orderListsFragment = new PersonalRecruitFourFragment();
                }
                return orderListsFragment;
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "找工作";
                } else if (position == 1) {
                    return "我的简历";
                } else if (position == 2) {
                    return "找人才";
                } else {
                    return "我的招聘";
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int currentPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // ViewPager.SCROLL_STATE_IDLE 标识的状态是当前页面完全展现，并且没有动画正在进行中，如果不
                // 是此状态下执行 setCurrentItem 方法回在首位替换的时候会出现跳动！
                if (state != ViewPager.SCROLL_STATE_IDLE) return;
//                ToastUtils.showShort(String.valueOf(currentPosition));
                // 当视图在第一个时，将页面号设置为图片的最后一张。
                if (currentPosition == 0) {

                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unRegisterOrderBroadcast();
//        EventBus.getDefault().unregister(this);
    }

    private BroadcastReceiver orderRecevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int orderItem=intent.getIntExtra("orderItem",0);
            viewPager.setCurrentItem(orderItem);
        }
    };

    private void registerOrderBoardcast() {
        IntentFilter intentFilter = new IntentFilter("com.caesar.action.order");
        getActivity().registerReceiver(orderRecevier, intentFilter);
    }

    private void unRegisterOrderBroadcast() {
        getActivity().unregisterReceiver(orderRecevier);
    }

}
