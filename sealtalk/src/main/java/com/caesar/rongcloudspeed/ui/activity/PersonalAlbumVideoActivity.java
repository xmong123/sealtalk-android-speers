package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AlbumSectionAdapter;
import com.caesar.rongcloudspeed.adapter.UserAlbumVideoAdapter;
import com.caesar.rongcloudspeed.bean.HomeSeekListBean;
import com.caesar.rongcloudspeed.bean.PersonalPhotoBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.caesar.rongcloudspeed.bean.SectionAlbumBean;
import com.caesar.rongcloudspeed.bean.SectionPersonalAlbumDataBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.entity.AlbumSectionEntity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class PersonalAlbumVideoActivity extends MultiStatusActivity implements OnRefreshListener {
    private List<PostsSeekBaseBean> personalVideoList = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview_album_view)
    RecyclerView recyclerview_album_view;
    private UserAlbumVideoAdapter albumVideoAdapter;
    private String uidString;
    private View notDataView;
    private View errorView;

    @Override
    public int getContentView() {
        return R.layout.activity_personal_album_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitleBarView(titlebar, "您的视频");
        uidString = UserInfoUtils.getAppUserId(this);
        initView();
        loadMPersonalVideoData();
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);

        recyclerview_album_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        albumVideoAdapter = new UserAlbumVideoAdapter(this, personalVideoList);

        albumVideoAdapter.setEmptyView(R.layout.custom_loading_view, (ViewGroup) recyclerview_album_view.getParent());

        recyclerview_album_view.setAdapter(albumVideoAdapter);

        notDataView = getLayoutInflater().inflate(R.layout.custom_empty_view, (ViewGroup) recyclerview_album_view.getParent(), false);
        errorView = getLayoutInflater().inflate(R.layout.custom_error_view, (ViewGroup) recyclerview_album_view.getParent(), false);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    private void loadMPersonalVideoData() {
        RetrofitManager.create().indexAvertkListJson(uidString, "44", "0")
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HomeSeekListBean>bindToLifecycle())
                .subscribe(new CommonObserver<HomeSeekListBean>(refreshLayout) {
                    @Override
                    public void onSuccess(HomeSeekListBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            personalVideoList = value.getReferer();
                            if (personalVideoList.size() > 0) {
                                albumVideoAdapter.setNewData(personalVideoList);
                            }else{
                                albumVideoAdapter.setEmptyView(notDataView);
                            }
                        }else{
                            albumVideoAdapter.setEmptyView(notDataView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        albumVideoAdapter.setEmptyView(errorView);
                        Toast.makeText(PersonalAlbumVideoActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadMPersonalVideoData();
    }


}
