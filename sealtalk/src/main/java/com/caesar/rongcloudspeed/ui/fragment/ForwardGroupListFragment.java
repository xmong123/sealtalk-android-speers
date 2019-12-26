package com.caesar.rongcloudspeed.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.viewmodel.CommonListBaseViewModel;
import com.caesar.rongcloudspeed.viewmodel.ForwardGroupListViewModel;

public class ForwardGroupListFragment extends CommonListBaseFragment {

    @Override
    protected CommonListBaseViewModel createViewModel() {
        boolean isSelect = getArguments().getBoolean(IntentExtra.IS_SELECT, false);
        return ViewModelProviders.of(this, new ForwardGroupListViewModel.Factory(isSelect, getActivity().getApplication())).get(ForwardGroupListViewModel.class);
    }
}
