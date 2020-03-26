package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.ArrayList;
import java.util.List;

public class HomeMoreLessonListActivity extends TitleBaseActivity {
    private RecyclerView lessonMoreRecyclerView;
    private LessonAdapter lessonMoreAdapter;
    private List<PostsArticleBaseBean> lessonMoreArray=new ArrayList<PostsArticleBaseBean>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more_lesson);
        initView();
        initViewModel();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        getTitleBar().setTitle("推荐课程");
        lessonMoreRecyclerView = findViewById(R.id.lessonMoreRecyclerView);
        lessonMoreAdapter = new LessonAdapter(this,lessonMoreArray);
        lessonMoreAdapter.openLoadAnimation();
        lessonMoreAdapter.setNotDoAnimationCount(4);
        lessonMoreAdapter.setOnItemClickListener((adapter, view, position) -> {
            PostsArticleBaseBean postsArticleBaseBean=lessonMoreArray.get( position );
            String postID=postsArticleBaseBean.getObject_id();
            String thumbVideoString = postsArticleBaseBean.getThumb_video();
            if (!(thumbVideoString.startsWith( "http://" )||thumbVideoString.startsWith( "https://" ))) {
                thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
            }
            if (TbsVideo.canUseTbsPlayer(HomeMoreLessonListActivity.this)) {
                TbsVideo.openVideo(HomeMoreLessonListActivity.this, thumbVideoString);
            }
        });
        lessonMoreRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        lessonMoreRecyclerView.setAdapter(lessonMoreAdapter);
    }



    /**
     * 初始话Viewmodel
     */
    private void initViewModel() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas("4"),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        lessonMoreArray=homeDataBean.getReferer().getPosts();
                        lessonMoreAdapter.setNewData(lessonMoreArray);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(HomeMoreLessonListActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
