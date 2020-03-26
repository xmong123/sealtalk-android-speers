package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.BaseShopActivity;
import com.caesar.rongcloudspeed.ui.fragment.PersonalSeekHelperFragment;
import com.caesar.rongcloudspeed.view.TranspanentTitleBar;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;


public class PersonalWalletActivity extends BaseShopActivity implements View.OnClickListener {

    @BindView(R.id.titleBar)
    TranspanentTitleBar titleBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_wallet);
        ButterKnife.bind(this);
        titleBar.getBack().setOnClickListener(this);
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
