package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationPostsAdapter;
import com.caesar.rongcloudspeed.network.Api;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.HomeDataUserBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManageres;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.SPConfirmDialog;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class AnimationPersonalPostsActivity extends MultiStatusActivity implements OnRefreshListener, SPConfirmDialog.ConfirmDialogListener {

    @BindView(R.id.recyclerview_anomation1)
    RecyclerView recyclerview_anomation1;
    private String cid = "3";
    private String titleString = "我的出售列表";
    private AnimationPostsAdapter animationPostsAdapter;
    private String uidString;
    private String type = "indexPersonalJson";
    private int removePosition = 0;
    public List<PostsArticleBaseBean> postsArticleBaseBeanList = new ArrayList<PostsArticleBaseBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        Bundle extras = getIntent().getExtras();
        cid = getIntent().getStringExtra("cat_id");
        type = getIntent().getStringExtra("type");
        titleString = getIntent().getStringExtra("titleString");
        initTitleBarView(titlebar, titleString);
        LogUtils.e("loadAnimation1Fragment");
        initAnimationPostsAdapter();
        loadData();
    }

    private void loadData() {
        RetrofitManageres.getInstance().create(Api.class).HomePersonalData("Portal", "list", type, cid, uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HomeDataUserBean>bindToLifecycle())
                .subscribe(new CommonObserver<HomeDataUserBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(HomeDataUserBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            postsArticleBaseBeanList = value.getReferer();
                            animationPostsAdapter.setNewData(postsArticleBaseBeanList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        Toast.makeText(AnimationPersonalActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_animation;
    }

    private void initAnimationPostsAdapter() {
        animationPostsAdapter = new AnimationPostsAdapter(AnimationPersonalPostsActivity.this, postsArticleBaseBeanList);
        animationPostsAdapter.setNotDoAnimationCount(3);
        recyclerview_anomation1.setLayoutManager(new GridLayoutManager(AnimationPersonalPostsActivity.this, 1));
        animationPostsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.post_check_btn:
                        break;
                    case R.id.post_del_btn:
                        removePosition = position;
                        showConfirmDialog("确定删除该发布信息", "删除提醒", AnimationPersonalPostsActivity.this, 1);
                        break;
                }
            }
        });
        animationPostsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostsArticleBaseBean postsArticleBaseBean = postsArticleBaseBeanList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("tid", postsArticleBaseBean.getTid());
                bundle.putString("term_id", postsArticleBaseBean.getTerm_id());
                bundle.putString("post_id", postsArticleBaseBean.getObject_id());
                bundle.putString("post_title", postsArticleBaseBean.getPost_title());
                bundle.putString("store_count", postsArticleBaseBean.getStore_count());
                bundle.putString("post_authorname", postsArticleBaseBean.getPost_authorname());
                bundle.putString("mobile", postsArticleBaseBean.getMobile());
                bundle.putString("post_area", postsArticleBaseBean.getPost_area());
                bundle.putString("post_price", postsArticleBaseBean.getPost_price());
                bundle.putString("post_date", postsArticleBaseBean.getPost_date());
                bundle.putString("photos_urls", postsArticleBaseBean.getPhotos_urls());
                bundle.putString("photos_content", postsArticleBaseBean.getPost_excerpt());
                ActivityUtils.startActivity(bundle, AnimationPersonalPostsActivity.this, TSShopDetailActivity.class);
            }
        });
        recyclerview_anomation1.setAdapter(animationPostsAdapter);
    }


    @Override
    public void clickOk(int actionType) {
        if (actionType == 1) {
            String tid = postsArticleBaseBeanList.get(removePosition).getTid();
            String post_title = postsArticleBaseBeanList.get(removePosition).getPost_title();
            RetrofitManageres.getInstance().create(Api.class).RemovePostData(tid)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.<CommonResonseBean>bindToLifecycle())
                    .subscribe(new CommonObserver<CommonResonseBean>(multipleStatusView) {
                        @Override
                        public void onSuccess(CommonResonseBean value) {
                            if (value.getCode() == CODE_SUCC) {
                                loadData();
                                ToastUtils.showShort("删除成功," + post_title);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
//                        Toast.makeText(AnimationPersonalActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadData();
    }
}