package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationCommentAdapter;
import com.caesar.rongcloudspeed.adapter.RecruitApplyHorizontalAdapter;
import com.caesar.rongcloudspeed.bean.ArticleCommentsBean;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.HomeArticleCommentsBean;
import com.caesar.rongcloudspeed.bean.RecruitApplyBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitApplyBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.easypop.CmmtPopup;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.util.ToastUitl;
import com.caesar.rongcloudspeed.util.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.classic.common.MultipleStatusView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class ArticlePostsCommentsListActivity extends MultiStatusActivity implements OnRefreshListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multiple_status_view;
    @BindView(R.id.article_comments_recyclerView)
    RecyclerView commentsRecyclerview;
    private String postID;
    private String userNikeName;
    private String titleString = "课程讨论";
    private static final String TAG = "ArticlePostsCommentsListActivity";
    private String uidString;
    private AnimationCommentAdapter animationCommentAdapter;
    private List<ArticleCommentsBean> comment_array = new ArrayList<ArticleCommentsBean>();
    private CmmtPopup mCmmtPopup;
    private String commentString;
    private View notDataView;
    private View errorView;
    private View errorNetWorkView;
    TextView userEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        postID = getIntent().getStringExtra("postID");
        userNikeName = UserInfoUtils.getNickName(this);
        initTitleBarView(titlebar, titleString);
        notDataView = getLayoutInflater().inflate(R.layout.comment_empty_view, (ViewGroup) commentsRecyclerview.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.custom_error_view, (ViewGroup) commentsRecyclerview.getParent(), false);
        errorNetWorkView = getLayoutInflater().inflate(R.layout.custom_no_network_view, (ViewGroup) commentsRecyclerview.getParent(), false);
        animationCommentAdapter = new AnimationCommentAdapter(this, comment_array);
        commentsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerview.setAdapter(animationCommentAdapter);
        refreshLayout.setOnRefreshListener(this);
        userEdittext = (TextView) notDataView.findViewById(R.id.user_edittext);
        userEdittext.setOnClickListener(v -> mCmmtPopup.showSoftInput()
                .showAtLocation(v, Gravity.BOTTOM, 0, 0));
        initCmmtPop();
        articleDetailsHandler.sendEmptyMessage(0);
    }

    private void initCmmtPop() {
        mCmmtPopup = CmmtPopup.create(this)
                .setOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCmmtPopup.isShowing()) {
                            //无法隐藏输入法。只有toggle方法起作用...
                            KeyboardUtils.hideSoftInput(ArticlePostsCommentsListActivity.this);
                            KeyboardUtils.toggleSoftInput();
                            mCmmtPopup.hideSoftInput().dismiss();
                        }
                    }
                })
                .setOnOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCmmtPopup.isShowing()) {
                            commentString = mCmmtPopup.mEditText.getText().toString();
                            if (TextUtils.isEmpty(commentString)) {
                                ToastUitl.showShort("评论内容不能为空");
                            } else {
                                KeyboardUtils.hideSoftInput(ArticlePostsCommentsListActivity.this);
                                KeyboardUtils.toggleSoftInput();
                                mCmmtPopup.hideSoftInput().dismiss();
                                articleDetailsHandler.sendEmptyMessage(1);
                            }
                        }
                    }
                })
                .apply();

    }

    @Override
    public int getContentView() {
        return R.layout.activity_article_comments_list;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @OnClick({R.id.article_bottom_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.article_bottom_text:
                mCmmtPopup.showSoftInput()
                        .showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    Handler articleDetailsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://获取评论
                    RetrofitManager.create()
                            .IndexArticleComment(postID)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new CommonObserver<HomeArticleCommentsBean>() {
                                @Override
                                public void onSuccess(HomeArticleCommentsBean articleCommentsBean) {
                                    if (articleCommentsBean.getCode() == CODE_SUCC) {
                                        comment_array = articleCommentsBean.getReferer();
                                        if (comment_array !=null&&comment_array.size()>0) {
                                            animationCommentAdapter.setNewData(comment_array);
                                        } else {
                                            animationCommentAdapter.setEmptyView(notDataView);
                                        }
                                    } else {
                                        animationCommentAdapter.setEmptyView(notDataView);
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {
                                    animationCommentAdapter.setEmptyView(errorView);
                                    super.onError(e);

                                }
                            });
                    break;
                case 1://发表评论
                    RetrofitManager.create()
                            .PostArticleComment(uidString, postID,userNikeName,commentString)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new CommonObserver<HomeArticleCommentsBean>() {
                                @Override
                                public void onSuccess(HomeArticleCommentsBean articleCommentsBean) {
                                    if (articleCommentsBean.getCode() == CODE_SUCC) {
                                        comment_array = articleCommentsBean.getReferer();
                                        animationCommentAdapter.setNewData(comment_array);
                                    } else {
                                        ToastUitl.showShort("评论未成功，请稍后再试");
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    ToastUitl.showShort("评论未成功，请稍后再试!");
                                }
                            });
                    break;
            }
        }
    };
}
