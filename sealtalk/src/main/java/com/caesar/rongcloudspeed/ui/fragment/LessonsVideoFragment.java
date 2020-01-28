package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationProAdapter;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.LessonCateBean;
import com.caesar.rongcloudspeed.bean.LessonCategoryBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.player.PLVideoViewActivity;
import com.caesar.rongcloudspeed.ui.activity.SPSpeerDetailActivity;
import com.caesar.rongcloudspeed.ui.adapter.BookAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonVideoAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonsVideoAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.AVOptions;
import com.tencent.smtt.sdk.TbsVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面子界面-发现界面
 */
public class LessonsVideoFragment extends BaseFragment {
    private RecyclerView menuRecyclerView;
    private RecyclerView lessonsRecyclerView;
    private AnimationProAdapter proAdapter;
    private LessonVideoAdapter lessonAdapter;
    private List<LessonCategoryBean> menuArray=new ArrayList<LessonCategoryBean>();
    private List<PostsArticleBaseBean> dataArray=new ArrayList<PostsArticleBaseBean>();
//    private View headView;
    private String catid="4";
    LessonCategoryBean categoryBean=new LessonCategoryBean(true,"4","全部课程","0");

    @Override
    protected int getLayoutResId() {
        return R.layout.main_fragment_lessons_video;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        menuRecyclerView = getActivity().findViewById(R.id.lessmenu_recyclerView);
        lessonsRecyclerView = getActivity().findViewById(R.id.lessons_recyclerView);
//        headView = getLayoutInflater().inflate(R.layout.main_fragment_lessons_header, (ViewGroup) lessonsRecyclerView.getParent(), false);
        proAdapter = new AnimationProAdapter(menuArray);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        proAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                catid=menuArray.get(position).getTerm_id();
                for (int i = 0; i < menuArray.size(); i++) {
                    if (i == position) {
                        menuArray.get( i ).setFlag( true );
                    } else {
                        menuArray.get( i ).setFlag( false );
                    }
                    adapter.notifyDataSetChanged();
                }
                loadData();
            }
        });

        lessonAdapter = new LessonVideoAdapter(getActivity(),dataArray);
        lessonAdapter.openLoadAnimation();
        lessonAdapter.setNotDoAnimationCount(4);

        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lessonsRecyclerView.setHasFixedSize(true);
//        lessonsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        lessonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostsArticleBaseBean postsArticleBaseBean=dataArray.get( position );
                String lesson_id=postsArticleBaseBean.getObject_id();
                String lesson_name=postsArticleBaseBean.getPost_title();
                String lesson_price=postsArticleBaseBean.getPost_price();
                String thumbVideoString = postsArticleBaseBean.getThumb_video();
                if (!thumbVideoString.startsWith( "http://" )) {
                    thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
                }
                Intent intent = new Intent(getActivity(), SPSpeerDetailActivity.class);
                intent.putExtra("videoPath", thumbVideoString);
                intent.putExtra("lesson_id" , lesson_id);
                intent.putExtra("lesson_name" , lesson_name);
                intent.putExtra("lesson_price" , lesson_price);
                startActivity(intent);
//                if (TbsVideo.canUseTbsPlayer(getActivity())) {
//                    TbsVideo.openVideo(getActivity(), thumbVideoString);
//                }
            }
        });
//        lessonAdapter.addHeaderView(headView);
        menuRecyclerView.setAdapter(proAdapter);
        lessonsRecyclerView.setAdapter(lessonAdapter);
        loadMenuData();
        loadData();
    }

    private void loadMenuData() {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteCateListDatas("4"),
                new NetworkCallback<LessonCateBean>() {
                    @Override
                    public void onSuccess(LessonCateBean lessonCateBean) {
                        menuArray=new ArrayList<LessonCategoryBean>();
                        menuArray.add(categoryBean);
                        menuArray.addAll(lessonCateBean.getReferer());
                        proAdapter.setNewData(menuArray);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void loadData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas(catid),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        dataArray=homeDataBean.getReferer().getPosts();
                        lessonAdapter.setNewData(dataArray);
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
            default:
                break;
        }
    }
}
