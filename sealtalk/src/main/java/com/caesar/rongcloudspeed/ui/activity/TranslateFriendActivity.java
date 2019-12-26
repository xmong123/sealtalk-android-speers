package com.caesar.rongcloudspeed.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.model.AddFriendResult;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.SearchFriendInfo;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.dialog.PayPassDialog;
import com.caesar.rongcloudspeed.ui.dialog.PayPassView;
import com.caesar.rongcloudspeed.ui.dialog.SimpleInputDialog;
import com.caesar.rongcloudspeed.ui.fragment.SearchFriendNetFragment;
import com.caesar.rongcloudspeed.ui.fragment.SearchFriendResultFragment;
import com.caesar.rongcloudspeed.ui.fragment.TransFriendResultFragment;
import com.caesar.rongcloudspeed.ui.fragment.TranslateFriendNetFragment;
import com.caesar.rongcloudspeed.ui.interfaces.OnSearchFriendClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnSearchFriendItemClickListener;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.SearchFriendNetViewModel;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

public class TranslateFriendActivity extends TitleBaseActivity implements OnSearchFriendClickListener,
        OnSearchFriendItemClickListener {
    private TranslateFriendNetFragment transFriendFragment;
    private TransFriendResultFragment transFriendResultFragment;
    private SearchFriendNetViewModel viewModel;
    private boolean isFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitle((R.string.seal_main_title_add_friends));
        getTitleBar().setOnBtnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setContentView(R.layout.activity_friend_search_content);
        transFriendFragment = new TranslateFriendNetFragment();
        transFriendFragment.setOnSearchFriendClickListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_fragment_container, transFriendFragment).commit();
        viewModel = ViewModelProviders.of(this).get(SearchFriendNetViewModel.class);
        viewModel.getSearchFriend().observe(this, new Observer<Resource<SearchFriendInfo>>() {
            @Override
            public void onChanged(Resource<SearchFriendInfo> searchFriendInfoResource) {
                if (searchFriendInfoResource.status == Status.SUCCESS) {
                    SearchFriendInfo friendInfo = searchFriendInfoResource.data;
                    transFriendResultFragment = new TransFriendResultFragment(TranslateFriendActivity.this, searchFriendInfoResource.data);
                    pushFragment(transFriendResultFragment);
                    viewModel.isFriend(friendInfo.getId());
                } else if (searchFriendInfoResource.status == Status.ERROR) {
                    Toast.makeText(TranslateFriendActivity.this, R.string.seal_account_not_exist, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getIsFriend().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isFriend = aBoolean;
            }
        });
        viewModel.getAddFriend().observe(this, new Observer<Resource<AddFriendResult>>() {
            @Override
            public void onChanged(Resource<AddFriendResult> addFriendResultResource) {
                if (addFriendResultResource.status == Status.SUCCESS) {
                    Toast.makeText(TranslateFriendActivity.this, R.string.common_request_success, Toast.LENGTH_SHORT).show();
                } else if (addFriendResultResource.status == Status.ERROR) {
                    Toast.makeText(TranslateFriendActivity.this,
                            String.format(getString(R.string.seal_quest_failed_error_code), addFriendResultResource.code),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pushFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_fragment_container, transFriendResultFragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onSearchClick(String region, String searchContent) {
        if(TextUtils.isDigitsOnly(searchContent)){
            viewModel.searchFriendFromServer(null, region, searchContent);
        } else {
            viewModel.searchFriendFromServer(searchContent, null, null);
        }
    }

    @Override
    public void onSearchFriendItemClick(SearchFriendInfo searchFriendInfo) {
        if (isFriend || searchFriendInfo.getId().equals(RongIM.getInstance().getCurrentUserId())) {
//            toDetailActivity(searchFriendInfo.getId());
        } else {
//            showAddFriendDialog(searchFriendInfo.getId());
        }
        final EditText et = new EditText(TranslateFriendActivity.this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(TranslateFriendActivity.this).setTitle("请设置付款金额")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String user_sum_add=et.getText().toString();
                        String targetUserName = searchFriendInfo == null ? "" : searchFriendInfo.getNickname();
                        String sum_foruserID= UserInfoUtils.getAppUserId(TranslateFriendActivity.this);
                        PayPassDialog payPassDialog=new PayPassDialog(TranslateFriendActivity.this);
                        payPassDialog.getPayViewPass().setPayClickListener(new PayPassView.OnPayClickListener() {
                            @Override
                            public void onPassFinish(String passContent) {
                                String payPassWord= UserInfoUtils.getPayPassWord(TranslateFriendActivity.this);
                                if(payPassWord.equals(passContent)){
                                    payPassDialog.dismiss();
                                    //按下确定键后的事件
                                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getCodeToUserName(user_sum_add,sum_foruserID,targetUserName),
                                            new NetworkCallback<UserSumResult>() {
                                                @Override
                                                public void onSuccess(UserSumResult userSumResult) {
                                                    UserSumUrl userSumUrl=userSumResult.getUrl();
                                                    String sumString=String.valueOf(userSumUrl.getUser_sum());
                                                    UserInfoUtils.setUserSum(sumString,TranslateFriendActivity.this);
                                                    Toast.makeText(TranslateFriendActivity.this, "交易成功，转账金额为:"+user_sum_add+"元",Toast.LENGTH_LONG).show();
                                                    finish();
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    Toast.makeText(TranslateFriendActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            });
                                }else{
                                    payPassDialog.dismiss();
                                    Toast.makeText(TranslateFriendActivity.this, "支付密码错误", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onPayClose() {
                                payPassDialog.dismiss();
                            }

                            @Override
                            public void onPayForget() {
                                payPassDialog.dismiss();
                            }
                        });

                    }
                }).setNegativeButton("取消",null).show();
    }

    private void toDetailActivity(String userId) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(IntentExtra.STR_TARGET_ID, userId);
        startActivity(intent);
    }

    private void showAddFriendDialog(String userId) {
        final EditText et = new EditText(this);
        SimpleInputDialog dialog = new SimpleInputDialog();
        dialog.setInputHint(getString(R.string.profile_add_friend_hint));
        dialog.setInputDialogListener(new SimpleInputDialog.InputDialogListener() {
            @Override
            public boolean onConfirmClicked(EditText input) {
                String inviteMsg = input.getText().toString();
                UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(IMManager.getInstance().getCurrentId());
                // 如果邀请信息为空则使用默认邀请语
                if (TextUtils.isEmpty(inviteMsg) && userInfo != null) {
                    // 当有附带群组名时显示来自哪个群组，没有时仅带自己的昵称
                    inviteMsg = getString(R.string.profile_invite_friend_description_format, userInfo.getName());
                }
                viewModel.inviteFriend(userId, inviteMsg);
                return true;
            }
        });
        dialog.show(getSupportFragmentManager(), null);

    }


}
