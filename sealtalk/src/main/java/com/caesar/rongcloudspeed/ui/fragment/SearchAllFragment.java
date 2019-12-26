package com.caesar.rongcloudspeed.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.util.List;

import com.caesar.rongcloudspeed.ui.adapter.models.SearchModel;
import com.caesar.rongcloudspeed.ui.interfaces.OnChatItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnContactItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnGroupItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnMessageRecordClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnShowMoreClickListener;
import com.caesar.rongcloudspeed.utils.log.SLog;

public class SearchAllFragment extends SearchBaseFragment {
    private static final String TAG = "SearchAllFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        viewModel.getSearchAll().observe(this, new Observer<List<SearchModel>>() {
            @Override
            public void onChanged(List<SearchModel> searchModels) {
                SLog.i(TAG, "searchModels.size(): " + searchModels.size());
                updateData(searchModels);
            }
        });
        if (!TextUtils.isEmpty(initSearch)) {
            search(initSearch);
        }
        return view;
    }

    @Override
    public void search(String search) {
        super.search(search);
        if (viewModel != null) {
            viewModel.searchAll(search);
        }
    }
}
