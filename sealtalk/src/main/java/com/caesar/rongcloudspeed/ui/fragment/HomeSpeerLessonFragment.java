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
import com.caesar.rongcloudspeed.ui.activity.HomeMoreLessonListActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonDetailActivity;
import com.caesar.rongcloudspeed.ui.adapter.BookAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面子界面-发现界面
 */
public class HomeSpeerLessonFragment extends BaseFragment implements OnRefreshListener {
    private RecyclerView homeRecyclerView;
    private LessonAdapter lessonAdapter;
    private View headView;
    private List<PostsArticleBaseBean> dataArray = new ArrayList<PostsArticleBaseBean>();
    private Button lesson_moreBtn;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home_beta;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        homeRecyclerView = getActivity().findViewById(R.id.homeRecyclerView);
        headView = getLayoutInflater().inflate(R.layout.main_fragment_home_header, (ViewGroup) homeRecyclerView.getParent(), false);
        lesson_moreBtn = headView.findViewById(R.id.lesson_moreBtn);
        lesson_moreBtn.setOnClickListener(this);

        lessonAdapter = new LessonAdapter(getActivity(), dataArray);
        lessonAdapter.openLoadAnimation();
        lessonAdapter.setNotDoAnimationCount(4);
        lessonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
                Intent intent = new Intent(getActivity(), SPLessonDetailActivity.class);
                intent.putExtra("videoPath", thumbVideoString);
                intent.putExtra("lesson_id", lessonID);
                intent.putExtra("lesson_name", lessonName);
                intent.putExtra("lesson_price", lessonPrice);
                intent.putExtra("lesson_smeta", lessonSmeta);
                intent.putExtra("lesson_content", lessonContent);
                intent.putExtra("lesson_source", lessonSource);
                startActivity(intent);
            }
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

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadData();
    }
}
