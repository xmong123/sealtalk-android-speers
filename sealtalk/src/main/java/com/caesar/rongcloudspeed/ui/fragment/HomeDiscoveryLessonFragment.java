package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.player.PLVideoViewActivity;
import com.caesar.rongcloudspeed.ui.activity.FullScreenActivity;
import com.caesar.rongcloudspeed.ui.activity.HomeMoreLessonListActivity;
import com.caesar.rongcloudspeed.ui.activity.MainActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.SPSpeerDetailActivity;
import com.caesar.rongcloudspeed.ui.adapter.BookAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.caesar.rongcloudspeed.widget.PagerGridLayoutManager;
import com.caesar.rongcloudspeed.widget.PagerGridSnapHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.AVOptions;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面子界面-发现界面
 */
public class HomeDiscoveryLessonFragment extends BaseFragment {
    private RecyclerView lessonRecyclerView;
    private RecyclerView homeRecyclerView;
    private LessonAdapter lessonAdapter;
    private BookAdapter bookAdapter;
    private View headView;
//    private PagerGridLayoutManager layoutManager;
    private List<PostsArticleBaseBean> dataArray=new ArrayList<PostsArticleBaseBean>();
    private List<PostsArticleBaseBean> bookArray=new ArrayList<PostsArticleBaseBean>();
    private Button lesson_moreBtn;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_beta;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        homeRecyclerView = getActivity().findViewById(R.id.homeRecyclerView);
        headView = getLayoutInflater().inflate(R.layout.main_fragment_discovery_header, (ViewGroup) homeRecyclerView.getParent(), false);
        lessonRecyclerView = headView.findViewById(R.id.lesson_recyclerView);
        lesson_moreBtn = headView.findViewById(R.id.lesson_moreBtn);
        lesson_moreBtn.setOnClickListener(this);

        lessonAdapter = new LessonAdapter(getActivity(),dataArray);
        lessonAdapter.openLoadAnimation();
        lessonAdapter.setNotDoAnimationCount(4);
//        lessonRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        lessonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostsArticleBaseBean postsArticleBaseBean=dataArray.get( position );
                String lessonID=postsArticleBaseBean.getObject_id();
                String lessonName=postsArticleBaseBean.getPost_title();
                String lessonPrice=postsArticleBaseBean.getPost_price();
                String thumbVideoString = postsArticleBaseBean.getThumb_video();
                if (!(thumbVideoString.startsWith( "http://" )||thumbVideoString.startsWith( "https://" ))) {
                    thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
                }
               Intent intent = new Intent(getActivity(), SPLessonDetailActivity.class);
                intent.putExtra("videoPath", thumbVideoString);
                intent.putExtra("lesson_id" , lessonID);
                intent.putExtra("lesson_name" , lessonName);
                intent.putExtra("lesson_price" , lessonPrice);
                startActivity(intent);
//                Intent intent = new Intent(getActivity(),
//                        FullScreenActivity.class);
//                intent.putExtra("videoPath", thumbVideoString);
//                startActivity(intent);
            }
        });
        // 1.水平分页布局管理器
//        layoutManager = new PagerGridLayoutManager(
//                2, 2, PagerGridLayoutManager.HORIZONTAL);
//        layoutManager.setPageListener(this);    // 设置页面变化监听器
//        lessonRecyclerView.setLayoutManager(layoutManager);

        // 2.设置滚动辅助工具
//        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
//        pageSnapHelper.attachToRecyclerView(lessonRecyclerView);
        lessonRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        lessonRecyclerView.setAdapter(lessonAdapter);
        bookAdapter = new BookAdapter(getActivity(),dataArray);
        bookAdapter.openLoadAnimation();
        bookAdapter.setNotDoAnimationCount(4);
        homeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        bookAdapter.addHeaderView(headView);
        homeRecyclerView.setAdapter(bookAdapter);
        loadData();
    }

    private void loadData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas("4"),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        dataArray=homeDataBean.getReferer().getPosts();
                        bookArray=homeDataBean.getReferer().getPosts();
                        lessonAdapter.setNewData(dataArray);
                        bookAdapter.setNewData(bookArray);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.lesson_moreBtn:
                ActivityUtils.startActivity(getActivity(), HomeMoreLessonListActivity.class);
                break;
            default:
                break;
        }
    }

}
