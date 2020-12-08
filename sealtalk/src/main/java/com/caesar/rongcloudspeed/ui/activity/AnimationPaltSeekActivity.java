package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.adapter.SeekRequestAdapter;
import com.caesar.rongcloudspeed.bean.HomeSeekListBean;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class AnimationPaltSeekActivity extends MultiStatusActivity implements OnRefreshListener {

    @BindView(R.id.recyclerview_anomation1)
    RecyclerView recyclerview_anomation1;
    private String cid = "43";
    private String titleString = "同行求助";
    private SeekRequestAdapter seekRequestAdapter;
    private String uidString;
    private String type = "neq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        initTitleBarView(titlebar, titleString);
        LogUtils.e("loadAnimation1Fragment");
        loadPaltSeekData();

    }

    private void loadPaltSeekData() {
        RetrofitManager.create().indexSeekListJson(uidString, cid, type)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HomeSeekListBean>bindToLifecycle())
                .subscribe(new CommonObserver<HomeSeekListBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(HomeSeekListBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            seekRequestAdapter = new SeekRequestAdapter(AnimationPaltSeekActivity.this, value.getReferer(), cid);
                            seekRequestAdapter.setNotDoAnimationCount(3);
                            recyclerview_anomation1.setLayoutManager(new GridLayoutManager(AnimationPaltSeekActivity.this, 1));
                            seekRequestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    PostsSeekBaseBean postsArticleBaseBean = value.getReferer().get(position);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("seek_id", postsArticleBaseBean.getObject_id());
                                    bundle.putString("rong_id", postsArticleBaseBean.getRongid());
                                    bundle.putString("user_nicename", postsArticleBaseBean.getUser_nicename());
                                    bundle.putString("seek_title", postsArticleBaseBean.getPost_title());
                                    bundle.putString("seek_date", postsArticleBaseBean.getPost_date());
                                    bundle.putString("seek_price", postsArticleBaseBean.getPost_price());
                                    bundle.putString("seek_content", postsArticleBaseBean.getPost_excerpt());
                                    bundle.putString("photos_urls", postsArticleBaseBean.getPhotos_urls());
                                    bundle.putString("post_author", postsArticleBaseBean.getPost_author());
                                    ActivityUtils.startActivity(bundle, AnimationPaltSeekActivity.this, SeekHelperDetailActivity.class);
                                }
                            });
                            recyclerview_anomation1.setAdapter(seekRequestAdapter);
                            seekRequestAdapter.loadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(AnimationPaltSeekActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public int getContentView() {
        return R.layout.activity_animation;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadPaltSeekData();
    }
}
