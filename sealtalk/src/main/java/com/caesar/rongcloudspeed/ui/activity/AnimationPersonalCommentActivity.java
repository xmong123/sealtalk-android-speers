package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationAdapter4;
import com.caesar.rongcloudspeed.network.Api;
import com.caesar.rongcloudspeed.bean.HomeDataUserBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManageres;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class AnimationPersonalCommentActivity extends MultiStatusActivity {

    @BindView(R.id.recyclerview_anomation1)
    RecyclerView recyclerview_anomation1;
    private String cid="3";
    private String titleString="我的报价列表";
    private AnimationAdapter4 animationAdapter4;
    private String uidString;
    private String type="indexPersonalJson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString= UserInfoUtils.getAppUserId(this);
        Bundle extras = getIntent().getExtras();
        cid=getIntent().getStringExtra( "cat_id" );
        type=getIntent().getStringExtra( "type" );
        titleString=getIntent().getStringExtra( "titleString" );
        initTitleBarView(titlebar, titleString);
        LogUtils.e("loadAnimation1Fragment");
        RetrofitManageres.getInstance().create(Api.class).HomePersonalData("Portal","list",type,cid,uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HomeDataUserBean>bindToLifecycle())
                .subscribe(new CommonObserver<HomeDataUserBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(HomeDataUserBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            animationAdapter4 = new AnimationAdapter4(AnimationPersonalCommentActivity.this,value.getReferer());
                            animationAdapter4.setNotDoAnimationCount(3);
                            recyclerview_anomation1.setLayoutManager(new GridLayoutManager(AnimationPersonalCommentActivity.this, 1));

                            animationAdapter4.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {

                                }
                            });
                            animationAdapter4.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    PostsArticleBaseBean postsArticleBaseBean=value.getReferer().get( position );
                                    Bundle bundle = new Bundle();
                                    bundle.putString( "tid",postsArticleBaseBean.getTid() );
                                    bundle.putString( "term_id",postsArticleBaseBean.getTerm_id() );
                                    bundle.putString( "post_id",postsArticleBaseBean.getObject_id() );
                                    bundle.putString( "post_title",postsArticleBaseBean.getPost_title() );
                                    bundle.putString( "post_authorname",postsArticleBaseBean.getPost_authorname() );
                                    bundle.putString( "mobile",postsArticleBaseBean.getMobile() );
                                    bundle.putString( "post_area",postsArticleBaseBean.getPost_area() );
                                    bundle.putString( "post_price",postsArticleBaseBean.getPost_price() );
                                    bundle.putString( "post_date",postsArticleBaseBean.getPost_date() );
                                    bundle.putString( "photos_urls",postsArticleBaseBean.getPhotos_urls() );
                                    bundle.putString( "photos_content",postsArticleBaseBean.getPost_excerpt() );
                                    ActivityUtils.startActivity(bundle, AnimationPersonalCommentActivity.this, TSShopDetailActivity.class);
                                }
                            });
                            recyclerview_anomation1.setAdapter(animationAdapter4);
                            animationAdapter4.loadMoreEnd();
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


}
