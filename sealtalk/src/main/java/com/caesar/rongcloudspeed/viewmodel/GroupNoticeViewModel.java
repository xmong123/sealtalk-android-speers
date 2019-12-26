package com.caesar.rongcloudspeed.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.caesar.rongcloudspeed.db.model.GroupEntity;
import com.caesar.rongcloudspeed.model.GroupNoticeResult;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.task.GroupTask;
import com.caesar.rongcloudspeed.utils.SingleSourceLiveData;

public class GroupNoticeViewModel extends AndroidViewModel {
    private SingleSourceLiveData<Resource<GroupEntity>> groupInfo = new SingleSourceLiveData<>();
    private SingleSourceLiveData<Resource<GroupNoticeResult>> groupNotice = new SingleSourceLiveData<>();
    private SingleSourceLiveData<Resource<Void>> publishNoticeResult = new SingleSourceLiveData<>();

    private GroupTask groupTask;
    private String groupId;

    public GroupNoticeViewModel(@NonNull Application application) {
        super(application);

        groupTask = new GroupTask(application);
    }

    public GroupNoticeViewModel(@NonNull Application application, String groupId){
        this(application);

        this.groupId = groupId;

        requestGroupInfo(groupId);

        requestGroupNotice(groupId);
    }

    /**
     * 请求群组信息
     *
     * @param groupId
     */
    public void requestGroupInfo(String groupId){
        groupInfo.setSource(groupTask.getGroupInfo(groupId));
    }

    /**
     * 获取群组信息
     *
     * @return
     */
    public LiveData<Resource<GroupEntity>> getGroupInfo(){
        return groupInfo;
    }

    /**
     * 请求群公告
     * @param groupId
     */
    public void requestGroupNotice(String groupId){
        groupNotice.setSource(groupTask.getGroupNotice(groupId));
    }

    /**
     * 获取群公告
     * @return
     */
    public LiveData<Resource<GroupNoticeResult>> getGroupNoticeResult(){
        return groupNotice;
    }

    /**
     * 发布群公告
     *
     * @param content
     */
    public void publishNotice(String content){
        publishNoticeResult.setSource(groupTask.setGroupNotice(groupId, content));
    }

    /**
     * 获取发布群公告结果
     *
     * @return
     */
    public LiveData<Resource<Void>> getPublishNoticeResult(){
        return publishNoticeResult;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private String targetId;
        private Application application;

        public Factory(Application application,String targetId) {
            this.targetId = targetId;
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            try {
                return modelClass.getConstructor(Application.class, String.class).newInstance(application, targetId);
            } catch (Exception e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
    }
}
