package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.model.UserSimpleInfo;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.player.PLVideoViewActivity;
import com.caesar.rongcloudspeed.ui.adapter.BlackListAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.viewmodel.BlackListViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.AVOptions;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.utilities.OptionsPopupDialog;

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
        lessonMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostsArticleBaseBean postsArticleBaseBean=lessonMoreArray.get( position );
                String postID=postsArticleBaseBean.getObject_id();
                String thumbVideoString = postsArticleBaseBean.getThumb_video();
                if (!(thumbVideoString.startsWith( "http://" )||thumbVideoString.startsWith( "https://" ))) {
                    thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
                }
//                Intent intent = new Intent(HomeMoreLessonListActivity.this, PLVideoViewActivity.class);
//                intent.putExtra("videoPath", thumbVideoString);
//                intent.putExtra("postID" , postID);
//                intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
//                intent.putExtra("liveStreaming", 1);
//                intent.putExtra("cache", true);
//                intent.putExtra("loop", true);
//                intent.putExtra("video-data-callback", false);
//                intent.putExtra("audio-data-callback", false);
//                intent.putExtra("disable-log", false);
//                startActivity(intent);
                if (TbsVideo.canUseTbsPlayer(HomeMoreLessonListActivity.this)) {
                    TbsVideo.openVideo(HomeMoreLessonListActivity.this, thumbVideoString);
                }
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
