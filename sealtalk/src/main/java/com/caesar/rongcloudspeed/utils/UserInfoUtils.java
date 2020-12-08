package com.caesar.rongcloudspeed.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import static com.caesar.rongcloudspeed.quick.QiniuLabConfig.ADVERT_SERVICE_VIDEO;

/**
 * Created by mathum on 2017-09-26.
 */
public class UserInfoUtils {
    private static void initUserSp(Context context) {
        if (userSp == null) {
            userSp = context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        }
    }

    private static SharedPreferences userSp;

    public static final String FileName = "UserInfo";
    public static final String KeyAuthToken = "auth_token";
    public static final String KeyPhone = "phone";
    public static final String PayPassWord = "paypassword";
    public static final String UserName = "username";
    public static final String NickName = "nickname";
    public static final String UserEmail = "useremail";
    public static final String UserSum = "usersum";
    public static final String UserType = "usertype";
    public static final String UserIndustry = "UserIndustry";
    public static final String UserProfession = "UserProfession";
    public static final String UserSoft = "UserSoft";
    public static final String KeyFirstLogin = "firstLogin";
    public static final String KeyLoginState = "KeyLoginState";
    public static final String AppUserId = "AppUserId";    //id
    public static final String AppUserUrl = "AppUserUrl";   //用户头像
    public static final String AppUserLessones = "AppUserLessones";//用户已经购买的课程
    public static final String AppUserOrderSum = "AppUserOrderSum";//用户订单数
    public static final String UserFriendNum = "UserFriendNum";
    public static final String UserFocusNum = "UserFocusNum";
    public static final String UserPostNum = "UserPostNum";
    public static final String UserFocusPostNum = "UserFocusPostNum";    //用户关注帖子数量
    public static final String PersonalIdentifyState = "PersonalIdentifyState"; //个人实名
    public static final String CompanyIdentifyState = "CompanyIdentifyState";  //公司实名认证
    public static final String UserApplyNum = "UserApplyNum";   //好友申请数量
    public static final String TeamInvitationNum = "TeamInvitationNum";  //团队邀请数量
    public static final String AppMemberState = "AppMemberState";   //app会员
    public static final String APPBalance = "APPBalance";   //app余额
    public static final String APPNeed = "APPNeed";   //首次发布需求
    public static final String APPTeamTask = "APPTeamTask";   //首次发布任务

    public static final String UserInfoData1 = "UserInfoData1";
    public static final String UserInfoData2 = "UserInfoData2";
    public static final String UserInfoData3 = "UserInfoData3";
    public static final String UserInfoData4 = "UserInfoData4";
    public static final String UserInfoData5 = "UserInfoData5";
    public static final String UserInfoData6 = "UserInfoData6";

    public static final String WechatInfoData = "WechatInfoData";
    public static final String AdvertVideoUrl = "AdvertVideoUrl";
    public static final String AdvertVideoList = "AdvertVideoList";
    public static final String AdvertLessonList = "AdvertLessonList";
    public static final String BaiduToken = "BaiduToken";

    //首次发布任务
    public static boolean getAPPTeamTask(Context context) {
        initUserSp(context);
        boolean appTeamTask = userSp.getBoolean(APPTeamTask, true);
        return appTeamTask;
    }

    public static void setAPPTeamTask(boolean appTeamTask, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putBoolean(APPTeamTask, appTeamTask);
        editor.commit();
    }

    //首次发布需求
    public static boolean getAPPNeed(Context context) {
        initUserSp(context);
        boolean appNeed = userSp.getBoolean(APPNeed, true);
        return appNeed;
    }

    public static void setAPPNeed(boolean appNeed, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putBoolean(APPNeed, appNeed);
        editor.commit();
    }

    //app余额
    public static String getAPPBalance(Context context) {
        initUserSp(context);
        String appBalance = userSp.getString(APPBalance, "0");
        return appBalance;
    }

    public static void setAPPBalance(String appBalance, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(APPBalance, appBalance);
        editor.commit();
    }

    //是否会员
    public static int getAppMemberState(Context context) {
        initUserSp(context);
        int appMemberState = userSp.getInt(AppMemberState, 1);
        return appMemberState;
    }

    public static void setAppMemberState(int appMemberState, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(AppMemberState, appMemberState);
        editor.commit();
    }

    //群组邀请数量
    public static int getTeamInvitationNum(Context context) {
        initUserSp(context);
        int teamInvitationNum = userSp.getInt(TeamInvitationNum, 0);
        return teamInvitationNum;
    }

    public static void setTeamInvitationNum(int teamInvitationNum, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(TeamInvitationNum, teamInvitationNum);
        editor.commit();
    }

    //好友申请数量
    public static int getUserApplyNum(Context context) {
        initUserSp(context);
        int userApplyNum = userSp.getInt(UserApplyNum, 0);
        return userApplyNum;
    }

    public static void setUserApplyNum(int userApplyNum, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(UserApplyNum, userApplyNum);
        editor.commit();
    }

    //企业认证状态
    public static String getCompanyIdentifyState(Context context) {
        initUserSp(context);
        String companyIdentifyState = userSp.getString(CompanyIdentifyState, "2");
        return companyIdentifyState;
    }

    public static void setPCompanyIdentifyState(String companyIdentifyState, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(CompanyIdentifyState, companyIdentifyState);
        editor.commit();
    }

    //个人认证状态
    public static String getPersonalIdentify(Context context) {
        initUserSp(context);
        String personalIdentifyState = userSp.getString(PersonalIdentifyState, "2");
        return personalIdentifyState;
    }

    public static void setPersonalIdentifyState(String personalIdentifyState, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(PersonalIdentifyState, personalIdentifyState);
        editor.commit();
    }

    //行业
    public static String getUserIndustry(Context context) {
        initUserSp(context);
        String userIndustry = userSp.getString(UserIndustry, "0");
        return userIndustry;
    }

    public static void setUserIndustry(String userIndustry, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserIndustry, userIndustry);
        editor.commit();
    }

    //专业
    public static String getUserProfession(Context context) {
        initUserSp(context);
        String userIndustry = userSp.getString(UserProfession, "0");
        return userIndustry;
    }

    public static void setUserProfession(String userIndustry, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserProfession, userIndustry);
        editor.commit();
    }

    //行业
    public static String getUserSoft(Context context) {
        initUserSp(context);
        String userIndustry = userSp.getString(UserSoft, "0");
        return userIndustry;
    }

    public static void setUserSoft(String userIndustry, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserSoft, userIndustry);
        editor.commit();
    }

    //好友数
    public static int getUserFriendNum(Context context) {
        initUserSp(context);
        int userFriendNum = userSp.getInt(UserFriendNum, 0);
        return userFriendNum;
    }

    public static void setUserFriendNum(int userFriendNum, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(UserFriendNum, userFriendNum);
        editor.commit();
    }

    //关注的好友数
    public static int getUserFocusNum(Context context) {
        initUserSp(context);
        int userFocusNum = userSp.getInt(UserFocusNum, 0);
        return userFocusNum;
    }

    public static void setUserFocusNum(int userFocusNum, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(UserFocusNum, userFocusNum);
        editor.commit();
    }

    //发布的帖子数
    public static int getUserPostNum(Context context) {
        initUserSp(context);
        int userPostNum = userSp.getInt(UserPostNum, 0);
        return userPostNum;
    }

    public static void setUserPostNum(int userPostNum, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(UserPostNum, userPostNum);
        editor.commit();
    }

    //收藏的帖子数
    public static int getUserFocusPostNum(Context context) {
        initUserSp(context);
        int userFocusPostNum = userSp.getInt(UserFocusPostNum, 0);
        return userFocusPostNum;
    }

    public static void setUserFocusPostNum(int userFocusPostNum, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putInt(UserFocusPostNum, userFocusPostNum);
        editor.commit();
    }

    //获取个人订单
    public static String getAppUserOrderSum(Context context) {
        initUserSp(context);
        String appUserOrderSum = userSp.getString(AppUserOrderSum, "");
        return appUserOrderSum;
    }

    public static void setAppUserOrderSum(String appUserOrderSum, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(AppUserOrderSum, appUserOrderSum);
        editor.commit();
    }

    //个人头像信息
    public static String getAppUserUrl(Context context) {
        initUserSp(context);
        String appUserUrl = userSp.getString(AppUserUrl, "");
        return appUserUrl;
    }

    public static void setAppUserUrl(String appUserUrl, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(AppUserUrl, appUserUrl);
        editor.commit();
    }

    public static void setAppUserLessones(Set<String> appUserLessones, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putStringSet(AppUserLessones, appUserLessones);
        editor.commit();
    }

    //获取个人课程订单
    public static Set<String> getAppUserLessones(Context context) {
        initUserSp(context);
        Set<String> appUserLessones = userSp.getStringSet(AppUserLessones, null);
        return appUserLessones;
    }

    public static String getAuthToken(Context context) {
        initUserSp(context);
        String token = userSp.getString(KeyAuthToken, "");
        return token;
    }

    public static void setPhone(String authToken, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KeyPhone, authToken);
        editor.commit();
    }

    public static String getPhone(Context context) {
        initUserSp(context);
        String token = userSp.getString(KeyPhone, "15358801339");
        return token;
    }

    public static void setAuthToken(String authToken, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(KeyAuthToken, authToken);
        editor.commit();
    }

    public static boolean getFirstLogin(Context context) {
        initUserSp(context);
        return userSp.getBoolean(KeyFirstLogin, true);
    }

    public static void setLoginState(boolean isLogin, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putBoolean(KeyLoginState, isLogin);
        editor.commit();
    }

    public static boolean getLoginState(Context context) {
        initUserSp(context);
        return userSp.getBoolean(KeyLoginState, false);
    }

    public static String getAppUserId(Context context) {
        initUserSp(context);
        return userSp.getString(AppUserId, "0");
    }

    public static void setAppUserId(String appUserId, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(AppUserId, appUserId);
        editor.commit();
    }

    public static void setUserName(String name, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserName, name);
        editor.commit();
    }

    public static void setNikeName(String name, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(NickName, name);
        editor.commit();
    }

    public static void setUserEmail(String email, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserEmail, email);
        editor.commit();
    }

    public static String getUserEmail(Context context) {
        initUserSp(context);
        String name = userSp.getString(UserEmail, "");
        return name;
    }

    public static void setUserSum(String userSum, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserSum, userSum);
        editor.commit();
    }

    public static void setUserType(String userType, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserType, userType);
        editor.commit();
    }

    public static void setPayPassWord(String payPassWord, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(PayPassWord, payPassWord);
        editor.commit();
    }

    public static String getUserName(Context context) {
        initUserSp(context);
        String name = userSp.getString(UserName, "");
        return name;
    }

    public static String getNickName(Context context) {
        initUserSp(context);
        String name = userSp.getString(NickName, "");
        return name;
    }

    public static String getUserSum(Context context) {
        initUserSp(context);
        String usersum = userSp.getString(UserSum, "0.00");
        return usersum;
    }

    public static String getUserType(Context context) {
        initUserSp(context);
        String usertype = userSp.getString(UserType, "0");
        return usertype;
    }

    public static String getPayPassWord(Context context) {
        initUserSp(context);
        String usersum = userSp.getString(PayPassWord, "");
        return usersum;
    }

    public static void clear(Context context) {

        setAuthToken("", context);
        setLoginState(false, context);
        setUserName("", context);
        setUserEmail("", context);
        setNikeName("", context);
        setUserSum("", context);
        setUserType("", context);
        setPhone("", context);
        setPayPassWord("", context);
        setAppUserId("0", context);
        setAppUserUrl("", context);
        setAppUserLessones(null, context);
    }


    public static String getUserInfoData1(Context context) {
        initUserSp(context);
        String usersum = userSp.getString(UserInfoData1, "");
        return usersum;
    }

    public static String getUserInfoData2(Context context) {
        initUserSp(context);
        String usersum = userSp.getString(UserInfoData2, "");
        return usersum;
    }

    public static String getUserInfoData3(Context context) {
        initUserSp(context);
        String usersum = userSp.getString(UserInfoData3, "");
        return usersum;
    }

    public static String getUserInfoData4(Context context) {
        initUserSp(context);
        String usersum = userSp.getString(UserInfoData4, "");
        return usersum;
    }

    public static String getUserInfoData5(Context context) {
        initUserSp(context);
        String usersum = userSp.getString(UserInfoData5, "");
        return usersum;
    }

    public static String getUserInfoData6(Context context) {
        initUserSp(context);
        String usersum = userSp.getString(UserInfoData6, "");
        return usersum;
    }

    public static boolean getWechatInfoData(Context context) {
        initUserSp(context);
        boolean isWeChatPay = userSp.getBoolean(WechatInfoData, false);
        return isWeChatPay;
    }

    public static String getAdvertVideoUrl(Context context) {
        initUserSp(context);
        String advertVideoUrl = userSp.getString(AdvertVideoUrl, "");
        if (!advertVideoUrl.endsWith(".mp4")) {
            advertVideoUrl = ADVERT_SERVICE_VIDEO;
        }
        return advertVideoUrl;
    }

    public static Set<String> getAdvertVideoList(Context context) {
        initUserSp(context);
        Set<String> advertVideoList= userSp.getStringSet(AdvertVideoList, null);
        return advertVideoList;
    }

    public static String getAdvertLessonList(Context context) {
        initUserSp(context);
        String advertLessonstring = userSp.getString(AdvertLessonList, "");
        return advertLessonstring;
    }

    @SuppressLint("ApplySharedPref")
    public static void setAdvertVideoList(Set<String> advertVideoList, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putStringSet(AdvertVideoList, advertVideoList);
        editor.commit();
    }

    public static void setAdvertLessonList(String advertLessonstring, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(AdvertLessonList, advertLessonstring);
        editor.commit();
    }

    public static void setAdvertVideoUrl(String advertVideoUrl, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(AdvertVideoUrl, advertVideoUrl);
        editor.commit();
    }

    public static void setBaiduToken(String baiduToken, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(BaiduToken, baiduToken);
        editor.commit();
    }

    public static String getBaiduToken(Context context) {
        initUserSp(context);
        String advertVideoUrl = userSp.getString(BaiduToken, "");
        return advertVideoUrl;
    }

    public static void setWechatInfoData(boolean isWeChatPay, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putBoolean(WechatInfoData, isWeChatPay);
        editor.commit();
    }

    public static void setUserInfoData1(String userInfoData1, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserInfoData1, userInfoData1);
        editor.commit();
    }

    public static void setUserInfoData2(String userInfoData2, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserInfoData1, userInfoData2);
        editor.commit();
    }

    public static void setUserInfoData3(String userInfoData3, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserInfoData1, userInfoData3);
        editor.commit();
    }

    public static void setUserInfoData4(String userInfoData4, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserInfoData1, userInfoData4);
        editor.commit();
    }

    public static void setUserInfoData5(String userInfoData5, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserInfoData1, userInfoData5);
        editor.commit();
    }

    public static void setUserInfoData6(String userInfoData6, Context context) {
        initUserSp(context);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(UserInfoData1, userInfoData6);
        editor.commit();
    }

}
