package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.LogTag;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.model.ChatRoomAction;
import com.caesar.rongcloudspeed.model.ChatRoomResult;
import com.caesar.rongcloudspeed.ui.activity.AddFriendActivity;
import com.caesar.rongcloudspeed.ui.activity.ScanActivity;
import com.caesar.rongcloudspeed.ui.activity.SealSearchActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectCreateGroupActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectSingleFriendActivity;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.log.SLog;
import com.caesar.rongcloudspeed.viewmodel.AppViewModel;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 主界面子界面-发现界面
 */
public class SPSpeerLeftFragment extends BaseFragment {
    private static final String TAG = "SPSpeerLeftFragment";

    @Override
    protected int getLayoutResId() {
        return R.layout.user_fragment_speer_shop_list;
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

    }
}
