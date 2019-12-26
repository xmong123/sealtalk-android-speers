package com.caesar.rongcloudspeed.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.caesar.rongcloudspeed.db.dao.FriendDao;
import com.caesar.rongcloudspeed.db.dao.GroupDao;
import com.caesar.rongcloudspeed.db.dao.GroupMemberDao;
import com.caesar.rongcloudspeed.db.dao.UserDao;
import com.caesar.rongcloudspeed.db.model.BlackListEntity;
import com.caesar.rongcloudspeed.db.model.FriendInfo;
import com.caesar.rongcloudspeed.db.model.GroupEntity;
import com.caesar.rongcloudspeed.db.model.GroupMemberInfoEntity;
import com.caesar.rongcloudspeed.db.model.GroupNoticeInfo;
import com.caesar.rongcloudspeed.db.model.PhoneContactInfoEntity;
import com.caesar.rongcloudspeed.db.model.UserInfo;

@Database(entities = {UserInfo.class, FriendInfo.class, GroupEntity.class, GroupMemberInfoEntity.class,
        BlackListEntity.class, GroupNoticeInfo.class, PhoneContactInfoEntity.class}, version = 2, exportSchema = false)
@TypeConverters(com.caesar.rongcloudspeed.db.TypeConverters.class)
public abstract class SealTalkDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();

    public abstract FriendDao getFriendDao();

    public abstract GroupDao getGroupDao();

    public abstract GroupMemberDao getGroupMemberDao();
}
