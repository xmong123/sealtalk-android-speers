package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.LessonSectionAdapter;
import com.caesar.rongcloudspeed.adapter.RecommendItemAdapter;
import com.caesar.rongcloudspeed.bean.AppPeopleBaseBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.PostsLessonBean;
import com.caesar.rongcloudspeed.bean.SectionLessonBaseBean;
import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.HomeMoreLessonListActivity;
import com.caesar.rongcloudspeed.ui.activity.HomeMorePeopleListActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.UserDetailActivity;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 主界面子界面-发现界面
 */
public class HomeLessonArticleFragment extends BaseFragment implements OnRefreshListener {
    private RecyclerView homeRecyclerView;
    private RecyclerView peopleRecyclerView;
    private LessonSectionAdapter lessonSectionAdapter;
    private RecommendItemAdapter peopleAdapter;
    private View headView;
    private SmartRefreshLayout refreshLayout;
    private List<SectionLessonBaseBean.SectionLessonBean> lessonBeanArrayList = new ArrayList<SectionLessonBaseBean.SectionLessonBean>();
    private List<AppPeopleBaseBean.PeopleDataBean> peopleArray = new ArrayList<AppPeopleBaseBean.PeopleDataBean>();
    private Button people_moreBtn;
    private String uidString;
    private static String TAG = "HomeLessonArticleFragment";

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_beta;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        refreshLayout= getActivity().findViewById(R.id.home_refreshLayout);
        homeRecyclerView = getActivity().findViewById(R.id.homeRecyclerView);
        headView = getLayoutInflater().inflate(R.layout.main_fragment_section_header, (ViewGroup) homeRecyclerView.getParent(), false);
        peopleRecyclerView = headView.findViewById(R.id.peopleRecyclerView);

        peopleAdapter = new RecommendItemAdapter(getActivity(), peopleArray);
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        peopleRecyclerView.setAdapter(peopleAdapter);

        peopleAdapter.setOnItemClickListener((adapter, view, position) -> {
            AppPeopleBaseBean.PeopleDataBean peopleDataBean = peopleArray.get(position);
            RongIM.getInstance().startPrivateChat(getActivity(), peopleDataBean.getRongid(), peopleDataBean.getUser_login());
        });
        peopleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemChildClick: ");
                AppPeopleBaseBean.PeopleDataBean peopleDataBean = peopleArray.get(position);
                Intent deintent = new Intent(getActivity(), UserDetailActivity.class);
                deintent.putExtra(IntentExtra.STR_TARGET_ID, peopleDataBean.getRongid());
                startActivity(deintent);
            }
        });
        people_moreBtn = headView.findViewById(R.id.people_moreBtn);
        people_moreBtn.setOnClickListener(this);
        uidString = UserInfoUtils.getAppUserId(getActivity());
        lessonSectionAdapter = new LessonSectionAdapter(getActivity(), lessonBeanArrayList);
        lessonSectionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SectionLessonBaseBean.SectionLessonBean sectionLessonBean = lessonBeanArrayList.get(position);
            if (view.getId() == R.id.section_more_btn) {
                Intent moreIntent = new Intent(getActivity(), HomeMoreLessonListActivity.class);
                moreIntent.putExtra("catid",sectionLessonBean.getTerm_id());
                moreIntent.putExtra("title",sectionLessonBean.getName());
                startActivity(moreIntent);
            } else {
                List<PostsLessonBean> lessonBeanList = sectionLessonBean.getPosts();
                PostsLessonBean postsLessonBean = lessonBeanList.get(0);
                if (view.getId() == R.id.section_lesson_layout) {
                    postsLessonBean = lessonBeanList.get(0);
                } else if (view.getId() == R.id.section_lesson_layout1) {
                    postsLessonBean = lessonBeanList.get(1);
                } else if (view.getId() == R.id.section_lesson_layout2) {
                    postsLessonBean = lessonBeanList.get(2);
                } else if (view.getId() == R.id.section_lesson_layout3) {
                    postsLessonBean = lessonBeanList.get(3);
                }
                String lessonID = postsLessonBean.getObject_id();
                String lessonName = postsLessonBean.getPost_title();
                String lessonContent = postsLessonBean.getPost_excerpt();
                String lessonSource = postsLessonBean.getPost_source();
                String lessonPrice = postsLessonBean.getPost_price();
                String lessonSmeta = postsLessonBean.getSmeta();
                String thumbVideoString = postsLessonBean.getThumb_video();
                if (!(thumbVideoString.startsWith("http"))) {
                    thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
                }
                Intent intent1 = new Intent(getActivity(), SPLessonDetailActivity.class);
                intent1.putExtra("videoPath", thumbVideoString);
                intent1.putExtra("lesson_id", lessonID);
                intent1.putExtra("lesson_name", lessonName);
                intent1.putExtra("lesson_price", lessonPrice);
                intent1.putExtra("lesson_smeta", lessonSmeta);
                intent1.putExtra("lesson_content", lessonContent);
                intent1.putExtra("lesson_source", lessonSource);
                startActivity(intent1);
            }

        });

        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeRecyclerView.setAdapter(lessonSectionAdapter);
        lessonSectionAdapter.addHeaderView(headView);
        refreshLayout.setOnRefreshListener(this);
        loadData();
    }

    private void loadData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getIndexLessonData("4"),
                new NetworkCallback<SectionLessonBaseBean>() {
                    @Override
                    public void onSuccess(SectionLessonBaseBean lessonBaseBean) {
                        lessonBeanArrayList = lessonBaseBean.getReferer();
                        lessonSectionAdapter.setNewData(lessonBeanArrayList);
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
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getAppRecommendMan(uidString),
                new NetworkCallback<AppPeopleBaseBean>() {
                    @Override
                    public void onSuccess(AppPeopleBaseBean peopleBaseBean) {
                        List<AppPeopleBaseBean.PeopleDataBean> peopleList = peopleBaseBean.getReferer();
                        peopleArray = new ArrayList<AppPeopleBaseBean.PeopleDataBean>();
                        for (int i = 0; i < 6; i++) {
                            AppPeopleBaseBean.PeopleDataBean peopleDataBean = peopleList.get(i);
                            peopleArray.add(peopleDataBean);
                        }
                        peopleAdapter.setNewData(peopleArray);
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
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.people_moreBtn:
                ActivityUtils.startActivity(getActivity(), HomeMorePeopleListActivity.class);
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
