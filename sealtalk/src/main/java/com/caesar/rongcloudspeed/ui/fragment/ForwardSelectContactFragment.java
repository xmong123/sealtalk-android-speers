package com.caesar.rongcloudspeed.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.viewmodel.CommonListBaseViewModel;
import com.caesar.rongcloudspeed.viewmodel.ForwardSelectContactViewModel;

public class ForwardSelectContactFragment extends CommonListBaseFragment {
    @Override
    protected CommonListBaseViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ForwardSelectContactViewModel.class);
    }

    @Override
    protected boolean isUseSideBar() {
        return true;
    }
}
