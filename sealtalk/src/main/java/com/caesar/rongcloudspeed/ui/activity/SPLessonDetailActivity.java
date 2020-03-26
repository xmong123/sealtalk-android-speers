package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.ui.fragment.SPSpeerLeftFragment;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

public class SPLessonDetailActivity extends MultiStatusActivity {

    @BindView(R.id.convenientBanner)
    ImageView convenientBanner;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.speer_btn)
    Button speerBtn;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private String lesson_id;
    private String thumbVideoString;
    private String lesson_name;
    private String lesson_price;
    private String lesson_smeta;
    private String uidString;
    private Fragment fragment;
    private Set<String> orderSet;
    private boolean isBuy = false;
    private String userType = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        lesson_id = getIntent().getExtras().getString("lesson_id");
        lesson_name = getIntent().getExtras().getString("lesson_name");
        lesson_price = getIntent().getExtras().getString("lesson_price");
        lesson_smeta = getIntent().getExtras().getString("lesson_smeta");
        thumbVideoString = getIntent().getExtras().getString("videoPath");
        initTitleBarView(titlebar, "课程详情");
        LinearLayout layout = (LinearLayout) findViewById(R.id.container);
        ImageButton button = new ImageButton(this);
        button.setImageResource(R.drawable.action_colloect_like);
        button.setBackgroundColor(0x00000000);
        button.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titlebar.setRightView(button);
        button.setOnClickListener(view -> {
            if(view.isSelected()){
                button.setSelected(false);
                button.setImageResource(R.drawable.product_unlike);
            }else{
                button.setSelected(true);
                button.setImageResource(R.drawable.product_like);
            }
        });
        initView();
        uidString = UserInfoUtils.getAppUserId(this);
        orderSet = UserInfoUtils.getAppUserLessones(this);
        String thumbString = null;
        try {
            JSONObject jsonSmeta = new JSONObject(lesson_smeta);
            thumbString = jsonSmeta.getString("thumb");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (thumbString != null && !thumbString.startsWith("http://")) {
            thumbString = Constant.THINKCMF_PATH + thumbString;
        }
        if (thumbString != null && thumbString.length() > 32) {
            Glide.with(this).load(thumbString).into(convenientBanner);
        }
        userType = UserInfoUtils.getUserType(this);
        if (userType.equals("6")) {
            speerBtn.setText("进入课程");
            isBuy = true;
        } else {
            if (lesson_price.startsWith("0.0")) {
                speerBtn.setText("进入课程");
                isBuy = true;
            } else if (orderSet != null && orderSet.size() > 0) {
                for (String str : orderSet) {
                    if (str.equals(lesson_id)) {
                        speerBtn.setText("进入课程");
                        isBuy = true;
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userType = UserInfoUtils.getUserType(this);
        if (userType.equals("6")) {
            speerBtn.setText("进入课程");
            isBuy = true;
        } else {
            if (lesson_price.startsWith("0.0")) {
                speerBtn.setText("进入课程");
                isBuy = true;
            } else if (orderSet != null && orderSet.size() > 0) {
                for (String str : orderSet) {
                    if (str.equals(lesson_id)) {
                        speerBtn.setText("进入课程");
                        isBuy = true;
                    }
                }
            }
        }
    }

    private void initView() {
        //tab的字体选择器,默认黑色,选择时红色
        int colorprimary = getResources().getColor(R.color.colorAccent);
        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorDark), colorprimary);
        //tab的下划线颜色,默认是粉红色
        tabLayout.setSelectedTabIndicatorColor(colorprimary);
        tabLayout.setTabMode(MODE_FIXED);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(), 0) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    fragment = new SPSpeerLeftFragment();
                } else {
                    fragment = new SPSpeerLeftFragment();
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "简介";
                } else {
                    return "课表";
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // ViewPager.SCROLL_STATE_IDLE 标识的状态是当前页面完全展现，并且没有动画正在进行中，如果不
                // 是此状态下执行 setCurrentItem 方法回在首位替换的时候会出现跳动！
                if (state != ViewPager.SCROLL_STATE_IDLE) return;
//                ToastUtils.showShort(String.valueOf(currentPosition));
                // 当视图在第一个时，将页面号设置为图片的最后一张。
            }
        });

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public int getContentView() {
        return R.layout.activity_shop_speer_detail;
    }

    @OnClick({R.id.convenientBanner, R.id.speer_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.convenientBanner:
//                if (TbsVideo.canUseTbsPlayer(this)) {
//                    TbsVideo.openVideo(this, thumbVideoString);
//                }
                break;
            case R.id.speer_btn:
                if (isBuy) {
                    Intent lessonsIntent = new Intent(SPLessonDetailActivity.this, SPLessonVideosActivity.class);
                    lessonsIntent.putExtra("lesson_status", isBuy);
                    lessonsIntent.putExtra("lesson_id", lesson_id);
                    lessonsIntent.putExtra("lesson_name", lesson_name);
                    lessonsIntent.putExtra("lesson_price", lesson_price);
                    lessonsIntent.putExtra("lesson_smeta", lesson_smeta);
                    lessonsIntent.putExtra("videoPath", thumbVideoString);
                    startActivity(lessonsIntent);
                } else {
                    Intent orderIntent = new Intent(SPLessonDetailActivity.this, SpeerOrderActivity.class);
                    orderIntent.putExtra("lesson_id", lesson_id);
                    orderIntent.putExtra("lesson_name", lesson_name);
                    orderIntent.putExtra("lesson_price", lesson_price);
                    orderIntent.putExtra("lesson_smeta", lesson_smeta);
                    orderIntent.putExtra("videoPath", thumbVideoString);
                    startActivity(orderIntent);
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
