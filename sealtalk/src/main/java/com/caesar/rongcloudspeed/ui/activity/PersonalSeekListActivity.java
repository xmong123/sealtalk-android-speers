package com.caesar.rongcloudspeed.ui.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.BaseShopActivity;
import com.caesar.rongcloudspeed.ui.fragment.PersonalSeekHelperFragment;
import com.caesar.rongcloudspeed.view.TranspanentTitleBar;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;


public class PersonalSeekListActivity extends BaseShopActivity implements View.OnClickListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.titleBar)
    TranspanentTitleBar titleBar;
    private Fragment seekHelperFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_seek_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //tab的字体选择器,默认黑色,选择时红色
        int colorprimary = getResources().getColor(R.color.home_title_color);
        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorDark), colorprimary);
        //tab的下划线颜色,默认是粉红色
        tabLayout.setSelectedTabIndicatorColor(colorprimary);
        tabLayout.setTabMode(MODE_FIXED);
        titleBar.getBack().setOnClickListener(this);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(position==0){
                    seekHelperFragment= PersonalSeekHelperFragment.newInstance(position,"neq");
                }else{
                    seekHelperFragment= PersonalSeekHelperFragment.newInstance(position,"eq");
                }
                return seekHelperFragment;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "悬 赏 求 助";
                } else {
                    return "我 的 求 助";
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.post(() -> setIndicator(tabLayout, 60, 60));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
