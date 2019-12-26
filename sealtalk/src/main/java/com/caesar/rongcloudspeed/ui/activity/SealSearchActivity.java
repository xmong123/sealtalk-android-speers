package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.db.model.GroupEntity;
import com.caesar.rongcloudspeed.ui.adapter.models.SearchConversationModel;
import com.caesar.rongcloudspeed.ui.fragment.SearchAllFragment;
import com.caesar.rongcloudspeed.ui.fragment.SearchBaseFragment;
import com.caesar.rongcloudspeed.ui.fragment.SearchConversationFragment;
import com.caesar.rongcloudspeed.ui.fragment.SearchFriendFragment;
import com.caesar.rongcloudspeed.ui.fragment.SearchGroupFragment;
import com.caesar.rongcloudspeed.ui.fragment.SearchMessageFragment;
import com.caesar.rongcloudspeed.ui.interfaces.OnChatItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnContactItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnGroupItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnMessageRecordClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnShowMoreClickListener;
import com.caesar.rongcloudspeed.viewmodel.SearchMessageModel;
import com.caesar.rongcloudspeed.utils.log.SLog;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.SearchConversationResult;

public class SealSearchActivity extends SealSearchBaseActivity implements TextWatcher, OnContactItemClickListener,
        OnGroupItemClickListener, OnChatItemClickListener, OnShowMoreClickListener, OnMessageRecordClickListener {
    private static final String TAG = "SealSearchActivity";
    private SearchAllFragment searchAllFragment;
    private SearchFriendFragment searchFriendFragment;
    private SearchConversationFragment searchConversationFragment;
    private SearchGroupFragment searchGroupFragment;
    private SearchMessageFragment searchMessageFragment;
    private SearchBaseFragment currentFragment; //当前Fragment

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchAllFragment = new SearchAllFragment();
        searchAllFragment.init(this, this, this, this, null);
        searchFriendFragment = new SearchFriendFragment();
        searchFriendFragment.init(this);
        searchConversationFragment = new SearchConversationFragment();
        searchConversationFragment.init(this);
        searchGroupFragment = new SearchGroupFragment();
        searchGroupFragment.init(this);
        pushFragment(searchAllFragment);
    }

    /**
     * 点击好友
     *
     * @param friendShipInfo
     */
    @Override
    public void onItemContactClick(FriendShipInfo friendShipInfo) {
        String displayName = friendShipInfo.getDisplayName();
        if(TextUtils.isEmpty(displayName)){
            displayName = friendShipInfo.getUser().getNickname();
        }
        RongIM.getInstance().startPrivateChat(this, friendShipInfo.getUser().getId(),
                displayName);
    }

    /**
     * 点击聊天记录
     *
     * @param searchConversationModel
     */

    @Override
    public void OnChatItemClicked(SearchConversationModel searchConversationModel) {
        SearchConversationResult result = searchConversationModel.getBean();
        if (result.getMatchCount() == 1) {
            RongIM.getInstance().startConversation(this,
                    result.getConversation().getConversationType(),
                    result.getConversation().getTargetId(),
                    searchConversationModel.getName());
        } else {

            searchMessageFragment = new SearchMessageFragment();
            searchMessageFragment.init(this,
                    searchConversationModel.getBean().getConversation().getTargetId(),
                    searchConversationModel.getBean().getConversation().getConversationType(),
                    searchConversationModel.getName(),
                    searchConversationModel.getPortraitUrl());
            pushFragment(searchMessageFragment);
        }
    }

    /**
     * 点击群组
     *
     * @param groupEntity
     */
    @Override
    public void onGroupClicked(GroupEntity groupEntity) {
        RongIM.getInstance().startGroupChat(this, groupEntity.getId(), groupEntity.getName());
    }

    /**
     * 点击显示更多
     *
     * @param type
     */
    @Override
    public void onSearchShowMoreClicked(int type) {
        SLog.i(TAG, "ShowMore:" + R.string.seal_ac_search_more_chatting_records + " type:" + type);
        switch (type) {
            case R.string.seal_search_more_chatting_records:
                pushFragment(searchConversationFragment);
                break;
            case R.string.seal_search_more_friend:
                pushFragment(searchFriendFragment);
                break;
            case R.string.seal_search_more_group:
                pushFragment(searchGroupFragment);
                break;
            default:
                break;
        }
    }

    public void search(String search) {
        currentFragment.search(search);
    }

    @Override
    public void clear() {
        currentFragment.clear();
    }

    /**
     * 搜索消息记录点击
     *
     * @param searchMessageModel
     */
    @Override
    public void onMessageRecordClick(SearchMessageModel searchMessageModel) {
        Message message = searchMessageModel.getBean();
        RongIM.getInstance().startConversation(this,
                message.getConversationType(),
                message.getTargetId(), searchMessageModel.getName(),
                message.getSentTime());
    }

    private void pushFragment(SearchBaseFragment fragment) {
        currentFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content_fragment, currentFragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
        search(search);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 1) {
            //只有searchAllFragment
            finish();
        } else {
            super.onBackPressed();
            getTitleBar().getEtSearch().setText(searchAllFragment.getInitSearch());
        }
    }
}
