package com.caesar.rongcloudspeed.ui.fragment;

import com.caesar.rongcloudspeed.viewmodel.SelectBaseViewModel;

public class SelectFriendsExcluedGroupFragment extends SelectMultiFriendFragment {
    private String groupId;

    public SelectFriendsExcluedGroupFragment(String groupId) {
        this.groupId = groupId;
    }

    @Override
    protected void onLoadData(SelectBaseViewModel viewModel) {
        viewModel.loadFriendShipExclude(groupId, uncheckableInitIdList);
    }
}
