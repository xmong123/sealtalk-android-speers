package com.caesar.rongcloudspeed.network;


import com.caesar.rongcloudspeed.bean.AddressBean;
import com.caesar.rongcloudspeed.bean.AddressDefaultBean;
import com.caesar.rongcloudspeed.bean.AdminInBean;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.GoodsCateBean;
import com.caesar.rongcloudspeed.bean.GoodsListBaseBean;
import com.caesar.rongcloudspeed.bean.GoodsListCartBean;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.LessonCateBean;
import com.caesar.rongcloudspeed.bean.LessonCategoryBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.UserListAddressBean;
import com.caesar.rongcloudspeed.bean.UserOrderBean;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.Qiniu;
import com.caesar.rongcloudspeed.data.UserInfo;
import com.caesar.rongcloudspeed.data.result.CircleItemResult;
import com.caesar.rongcloudspeed.data.result.CircleItemResult1;
import com.caesar.rongcloudspeed.data.result.SmsCode;
import com.caesar.rongcloudspeed.data.result.TransferDetailResult;
import com.caesar.rongcloudspeed.data.result.UserDataInfoResult;
import com.caesar.rongcloudspeed.data.result.UserInfoResult;
import com.caesar.rongcloudspeed.data.result.UserPayListResult;
import com.caesar.rongcloudspeed.data.result.UserSumResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mathum on 2017/1/4.
 */

public interface Api {

    /*获取验证码*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=register&a=doregisterPhoneSendCode")
    Call<SmsCode> fetchSmsCode(@Field("mobile") String mobile);

    /*获取验证码*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=register&a=doregisterPhoneSendCode")
    Observable<CommonResonseBean> sendCode(@Field("mobile") String mobile);

    /*注册手机号*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=register&a=doregisterjson")
    Call<BaseData> register(@Field("mobile") String mobile, @Field("password") String password, @Field("mobile_verify") String mobile_verify);

    /*注册手机号*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=register&a=doregisterjson")
    Call<BaseData> register1(@Field("rongid") String rongid, @Field("mobile") String mobile, @Field("user_login") String username, @Field("password") String password, @Field("user_nicename") String nicename, @Field("mobile_verify") String mobile_verify, @Field("avatar") String avatar);

    /*login*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=login&a=dologinjson")
    Call<UserInfoResult> login(@Field("username") String mobile, @Field("password") String password);

    /*updateIndustry*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=edit_user_industry")
    Call<BaseData> updateIndustry(@Field("userid") String userid, @Field("user_industry") String user_industry, @Field("user_profession") String user_profession, @Field("user_soft") String user_soft);

    /*重置密码*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=login&a=do_mobile_forgot_password_json")
    Call<BaseData> updatePwd(@Field("mobile") String mobile, @Field("password") String password, @Field("mobile_verify") String mobile_verify);

    /*获取朋友圈列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_json&page=1")
    Call<CircleItemResult> fetchFriendCircle(@Field("userid") String uid);

    /*获取朋友圈列表*/
    //@FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexjson&page=1")
    Call<HomeDataBean> fetchVoteListData();

    /*获取用户行业分类列表*/
//  @FormUrlEncoded
    @GET("index.php?g=user&m=list&a=user_industry_json")
    Call<AdminInBean> AdminIndustryBeanDatas();

    /*获取用户专业分类列表*/
//    @FormUrlEncoded
    @GET("index.php?g=user&m=list&a=user_profession_json")
    Call<AdminInBean> AdminProfessionBeanDatas();

    /*获取用户软件分类列表*/
//    @FormUrlEncoded
    @GET("index.php?g=user&m=list&a=user_soft_json")
    Call<AdminInBean> AdminSoftBeanDatas();

    /*获取课程分类列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=nav_index_json&page=1")
    Call<LessonCateBean> fetchVoteCateListDatas(@Field("cid") String cid);

    /*获取视频课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexjson&page=1")
    Call<HomeDataBean> fetchVoteListDatas(@Field("cid") String catid);

    /*获取视频书籍分类列表*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=list&a=nav_index_json&page=1")
    Call<GoodsCateBean> fetchGoodsCateListData(@Field("parent") String catid);

    /*获取视频书籍商品列表*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=list&a=index_json&page=1")
    Call<GoodsListBaseBean> fetchGoodsListDataes(@Field("cid") String catid);

    /*获取视频课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexjson&page=1")
    Call<HomeDataBean> fetchVoteListDataLimit(@Field("cid") String catid,@Field("limit") String limit);

    /*加入购物车*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=ajaxAddCart")
    Call<BaseData> ajaxAddCart(@Field("userid") String userid,@Field("goods_id") String goods_id,@Field("goods_num") String goods_num);

    /*获取购物车清单*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=ajaxCartJsonList")
    Call<GoodsListCartBean> ajaxAddCart(@Field("userid") String userid);

    /*修改购物车清单*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=updateCartJsonData")
    Call<GoodsListCartBean> ajaxUpdateCart(@Field("cart_id") String cart_id, @Field("goods_num") String goods_num);

    /*修改购物车选中*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=updateCartJsonChecked")
    Call<GoodsListCartBean> ajaxUpdateCartChecked(@Field("cart_id") String cart_id,@Field("selected") String selected);

    /*删除购物车清单*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=ajaxDelCartJson")
    Call<GoodsListCartBean> ajaxDeleteCart(@Field("cart_id") String cart_id);

    /*获取用户地址清单*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=address&a=ajaxAddressJson")
    Call<UserListAddressBean> ajaxAddressJson(@Field("userid") String userid);

    /*获取用户默认收获地址*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=address&a=getDefaultAddressJson")
    Observable<AddressDefaultBean> getDefaultAddressJson(@Field("userid") String userid);

    /*新增用户收获地址*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=address&a=addAddressJson")
    Observable<AddressDefaultBean> addAddressJson(@Field("user_id") String userid, @Field("consignee") String consignee, @Field("province") String province, @Field("city") String city, @Field("mobile") String mobile, @Field("address") String address);

    /*删除用户收获地址*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=address&a=delAddressJson")
    Observable<CommonResonseBean> delAddressJson(@Field("address_id") String address_id);

    /*设置用户默认收获地址*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=address&a=setDefaultAddressJson")
    Observable<CommonResonseBean> setDefaultAddressJson(@Field("user_id") String userid, @Field("address_id") String address_id);

    /*提交用户订单*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=addOrderJson")
    Observable<UserOrderBean> addNewOrderJson(@Field("userid") String userid, @Field("address_id") String address_id);

    /*获取用户地址清单*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=address&a=ajaxAddressJson")
    Observable<AddressBean> address_list(@Field("userid") String userid);

    /*获取相册列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_me_json")
    Call<CircleItemResult1> fetchAblum(@Field("userid") String userid);

    /*评论朋友圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=post_comment_json")
    Call<BaseData> updateComment(@Field("uid") String uid, @Field("post_id") String postid, @Field("full_name") String full_name, @Field("content") String content);

    /*点赞朋友圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=do_like_json")
    Call<BaseData> updateSupport(@Field("userid") String uid, @Field("postid") String postid);

    /*投票*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=do_like_json_vote")
    Call<BaseData> DoLikeVote(@Field("userid") String uid, @Field("postid") String postid);

    /*删除一条朋友圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=delete_posts_json")
    Call<BaseData> deleteSupport(@Field("userid") String uid, @Field("postid") String postid);

    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=do_hits_json")
    Call<BaseData> DoHits(@Field("userid") String userid, @Field("title") String title, @Field("table") String table, @Field("object_id") String id);


    /*获取qiniu Token*/
    @GET("index.php?g=portal&m=index&a=get_QiniuAuthJSON")
    Call<Qiniu> fetchQiniuToken();

    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> AddPostCartArticle(@Field("userID") String username, @Field("tid") String termId, @Field("post[post_title]") String post_title, @Field("post[post_mobile]") String post_mobile, @Field("photos_url[]") String[] photos_url, @Field("post[post_excerpt]") String post_content);


    /*发布朋友圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> updateFriendCircle(@Field("userID") String uid, @Field("post[post_title]") String post, @Field("photos_url[]") String[] photos_url);

    /*发布投票视频*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> addVoteArticle(@Field("userID") String uid, @Field("tid") String term, @Field("post[post_title]") String post, @Field("smeta[thumb]") String videoUrl);

    /*二维码付款交易请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=getcode_to_user")
    Call<UserSumResult> getCodeToUser(@Field("user_sum_add") String user_sum_add, @Field("sum_touserID") String sum_touserID, @Field("sum_foruserID") String sum_foruserID);

    /*二维码付款交易请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=getcode_to_username")
    Call<UserSumResult> getCodeToUserName(@Field("user_sum_add") String user_sum_add, @Field("sum_touserID") String sum_touserID, @Field("sum_foruserName") String sum_foruserName);

    /*二维码付款交易请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=getcode_to_userid")
    Call<UserSumResult> getCodeToUserRID(@Field("user_sum_add") String user_sum_add, @Field("sum_touserID") String sum_touserID, @Field("sum_foruserRID") String sum_foruserRID);


    /*同行快线转账发起交易请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=transfer_to_username")
    Call<UserSumResult> transferCodeToUserName(@Field("user_sum_add") String user_sum_add, @Field("sum_touserID") String sum_touserID, @Field("sum_foruserName") String sum_foruserName);

    /*同行快线转账发起交易请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=transfer_to_userID")
    Call<UserSumResult> transferCodeToUserRID(@Field("user_sum_add") String user_sum_add, @Field("sum_touserID") String sum_touserID, @Field("sum_foruserRID") String sum_foruserRID);


    /*同行快线转账确认收款请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=transfer_confirm_username")
    Call<UserSumResult> confirmCodeToUserName(@Field("pay_id") String pay_id, @Field("sum_foruserID") String sum_foruserID, @Field("user_sum_add") String user_sum_add);


    /*二维码收款交易请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=getcode_for_user")
    Call<UserSumResult> getCodeForUser(@Field("user_sum_add") String user_sum_add, @Field("sum_touserID") String sum_touserID, @Field("sum_foruserID") String sum_foruserID);

    /*获取用户个人资料请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_sum_user")
    Call<UserSumResult> getSumUser(@Field("userID") String userID);

    /*获取好友个人资料请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_userid_forname")
    Call<UserSumResult> getUserInfo(@Field("username") String username);

    /*获取好友个人资料请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_userid_forRID")
    Call<UserSumResult> getUserRIDInfo(@Field("userRID") String userRID);

    /*获取个人交易记录表*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_user_paylist")
    Call<UserPayListResult> getUserPayList(@Field("userID") String userID);

    /*获取单次交易详情*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_user_paydetail")
    Call<TransferDetailResult> getTransferPayDetailData(@Field("userID") String userID,@Field("payid") String payid);

    /*修改同行快线用户昵称*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=do_avatar_json")
    Call<BaseData> updatenickname(@Field("userid") String userid, @Field("user_nicename") String nicename, @Field("imgurl") String imgurl);

    /*修改同行快线用户支付密码*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=reset_user_paypass")
    Call<BaseData> resetuserpaypass(@Field("userid") String userid, @Field("user_paypass") String user_paypass);

    /*修改同行快线用户登录密码*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=profile&a=password_post_json")
    Call<BaseData> resetuserpass(@Field("userid") String userid, @Field("password") String password);

    /*同步同行快线用户好友资料*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=do_follow_rongcloud_json")
    Call<BaseData> updateFriendShip(@Field("userid") String userid, @Field("follow_uid") String follow_uid);

    /*获取用户个人资料*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=get_user_info")
    Call<UserDataInfoResult> getUserDataInfo(@Field("user_id") String user_id);

    /*更新用户个人资料*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=edit_user_info")
    Call<BaseData> edituserinfo(@Field("user_id") String user_id,@Field("real_name") String real_name,@Field("user_idnumber") String user_idnumber,@Field("user_address") String user_address,@Field("user_friend") String user_friend,@Field("friend_mobile") String friend_mobile,@Field("user_cardphoto") String user_cardphoto);


}
