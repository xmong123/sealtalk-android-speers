package com.caesar.rongcloudspeed.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.model.SearchFriendInfo;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.ScanActivity;
import com.caesar.rongcloudspeed.ui.dialog.PayPassDialog;
import com.caesar.rongcloudspeed.ui.dialog.PayPassView;
import com.caesar.rongcloudspeed.ui.interfaces.OnSearchFriendItemClickListener;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.RongGenerate;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

public class TransFriendResultFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SearchFriendResultFragment";
    private SearchFriendInfo searchFriendInfo;
    private ImageView ivPortrait;
    private TextView tvName;
    private TextView tvStAccount;
    private OnSearchFriendItemClickListener searchFriendItemClickListener;

    public TransFriendResultFragment(OnSearchFriendItemClickListener listener, SearchFriendInfo searchFriendInfo) {
        this.searchFriendInfo = searchFriendInfo;
        searchFriendItemClickListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_friend_result_tra, container, false);
        ivPortrait = view.findViewById(R.id.search_header);
        tvName = view.findViewById(R.id.search_name);
        tvStAccount = view.findViewById(R.id.search_sealtalk_acctount);
        view.setOnClickListener(this);
        view.findViewById(R.id.profile_btn_detail_tra_friend).setOnClickListener(this);
        initView();
        return view;
    }

    private void initView() {
        tvName.setText(searchFriendInfo.getNickname());
        String stAccount = searchFriendInfo.getStAccount();
        if (!TextUtils.isEmpty(stAccount)) {
            tvStAccount.setText(getString(R.string.seal_st_account_content_format, stAccount));
        }
        String portraitUri = searchFriendInfo.getPortraitUri();
        if (TextUtils.isEmpty(portraitUri)) {
            String generateDefaultAvatar = RongGenerate.generateDefaultAvatar(getContext(), searchFriendInfo.getId(), searchFriendInfo.getNickname());
            ImageLoaderUtils.displayUserPortraitImage(generateDefaultAvatar, ivPortrait);
        } else {
            ImageLoaderUtils.displayUserPortraitImage(portraitUri, ivPortrait);
        }
    }

    @Override
    public void onClick(View v) {
        if (searchFriendItemClickListener != null) {
            searchFriendItemClickListener.onSearchFriendItemClick(searchFriendInfo);
        }
        switch (v.getId()) {
            case R.id.profile_btn_detail_tra_friend:
                final EditText et = new EditText(getActivity());
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(getActivity()).setTitle("请设置付款金额")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String user_sum_add=et.getText().toString();
                                String targetUserName = searchFriendInfo == null ? "" : searchFriendInfo.getNickname();
                                String sum_foruserID= UserInfoUtils.getAppUserId(getActivity());
                                PayPassDialog payPassDialog=new PayPassDialog(getActivity());
                                payPassDialog.getPayViewPass().setPayClickListener(new PayPassView.OnPayClickListener() {
                                    @Override
                                    public void onPassFinish(String passContent) {
                                        String payPassWord= UserInfoUtils.getPayPassWord(getActivity());
                                        if(payPassWord.equals(passContent)){
                                            payPassDialog.dismiss();
                                            //按下确定键后的事件
                                            NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getCodeToUserName(user_sum_add,sum_foruserID,targetUserName),
                                                    new NetworkCallback<UserSumResult>() {
                                                        @Override
                                                        public void onSuccess(UserSumResult userSumResult) {
                                                            UserSumUrl userSumUrl=userSumResult.getUrl();
                                                            String sumString=String.valueOf(userSumUrl.getUser_sum());
                                                            UserInfoUtils.setUserSum(sumString,getActivity());
                                                            Toast.makeText(getActivity(), "交易成功，转账金额为:"+user_sum_add+"元",Toast.LENGTH_LONG).show();
                                                        }

                                                        @Override
                                                        public void onFailure(Throwable t) {
                                                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }else{
                                            payPassDialog.dismiss();
                                            Toast.makeText(getActivity(), "支付密码错误", Toast.LENGTH_LONG).show();
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
                break;
            default:
                break;
        }
    }
}
