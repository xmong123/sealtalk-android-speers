package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.circle.ui.FriendCircleActivity;
import com.caesar.rongcloudspeed.common.LogTag;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.db.model.FriendStatus;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.model.ChatRoomAction;
import com.caesar.rongcloudspeed.model.ChatRoomResult;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.task.FriendTask;
import com.caesar.rongcloudspeed.ui.activity.AddFriendActivity;
import com.caesar.rongcloudspeed.ui.activity.ScanActivity;
import com.caesar.rongcloudspeed.ui.activity.SealSearchActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectCreateGroupActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectSingleFriendActivity;
import com.caesar.rongcloudspeed.ui.activity.VoteBaseActivity;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.AppViewModel;
import com.caesar.rongcloudspeed.utils.log.SLog;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 主界面子界面-发现界面
 */
public class MainDiscoveryFragment extends BaseFragment {
    private AppViewModel appViewModel;
    private List<ChatRoomResult> latestChatRoomList;
    private static final int REQUEST_START_CHAT = 0;
    private static final int REQUEST_START_GROUP = 1;
    private static final String TAG = "MainDiscoveryFragment";

    @Override
    protected int getLayoutResId() {
        return R.layout.main_fragment_discovery;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        findView(R.id.btn_start_chat, true);
        findView(R.id.btn_create_group, true);
        findView(R.id.btn_add_friends, true);
        findView(R.id.btn_scan, true);
        findView(R.id.btn_search, true);
        findView(R.id.discovery_ll_chat_room_1, true);
        findView(R.id.discovery_ll_chat_room_2, true);
        findView(R.id.discovery_ll_chat_room_3, true);
        findView(R.id.discovery_ll_chat_room_4, true);

    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
        appViewModel = ViewModelProviders.of(getActivity()).get(AppViewModel.class);

        // 获取聊天室列表
        appViewModel.getChatRoonList().observe(this, listResource -> {
            List<ChatRoomResult> chatRoomResultList = listResource.data;
            if (chatRoomResultList != null) {
                latestChatRoomList = chatRoomResultList;
            }
        });


        /*
         * 以下代码使用 lambda 表达式会崩溃，因为 lambda 特性复用时注册同一个 Observer 时引发崩溃
         */

        // 监听聊天室加入状态
        IMManager.getInstance().getChatRoomAction().observe(this, new Observer<ChatRoomAction>() {
            @Override
            public void onChanged(ChatRoomAction chatRoomAction) {
                if (chatRoomAction.status == ChatRoomAction.Status.ERROR) {
                    ToastUtils.showToast(R.string.discovery_chat_room_join_failure);
                } else {
                    SLog.d(LogTag.IM, "ChatRoom action, status: " + chatRoomAction.status.name() + " - ChatRoom id:" + chatRoomAction.roomId);
                }
            }
        });



    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.btn_start_chat:
                Intent intent = new Intent(getActivity(), SelectSingleFriendActivity.class);
                startActivityForResult(intent, REQUEST_START_CHAT);
                break;
            case R.id.btn_create_group:
                intent = new Intent(getActivity(), SelectCreateGroupActivity.class);
                startActivityForResult(intent, REQUEST_START_GROUP);
                break;
            case R.id.btn_add_friends:
                intent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_scan:
                intent = new Intent(getActivity(), ScanActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_search:
                intent = new Intent(getActivity(), SealSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.discovery_ll_chat_room_1:
                enterChatRoom(0, getString(R.string.discovery_chat_room_one));
                break;
            case R.id.discovery_ll_chat_room_2:
                enterChatRoom(1, getString(R.string.discovery_chat_room_two));
                break;
            case R.id.discovery_ll_chat_room_3:
                enterChatRoom(2, getString(R.string.discovery_chat_room_three));
                break;
            case R.id.discovery_ll_chat_room_4:
                enterChatRoom(3, getString(R.string.discovery_chat_room_four));
                break;
            default:
                break;
        }
    }

    /**
     * 进入聊天室
     *
     * @param roomIndex
     * @param roomTitle
     */
    private void enterChatRoom(int roomIndex, String roomTitle) {
        if (roomIndex >= (latestChatRoomList != null ? latestChatRoomList.size() : 0)) {
            ToastUtils.showToast(R.string.discovery_join_chat_room_error);
            appViewModel.requestChatRoomList();
            return;
        }

        ChatRoomResult chatRoomResult = latestChatRoomList.get(roomIndex);
        String roomId = chatRoomResult.getId();

        RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.CHATROOM, roomId, roomTitle);
    }
}
