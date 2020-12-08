package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.LessonSectionAdapter;
import com.caesar.rongcloudspeed.adapter.MainLessonAdapter;
import com.caesar.rongcloudspeed.adapter.PersonnelRecruitAdapter;
import com.caesar.rongcloudspeed.adapter.RecommendExpertAdapter;
import com.caesar.rongcloudspeed.adapter.RecommendItemAdapter;
import com.caesar.rongcloudspeed.adapter.RecruitItemAdapter;
import com.caesar.rongcloudspeed.bean.AppPeopleBaseBean;
import com.caesar.rongcloudspeed.bean.HomeDataBaseBean;
import com.caesar.rongcloudspeed.bean.HomeLessonesBaseBean;
import com.caesar.rongcloudspeed.bean.PeopleItemBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.PostsArticleVideoBean;
import com.caesar.rongcloudspeed.bean.PostsLessonBean;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBean;
import com.caesar.rongcloudspeed.bean.RecruitWorkBaseBean;
import com.caesar.rongcloudspeed.bean.SectionLessonBaseBean;
import com.caesar.rongcloudspeed.bean.VideoSmetArrayBean;
import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.implement.MainTabItemListener;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.player.PLVideoViewActivity;
import com.caesar.rongcloudspeed.ui.activity.HomeMoreLessonListActivity;
import com.caesar.rongcloudspeed.ui.activity.HomeMorePeopleListActivity;
import com.caesar.rongcloudspeed.ui.activity.PersonnelDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.RecruitPostDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonVideoDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.UserDetailActivity;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.AVOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.rong.imkit.RongIM;

/**
 * 主界面子界面-发现界面
 */
public class MainHomeArticleFragment extends BaseFragment implements OnRefreshListener {
    private RecyclerView homeRecyclerView;
    private RecyclerView peopleRecyclerView;
    private RecyclerView lessonRecyclerView;
    private RecyclerView personnelRecyclerView;
    private RecommendExpertAdapter peopleAdapter;
    private MainLessonAdapter lessonAdapter;
    private RecruitItemAdapter recruitItemAdapter;
    private PersonnelRecruitAdapter personnelRecruitAdapter;
    private View headerView;
    private View footerView;
    private SmartRefreshLayout refreshLayout;
    private List<PeopleItemBean> peopleItemBeanList = new ArrayList<PeopleItemBean>();
    private List<PostsArticleVideoBean> lessonArray = new ArrayList<PostsArticleVideoBean>();
    private List<RecruitJobBean> recruitJobBeanList = new ArrayList<RecruitJobBean>();
    private List<RecruitItemBean> personnelList = new ArrayList<RecruitItemBean>();
    private Button peopleMoreBtn, lessonMoreBtn, jobMoreBtn, personnelMoreBtn;
    private String uidString;
    private static String TAG = "HomeLessonArticleFragment";
    private static MainTabItemListener mainTabItemListener;

    public static MainHomeArticleFragment newInstance(MainTabItemListener mainTabItemListener1) {
        MainHomeArticleFragment fragment = new MainHomeArticleFragment();
        mainTabItemListener = mainTabItemListener1;
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_beta;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        uidString = UserInfoUtils.getAppUserId(getActivity());
        refreshLayout = getActivity().findViewById(R.id.home_refreshLayout);
        homeRecyclerView = getActivity().findViewById(R.id.homeRecyclerView);
        headerView = getLayoutInflater().inflate(R.layout.main_fragment_recycle_header, (ViewGroup) homeRecyclerView.getParent(), false);
        footerView = getLayoutInflater().inflate(R.layout.main_fragment_recycle_footer, (ViewGroup) homeRecyclerView.getParent(), false);

        peopleRecyclerView = headerView.findViewById(R.id.peopleRecyclerView);
        lessonRecyclerView = headerView.findViewById(R.id.lessonRecyclerView);
        personnelRecyclerView = footerView.findViewById(R.id.personnelRecyclerView);
        peopleAdapter = new RecommendExpertAdapter(getActivity(), peopleItemBeanList);

        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        peopleRecyclerView.setAdapter(peopleAdapter);

        peopleAdapter.setOnItemClickListener((adapter, view, position) -> {
            PeopleItemBean peopleDataBean = peopleItemBeanList.get(position);
            RongIM.getInstance().startPrivateChat(getActivity(), peopleDataBean.getRongid(), peopleDataBean.getUser_login());
        });
        peopleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemChildClick: ");
                PeopleItemBean peopleDataBean = peopleItemBeanList.get(position);
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra(IntentExtra.STR_TARGET_ID, peopleDataBean.getRongid());
                startActivity(intent);
            }
        });
        peopleMoreBtn = headerView.findViewById(R.id.people_more_btn);
        lessonMoreBtn = headerView.findViewById(R.id.lesson_more_btn);
        jobMoreBtn = headerView.findViewById(R.id.job_more_btn);
        personnelMoreBtn = footerView.findViewById(R.id.personnel_more_btn);
        peopleMoreBtn.setOnClickListener(this);
        lessonMoreBtn.setOnClickListener(this);
        jobMoreBtn.setOnClickListener(this);
        personnelMoreBtn.setOnClickListener(this);
        lessonAdapter = new MainLessonAdapter(getActivity(), lessonArray);
        lessonAdapter.openLoadAnimation();
        lessonAdapter.setNotDoAnimationCount(4);
        lessonRecyclerView.setAdapter(lessonAdapter);
        lessonRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        lessonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostsArticleVideoBean postsArticleBaseBean = lessonArray.get(position);
                String lessonID = postsArticleBaseBean.getObject_id();
                String lessonName = postsArticleBaseBean.getPost_title();
                String lessonContent = postsArticleBaseBean.getPost_excerpt();
                String lessonSource = postsArticleBaseBean.getPost_source();
                String lessonPrice = postsArticleBaseBean.getPost_price();
                String lessonSmeta = postsArticleBaseBean.getSmeta();
                VideoSmetArrayBean videoSmetArrayBean=postsArticleBaseBean.getSmetarray();
                String thumbVideoString = postsArticleBaseBean.getThumb_video();
                if (!(thumbVideoString.startsWith("http"))) {
                    thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
                }
                Intent intent1 = new Intent(getActivity(), SPLessonVideoDetailActivity.class);
                intent1.putExtra("videoPath", thumbVideoString);
                intent1.putExtra("lesson_id", lessonID);
                intent1.putExtra("lesson_name", lessonName);
                intent1.putExtra("lesson_price", lessonPrice);
                intent1.putExtra("lesson_smeta", lessonSmeta);
                intent1.putExtra("lesson_content", lessonContent);
                intent1.putExtra("lesson_source", lessonSource);
                intent1.putExtra("videoSmetArrayBean", videoSmetArrayBean);
                startActivity(intent1);

            }
        });

        personnelRecruitAdapter = new PersonnelRecruitAdapter(getActivity(), personnelList);
        personnelRecruitAdapter.openLoadAnimation();
        personnelRecruitAdapter.setNotDoAnimationCount(3);
        personnelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        personnelRecruitAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecruitItemBean recruitItemBean = personnelList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("recruitItemBean", recruitItemBean);
                ActivityUtils.startActivity(bundle, getActivity(), PersonnelDetailActivity.class);
            }
        });
        personnelRecyclerView.setAdapter(personnelRecruitAdapter);

        recruitItemAdapter = new RecruitItemAdapter(getActivity(), recruitJobBeanList);
        recruitItemAdapter.openLoadAnimation();
        recruitItemAdapter.setNotDoAnimationCount(3);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recruitItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RecruitJobBean recruitJobBean = recruitJobBeanList.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("recruitJobBean", recruitJobBean);
                ActivityUtils.startActivity(bundle, getActivity(), RecruitPostDetailActivity.class);
            }
        });
        homeRecyclerView.setAdapter(recruitItemAdapter);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recruitItemAdapter.addHeaderView(headerView);
        recruitItemAdapter.addFooterView(footerView);
        refreshLayout.setOnRefreshListener(this);
        loadData();
    }

    private void loadData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getRecommandLessonVideoData("4"),
                new NetworkCallback<HomeLessonesBaseBean>() {
                    @Override
                    public void onSuccess(HomeLessonesBaseBean homeLessonesBaseBean) {
                        lessonArray = homeLessonesBaseBean.getReferer();
                        lessonAdapter.setNewData(lessonArray);
                        dismissLoadingDialog();
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        refreshLayout.finishRefresh();
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                });

        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getRecruitPostLists(uidString),
                new NetworkCallback<RecruitWorkBaseBean>() {
                    @Override
                    public void onSuccess(RecruitWorkBaseBean recruitWorkBaseBean) {
                        if (recruitWorkBaseBean.getCode() == Constant.CODE_SUCC) {
                            recruitJobBeanList = recruitWorkBaseBean.getReferer().getJobList();
                            personnelList = recruitWorkBaseBean.getReferer().getPersonnelList();
                            peopleItemBeanList = recruitWorkBaseBean.getReferer().getPeopleList();
                            recruitItemAdapter.setNewData(recruitJobBeanList);
                            personnelRecruitAdapter.setNewData(personnelList);
                            peopleAdapter.setNewData(peopleItemBeanList);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        refreshLayout.finishRefresh();
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
            case R.id.people_more_btn:
                ActivityUtils.startActivity(getActivity(), HomeMorePeopleListActivity.class);
                break;
            case R.id.lesson_more_btn:
                if (mainTabItemListener != null) {
                    mainTabItemListener.onMainTabItemListener(1);
                }
                break;
            case R.id.job_more_btn:
                if (mainTabItemListener != null) {
                    mainTabItemListener.onMainTabItemListener(3);
                    Intent intent = new Intent("com.caesar.action.order");
                    intent.putExtra("orderItem",0);
                    getActivity().sendBroadcast(intent);
                }
                break;
            case R.id.personnel_more_btn:
                if (mainTabItemListener != null) {
                    mainTabItemListener.onMainTabItemListener(3);
                    Intent intent = new Intent("com.caesar.action.order");
                    intent.putExtra("orderItem",2);
                    getActivity().sendBroadcast(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadData();
    }
}
