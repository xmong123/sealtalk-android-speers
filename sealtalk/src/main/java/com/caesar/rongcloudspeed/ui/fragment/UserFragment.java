package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.bean.PersonalMessageBean;
import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.model.qrcode.QrCodeDisplayType;
import com.caesar.rongcloudspeed.ui.activity.AccountSettingActivity;
import com.caesar.rongcloudspeed.ui.activity.AnimationPersonalTagActivity;
import com.caesar.rongcloudspeed.ui.activity.MyAccountActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonalAdvertListActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonalAlbumSectionActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonalAlbumVideoActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonalOrderListActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonalSeekListActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonalLessonListActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonalWalletActivity;
import com.caesar.rongcloudspeed.ui.activity.QrCodeDisplayActivity;
import com.caesar.rongcloudspeed.ui.dialog.CommonDialog;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import com.caesar.rongcloudspeed.utils.UserInfoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.ui.activity.LoginActivity;
import com.caesar.rongcloudspeed.bean.EventBusBaseBean;
import com.caesar.rongcloudspeed.bean.PersonCenterBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.rxlife.RxFragment;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.rong.imkit.RongIM;

/**
 *
 */
public class UserFragment extends RxFragment implements OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.myOrderStv)
    SuperTextView myOrderStv;
    @BindView(R.id.myInviteCodeStv)
    SuperTextView myInviteCodeStv;
    @BindView(R.id.settingStv)
    SuperTextView settingStv;
    @BindView(R.id.aboutMeStv)
    SuperTextView aboutMeStv;
    @BindView(R.id.helpStv)
    SuperTextView helpStv;
    @BindView(R.id.volumeStv)
    SuperTextView volumeStv;
    @BindView(R.id.logoutStv)
    SuperTextView logoutStv;
    @BindView(R.id.personalMoney)
    TextView personalMoney;
    @BindView(R.id.personalLesson)
    TextView personalLesson;
    @BindView(R.id.personalOrder)
    TextView personalOrder;
    Unbinder unbinder;
    @BindView(R.id.user_tip_layout01)
    LinearLayout user_tip_layout01;
    @BindView(R.id.user_tip_layout02)
    LinearLayout user_tip_layout02;
    @BindView(R.id.user_tip_layout03)
    LinearLayout user_tip_layout03;
    @BindView(R.id.withdrawTv)
    TextView withdrawTv;
    @BindView(R.id.userHeaderImage)
    ImageView userHeaderImage;
    @BindView(R.id.redTips)
    TextView redTips;
    @BindView(R.id.nameTv)
    TextView nameTv;
    @BindView(R.id.teleTv)
    TextView teleTv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String uidString;
    private Set<String> orderSet;
    private String userOrderSum;
    private String mParam1;
    private String mParam2;
    private String moneyString = "0";
    private String lessonCounString = "0";
    private String orderCountString = "0";

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_speer, container, false);
        unbinder = ButterKnife.bind(this, view);
        refreshLayout.setOnRefreshListener(this);
        uidString = UserInfoUtils.getAppUserId(getActivity());
        orderSet = UserInfoUtils.getAppUserLessones(getActivity());
        moneyString = UserInfoUtils.getUserSum(getActivity());
        orderCountString = UserInfoUtils.getAppUserOrderSum(getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.userHeaderImage, R.id.siv_setting_qrcode, R.id.userHeaderLayout, R.id.myOrderStv, R.id.myInviteCodeStv, R.id.settingStv, R.id.aboutMeStv, R.id.helpStv, R.id.user_tip_layout01, R.id.user_tip_layout02, R.id.user_tip_layout03, R.id.personalMoney, R.id.personalLesson, R.id.volumeStv, R.id.logoutStv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.userHeaderImage:
                //我的个人信息
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.userHeaderLayout:
                //我的个人信息
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.siv_setting_qrcode:
                //我的二维码
                Intent qrCodeIntent = new Intent(getActivity(), QrCodeDisplayActivity.class);
                qrCodeIntent.putExtra(IntentExtra.STR_TARGET_ID, RongIM.getInstance().getCurrentUserId());
                qrCodeIntent.putExtra(IntentExtra.SERIA_QRCODE_DISPLAY_TYPE, QrCodeDisplayType.PRIVATE);
                startActivity(qrCodeIntent);
                break;
            case R.id.myOrderStv:
                //同行求助
                startActivity(new Intent(getActivity(), PersonalSeekListActivity.class));
                break;
            case R.id.myInviteCodeStv:
                //我的标签
                startActivity(new Intent(getActivity(), AnimationPersonalTagActivity.class));
                break;
            case R.id.settingStv:
                //我的动态
                startActivity(new Intent(getActivity(), PersonalAdvertListActivity.class));
                break;
            case R.id.aboutMeStv:
                //我的相册
                startActivity(new Intent(getActivity(), PersonalAlbumSectionActivity.class));
                break;
            case R.id.helpStv:
                //我的视频
                startActivity(new Intent(getActivity(), PersonalAlbumVideoActivity.class));
                break;
            case R.id.user_tip_layout01://我的钱包PersonalWalletActivity
                startActivity(new Intent(getActivity(), PersonalWalletActivity.class));
                break;
            case R.id.user_tip_layout02://我的课程
                startActivity(new Intent(getActivity(), PersonalLessonListActivity.class));
                break;
            case R.id.user_tip_layout03://我的订单
                startActivity(new Intent(getActivity(), PersonalOrderListActivity.class));
                break;
            case R.id.volumeStv:
                break;
            case R.id.logoutStv:
//                    startActivity(new Intent(getActivity(), AccountSettingActivity.class));
                showLoginOutDialog();
                return;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uidString = UserInfoUtils.getAppUserId(getActivity());
        if (!uidString.equals("0")) {
            nameTv.setText(UserInfoUtils.getNickName(getActivity()));
            String phonenum = UserInfoUtils.getPhone(getActivity());
            if (phonenum.length() > 10) {
                phonenum = phonenum.substring(0, 3) + "****" + phonenum.substring(7, 11);
            }
            teleTv.setText(phonenum);
            String userAvatarString = UserInfoUtils.getAppUserUrl(getActivity());
            if (userAvatarString.length() > 32 && !userAvatarString.startsWith("avatar")) {
                Glide.with(UserFragment.this).load(userAvatarString).into(userHeaderImage);
            }
        }
        orderSet = UserInfoUtils.getAppUserLessones(getActivity());
        moneyString = UserInfoUtils.getUserSum(getActivity());
        orderCountString = UserInfoUtils.getAppUserOrderSum(getActivity());
        if(orderSet!=null&&orderSet.size()>0){
            lessonCounString=String.valueOf(orderSet.size());
        }
        personalMoney.setText(moneyString);
        personalLesson.setText(lessonCounString);
        personalOrder.setText(orderCountString);
    }

    private void showExitDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder();
        builder.setContentMessage(getString(R.string.seal_mine_set_account_dialog_logout_message));
        builder.setDialogButtonClickListener(new CommonDialog.OnDialogButtonClickListener() {
            @Override
            public void onPositiveClick(View v, Bundle bundle) {
                UserInfoUtils.clear(getActivity());
                Intent intent = new Intent("com.rong.im.action.logout");
                getActivity().sendBroadcast(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(loginIntent);
                getActivity().finish();
            }

            @Override
            public void onNegativeClick(View v, Bundle bundle) {

            }
        });
        CommonDialog dialog = builder.build();
        dialog.show(getChildFragmentManager(), "logout_dialog");
    }

    private void showLoginOutDialog() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(android.R.color.transparent)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)  // or any custom width ie: 300
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentHolder(new ViewHolder(R.layout.logout_dialog_layout))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        View holderView = dialog.getHolderView();
        holderView.findViewById(R.id.loginoutTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UserInfoUtils.clear(getActivity());
                Intent intent = new Intent("com.rong.im.action.logout");
                getActivity().sendBroadcast(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(loginIntent);
                getActivity().finish();
            }
        });

        dialog.show();


    }

    private static final String TAG = "UserFragment";

    private void loadPersonalData() {
        Log.e(TAG, "loadData: ");
        RetrofitManager.create().personal_center(uidString)
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<PersonalMessageBean>(refreshLayout) {
                    @Override
                    public void onSuccess(PersonalMessageBean personalMessageBean) {
                        if (personalMessageBean.getCode() == Constant.CODE_SUCC) {
                            PersonalMessageBean.PersonalMessageData messageData=personalMessageBean.getReferer();
                            String avatarString = messageData.getAvatar();
                            if (avatarString != null && !avatarString.startsWith("http://")) {
                                avatarString = Constant.THINKCMF_PATH + avatarString;
                            }
                            Glide.with(UserFragment.this).load(avatarString).into(userHeaderImage);
                            String orderCount=messageData.getOrderCount();
                            String userSumString=messageData.getUser_sum();
                            String lessonCount=messageData.getLessonCount();
                            UserInfoUtils.setUserSum(userSumString,getActivity());
                            UserInfoUtils.setAppUserOrderSum(orderCount,getActivity());
                            personalMoney.setText(userSumString);
                            personalLesson.setText(lessonCount);
                            personalOrder.setText(orderCount);
                        }
                    }
                });


    }

    @OnClick(R.id.withdrawTv)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), AccountSettingActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(EventBusBaseBean event) {
//        loadPersonalData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadPersonalData();
    }
}
