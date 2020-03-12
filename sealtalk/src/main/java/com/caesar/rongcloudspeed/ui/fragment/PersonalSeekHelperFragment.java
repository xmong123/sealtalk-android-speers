package com.caesar.rongcloudspeed.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.UserSeekHelperAdapter;
import com.caesar.rongcloudspeed.bean.HomeSeekListBean;
import com.caesar.rongcloudspeed.bean.PostsSeekBaseBean;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.rxlife.RxFragment;
import com.caesar.rongcloudspeed.ui.activity.AnimationPaltSeekActivity;
import com.caesar.rongcloudspeed.ui.activity.SeekHelperDetailActivity;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;


public class PersonalSeekHelperFragment extends RxFragment implements OnRefreshListener {
    Unbinder unbinder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.userseek_recyclerview)
    RecyclerView userSeekRecyclerview;
    private String cid = "43";
    private String tag = "neq";
    private String uidString;
    private UserSeekHelperAdapter userSeekHelperAdapter;

    public static PersonalSeekHelperFragment newInstance(int param1, String param2) {
        PersonalSeekHelperFragment fragment = new PersonalSeekHelperFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uidString = UserInfoUtils.getAppUserId(getActivity());
        LogUtils.e("loadAnimation1Fragment");
        initAnimationPostsAdapter();
        loadSeekData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_seekhelper, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void loadSeekData() {
        RetrofitManager.create().indexSeekListJson(uidString,cid,tag)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HomeSeekListBean>bindToLifecycle())
                .subscribe(new CommonObserver<HomeSeekListBean>(refreshLayout) {
                    @Override
                    public void onSuccess(HomeSeekListBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            userSeekHelperAdapter = new UserSeekHelperAdapter(getActivity(), value.getReferer(), tag);
                            userSeekHelperAdapter.setNotDoAnimationCount(3);
                            userSeekRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                            userSeekHelperAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    PostsSeekBaseBean postsArticleBaseBean = value.getReferer().get(position);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("seek_id", postsArticleBaseBean.getObject_id());
                                    bundle.putString("seek_title", postsArticleBaseBean.getPost_title());
                                    bundle.putString("seek_date", postsArticleBaseBean.getPost_date());
                                    bundle.putString("seek_price", postsArticleBaseBean.getPost_price());
                                    bundle.putString("seek_content", postsArticleBaseBean.getPost_excerpt());
                                    bundle.putString("photos_urls", postsArticleBaseBean.getPhotos_urls());
                                    ActivityUtils.startActivity(bundle, SeekHelperDetailActivity.class);
                                }
                            });
                            userSeekRecyclerview.setAdapter(userSeekHelperAdapter);
                            userSeekHelperAdapter.loadMoreEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initAnimationPostsAdapter() {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadSeekData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
