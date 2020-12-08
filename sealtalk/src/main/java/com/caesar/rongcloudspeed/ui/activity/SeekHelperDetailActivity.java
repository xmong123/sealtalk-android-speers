package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.ArticleCommentsAdapter;
import com.caesar.rongcloudspeed.bean.GoodInfoDetail;
import com.caesar.rongcloudspeed.circle.adapter.CommentAdapter;
import com.caesar.rongcloudspeed.circle.ui.FriendTimeLineActivity;
import com.caesar.rongcloudspeed.circle.ui.ImagePagerActivity;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.CommentsItemData;
import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.CommentsListData;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.easypop.CmmtPopup;
import com.caesar.rongcloudspeed.holders.LocalImageHolderView;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.model.AddFriendResult;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.dialog.SimpleInputDialog;
import com.caesar.rongcloudspeed.util.ToastUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.SearchFriendNetViewModel;
import com.caesar.rongcloudspeed.viewmodel.UserDetailViewModel;
import com.just.agentweb.AgentWeb;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.contactcard.message.ContactSeekInfo;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import me.leefeng.promptlibrary.PromptDialog;

public class SeekHelperDetailActivity extends MultiStatusActivity {

    public PromptDialog prompDialog;
    private String seekID;
    private String rongID;
    private String userNiceName;
    private String seek_title;
    private String seek_price;
    private String seek_date;
    private String photos_urls;
    private String seek_content;
    @BindView(R.id.seek_convenientBanner)
    ConvenientBanner seek_convenientBanner;
    @BindView(R.id.seek_price_text)
    TextView seek_price_text;
    @BindView(R.id.seek_contact_text)
    TextView seek_contact_text;
    @BindView(R.id.seek_title_text)
    TextView seek_title_text;
    @BindView(R.id.seek_tag1)
    TextView seek_tag1;
    @BindView(R.id.seek_tag2)
    TextView seek_tag2;
    @BindView(R.id.seek_content_text)
    TextView seek_content_text;
    @BindView(R.id.seek_text1)
    TextView seek_text1;
    @BindView(R.id.seek_text2)
    TextView seek_text2;
    @BindView(R.id.seek_text3)
    TextView seek_text3;
    @BindView(R.id.seekHelperRecyclerview)
    RecyclerView seekHelperRecyclerview;
    @BindView(R.id.seek_btn1)
    Button seek_btn1;
    @BindView(R.id.seek_btn2)
    Button seek_btn2;
    private String uidString;
    private String post_author;
    private String user_type;
    private String mobileString = "13815067320";
    private SearchFriendNetViewModel viewModel;
    private boolean isFriend;
    private CmmtPopup mCmmtPopup;
    private ArticleCommentsAdapter articleCommentsAdapter;
    private List<CommentsItemData> commentsList = new ArrayList<CommentsItemData>();
    private boolean isoneself = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        post_author = getIntent().getStringExtra("post_author");
        user_type = UserInfoUtils.getUserType(this);
        seekID = getIntent().getStringExtra("seek_id");
        rongID = getIntent().getStringExtra("rong_id");
        userNiceName = getIntent().getStringExtra("user_nicename");
        seek_title = getIntent().getStringExtra("seek_title");
        seek_content = getIntent().getStringExtra("seek_content");
        seek_price = getIntent().getStringExtra("seek_price");
        if (seek_price.startsWith("0.0")) {
            seek_price = "价格可议";
        }
        seek_date = getIntent().getStringExtra("seek_date");
        String[] dateString = seek_date.split(" ");
        if (dateString.length > 0) {
            seek_date = dateString[0];
        }
        photos_urls = getIntent().getStringExtra("photos_urls");
        initTitleBarView(titlebar, "同行求助");
        seek_title_text.setText(seek_title);
        seek_price_text.setText(seek_price);
        seek_text3.setText(userNiceName);
        if (seek_price.equals("价格可议")) {
            seek_price_text.setText(seek_price);
        } else {
            seek_price_text.setText("￥" + seek_price);
        }
        seek_content_text.setText("\u3000\u3000\u3000" + seek_content);
        seek_text2.setText(seek_date);
        if (!TextUtils.isEmpty(photos_urls) && photos_urls.length() > 32) {
            try {
                JSONArray imageArray = new JSONArray(photos_urls);
                if (imageArray.length() > 0) {
                    ArrayList<String> viewpageDatas = new ArrayList<>();
                    for (int i = 0; i < imageArray.length(); i++) {
                        String str = imageArray.getString(i);
                        if (!str.startsWith("http://")) {
                            str = Constant.THINKCMF_PATH + str;
                        }
                        viewpageDatas.add(str);
                    }
                    setSeekViewPagerData(viewpageDatas);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        viewModel = ViewModelProviders.of(this).get(SearchFriendNetViewModel.class);
        viewModel.isFriend(rongID);
        viewModel.getIsFriend().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isFriend = aBoolean;
                if (aBoolean) {
                    seek_btn2.setText("立即沟通");
                }
            }
        });

        if (post_author.equals(uidString)) {
            isoneself = true;
            seek_btn1.setVisibility(View.INVISIBLE);
            seek_btn2.setVisibility(View.INVISIBLE);
        }
        viewModel.getAddFriend().observe(this, new Observer<Resource<AddFriendResult>>() {
            @Override
            public void onChanged(Resource<AddFriendResult> addFriendResultResource) {
                if (addFriendResultResource.status == Status.SUCCESS) {
                    Toast.makeText(SeekHelperDetailActivity.this, R.string.common_request_success, Toast.LENGTH_SHORT).show();
                } else if (addFriendResultResource.status == Status.ERROR) {
                    Toast.makeText(SeekHelperDetailActivity.this,
                            String.format(getString(R.string.seal_quest_failed_error_code), addFriendResultResource.code),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        seek_btn1.setOnClickListener(view -> {
            showCmmtPop();
        });

        seek_btn2.setOnClickListener(view -> {
            if (isFriend) {
                ContactSeekInfo seekMessage = ContactSeekInfo.obtain(seekID,
                        seek_title, photos_urls, seek_content, rongID, userNiceName, seek_price, seek_date);
                RongIM.getInstance().sendMessage(Message.obtain(rongID, Conversation.ConversationType.PRIVATE, seekMessage),
                        "同行求助", null, new IRongCallback.ISendMessageCallback() {
                            @Override
                            public void onAttached(Message message) {

                            }

                            @Override
                            public void onSuccess(Message message) {

                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                            }
                        });

                RongIM.getInstance().startPrivateChat(this, rongID, userNiceName);
            } else {
                showAddFriendDialog(rongID);
            }

        });

//        if(uidString.equals())
        articleCommentsAdapter = new ArticleCommentsAdapter(this, commentsList);
        articleCommentsAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommentsItemData commentsItemData = commentsList.get(position);
            if (isoneself) {
                String rongID = commentsItemData.getRongid();
                if (!rongID.equals(uidString)) {
                    RongIM.getInstance().startPrivateChat(this, commentsItemData.getRongid(), commentsItemData.getUser_nicename());
                }
            }
        });
        seekHelperRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        seekHelperRecyclerview.setAdapter(articleCommentsAdapter);
        initCmmtPop();
        getSeekComments();

    }

    private void initCmmtPop() {
        mCmmtPopup = CmmtPopup.create(this)
                .setOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCmmtPopup.isShowing()) {
                            //无法隐藏输入法。只有toggle方法起作用...
                            KeyboardUtils.hideSoftInput(SeekHelperDetailActivity.this);
                            mCmmtPopup.hideSoftInput()
                                    .dismiss();

                        }
                    }
                })
                .setOnOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCmmtPopup.isShowing()) {
                            String content = mCmmtPopup.mEditText.getText().toString();
                            if (TextUtils.isEmpty(content) || content.trim().length() < 7) {
                                Toast.makeText(SeekHelperDetailActivity.this, "内容不能为空！(沟通说明请保持在8个字符以上)", Toast.LENGTH_LONG).show();
                            } else {
                                updateSeekComment(content);
                                //无法隐藏输入法。只有toggle方法起作用...
                                mCmmtPopup.mEditText.setText("");
                                mCmmtPopup.hideSoftInput().dismiss();
                                hideInput();
                            }

                        }
                    }
                })
                .apply();

    }

    private void updateSeekComment(String comment) {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateComment(uidString,
                seekID, UserInfoUtils.getNickName(SeekHelperDetailActivity.this), comment),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData circleItemResult) {
                        Toast.makeText(SeekHelperDetailActivity.this, "评论成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(SeekHelperDetailActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getSeekComments() {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getCommentsForArticle(seekID),
                new NetworkCallback<CommentsListData>() {
                    @Override
                    public void onSuccess(CommentsListData commentsListData) {
                        if (commentsListData.getCode() == 101) {
                            commentsList = commentsListData.getReferer();
                            articleCommentsAdapter.setNewData(commentsList);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void showCmmtPop() {
        mCmmtPopup.showSoftInput()
                .showAtLocation(seek_btn1, Gravity.BOTTOM, 0, 0);
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

    @Override
    public int getContentView() {
        return R.layout.activity_seek_help_detail;
    }

    @OnClick({R.id.seek_contact_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.seek_contact_text:
                if (user_type.equals("2")) {
                    onShowReviewDialog();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileString));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setSeekViewPagerData(List<String> data) {
        seek_convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, data)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.indicator_normal, R.mipmap.indicator_focus})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        seek_convenientBanner.startTurning(5000);
        seek_convenientBanner.setScrollDuration(3000);
        try {
            Class<?> clazz = Class.forName("com.bigkoo.convenientbanner.ConvenientBanner");
            Field loPageTurningPoint = clazz.getDeclaredField("loPageTurningPoint");
            loPageTurningPoint.setAccessible(true);
            ViewGroup o = (ViewGroup) loPageTurningPoint.get(seek_convenientBanner);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) o.getLayoutParams();
            layoutParams.bottomMargin = SizeUtils.dp2px(36);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        seek_convenientBanner.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                ImagePagerActivity.imageSize = new ImageSize(seek_convenientBanner.getWidth(), seek_convenientBanner.getHeight());
                ImagePagerActivity.startImagePagerActivity(SeekHelperDetailActivity.this, data, position);
            }
        });

    }
}
