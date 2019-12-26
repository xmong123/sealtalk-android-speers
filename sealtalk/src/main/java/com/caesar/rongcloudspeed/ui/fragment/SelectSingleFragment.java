package com.caesar.rongcloudspeed.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.viewmodel.SelectBaseViewModel;
import com.caesar.rongcloudspeed.viewmodel.SelectSingleViewModel;

public class SelectSingleFragment extends SelectBaseFragment {

    private static final String TAG = "SelectSingleFragment";
    @Override
    protected SelectBaseViewModel getViewModel() {
        return ViewModelProviders.of(this).get(SelectSingleViewModel.class);
    }
}
