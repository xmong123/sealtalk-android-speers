package com.caesar.rongcloudspeed.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.db.model.FriendDetailInfo;
import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.db.model.UserInfo;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.ScreenCaptureResult;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.ui.dialog.SimpleInputDialog;
import com.caesar.rongcloudspeed.ui.view.SealTitleBar;
import com.caesar.rongcloudspeed.ui.view.SettingItemView;
import com.caesar.rongcloudspeed.ui.widget.SelectableRoundedImageView;
import com.caesar.rongcloudspeed.utils.CheckPermissionUtils;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.viewmodel.PrivateChatSettingViewModel;
import com.caesar.rongcloudspeed.utils.log.SLog;
import com.caesar.rongcloudspeed.viewmodel.UserDetailViewModel;
import com.caesar.rongcloudspeed.viewmodel.UserInfoViewModel;

import io.rong.imkit.RongIM;
import io.rong.imkit.utilities.PromptPopupDialog;
import io.rong.imlib.model.Conversation;

public class PrivateChatSettingActivity extends TitleBaseActivity implements View.OnClickListener {
    private final String TAG = "PrivateChatSettingActivity";
    /**
     * 发起创建群组
     */
    private UserDetailViewModel userDetailViewModel;
    private final int REQUEST_START_GROUP = 1000;

    private PrivateChatSettingViewModel privateChatSettingViewModel;

    private SettingItemView isNotifySb;
    private SettingItemView isTopSb;
    private SettingItemView isScreenShotSiv;

    private String targetId;
    private String name;
    private String portraitUrl;
    private Conversation.ConversationType conversationType;
    private SelectableRoundedImageView portraitIv;
    private TextView nameTv;
    private boolean isScreenShotSivClicked = false;
    private Button addFriendBtn;
    private final int REQUEST_CODE_PERMISSION = 114;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private UserInfo myUserInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SealTitleBar titleBar = getTitleBar();
        titleBar.setTitle(R.string.profile_user_details);

        setContentView(R.layout.profile_activity_private_chat_setting);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        targetId = intent.getStringExtra(IntentExtra.STR_TARGET_ID);
        conversationType = (Conversation.ConversationType) intent.getSerializableExtra(IntentExtra.SERIA_CONVERSATION_TYPE);
        initView();
        initViewModel();
        initData();
    }

    private void initView() {
        portraitIv = findViewById(R.id.profile_siv_user_header);
        portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrivateChatSettingActivity.this, UserDetailActivity.class);
                intent.putExtra(IntentExtra.STR_TARGET_ID, targetId);
                startActivity(intent);
            }
        });

        // 用户名
        nameTv = findViewById(R.id.profile_tv_user_name);

        // 添加用户至群聊
        findViewById(R.id.profile_iv_add_member).setOnClickListener(this);

        // 查询聊天记录
        findViewById(R.id.siv_search_messages).setOnClickListener(this);
        // 清除聊天记录
        findViewById(R.id.siv_clean_chat_message).setOnClickListener(this);

        isNotifySb = findViewById(R.id.siv_user_notification);
        isNotifySb.setSwitchCheckListener((buttonView, isChecked) ->
                privateChatSettingViewModel.setIsNotifyConversation(!isChecked));
        isTopSb = findViewById(R.id.siv_conversation_top);
        isTopSb.setSwitchCheckListener((buttonView, isChecked) ->
                privateChatSettingViewModel.setConversationOnTop(isChecked));

        isScreenShotSiv = findViewById(R.id.profile_siv_group_screen_shot_notification);
        isScreenShotSiv.setSwitchTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isScreenShotSivClicked) {
                    isScreenShotSivClicked = true;
                }
                return false;
            }
        });
        isScreenShotSiv.setSwitchCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //初始化不触发逻辑
                if (!isScreenShotSivClicked) {
                    return;
                }
                // 0 关闭 1 打开
                if (isChecked) {
                    //没有权限不开启设置
                    if (!requestReadPermissions()) {
                        return;
                    }
                    privateChatSettingViewModel.setScreenCaptureStatus(1);
                } else {
                    privateChatSettingViewModel.setScreenCaptureStatus(0);
                }
            }
        });
        // 添加好友
        addFriendBtn = findViewById(R.id.profile_btn_detail_add_friend);
        addFriendBtn.setOnClickListener(this);
    }

    private boolean requestReadPermissions() {
        return CheckPermissionUtils.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
    }

    private void initViewModel() {
        userDetailViewModel = ViewModelProviders.of(this, new UserDetailViewModel.Factory(getApplication(), targetId)).get(UserDetailViewModel.class);
        privateChatSettingViewModel = ViewModelProviders.of(this, new PrivateChatSettingViewModel.Factory(getApplication(), targetId, conversationType)).get(PrivateChatSettingViewModel.class);
        privateChatSettingViewModel.getFriendInfo().observe(this, friendShipInfoResource -> {
            FriendShipInfo data = friendShipInfoResource.data;
            if (data == null){
                userDetailViewModel.getUserInfo().observe(this, resource -> {
                    if (resource.data != null) { //在删除好友时不进行好友信息的刷新，防止删除成功时画面会刷新再关闭
                        UserInfo userInfo=resource.data;
                        // 设置备注名
                        nameTv.setText(userInfo.getName());
                        name = userInfo.getName();
                        ImageLoaderUtils.displayUserPortraitImage(userInfo.getPortraitUri(), portraitIv);
                        // 自己时不显示添加好友
                        if (userInfo.getId().equals(RongIM.getInstance().getCurrentUserId())) {
                            addFriendBtn.setVisibility(View.GONE);
                        } else {
                            addFriendBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
                return;
            }


            FriendDetailInfo user = data.getUser();
            String displayName = data.getDisplayName();
            // 设置备注名
            if (!TextUtils.isEmpty(displayName)) {
                nameTv.setText(displayName);
                name = displayName;
            } else if (user != null) {
                nameTv.setText(user.getNickname());
                name = user.getNickname();
            }

            if (user != null) {
                ImageLoaderUtils.displayUserPortraitImage(user.getPortraitUri(), portraitIv);
                portraitUrl = user.getPortraitUri();
            }
        });

        // 获取是否通知消息状态
        privateChatSettingViewModel.getIsNotify().observe(this, resource -> {
            if (resource.data != null) {
                if (resource.status == Status.SUCCESS) {
                    isNotifySb.setChecked(!resource.data);
                } else {
                    isNotifySb.setCheckedImmediately(!resource.data);
                }
            }

            if (resource.status == Status.ERROR) {
                if (resource.data != null) {
                    ToastUtils.showToast(R.string.common_set_failed);
                } else {
                    // do nothing
                }
            }
        });

        // 获取是否消息置顶状态
        privateChatSettingViewModel.getIsTop().observe(this, resource -> {
            if (resource.data != null) {
                if (resource.status == Status.SUCCESS) {
                    isTopSb.setChecked(resource.data);
                } else {
                    isTopSb.setCheckedImmediately(resource.data);
                }
            }

            if (resource.status == Status.ERROR) {
                if (resource.data != null) {
                    ToastUtils.showToast(R.string.common_set_failed);
                } else {
                    // do nothing
                }
            }
        });

        // 获取清除历史消息结果
        privateChatSettingViewModel.getCleanHistoryMessageResult().observe(this, resource -> {
            if (resource.status == Status.SUCCESS) {
                ToastUtils.showToast(R.string.common_clear_success);
            } else if (resource.status == Status.ERROR) {
                ToastUtils.showToast(R.string.common_clear_failure);
            }
        });

        // 获取截屏通知结果
        privateChatSettingViewModel.getScreenCaptureStatusResult().observe(this, new Observer<Resource<ScreenCaptureResult>>() {
            @Override
            public void onChanged(Resource<ScreenCaptureResult> screenCaptureResultResource) {
                if (screenCaptureResultResource.status == Status.SUCCESS) {
                    //0 关闭 1 打开
                    if (screenCaptureResultResource.data != null && screenCaptureResultResource.data.status == 1) {
                        isScreenShotSiv.setCheckedImmediately(true);
                    }
                }
            }
        });
        // 获取设置截屏通知结果
        privateChatSettingViewModel.getSetScreenCaptureResult().observe(this, new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> voidResource) {
                if (voidResource.status == Status.SUCCESS) {
                    ToastUtils.showToast(getString(R.string.seal_set_clean_time_success));
                } else if (voidResource.status == Status.ERROR) {
                    ToastUtils.showToast(getString(R.string.seal_set_clean_time_fail));
                }
            }
        });
        // 获取自己的信息
        UserInfoViewModel userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
        userInfoViewModel.getUserInfo().observe(this, new Observer<Resource<UserInfo>>() {
            @Override
            public void onChanged(Resource<UserInfo> resource) {
                if (resource.data != null) {
                    myUserInfo = resource.data;
                }
            }
        });
    }

    private void initData() {
        privateChatSettingViewModel.requestFriendInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siv_clean_chat_message:
                showCleanMessageDialog();
                break;
            case R.id.siv_search_messages:
                goSearchChatMessage();
                break;
            case R.id.profile_iv_add_member:
                addOtherMemberToGroup();
                break;
            case R.id.profile_btn_detail_add_friend:
                showAddFriendDialog();
                break;
            default:
        }
    }

    /**
     * 显示添加好友对话框
     */
    private void showAddFriendDialog() {
        SimpleInputDialog dialog = new SimpleInputDialog();
        dialog.setInputHint(getString(R.string.profile_add_friend_hint));
        dialog.setInputDialogListener(new SimpleInputDialog.InputDialogListener() {
            @Override
            public boolean onConfirmClicked(EditText input) {
                String inviteMsg = input.getText().toString();
                // 如果邀请信息为空则使用默认邀请语
                if (TextUtils.isEmpty(inviteMsg) && myUserInfo != null) {
                    // 当有附带群组名时显示来自哪个群组，没有时仅带自己的昵称
                    inviteMsg = getString(R.string.profile_invite_friend_description_format, myUserInfo.getName());
                }
                userDetailViewModel.inviteFriend(inviteMsg);
                return true;
            }
        });
        dialog.show(getSupportFragmentManager(), null);
    }

    /**
     * 显示清除聊天消息对话框
     */
    private void showCleanMessageDialog() {
        PromptPopupDialog.newInstance(this,
                getString(R.string.profile_clean_private_chat_history)).setLayoutRes(io.rong.imkit.R.layout.rc_dialog_popup_prompt_warning)
                .setPromptButtonClickedListener(() -> {
                    privateChatSettingViewModel.cleanHistoryMessage();
                }).show();
    }

    /**
     * 跳转到聊天记录搜索界面
     */
    private void goSearchChatMessage() {
        Intent intent = new Intent(this, SearchHistoryMessageActivity.class);
        intent.putExtra(IntentExtra.STR_TARGET_ID, targetId);
        intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, conversationType);
        intent.putExtra(IntentExtra.STR_CHAT_NAME, name);
        intent.putExtra(IntentExtra.STR_CHAT_PORTRAIT, portraitUrl);
        startActivity(intent);
    }

    /**
     * 添加其他人发起群聊
     */
    private void addOtherMemberToGroup() {
        Intent intent = new Intent(this, SelectCreateGroupActivity.class);
        ArrayList<String> friendIdList = new ArrayList<>();
        friendIdList.add(targetId);
        intent.putStringArrayListExtra(IntentExtra.LIST_CAN_NOT_CHECK_ID_LIST, friendIdList);
        startActivityForResult(intent, REQUEST_START_GROUP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_START_GROUP && resultCode == RESULT_OK) {
            ArrayList<String> memberList = data.getStringArrayListExtra(IntentExtra.LIST_STR_ID_LIST);
            // 添加该好友的id
            memberList.add(targetId);
            SLog.i(TAG, "memberList.size = " + memberList.size());
            Intent intent = new Intent(this, CreateGroupActivity.class);
            intent.putExtra(IntentExtra.LIST_STR_ID_LIST, memberList);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION && !CheckPermissionUtils.allPermissionGranted(grantResults)) {
            List<String> permissionsNotGranted = new ArrayList<>();
            for (String permission : permissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    permissionsNotGranted.add(permission);
                }
            }
            if (permissionsNotGranted.size() > 0) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, requestCode);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            default:
                                break;
                        }
                    }
                };
                CheckPermissionUtils.showPermissionAlert(this, getResources().getString(R.string.seal_grant_permissions) + CheckPermissionUtils.getNotGrantedPermissionMsg(this, permissionsNotGranted), listener);
            } else {
                ToastUtils.showToast(getString(R.string.seal_set_clean_time_fail));
            }
        } else {
            //权限获得后在请求次网络设置状态
            privateChatSettingViewModel.setScreenCaptureStatus(1);
        }
    }
}
