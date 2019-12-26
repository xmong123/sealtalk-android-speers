package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.result.SmsCode;
import com.caesar.rongcloudspeed.listener.RegisterNextStepListener;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.BaseActivity;
import com.caesar.rongcloudspeed.ui.fragment.AdminIndustryFragment;
import com.caesar.rongcloudspeed.ui.fragment.AdminPerfesssionFragment;
import com.caesar.rongcloudspeed.ui.fragment.AdminSoftFragment;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * 账号设置
 */
public class AdminMainActivity extends BaseActivity {
    private String industryString="0";
    private String professionString="0";
    private String softString="0";
    private String userid="0";
    private ViewPager adminFragmentContainer;
    private TextView admintitle1;
    private List<Fragment> fragments = new ArrayList<>();
    private AdminIndustryFragment adminIndustryFragment=new AdminIndustryFragment();
    private AdminPerfesssionFragment adminPerfesssionFragment=new AdminPerfesssionFragment();
    private AdminSoftFragment adminSoftFragment=new AdminSoftFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initView();
        userid= UserInfoUtils.getAppUserId(this);
    }

    private void initView() {
        admintitle1 = findViewById(R.id.admintitle1);
        adminFragmentContainer = findViewById(R.id.admin_main_container);
        adminIndustryFragment.setOnNextStepListener(new RegisterNextStepListener(){
            @Override
            public void onNextStep(String string) {
                industryString=string;
                adminFragmentContainer.setCurrentItem(1,true);
            }
        });
        adminPerfesssionFragment.setOnNextStepListener(new RegisterNextStepListener(){
            @Override
            public void onNextStep(String string) {
                professionString=string;
                adminFragmentContainer.setCurrentItem(2,true);
            }
        });
        adminSoftFragment.setOnNextStepListener(new RegisterNextStepListener(){
            @Override
            public void onNextStep(String string) {
                softString=string;
                NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateIndustry(userid,industryString,professionString,softString),
                        new NetworkCallback<BaseData>() {
                            @Override
                            public void onSuccess(BaseData baseData) {
                                if(baseData.getCode()==CODE_SUCC){
                                    UserInfoUtils.setUserIndustry(industryString,AdminMainActivity.this);
                                    UserInfoUtils.setUserProfession(professionString,AdminMainActivity.this);
                                    UserInfoUtils.setUserSoft(softString,AdminMainActivity.this);
                                    ToastUtils.showToast("资料更新成功");
                                }
                                finish();
                            }

                            @Override
                            public void onFailure(Throwable t) {
//                                dismissLoadingDialog();
                                Toast.makeText(AdminMainActivity.this, "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        fragments.add(adminIndustryFragment);
        fragments.add(adminPerfesssionFragment);
        fragments.add(adminSoftFragment);

        // ViewPager 的 Adpater
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        adminFragmentContainer.setAdapter(fragmentPagerAdapter);
        adminFragmentContainer.setOffscreenPageLimit(fragments.size());
        // 设置页面切换监听
        adminFragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 当页面切换完成之后， 同时也要把 tab 设置到正确的位置
                switch (position) {
                    case 0:
                        admintitle1.setText("请选择您的行业(可多选)!");
                        break;
                    case 1:
                        admintitle1.setText("请选择您的专业(可多选)!");
                        break;
                    case 2:
                        admintitle1.setText("请选择您的常用软件所在的企业!\n(括号内为主要软件)");
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
