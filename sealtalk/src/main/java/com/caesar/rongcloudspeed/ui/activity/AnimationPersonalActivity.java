package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationAdapter1;
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

public class AnimationPersonalActivity extends MultiStatusActivity {

    @BindView(R.id.recyclerview_anomation1)
    RecyclerView recyclerview_anomation1;
    private String cid="5";
    private String titleString="城库交易资讯";
    private AnimationAdapter1 animationAdapter1;
    private String uidString;
    private String type="indexPersonalFavoriteJson";

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
                            animationAdapter1 = new AnimationAdapter1(AnimationPersonalActivity.this,value.getReferer());
                            animationAdapter1.openLoadAnimation();
                            animationAdapter1.setNotDoAnimationCount(3);
                            recyclerview_anomation1.setLayoutManager(new GridLayoutManager(AnimationPersonalActivity.this, 1));
                            animationAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    PostsArticleBaseBean postsArticleBaseBean=value.getReferer().get( position );
                                    Bundle bundle = new Bundle();
                                    String termID=postsArticleBaseBean.getTerm_id();
                                    String postID=postsArticleBaseBean.getObject_id();
                                    String webString="http://thinkcmf.500-china.com/index.php?g=&m=article&a=index_masonry&id="+postID+"&cid="+termID;
                                    bundle.putString("webString", webString);
                                    bundle.putString("title" , postsArticleBaseBean.getPost_title());
                                    bundle.putString("postID" , postID);
                                    ActivityUtils.startActivity(bundle, AnimationPersonalActivity.this, WebActivity.class);
                                }
                            });
                            recyclerview_anomation1.setAdapter(animationAdapter1);
                            animationAdapter1.loadMoreEnd();
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
