package com.caesar.rongcloudspeed.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.ui.adapter.AddFriendFromContactListAdapter;
import com.caesar.rongcloudspeed.ui.adapter.ListWithSideBarBaseAdapter;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.AddFriendFromContactItemViewHolder;
import com.caesar.rongcloudspeed.viewmodel.AddFriendFromContactViewModel;
import com.caesar.rongcloudspeed.viewmodel.CommonListBaseViewModel;

public class AddFriendFromContactFragment extends CommonListBaseFragment {
    private AddFriendFromContactItemViewHolder.OnAddFriendClickedListener addFriendClickedListener;
    private AddFriendFromContactViewModel addFriendFromContactViewModel;
    @Override
    protected CommonListBaseViewModel createViewModel() {
        addFriendFromContactViewModel = ViewModelProviders.of(this).get(AddFriendFromContactViewModel.class);
        return addFriendFromContactViewModel;
    }

    @Override
    protected boolean isUseSideBar() {
        return true;
    }

    @Override
    protected ListWithSideBarBaseAdapter getListAdapter() {
        AddFriendFromContactListAdapter listAdapter = new AddFriendFromContactListAdapter();
        listAdapter.setAddFriendClickedListener(addFriendClickedListener);
        return listAdapter;
    }

    public void setAddFriendClickedListener(AddFriendFromContactItemViewHolder.OnAddFriendClickedListener listener) {
        this.addFriendClickedListener = listener;
    }

    public void search(String keyword){
        addFriendFromContactViewModel.search(keyword);
    }
}
