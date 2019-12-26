package com.caesar.rongcloudspeed.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.model.UserSimpleInfo;
import com.caesar.rongcloudspeed.task.FriendTask;
import com.caesar.rongcloudspeed.task.UserTask;
import com.caesar.rongcloudspeed.utils.SingleSourceLiveData;
import com.caesar.rongcloudspeed.utils.SingleSourceMapLiveData;

public class BlackListViewModel extends AndroidViewModel {
    private UserTask userTask;
    private FriendTask friendTask;

    private SingleSourceLiveData<Resource<List<UserSimpleInfo>>> blacklistResult = new SingleSourceLiveData<>();
    private SingleSourceMapLiveData<Resource<Void>, Resource<Void>> removeBlackListResult;

    public BlackListViewModel(@NonNull Application application) {
        super(application);
        userTask = new UserTask(application);
        friendTask = new FriendTask(application);
        getBlackList();


        removeBlackListResult = new SingleSourceMapLiveData<>(new Function<Resource<Void>, Resource<Void>>() {
            @Override
            public Resource<Void> apply(Resource<Void> input) {
                if(input.status == Status.SUCCESS) {
                    // 在移除黑名单成功后刷新好友列表
                    LiveData<Resource<List<FriendShipInfo>>> allFriends = friendTask.getAllFriends();
                    allFriends.observeForever(new Observer<Resource<List<FriendShipInfo>>>() {
                        @Override
                        public void onChanged(Resource<List<FriendShipInfo>> listResource) {
                            if(listResource.status != Status.LOADING) {
                                allFriends.removeObserver(this);
                            }
                        }
                    });
                }
                return input;
            }
        });
    }

    /**
     * 黑名单
     *
     * @return
     */
    public LiveData<Resource<List<UserSimpleInfo>>> getBlackListResult() {
        return blacklistResult;
    }

    /**
     * 获取黑名单
     */
    private void getBlackList() {
        blacklistResult.setSource(userTask.getBlackList());
    }

    /**
     * 移除黑名单
     */
    public void removeFromBlackList(String userId) {
        removeBlackListResult.setSource(userTask.removeFromBlackList(userId));
    }

    /**
     * 获取移除黑名单结果
     *
     * @return
     */
    public LiveData<Resource<Void>> getRemoveBlackListResult() {
        return removeBlackListResult;
    }


}
