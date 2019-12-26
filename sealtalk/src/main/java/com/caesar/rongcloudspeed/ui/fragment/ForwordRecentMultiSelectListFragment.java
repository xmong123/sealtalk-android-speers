package com.caesar.rongcloudspeed.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.viewmodel.ForwardRecentListViewModel;
import com.caesar.rongcloudspeed.viewmodel.ForwardRecentSelectListViewModel;

/**
 *  转发最近联系人列表
 */
public class ForwordRecentMultiSelectListFragment extends ForwordRecentListFragment {

    @Override
    protected ForwardRecentListViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ForwardRecentSelectListViewModel.class);
    }
}
