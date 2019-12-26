package com.caesar.rongcloudspeed.ui.fragment;

import com.caesar.rongcloudspeed.viewmodel.SelectBaseViewModel;

public class SelectGroupMemberMultiFragment extends SelectMultiFriendFragment {
    private String groupId;

    public SelectGroupMemberMultiFragment(String groupId) {
        this.groupId = groupId;
    }

    @Override
    protected void onLoadData(SelectBaseViewModel viewModel) {
        viewModel.loadGroupMemberExclude(groupId, excludeInitIdList);
    }
}
