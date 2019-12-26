package com.caesar.rongcloudspeed.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.caesar.rongcloudspeed.db.model.BlackListEntity;
import com.caesar.rongcloudspeed.db.model.FriendInfo;
import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.db.model.PhoneContactInfoEntity;
import com.caesar.rongcloudspeed.model.PhoneContactInfo;
import com.caesar.rongcloudspeed.model.UserSimpleInfo;


@Dao
public interface FriendDao {
    @Query("SELECT friend.id as id ,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend " +
            "left join user " +
            "on friend.id = user.id " +
            "order by user.order_spelling")
    LiveData<List<FriendShipInfo>> getAllFriendListDB();

    @Query("SELECT friend.id,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend INNER JOIN user on friend.id = user.id WHERE friend.id = :id")
    LiveData<FriendShipInfo> getFriendInfo(String id);

    @Query("SELECT friend.id,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend INNER JOIN user on friend.id = user.id WHERE friend.id = :id")
    FriendShipInfo getFriendInfoSync(String id);

    @Query("SELECT friend.id,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling,order_spelling " +
            "FROM friend INNER JOIN user on friend.id = user.id WHERE friend.id in (:ids)")
    List<FriendShipInfo> getFriendInfoListSync(String[] ids);

    @Query("SELECT friend.id,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling,order_spelling " +
            "FROM friend INNER JOIN user on friend.id = user.id WHERE friend.id in (:ids)")
    LiveData<List<FriendShipInfo>> getFriendInfoList(String[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFriendShip(FriendInfo friendInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFriendShipList(List<FriendInfo> friendInfoList);

    @Query("SELECT friend.id as id ,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend " +
            "left join user " +
            "on friend.id = user.id " +
            "where user.name like '%' || :matchSearch || '%'" +
            "OR user.alias like '%' || :matchSearch || '%' " +
            "OR user.name_spelling like '%$' || :matchSearch || '%' " +
            "OR user.alias_spelling like '%$' || :matchSearch || '%' " +
            "OR user.name_spelling_initial  like '%' || :matchSearch || '%' " +
            "OR user.alias_spelling_initial  like '%' || :matchSearch || '%' " +
            "order by user.order_spelling")
    LiveData<List<FriendShipInfo>> searchFriendShip(String matchSearch);

    @Query("SELECT user.id,user.name,user.portrait_uri FROM black_list INNER JOIN user ON black_list.id = user.id WHERE black_list.id=:userId")
    LiveData<UserSimpleInfo> getUserInBlackList(String userId);

    @Query("SELECT user.id,user.name,user.portrait_uri FROM black_list INNER JOIN user ON black_list.id = user.id")
    LiveData<List<UserSimpleInfo>> getBlackListUser();

    @Query("DELETE FROM black_list")
    void deleteAllBlackList();

    @Query("DELETE FROM black_list WHERE id=:id")
    void removeFromBlackList(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToBlackList(BlackListEntity entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateBlackList(List<BlackListEntity> blackListEntityList);

    @Query("SELECT friend.id as id ,alias,portrait_uri,name,region,phone_number,friend_status,message,updateAt,alias_spelling, name_spelling,order_spelling " +
            "FROM friend " +
            "left join user " +
            "on friend.id = user.id " +
            "where friend.id " +
            "not in (select DISTINCT(group_member.user_id) from group_member where group_member.group_id =:excluedGroupId) " +
            "order by user.order_spelling")
    LiveData<List<FriendShipInfo>> getAllFriendsExcluedGroup(String excluedGroupId);

    @Query("SELECT group_member.user_id as id ,alias,portrait_uri,name,region,phone_number,friend_status,alias_spelling, name_spelling,order_spelling,message, updateAt " +
            "FROM group_member " +
            "left join user " +
            "on group_member.user_id = user.id " +
            "left join friend " +
            "on group_member.user_id = friend.id " +
            "where group_member.group_id =:includeGroupId " +
            "order by user.order_spelling")
    LiveData<List<FriendShipInfo>> getFriendsIncludeGroup(String includeGroupId);

    @Query("DELETE FROM friend WHERE id=:friendId")
    void deleteFriend(String friendId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoneContactInfo(List<PhoneContactInfoEntity> contactInfoEntityList);

    @Query("SELECT phone_contact.user_id, phone_contact.phone_number, phone_contact.is_friend,phone_contact.contact_name, user.name, user.portrait_uri, user.name_spelling, user.st_account FROM phone_contact " +
            "LEFT JOIN user " +
            "ON phone_contact.user_id = user.id " +
            "ORDER BY user.order_spelling")
    LiveData<List<PhoneContactInfo>> getPhoneContactInfo();

    @Query("SELECT phone_contact.user_id, phone_contact.phone_number, phone_contact.is_friend,phone_contact.contact_name, user.name, user.portrait_uri, user.name_spelling, user.st_account FROM phone_contact " +
            "LEFT JOIN user " +
            "ON phone_contact.user_id = user.id " +
            "WHERE phone_contact.contact_name like '%' || :keyword || '%' " +
            "OR user.st_account like '%' || :keyword || '%'" +
            "ORDER BY user.order_spelling")
    LiveData<List<PhoneContactInfo>> searchPhoneContactInfo(String keyword);
}
