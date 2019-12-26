package com.caesar.rongcloudspeed.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.List;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.ThreadManager;
import com.caesar.rongcloudspeed.contact.PhoneContactManager;
import com.caesar.rongcloudspeed.model.SimplePhoneContactInfo;
import com.caesar.rongcloudspeed.ui.adapter.models.ListItemModel;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.InviteFriendFromContactItemViewHolder;
import com.caesar.rongcloudspeed.utils.CharacterParser;

/**
 * 添加好友视图模型
 */
public class InviteFriendFromContactViewModel extends CommonListBaseViewModel {
    private ModelBuilder allContactBuilder;
    private List<SimplePhoneContactInfo> allContactInfo;


    public InviteFriendFromContactViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void loadData() {
        ThreadManager.getInstance().runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                allContactBuilder = builderModel();
                allContactInfo = PhoneContactManager.getInstance().getAllContactInfo();
                for(SimplePhoneContactInfo info : allContactInfo){
                    allContactBuilder.addModel(createPhoneContact(info));
                }
                allContactBuilder.buildFirstChar();
                allContactBuilder.post();
            }
        });
    }

    /**
     * 创建联系人对象
     *
     * @param info
     * @return
     */
    protected ListItemModel createPhoneContact(SimplePhoneContactInfo info) {
        String name = info.getName();
        ListItemModel.ItemView itemView = new ListItemModel
                .ItemView(R.layout.invite_friend_item_from_contact, ListItemModel.ItemView.Type.OTHER, InviteFriendFromContactItemViewHolder.class);
        ListItemModel<SimplePhoneContactInfo> model = new ListItemModel<>(info.getPhone(), name, info, itemView);
        model.setFirstChar(CharacterParser.getInstance().getSpelling(name).toUpperCase());
        return model;
    }

    public void search(String keyword){
        if(TextUtils.isEmpty(keyword)){
            allContactBuilder.clearCheckedState();
            allContactBuilder.post();
        }else {
            ModelBuilder searchBuilder = builderModel();
            if(allContactInfo != null){
                for(SimplePhoneContactInfo info : allContactInfo){
                    if(info.getName().contains(keyword) || info.getPhone().contains(keyword)){
                        searchBuilder.addModel(createPhoneContact(info));
                    }
                }

                searchBuilder.buildFirstChar();
                searchBuilder.post();
            }
        }
    }

}
