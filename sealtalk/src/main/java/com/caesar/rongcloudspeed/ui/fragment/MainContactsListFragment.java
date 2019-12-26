package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.db.model.FriendStatus;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.GroupListActivity;
import com.caesar.rongcloudspeed.ui.activity.MainActivity;
import com.caesar.rongcloudspeed.ui.activity.NewFriendListActivity;
import com.caesar.rongcloudspeed.ui.activity.PublicServiceActivity;
import com.caesar.rongcloudspeed.ui.activity.UserDetailActivity;
import com.caesar.rongcloudspeed.ui.adapter.CommonListAdapter;
import com.caesar.rongcloudspeed.ui.adapter.models.FunctionInfo;
import com.caesar.rongcloudspeed.ui.adapter.models.ListItemModel;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.CommonListBaseViewModel;
import com.caesar.rongcloudspeed.viewmodel.MainContactsListViewModel;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.caesar.rongcloudspeed.common.IntentExtra.STR_TARGET_ID;

public class MainContactsListFragment extends CommonListBaseFragment {
    private static final String TAG = "MainContactsListFragment";
    private String friendshipString ;
    //    private ContactsAdapter adapter;
    private MainContactsListViewModel viewModel;
    private MainActivity mainActivity;

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        super.onInitView(savedInstanceState, intent);

        // Adapter 的点击监听
        setOnItemClickListener(new CommonListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, ListItemModel data) {
                final ListItemModel.ItemView.Type type = data.getItemView().getType();
                switch (type) {
                    case FUN:
                        FunctionInfo functionInfo = (FunctionInfo) data.getData();
                        handleFunItemClick(functionInfo);
                        break;
                    case FRIEND:
                        FriendShipInfo friendShipInfo = (FriendShipInfo) data.getData();
                        handleFriendItemClick(friendShipInfo);
                        break;
                    default:
                        //Do nothing
                        break;
                }
            }
        });
    }


    @Override
    protected boolean isUseSideBar() {
        return true;
    }

    @Override
    protected CommonListBaseViewModel createViewModel() {
        viewModel = ViewModelProviders.of(MainContactsListFragment.this).get(MainContactsListViewModel.class);
        viewModel.getRefreshItem().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                //getAdapter().notifyItemChanged(integer);
            }
        });
        viewModel.getLoadAllFriendInfoResult().observe(this, new Observer<Resource<List<FriendShipInfo>>>() {
            @Override
            public void onChanged(Resource<List<FriendShipInfo>> listResource) {
                if (listResource.status == Status.SUCCESS) {
                    if (listResource.data != null && listResource.data.size() > 0) {
                        updateDotNum(listResource.data);
                        List<FriendShipInfo> dataList = listResource.data;
                        StringBuffer sb = new StringBuffer();
                        for(FriendShipInfo data : dataList){
                            if(data.getStatus() == FriendStatus.IS_FRIEND.getStatusCode()) {
                                sb.append(data.getUser().getPhone()+",");
                            }
                        }
                        if(sb.length()>0){
                        friendshipString=sb.deleteCharAt(sb.length() - 1).toString();
                        Log.d("friendshipString",friendshipString);
                        String userid= UserInfoUtils.getAppUserId(getActivity());
                        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateFriendShip(userid,friendshipString),
                                new NetworkCallback<BaseData>() {
                                    @Override
                                    public void onSuccess(BaseData baseData) {

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                        }
                    }
                }
            }
        });
        return viewModel;
    }

    /**
     * 更新未读状态数量
     *
     * @param list
     */
    private void updateDotNum(List<?> list) {
        int dotNum = 0;
        for (Object info : list) {
            FriendShipInfo friendShipInfo = null;
            if (info instanceof FriendShipInfo) {
                friendShipInfo = (FriendShipInfo) info;
            } else if (info instanceof ListItemModel) {
                ListItemModel listItemModel = (ListItemModel) info;
                if (listItemModel.getData() instanceof FriendShipInfo) {
                    friendShipInfo = (FriendShipInfo) listItemModel.getData();
                }
            }
            //待处理状态时数量加1
            if (friendShipInfo != null && friendShipInfo.getStatus() == FriendStatus.RECEIVE_REQUEST.getStatusCode()) {
                dotNum++;
            }
        }
        int position;
        if (dotNum > 0) {
            position = viewModel.setFunctionShowRedDot("1", dotNum, true);
        } else {
            position = viewModel.setFunctionShowRedDot("1", dotNum, false);
        }
        if (mainActivity == null) {
            mainActivity = (MainActivity) getActivity();
        }
        mainActivity.mainViewModel.setNewFriendNum(dotNum);
        getAdapter().notifyItemChanged(position);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 处理 好友 item 点击事件
     *
     * @param friendShipInfo
     */
    private void handleFriendItemClick(FriendShipInfo friendShipInfo) {
        if (friendShipInfo.getUser().getId().equals(IMManager.getInstance().getCurrentId())) {
            String title = TextUtils.isEmpty(friendShipInfo.getDisplayName()) ? friendShipInfo.getUser().getNickname() : friendShipInfo.getDisplayName();
            RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.PRIVATE, friendShipInfo.getUser().getId(), title);
        } else {
            Intent intent = new Intent(getContext(), UserDetailActivity.class);
            intent.putExtra(STR_TARGET_ID, friendShipInfo.getUser().getId());
            startActivity(intent);
        }
    }


    /**
     * 处理功能事件
     *
     * @param functionInfo
     */
    private void handleFunItemClick(FunctionInfo functionInfo) {
        final String id = functionInfo.getId();
        switch (id) {
            case "1":
                //新的朋友
                viewModel.setFunRedDotShowStatus(id, false);
                Intent intent = new Intent(getActivity(), NewFriendListActivity.class);
                startActivity(intent);
                break;
            case "2":
                //群组
                intent = new Intent(getActivity(), GroupListActivity.class);
                startActivity(intent);
                break;
            case "3":
                //公众号
                intent = new Intent(getActivity(), PublicServiceActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
