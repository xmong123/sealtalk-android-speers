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
import com.caesar.rongcloudspeed.adapter.RecommendListAdapter;
import com.caesar.rongcloudspeed.adapter.RecommendItemAdapter;
import com.caesar.rongcloudspeed.bean.AppPeopleBaseBean;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.common.IntentExtra;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.HomeMoreLessonListActivity;
import com.caesar.rongcloudspeed.ui.activity.HomeMorePeopleListActivity;
import com.caesar.rongcloudspeed.ui.activity.PrivateChatSettingActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.UserDetailActivity;
import com.caesar.rongcloudspeed.ui.adapter.BookAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 主界面子界面-发现界面
 */
public class HomeSpeerLessonFragment extends BaseFragment implements OnRefreshListener {
    private RecyclerView homeRecyclerView;
    private RecyclerView peopleRecyclerView;
    private LessonAdapter lessonAdapter;
    private RecommendItemAdapter peopleAdapter;
    private View headView;
    private List<PostsArticleBaseBean> dataArray = new ArrayList<PostsArticleBaseBean>();
    private List<AppPeopleBaseBean.PeopleDataBean> peopleArray = new ArrayList<AppPeopleBaseBean.PeopleDataBean>();
    private Button people_moreBtn;
    private Button lesson_moreBtn;
    private String uidString;
    private static String TAG = "HomeSpeerLessonFragment";
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_beta;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        homeRecyclerView = getActivity().findViewById(R.id.homeRecyclerView);
        headView = getLayoutInflater().inflate(R.layout.main_fragment_home_header, (ViewGroup) homeRecyclerView.getParent(), false);
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
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra(IntentExtra.STR_TARGET_ID, peopleDataBean.getRongid());
                startActivity(intent);
            }
        });
        people_moreBtn = headView.findViewById(R.id.people_moreBtn);
        lesson_moreBtn = headView.findViewById(R.id.lesson_moreBtn);
        people_moreBtn.setOnClickListener(this);
        lesson_moreBtn.setOnClickListener(this);
        uidString = UserInfoUtils.getAppUserId(getActivity());
        lessonAdapter = new LessonAdapter(getActivity(), dataArray);
        lessonAdapter.openLoadAnimation();
        lessonAdapter.setNotDoAnimationCount(4);
        lessonAdapter.setOnItemClickListener((adapter, view, position) -> {
            PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
            String lessonID = postsArticleBaseBean.getObject_id();
            String lessonName = postsArticleBaseBean.getPost_title();
            String lessonContent = postsArticleBaseBean.getPost_excerpt();
            String lessonSource = postsArticleBaseBean.getPost_source();
            String lessonPrice = postsArticleBaseBean.getPost_price();
            String lessonSmeta = postsArticleBaseBean.getSmeta();
            String thumbVideoString = postsArticleBaseBean.getThumb_video();
            if (!(thumbVideoString.startsWith("http://") || thumbVideoString.startsWith("https://"))) {
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
        });
        homeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        homeRecyclerView.setAdapter(lessonAdapter);
        lessonAdapter.addHeaderView(headView);
        loadData();
    }

    private void loadData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas("4"),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        dataArray = homeDataBean.getReferer().getPosts();
                        lessonAdapter.setNewData(dataArray);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getAppRecommendMan(uidString),
                new NetworkCallback<AppPeopleBaseBean>() {
                    @Override
                    public void onSuccess(AppPeopleBaseBean peopleBaseBean) {
                        List<AppPeopleBaseBean.PeopleDataBean> peopleList = peopleBaseBean.getReferer();
                        peopleArray = new ArrayList<AppPeopleBaseBean.PeopleDataBean>();
                        for (int i = 0; i < 3; i++) {
                            AppPeopleBaseBean.PeopleDataBean peopleDataBean = peopleList.get(i);
                            peopleArray.add(peopleDataBean);
                        }
                        peopleAdapter.setNewData(peopleArray);
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
            case R.id.people_moreBtn:
                ActivityUtils.startActivity(getActivity(), HomeMorePeopleListActivity.class);
                break;
            case R.id.lesson_moreBtn:
                ActivityUtils.startActivity(getActivity(), HomeMoreLessonListActivity.class);
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
