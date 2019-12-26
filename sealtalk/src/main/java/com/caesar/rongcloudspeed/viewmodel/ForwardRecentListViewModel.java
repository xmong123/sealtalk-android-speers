package com.caesar.rongcloudspeed.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.ThreadManager;
import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.db.model.GroupEntity;
import com.caesar.rongcloudspeed.task.FriendTask;
import com.caesar.rongcloudspeed.task.GroupTask;
import com.caesar.rongcloudspeed.ui.adapter.models.ListItemModel;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 最近联系人的 ViewModel
 */
public class ForwardRecentListViewModel extends CommonListBaseViewModel {
    private static final String TAG = "ForwardRecentListViewModel";
    private GroupTask groupTask;
    private FriendTask friendTask;

    public ForwardRecentListViewModel(@NonNull Application application) {
        super(application);
        groupTask = new GroupTask(application);
        friendTask = new FriendTask(application);
    }

    @Override
    public void loadData() {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                ThreadManager.getInstance().runOnWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        convertConversationAndSetValue(conversations);
                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void convertConversationAndSetValue(List<Conversation> conversations) {
        final ModelBuilder modelBuilder = builderModel();

        List<ListItemModel> berforeItems = getBeforeItems();
        modelBuilder.addModelList(berforeItems);

        for (Conversation conversation : conversations) {
            if (conversation.getConversationType().equals(Conversation.ConversationType.GROUP)) {
                final GroupEntity groupInfoSync = groupTask.getGroupInfoSync(conversation.getTargetId());
                if (groupInfoSync != null) {
                    modelBuilder.addGroup(groupInfoSync);
                }
            } else if (conversation.getConversationType() == Conversation.ConversationType.PRIVATE) {
                final FriendShipInfo friendShipInfo = friendTask.getFriendShipInfoFromDBSync(conversation.getTargetId());
                if (friendShipInfo != null) {
                    modelBuilder.addFriend(friendShipInfo);
                }
            }
        }
        modelBuilder.post();
    }


    /**
     * 前置功能项
     * @return
     */
    protected List<ListItemModel> getBeforeItems() {
        List<ListItemModel> berforeItems = new ArrayList<>();
        berforeItems.add(createFunModel("1", getApplication().getString(R.string.seal_select_forward_create_new_chat)));
        berforeItems.add(createFunModel("2", getApplication().getString(R.string.seal_select_forward_select_group)));
        berforeItems.add(createTextModel(getApplication().getString(R.string.seal_select_forward_message_recent_chat)));
       return berforeItems;
    }


}
