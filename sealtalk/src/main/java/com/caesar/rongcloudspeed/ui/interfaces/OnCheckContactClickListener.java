package com.caesar.rongcloudspeed.ui.interfaces;

import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.ui.adapter.models.CheckableContactModel;

public interface OnCheckContactClickListener {
    void onContactContactClick(CheckableContactModel<FriendShipInfo> contactModel);
}
