package com.caesar.rongcloudspeed.network;


import com.caesar.rongcloudspeed.bean.AddressBean;
import com.caesar.rongcloudspeed.bean.AddressDefaultBean;
import com.caesar.rongcloudspeed.bean.AdminInBean;
import com.caesar.rongcloudspeed.bean.AppAdvertVideoBean;
import com.caesar.rongcloudspeed.bean.AppLessonAdvertBean;
import com.caesar.rongcloudspeed.bean.AppPeopleBaseBean;
import com.caesar.rongcloudspeed.bean.BaiduTextBean;
import com.caesar.rongcloudspeed.bean.BaiduTokenBean;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.CompanyBaseBean;
import com.caesar.rongcloudspeed.bean.GoodsCateBean;
import com.caesar.rongcloudspeed.bean.GoodsListBaseBean;
import com.caesar.rongcloudspeed.bean.GoodsListCartBean;
import com.caesar.rongcloudspeed.bean.GoodsOrderBaseBean;
import com.caesar.rongcloudspeed.bean.HomeArticleCommentsBean;
import com.caesar.rongcloudspeed.bean.HomeDataBaseBean;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.HomeDataLessonBean;
import com.caesar.rongcloudspeed.bean.HomeDataOtherBean;
import com.caesar.rongcloudspeed.bean.HomeDataUserBean;
import com.caesar.rongcloudspeed.bean.HomeLessonesBaseBean;
import com.caesar.rongcloudspeed.bean.HomeMainDataBean;
import com.caesar.rongcloudspeed.bean.HomeSeekListBean;
import com.caesar.rongcloudspeed.bean.LessonCateBean;
import com.caesar.rongcloudspeed.bean.NavCategoryBean;
import com.caesar.rongcloudspeed.bean.PersonCenterBean;
import com.caesar.rongcloudspeed.bean.PersonalCountData;
import com.caesar.rongcloudspeed.bean.PersonalInvoiceBean;
import com.caesar.rongcloudspeed.bean.PersonalMessageBean;
import com.caesar.rongcloudspeed.bean.PersonalPayListData;
import com.caesar.rongcloudspeed.bean.PersonalTagBean;
import com.caesar.rongcloudspeed.bean.PersonnelRecruitsBean;
import com.caesar.rongcloudspeed.bean.QiniuBaseBean;
import com.caesar.rongcloudspeed.bean.QiniuBean;
import com.caesar.rongcloudspeed.bean.RecruitApplyBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitWorkBaseBean;
import com.caesar.rongcloudspeed.bean.SectionLessonBaseBean;
import com.caesar.rongcloudspeed.bean.SectionLessonResonseBean;
import com.caesar.rongcloudspeed.bean.SectionMessageDataBean;
import com.caesar.rongcloudspeed.bean.SectionPersonalAlbumDataBean;
import com.caesar.rongcloudspeed.bean.UserLessonOrderListBean;
import com.caesar.rongcloudspeed.bean.UserListAddressBean;
import com.caesar.rongcloudspeed.bean.UserOrderBean;
import com.caesar.rongcloudspeed.bean.UserOrderListBean;
import com.caesar.rongcloudspeed.bean.UserRecruitMessageBean;
import com.caesar.rongcloudspeed.bean.WechatPayBaseBean;
import com.caesar.rongcloudspeed.bean.WechatPayCommonBean;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.Qiniu;
import com.caesar.rongcloudspeed.data.result.CircleHeaderResult;
import com.caesar.rongcloudspeed.data.result.CircleItemResult;
import com.caesar.rongcloudspeed.data.result.CircleItemResult1;
import com.caesar.rongcloudspeed.data.result.CommentsListData;
import com.caesar.rongcloudspeed.data.result.SmsCode;
import com.caesar.rongcloudspeed.data.result.TargetNumberData;
import com.caesar.rongcloudspeed.data.result.TransferDetailResult;
import com.caesar.rongcloudspeed.data.result.UserDataInfoResult;
import com.caesar.rongcloudspeed.data.result.UserInfoResult;
import com.caesar.rongcloudspeed.data.result.UserPayListResult;
import com.caesar.rongcloudspeed.data.result.UserSumResult;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    /*获取同行圈潜在用户数*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=list&a=getUserNumberForPath")
    Call<TargetNumberData> getNumberForTarget(@Field("user_industry") String user_industry, @Field("user_profession") String user_profession, @Field("user_soft") String user_soft);

    /*获取同行圈列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_json&page=1")
    Call<CircleItemResult> fetchFriendCircle(@Field("userid") String uid);

    /*获取同行圈列表广告*/
//    @FormUrlEncoded
    @GET("index.php?g=portal&m=list&a=circle_header_json")
    Call<CircleHeaderResult> headerFriendCircle();

    /*获取同行圈列表*/
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


    /*获取首页课程视频列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexjson&page=1")
    Call<HomeDataLessonBean> fetchHomeLessonLists(@Field("cid") String catid);

    /*获取视频课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexJsonWithQuery&page=1")
    Call<HomeDataBaseBean> fetchVoteListDatasWithTag(@Field("cid") String catid, @Field("query") String query);

    /*获取视频课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexjson&page=1")
    Call<HomeDataBean> fetchVoteListDataForPage(@Field("cid") String catid, @Field("page") String page);

    /*获取视频课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_home_json&page=1")
    Call<HomeDataBean> fetchHomeListDatas(@Field("cid") String catid);

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
    Call<HomeDataBean> fetchVoteListDataLimit(@Field("cid") String catid, @Field("limit") String limit);

    /*加入购物车*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=ajaxAddCart")
    Call<BaseData> ajaxAddCart(@Field("userid") String userid, @Field("goods_id") String goods_id, @Field("goods_num") String goods_num);

    /*购买APP课程*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=cart_lesson_order")
    Call<GoodsOrderBaseBean> cartLessonOrder(@Field("user_id") String user_id, @Field("lesson_id") String lesson_id, @Field("stotal_price") String stotal_price, @Field("pay_code") String pay_code, @Field("order_type") String order_type, @Field("user_note") String user_note);

    /*求助支付订单*/
    @FormUrlEncoded
    @POST("index.php?g=goods&m=cart&a=cart_lesson_order")
    Call<GoodsOrderBaseBean> cartSeekOrder(@Field("user_id") String user_id, @Field("lesson_id") String lesson_id, @Field("stotal_price") String stotal_price, @Field("pay_code") String pay_code, @Field("order_type") String order_type, @Field("user_note") String user_note);

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
    Call<GoodsListCartBean> ajaxUpdateCartChecked(@Field("cart_id") String cart_id, @Field("selected") String selected);

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

    /*获取相册列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_me_json")
    Call<CircleItemResult1> fetchAblum(@Field("userid") String userid);

    /*评论同行圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=post_comment_json")
    Call<BaseData> updateComment(@Field("uid") String uid, @Field("post_id") String postid, @Field("full_name") String full_name, @Field("content") String content);

    /*获取某一篇文章的评论*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=index_comment_list")
    Call<CommentsListData> getCommentsForArticle(@Field("post_id") String postid);

    /*点赞同行圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=do_like_json")
    Call<BaseData> updateSupport(@Field("userid") String uid, @Field("postid") String postid);

    /*点赞同行圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=cancel_like_json")
    Call<BaseData> cancelSupport(@Field("userid") String uid, @Field("postid") String postid);

    /*投票*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=do_like_json_vote")
    Call<BaseData> DoLikeVote(@Field("userid") String uid, @Field("postid") String postid);

    /*删除一条同行圈*/
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

    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> AddPostCartArticle(@Field("userID") String username, @Field("tid") String termId, @Field("post[post_title]") String post_title, @Field("post[post_mobile]") String post_mobile, @Field("post[thumb_video]") String thumb_video, @Field("post[post_excerpt]") String post_content);

    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> AddPostAdvertType(@Field("userID") String username, @Field("tid") String termId, @Field("post[post_title]") String post_title, @Field("post[post_mobile]") String post_mobile, @Field("post[thumb_video]") String thumb_video, @Field("post[post_excerpt]") String post_content, @Field("post[post_type]") String post_type);


    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> AddPostCircleArticle(@Field("userID") String username, @Field("tid") String termId, @Field("post[post_title]") String post_title, @Field("post[post_price]") String post_price, @Field("post[post_mobile]") String post_mobile,
                                        @Field("photos_url[]") String[] photos_url, @Field("post[post_excerpt]") String post_content, @Field("post[industry_id]") String industry_id, @Field("post[profession_id]") String profession_id, @Field("post[soft_id]") String soft_id);

    /*获取系统消息*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexJsonSectionMessage")
    Observable<SectionMessageDataBean> SectionMessageQuery(@Field("cid") String cid, @Field("query_date") String query_date);

    /*获取系统消息*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexMinePhotoJsonSection")
    Observable<SectionPersonalAlbumDataBean> getSectionPersonalPhoto(@Field("userid") String cid, @Field("query_date") String query_date);

    /*获取用户地址清单*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=address&a=ajaxAddressJson")
    Observable<AddressBean> address_list(@Field("userid") String userid);

    /*发布同行圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> updateFriendCircleData(@Field("userID") String uid, @Field("post[post_title]") String post, @Field("photos_url[]") String[] photos_url);

    /*发布同行圈*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> uploadFriendCircle(@Field("userID") String uid, @Field("tid") String term, @Field("post[post_title]") String post, @Field("photos_url[]") String[] photos_url);


    /*发布投票视频*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=add_post_json")
    Call<BaseData> addVoteArticle(@Field("userID") String uid, @Field("tid") String term, @Field("post[post_type]") String post_type, @Field("post[post_title]") String post_title, @Field("post[post_excerpt]") String post_excerpt, @Field("smeta[thumb]") String videoUrl);

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

    /*获取好友个人资料请求*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_user_forID")
    Call<UserSumResult> getUserDeatilInfo(@Field("userID") String userID);

    /*获取个人交易记录表*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_user_paylist")
    Call<UserPayListResult> getUserPayList(@Field("userID") String userID);

    /*获取单次交易详情*/
    //@FormUrlEncoded
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_user_paydetail")
    Call<TransferDetailResult> getTransferPayDetailData(@Field("userID") String userID, @Field("payid") String payid);

    /*修改同行快线用户昵称*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=do_avatar_json")
    Call<BaseData> updatenickname(@Field("userid") String userid, @Field("user_nicename") String nicename, @Field("imgurl") String imgurl);

    /*修改同行快线用户邮件*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=updateUserEmail")
    Call<BaseData> updateEmail(@Field("userid") String userid, @Field("user_email") String user_email);

    /*发送用户简历到邮箱*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=SendRecruitEmail")
    Call<BaseData> SendRecruitEmail(@Field("userid") String userid, @Field("recruit_id") String recruit_id);

    /*发送用户简历到邮箱*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=testPhpWordMail")
    Call<BaseData> testPhpWordMail(@Field("userid") String userid, @Field("recruit_id") String recruit_id);

    /*修改同行快线用户支付密码*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=reset_user_paypass")
    Call<BaseData> resetuserpaypass(@Field("userid") String userid, @Field("user_paypass") String user_paypass);

    /*修改同行快线用户登录密码*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=password_post_json")
    Call<BaseData> resetuserpass(@Field("userid") String userid, @Field("password") String password);

    /*同步同行快线用户好友资料*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=do_follow_rongcloud_json")
    Call<BaseData> updateFriendShip(@Field("userid") String userid, @Field("follow_uid") String follow_uid);

    /*获取用户个人资料*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=get_user_info")
    Call<UserDataInfoResult> getUserDataInfo(@Field("user_id") String user_id);

    /*获取用户个人资料*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=getPersonalMessage")
    Observable<PersonalMessageBean> personal_center(@Field("user_id") String user_id);

    @GET("index.php")
    Observable<HomeDataUserBean> HomePersonalData(@Query("g") String group, @Query("m") String model, @Query("a") String action, @Query("cid") String cid, @Query("userid") String userid);

    @GET("index.php")
    Observable<HomeSeekListBean> HomeSeekListData(@Query("g") String group, @Query("m") String model, @Query("a") String action, @Query("cid") String cid, @Query("userid") String userid);

    @GET("index.php")
    Observable<HomeDataBean> ReplayData(@Query("g") String group, @Query("m") String model, @Query("a") String action);

    @GET("index.php")
    Observable<NavCategoryBean> RequestMenuDate(@Query("g") String group, @Query("m") String model, @Query("a") String action, @Query("parent") String parentID);

    @GET("index.php")
    Observable<HomeMainDataBean> HomeMainData(@Query("g") String group, @Query("m") String model, @Query("a") String action);

    @GET("index.php")
    Observable<QiniuBean> getQiniuToken(@Query("g") String group, @Query("m") String model, @Query("a") String action);

    @FormUrlEncoded
    @POST("index.php?g=portal&m=public&a=indexPersonalMessageJson")
    Observable<HomeDataUserBean> getPersonalMessageData(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("index.php?g=portal&m=article&a=remove_post_json")
    Observable<CommonResonseBean> RemovePostData(@Field("tid") String tid);

    @FormUrlEncoded
    @POST("index.php?g=User&m=index&a=get_user_taglist")
    Observable<PersonalTagBean> getPersonalTagData(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("index.php?g=User&m=index&a=set_user_taglist")
    Observable<CommonResonseBean> setPersonalTagData(@Field("user_id") String user_id, @Field("user_industry") String industryIDString, @Field("user_profession") String professionIDString, @Field("user_soft") String softIDString);

    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexUserSeekJson")
    Observable<HomeSeekListBean> indexUserSeekJson(@Field("cid") String cid, @Field("userid") String userid);

    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexSeekListJson")
    Observable<HomeSeekListBean> indexSeekListJson(@Field("userid") String userid, @Field("cid") String cid, @Field("condition") String condition);

    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexSeekListJson")
    Observable<HomeSeekListBean> indexAvertkListJson(@Field("userid") String userid, @Field("cid") String cid, @Field("tag") String tag);

    /*获取用户增值税资质资料*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=get_user_invoice")
    Observable<PersonalInvoiceBean> getUserInvoiceData(@Field("user_id") String user_id);

    /*获取用户增值税资质资料*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=edit_user_invoice")
    Observable<CommonResonseBean> editUserInvoiceData(@Field("user_id") String user_id, @Field("invoice_name") String invoice_name, @Field("invoice_identifi") String invoice_identifi,
                                                      @Field("invoice_address") String user_address, @Field("invoice_phone") String user_friend, @Field("invoice_bank") String invoice_bank, @Field("invoice_account") String invoice_account);

    /*获取APP支付签名资质*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=getWechatSign")
    Call<WechatPayBaseBean> WechatAppPaySign(@Field("user_id") String user_id, @Field("out_trade_no") String out_trade_no, @Field("total_fee") String total_fee, @Field("body") String body);

    /*获取APP支付签名资质*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=getWechatSign")
    Observable<WechatPayCommonBean> getWechatAppPay(@Field("user_id") String user_id, @Field("out_trade_no") String out_trade_no, @Field("total_fee") String total_fee, @Field("body") String body);

    /*获取用户订单列表*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=myorderjson")
    Observable<UserOrderListBean> getUserOrderJson(@Field("user_id") String user_id, @Field("order_status") String order_status);

    /*获取开机广告列表*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=getAppAdvertVideo")
    Call<AppAdvertVideoBean> getAppAdvertVideo(@Field("user_id") String user_id);

    /*获取课程广告列表*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=getAppAdvertLesson")
    Call<AppLessonAdvertBean> getAppAdvertLesson(@Field("user_id") String user_id);

    /*获取推荐用户*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=getAppRecommendMan")
    Call<AppPeopleBaseBean> getAppRecommendMan(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=do_favorite_json")
    Observable<CommonResonseBean> DoFavorite(@Field("uid") String uid, @Field("title") String title, @Field("table") String table, @Field("object_id") String id);

    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=do_favorite_json")
    Observable<CommonResonseBean> DoFavoriteMobile(@Field("uid") String uid, @Field("title") String title, @Field("table") String table, @Field("object_id") String id, @Field("description") String description);

    /*获取用户购买课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=getUserOrderLesson")
    Observable<UserLessonOrderListBean> getUserOrderLesson(@Field("user_id") String user_id, @Field("order_type") String order_type);

    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=indexUserFavouriteJson")
    Observable<HomeDataUserBean> indexUserFavouriteJson(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=personalInfoData")
    Observable<PersonalCountData> personalInfoData(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=get_user_paylist")
    Observable<PersonalPayListData> get_user_paylist(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_lesson_json")
    Observable<SectionLessonResonseBean> index_lesson_json(@Field("cid") String cid);

    /*获取视频课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_lesson_json")
    Call<SectionLessonBaseBean> getIndexLessonData(@Field("cid") String catid);

    /*获取视频课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_recommand_lesson")
    Call<HomeDataBaseBean> getRecommandLessonData(@Field("cid") String catid);

    /*获取视频课程列表*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=index_recommand_lesson")
    Call<HomeLessonesBaseBean> getRecommandLessonVideoData(@Field("cid") String catid);

    /*用户永久注销账户申请*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=index&a=cancellation_post_json")
    Call<BaseData> userCancellation(@Field("user_id") String user_id);

    /*获取百度AI接口token*/
    @FormUrlEncoded
    @POST("oauth/2.0/token")
    Call<BaiduTokenBean> getBaiduToken(@Field("grant_type") String grant_type, @Field("client_id") String client_id, @Field("client_secret") String client_secret);

    /*获取百度AI接口文本检测*/
    @FormUrlEncoded
    @POST("rest/2.0/solution/v1/text_censor/v2/user_defined")
    Call<BaiduTextBean> getBaiduTextCheck(@Field("text") String text, @Field("access_token") String access_token);

    @FormUrlEncoded
    @POST("index.php?g=portal&m=list&a=indexJsonByQuery")
    Observable<HomeDataUserBean> HomeDataQueryByPage(@Field("cid") String cid, @Field("query") String query, @Field("rule") String rule, @Field("category") String category, @Field("region") String region, @Field("page") String page);

    @GET("index.php")
    Observable<HomeDataOtherBean> HomeData(@Query("g") String group, @Query("m") String model, @Query("a") String action, @Query("cid") String cid);

    /*获取同行快线简历*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=get_user_recruit")
    Observable<UserRecruitMessageBean> LoadPersonalRecruitData(@Field("user_id") String user_id);

    /*提交简历*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=add_user_recruit")
    Call<BaseData> addUserRecruit(@Field("user_id") String user_id, @Field("recruit_name") String recruit_name, @Field("recruit_avatar") String recruit_avatar, @Field("recruit_sex") String recruit_sex, @Field("recruit_birthday") String recruit_birthday, @Field("recruit_age") String recruit_age,@Field("recruit_mobile") String recruit_mobile,
                                  @Field("recruit_email") String recruit_email,@Field("recruit_natives") String recruit_natives, @Field("recruit_native") String recruit_native, @Field("recruit_nation") String recruit_nation, @Field("recruit_marry") String recruit_marry, @Field("recruit_show") String recruit_show);

    /*提交简历*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=update_user_recruit")
    Call<BaseData> updateUserRecruit(@Field("recruit_id") String recruit_id, @Field("recruit_name") String recruit_name, @Field("recruit_avatar") String recruit_avatar, @Field("recruit_sex") String recruit_sex, @Field("recruit_birthday") String recruit_birthday, @Field("recruit_age") String recruit_age,@Field("recruit_mobile") String recruit_mobile,
                                     @Field("recruit_email") String recruit_email, @Field("recruit_natives") String recruit_natives,@Field("recruit_native") String recruit_native, @Field("recruit_nation") String recruit_nation, @Field("recruit_marry") String recruit_marry, @Field("recruit_show") String recruit_show);


    /*提交简历*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=set_user_recruit")
    Call<BaseData> updateUserRecruitJob(@Field("user_id") String user_id, @Field("recruit_job") String recruit_job, @Field("recruit_place") String recruit_place, @Field("recruit_salary") String recruit_salary, @Field("recruit_workingyears") String recruit_workingyears, @Field("recruit_experience") String recruit_experience);

    /*提交简历*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=public&a=set_user_recruit")
    Call<BaseData> updateUserRecruitEducare(@Field("user_id") String user_id, @Field("recruit_grade") String recruit_grade, @Field("recruit_major") String recruit_major, @Field("recruit_school") String recruit_school, @Field("recruit_education") String recruit_education, @Field("recruit_expert1") String recruit_expert1);

    /*获取推荐人才简历*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=list&a=get_personnel_recruits")
    Observable<PersonnelRecruitsBean> getRecommandPersonnelist(@Field("recruit_salary") String recruit_salary, @Field("recruit_grade") String recruit_grade, @Field("recruit_place") String recruit_place);

    /*获取公司信息*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=company&a=getCompany")
    Observable<CompanyBaseBean> getRecruitCompany(@Field("company_id") String company_id);

    /*创建公司信息*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=company&a=addCompany")
    Call<BaseData> addRecruitCompany(@Field("user_id") String user_id, @Field("company_name") String company_name, @Field("company_nature") String company_nature,
                                     @Field("company_size") String company_size, @Field("company_licence") String company_licence, @Field("company_address") String company_address,
                                     @Field("company_contact") String company_contact, @Field("company_phone") String company_phone);


    /*创建公司信息*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=company&a=editCompany")
    Observable<CommonResonseBean> editRecruitCompany(@Field("company_id") String company_id, @Field("user_id") String user_id, @Field("company_name") String company_name, @Field("company_nature") String company_nature,
                                                     @Field("company_size") String company_size, @Field("company_licence") String company_licence, @Field("company_address") String company_address,
                                                     @Field("company_contact") String company_contact, @Field("company_phone") String company_phone);

    /*发布职位招聘信息
    * `post_id` mediumint(8) unsigned NOT NULL COMMENT '职位ID',
  `post_author` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '职位者关联ID',
  `post_title` text CHARACTER SET utf8 NOT NULL COMMENT '求职标题',
  `post_date` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '职位发布日期',
  `post_area_code` int(11) unsigned NOT NULL COMMENT '职位发布地区编码',
  `post_area_name` varchar(64) NOT NULL COMMENT '职位发布地区名称',
  `post_salary` smallint(2) NOT NULL COMMENT '职位发布薪资',
  `post_grade` smallint(2) NOT NULL COMMENT '职位发布学历',
  `post_mobile` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '职位发布手机号',
  `post_tag` smallint(2) unsigned NOT NULL COMMENT '职位发布手机号',
  `post_level` smallint(2) NOT NULL COMMENT '职位级别',
  `post_status` smallint(2) NOT NULL COMMENT '职位状态'
    * */
    @FormUrlEncoded
    @POST("index.php?g=user&m=RecruitPost&a=addRecruitPost")
    Call<BaseData> addRecruitPost(@Field("post_author") String post_author, @Field("post_title") String post_title, @Field("post_area_code") String post_area_code, @Field("post_area_name") String post_area_name,
                                  @Field("post_salary") String post_salary, @Field("post_grade") String post_grade, @Field("post_level") String post_level, @Field("post_mobile") String post_mobile, @Field("post_excerpt") String post_excerpt);

    @FormUrlEncoded
    @POST("index.php?g=user&m=RecruitPost&a=editRecruitPost")
    Call<BaseData> editRecruitPost(@Field("post_id") String post_id, @Field("post_title") String post_title, @Field("post_area_code") String post_area_code, @Field("post_area_name") String post_area_name,
                                  @Field("post_salary") String post_salary, @Field("post_grade") String post_grade, @Field("post_level") String post_level, @Field("post_mobile") String post_mobile, @Field("post_excerpt") String post_excerpt);

    @FormUrlEncoded
    @POST("index.php?g=user&m=RecruitPost&a=getRecruitPostList")
    Observable<RecruitJobBaseBean> getRecruitPostList(@Field("post_salary") String post_salary, @Field("post_grade") String post_grade, @Field("post_area_code") String post_area_code);

    /*获取推荐用户*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=list&a=getPersonnelPostList")
    Call<RecruitWorkBaseBean> getRecruitPostLists(@Field("user_id") String user_id);

    /*申请职位*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=list&a=applyRecruitPost")
    Call<BaseData> applyRecruitPost(@Field("user_id") String user_id,@Field("post_id") String post_id);

    @FormUrlEncoded
    @POST("index.php?g=user&m=RecruitPost&a=getUserRecruitList")
    Observable<RecruitJobBaseBean> getUserRecruitList(@Field("user_id") String user_id);

    /*获取推荐人才简历*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=list&a=getApplyRecruits")
    Observable<RecruitApplyBaseBean> getApplyRecruitList(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("index.php?g=comment&m=comment&a=post_comment_json")
    Observable<HomeArticleCommentsBean> PostArticleComment(@Field("uid") String uid, @Field("post_id") String post_id, @Field("full_name") String full_name, @Field("content") String content);

    /*点赞文章*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=postsfollow&a=post_follow_json")
    Observable<CommonResonseBean> postFollowAction(@Field("userid") String userid,@Field("postid") String postid);

    /*取消文章点赞*/
    @FormUrlEncoded
    @POST("index.php?g=user&m=postsfollow&a=cancel_follow_json")
    Observable<CommonResonseBean> cancelFollowAction(@Field("userid") String userid,@Field("postid") String postid);

    @FormUrlEncoded
    @POST("index.php?g=comment&m=comment&a=index_comment_list")
    Observable<HomeArticleCommentsBean> IndexArticleComment(@Field("post_id") String post_id);

    /*获取七牛云token*/
    @FormUrlEncoded
    @POST("index.php?g=portal&m=index&a=get_QiniuAuthJSON")
    Call<QiniuBaseBean> getQiniuToken(@Field("userid") String userid);

}
