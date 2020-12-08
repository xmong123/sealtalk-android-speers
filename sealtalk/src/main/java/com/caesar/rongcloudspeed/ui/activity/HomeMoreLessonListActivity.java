package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
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
    private List<PostsArticleBaseBean> lessonMoreArray = new ArrayList<PostsArticleBaseBean>();
    private String cid = "4";
    private String title = "推荐课程";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more_lesson);
        cid = getIntent().getStringExtra("catid");
        title = getIntent().getStringExtra("title");
        if (cid == null) {
            cid = "4";
        }
        if (title == null) {
            title = "推荐课程";
        }
        initView();
        initViewModel();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        getTitleBar().setTitle(title);
        lessonMoreRecyclerView = findViewById(R.id.lessonMoreRecyclerView);
        lessonMoreAdapter = new LessonAdapter(this, lessonMoreArray);
        lessonMoreAdapter.openLoadAnimation();
        lessonMoreAdapter.setNotDoAnimationCount(4);
        lessonMoreAdapter.setOnItemClickListener((adapter, view, position) -> {
            PostsArticleBaseBean postsArticleBaseBean = lessonMoreArray.get(position);
//            String postID = postsArticleBaseBean.getObject_id();
//            String thumbVideoString = postsArticleBaseBean.getThumb_video();
//            if (!(thumbVideoString.startsWith("http://") || thumbVideoString.startsWith("https://"))) {
//                thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
//            }
//            if (TbsVideo.canUseTbsPlayer(HomeMoreLessonListActivity.this)) {
//                TbsVideo.openVideo(HomeMoreLessonListActivity.this, thumbVideoString);
//            }
            String lessonID = postsArticleBaseBean.getObject_id();
            String lessonName = postsArticleBaseBean.getPost_title();
            String lessonContent = postsArticleBaseBean.getPost_excerpt();
            String lessonSource = postsArticleBaseBean.getPost_source();
            String lessonPrice = postsArticleBaseBean.getPost_price();
            String lessonSmeta = postsArticleBaseBean.getSmeta();
            String thumbVideoString = postsArticleBaseBean.getThumb_video();
            if (!(thumbVideoString.startsWith("http"))) {
                thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
            }
            Intent intent1 = new Intent(this, SPLessonDetailActivity.class);
            intent1.putExtra("videoPath", thumbVideoString);
            intent1.putExtra("lesson_id", lessonID);
            intent1.putExtra("lesson_name", lessonName);
            intent1.putExtra("lesson_price", lessonPrice);
            intent1.putExtra("lesson_smeta", lessonSmeta);
            intent1.putExtra("lesson_content", lessonContent);
            intent1.putExtra("lesson_source", lessonSource);
            startActivity(intent1);
        });
        lessonMoreRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        lessonMoreRecyclerView.setAdapter(lessonMoreAdapter);
    }


    /**
     * 初始话Viewmodel
     */
    private void initViewModel() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas(cid),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        lessonMoreArray = homeDataBean.getReferer().getPosts();
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
