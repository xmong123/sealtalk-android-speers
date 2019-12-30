package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationAdapter2;
import com.caesar.rongcloudspeed.network.Api;
import com.caesar.rongcloudspeed.bean.HomeDataUserBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManageres;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class AnimationPersonalFavouriteActivity extends MultiStatusActivity implements OnRefreshListener {

    @BindView(R.id.recyclerview_anomation1)
    RecyclerView recyclerview_anomation1;
    private String cid = "3";
    private String titleString = "城库货源出售关注";
    private AnimationAdapter2 animationAdapter2;
    private String uidString;
    private String type = "indexPersonalFavoriteJson";
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
        RetrofitManageres.getInstance().create(Api.class).getPersonalMessageData(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HomeDataUserBean>bindToLifecycle())
                .subscribe(new CommonObserver<HomeDataUserBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(HomeDataUserBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            postsArticleBaseBeanList = value.getReferer();
                            animationAdapter2.setNewData(postsArticleBaseBeanList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        Toast.makeText(AnimationPersonalActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initAnimationPostsAdapter() {
        animationAdapter2 = new AnimationAdapter2(AnimationPersonalFavouriteActivity.this, postsArticleBaseBeanList, cid);
        animationAdapter2.openLoadAnimation();
        animationAdapter2.setNotDoAnimationCount(3);
        recyclerview_anomation1.setLayoutManager(new GridLayoutManager(AnimationPersonalFavouriteActivity.this, 1));
        animationAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
                ActivityUtils.startActivity(bundle, AnimationPersonalFavouriteActivity.this, TSShopDetailActivity.class);
            }
        });
        recyclerview_anomation1.setAdapter(animationAdapter2);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_animation;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadData();
    }
}
