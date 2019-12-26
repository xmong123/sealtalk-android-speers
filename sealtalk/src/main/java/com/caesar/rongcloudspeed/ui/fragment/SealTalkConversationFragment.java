package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.caesar.rongcloudspeed.R;
/**
 * 主界面子界面-发现界面
 */
public class SealTalkConversationFragment extends BaseFragment {
    private ViewPager viewPager;

    @Override
    protected int getLayoutResId() {
        return R.layout.main_fragment_conversation_manager;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            default:
                break;
        }
    }
}
