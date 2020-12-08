package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationCategoryAdapter;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.LessonCateBean;
import com.caesar.rongcloudspeed.bean.LessonCategoryBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.SPLessonDetailActivity;
import com.caesar.rongcloudspeed.ui.adapter.LessonVideoAdapter;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面子界面-发现界面
 */
public class LessonsVideoFragment extends BaseFragment implements OnRefreshListener {
    private RecyclerView menuRecyclerView;
    private RecyclerView lessonsRecyclerView;
    private AnimationCategoryAdapter proAdapter;
    private LessonVideoAdapter lessonAdapter;
    private SmartRefreshLayout refreshLayout;
    private List<LessonCategoryBean> menuArray = new ArrayList<LessonCategoryBean>();
    private List<PostsArticleBaseBean> dataArray = new ArrayList<PostsArticleBaseBean>();
    private String catid = "4";
    private int page = 0;
    private boolean canLoadMore = false;
    private int totalCount = 20;
    LessonCategoryBean categoryBean = new LessonCategoryBean(true, "4", "全部课程", "0");

    @Override
    protected int getLayoutResId() {
        return R.layout.main_fragment_lessons_video;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        refreshLayout = getActivity().findViewById(R.id.refreshLayout);
        menuRecyclerView = getActivity().findViewById(R.id.lessmenu_recyclerView);
        lessonsRecyclerView = getActivity().findViewById(R.id.lessons_recyclerView);
        proAdapter = new AnimationCategoryAdapter(menuArray);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        proAdapter.setOnItemClickListener((adapter, view, position) -> {
            catid = menuArray.get(position).getTerm_id();
            for (int i = 0; i < menuArray.size(); i++) {
                if (i == position) {
                    menuArray.get(i).setFlag(true);
                } else {
                    menuArray.get(i).setFlag(false);
                }
                adapter.notifyDataSetChanged();
            }
            loadData();
        });

        lessonAdapter = new LessonVideoAdapter(getActivity(), dataArray);
        lessonAdapter.openLoadAnimation();
        lessonAdapter.setNotDoAnimationCount(4);
        lessonAdapter.setOnLoadMoreListener(() -> {
            if (canLoadMore) {
                page = page + 1;
                loadMoreData();
            } else {
                lessonAdapter.closeLoadAnimation();
                ToastUtils.showToast("加载结束,当前分类一共" + totalCount + "个视频课程");
            }
        }, lessonsRecyclerView);
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lessonsRecyclerView.setHasFixedSize(true);

        lessonAdapter.setOnItemClickListener((adapter, view, position) -> {
            PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
            String lesson_id = postsArticleBaseBean.getObject_id();
            String lesson_name = postsArticleBaseBean.getPost_title();
            String lesson_price = postsArticleBaseBean.getPost_price();
            String lessonSource = postsArticleBaseBean.getPost_source();
            String lessonSmeta = postsArticleBaseBean.getSmeta();
            String lessonContent = postsArticleBaseBean.getPost_excerpt();
            String thumbVideoString = postsArticleBaseBean.getThumb_video();
            if (!thumbVideoString.startsWith("http://")) {
                thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
            }
            Intent intent1 = new Intent(getActivity(), SPLessonDetailActivity.class);
            intent1.putExtra("videoPath", thumbVideoString);
            intent1.putExtra("lesson_id", lesson_id);
            intent1.putExtra("lesson_name", lesson_name);
            intent1.putExtra("lesson_price", lesson_price);
            intent1.putExtra("lesson_smeta", lessonSmeta);
            intent1.putExtra("lesson_content", lessonContent);
            intent1.putExtra("lesson_source", lessonSource);
            startActivity(intent1);
        });
        menuRecyclerView.setAdapter(proAdapter);
        lessonsRecyclerView.setAdapter(lessonAdapter);
        refreshLayout.setOnRefreshListener(this);
        loadMenuData();
        loadData();
    }

    private void loadMenuData() {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteCateListDatas("4"),
                new NetworkCallback<LessonCateBean>() {
                    @Override
                    public void onSuccess(LessonCateBean lessonCateBean) {
                        menuArray = new ArrayList<LessonCategoryBean>();
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
        canLoadMore = true;
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas(catid),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        List<PostsArticleBaseBean> articleBaseBeanList = homeDataBean.getReferer().getPosts();
                        if (articleBaseBeanList.size() < 20) {
                            canLoadMore = false;
                            totalCount = articleBaseBeanList.size();
                        }
                        dataArray = articleBaseBeanList;
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

    private void loadMoreData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDataForPage(catid, String.valueOf(page)),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        List<PostsArticleBaseBean> articleBaseBeanList = homeDataBean.getReferer().getPosts();
                        if (articleBaseBeanList.size() < 20) {
                            canLoadMore = false;
                            totalCount = (page + 1) * 20 + articleBaseBeanList.size();
                        }
                        dataArray.addAll(articleBaseBeanList);
                        lessonAdapter.setNewData(dataArray);
                        lessonAdapter.loadMoreEnd();
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        lessonAdapter.loadMoreEnd();
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 0;
        loadData();
    }
}
