package com.caesar.rongcloudspeed.ui.interfaces;

import java.util.List;

import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.db.model.GroupEntity;

public interface OnForwardComfirmListener {
    void onForward(List<GroupEntity> groups, List<FriendShipInfo> friendShipInfos);
}
