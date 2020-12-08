package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.UserFindOtherAdapter;
import com.caesar.rongcloudspeed.bean.HomeDataBaseBean;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.HomeDataUserBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.SPLessonDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonVideosActivity;
import com.caesar.rongcloudspeed.ui.activity.SeekHelperDetailActivity;
import com.caesar.rongcloudspeed.ui.adapter.SearchAdapter;
import com.caesar.rongcloudspeed.ui.adapter.models.SearchModel;
import com.caesar.rongcloudspeed.ui.interfaces.OnChatItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnContactItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnGroupItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnMessageRecordClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnShowMoreClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.SearchableInterface;
import com.caesar.rongcloudspeed.utils.CharacterParser;
import com.caesar.rongcloudspeed.utils.log.SLog;
import com.caesar.rongcloudspeed.viewmodel.SealSearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchOtherBaseFragment extends Fragment implements SearchableInterface {
    private static final String TAG = "SearchBaseFragment";
    private RecyclerView recyclerView;
    private UserFindOtherAdapter otherAdapter;
    private List<PostsArticleBaseBean> dataArray;
    protected String initSearch;
    private TextView tvEmpty;
    private String catid;
    private int type;

    public void init(int type) {
        this.type = type;
        switch (type) {
            case 0:
                catid = "4";
                initSearch = "同行课程";
                break;
            case 1:
                catid = "41";
                initSearch = "同行圈";
                break;
            case 2:
                catid = "47";
                initSearch = "同行招聘";
                break;
            case 3:
                catid = "43";
                initSearch = "同行求助";
                break;
            default:
                catid = "4";
                initSearch = "同行课程";
                break;
        }
        otherAdapter = new UserFindOtherAdapter(this, dataArray, type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_fragment_list, container, false);
        recyclerView = rootView.findViewById(R.id.rv_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(otherAdapter);
        otherAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (type == 0) {
                PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
                String lessonID = postsArticleBaseBean.getObject_id();
                String lessonName = postsArticleBaseBean.getPost_title();
                String lessonPrice = postsArticleBaseBean.getPost_price();
                String lessonContent = postsArticleBaseBean.getPost_excerpt();
                String lessonSmeta = postsArticleBaseBean.getSmeta();
                String lessonSource = postsArticleBaseBean.getPost_source();
                String thumbVideoString = postsArticleBaseBean.getThumb_video();
                if (!(thumbVideoString.startsWith("http://") || thumbVideoString.startsWith("https://"))) {
                    thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
                }
                Intent intent = new Intent(getContext(), SPLessonDetailActivity.class);
                intent.putExtra("videoPath", thumbVideoString);
                intent.putExtra("lesson_id", lessonID);
                intent.putExtra("lesson_name", lessonName);
                intent.putExtra("lesson_price", lessonPrice);
                intent.putExtra("lesson_smeta", lessonSmeta);
                intent.putExtra("lesson_content", lessonContent);
                intent.putExtra("lesson_source", lessonSource);
                startActivity(intent);
            } else if (type == 1) {

            } else if (type == 2) {

            } else if (type == 3) {
                PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("seek_id", postsArticleBaseBean.getObject_id());
                bundle.putString("rong_id", postsArticleBaseBean.getRongid());
                bundle.putString("user_nicename", postsArticleBaseBean.getUser_nicename());
                bundle.putString("seek_title", postsArticleBaseBean.getPost_title());
                bundle.putString("seek_date", postsArticleBaseBean.getPost_date());
                bundle.putString("seek_price", postsArticleBaseBean.getPost_price());
                bundle.putString("seek_content", postsArticleBaseBean.getPost_excerpt());
                bundle.putString("photos_urls", postsArticleBaseBean.getPhotos_urls());
                bundle.putString("post_author", postsArticleBaseBean.getPost_author());
                ActivityUtils.startActivity(bundle, SeekHelperDetailActivity.class);
            }

        });
        tvEmpty = rootView.findViewById(R.id.tv_empty_view);
        return rootView;
    }

    public void search(String search) {
        SLog.i(TAG, "search: " + search);
        initSearch = search;
        if (!TextUtils.isEmpty(search)) {
            NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatasWithTag(catid, search),
                    new NetworkCallback<HomeDataBaseBean>() {
                        @Override
                        public void onSuccess(HomeDataBaseBean homeDataBaseBean) {
                            List<PostsArticleBaseBean> articleBaseBeanList = homeDataBaseBean.getReferer();
                            dataArray = articleBaseBeanList;
                            if (dataArray != null && dataArray.size() > 0) {
                                otherAdapter.setNewData(dataArray);
                                tvEmpty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                tvEmpty.setVisibility(View.VISIBLE);
                                String empty = String.format(getString(R.string.seal_search_empty), initSearch);
                                int start = empty.indexOf(initSearch);
                                tvEmpty.setText(CharacterParser.getSpannable(empty, start, start + initSearch.length()));
                                recyclerView.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    public void clear() {
        otherAdapter.setNewData(new ArrayList<>());
    }

    /**
     * @return 上次搜索关键字
     */
    public String getInitSearch() {
        return initSearch;
    }
}
