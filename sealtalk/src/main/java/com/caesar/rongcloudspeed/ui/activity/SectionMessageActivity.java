package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.MessageSectionAdapter;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.SectionMessageBean;
import com.caesar.rongcloudspeed.bean.SectionMessageDataBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.entity.MySectionEntity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.manager.RetrofitManageres;
import com.caesar.rongcloudspeed.network.Api;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiw.circledemo.bean.CircleHeaderItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionMessageActivity extends MultiStatusActivity implements OnRefreshListener {
    private List<MySectionEntity> sectionEntityList = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview_message_view)
    RecyclerView recyclerview_message_view;
    private MessageSectionAdapter sectionAdapter;
    private CircleHeaderItem headerItem1;
    private CircleHeaderItem headerItem2;
//    private View headView;

    @Override
    public int getContentView() {
        return R.layout.activity_factory_mesage_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitleBarView(titlebar, "系统消息");
        initView();
        loadMessageData();
        headerItem1 = (CircleHeaderItem) getIntent().getSerializableExtra("headerItem1");
        headerItem2 = (CircleHeaderItem) getIntent().getSerializableExtra("headerItem2");
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
//        headView = getLayoutInflater().inflate(R.layout.factory_extension_view, (ViewGroup) recyclerview_message_view.getParent(), false);

        recyclerview_message_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

//        mRecyclerView.addItemDecoration(new GridSectionAverageGapItemDecoration(10,10,20,15));

        sectionAdapter = new MessageSectionAdapter(R.layout.fragment_message_recyclerview_item, R.layout.message_section_head, sectionEntityList);

        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MySectionEntity mySection = sectionEntityList.get(position);
                if (mySection.isHeader) {
                    Toast.makeText(SectionMessageActivity.this, mySection.header, Toast.LENGTH_LONG).show();
                } else {
                    if (position == 1) {
                        String lessonID = headerItem1.getObject_id();
                        String lessonName = headerItem1.getPost_title();
                        String lessonPrice = headerItem1.getPost_price();
                        String lessonSmeta = headerItem1.getSmeta();
                        String lessonContent = headerItem1.getPost_excerpt();
                        String lessonSource = headerItem1.getPost_source();
                        Intent intent = new Intent(SectionMessageActivity.this, SPLessonDetailActivity.class);
                        intent.putExtra("lesson_id", lessonID);
                        intent.putExtra("lesson_name", lessonName);
                        intent.putExtra("lesson_price", lessonPrice);
                        intent.putExtra("lesson_smeta", lessonSmeta);
                        intent.putExtra("lesson_content", lessonContent);
                        intent.putExtra("lesson_source", lessonSource);
                        startActivity(intent);
                    } else if (position == 3) {
                        String seekID = headerItem2.getObject_id();
                        String rongID = headerItem2.getRongid();
                        String userNiceName = headerItem2.getUser_nicename();
                        String seekTitle = headerItem2.getPost_title();
                        String seekPrice = headerItem2.getPost_price();
                        String seekContent = headerItem2.getPost_excerpt();
                        String seekDate = headerItem2.getPost_date();
                        String photos_urls = headerItem2.getPhotos_urls();
                        String post_author = headerItem2.getPost_author();
                        Intent intent = new Intent(SectionMessageActivity.this, SeekHelperDetailActivity.class);
                        intent.putExtra("seek_id", seekID);
                        intent.putExtra("rong_id", rongID);
                        intent.putExtra("user_nicename", userNiceName);
                        intent.putExtra("seek_title", seekTitle);
                        intent.putExtra("seek_date", seekDate);
                        intent.putExtra("seek_price", seekPrice);
                        intent.putExtra("seek_content", seekContent);
                        intent.putExtra("photos_urls", photos_urls);
                        intent.putExtra("post_author", post_author);
                        startActivity(intent);
                    }
//                    Toast.makeText(SectionMessageActivity.this, mySection.t.getPost_title(), Toast.LENGTH_LONG).show();
                }
            }
        });
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(SectionMessageActivity.this, "onItemChildClick" + position, Toast.LENGTH_LONG).show();
            }
        });
        recyclerview_message_view.setAdapter(sectionAdapter);
//        sectionAdapter.addHeaderView(headView);
    }

    private void loadMessageData() {
        LogUtils.e("indexJsonSectionMessage");
        RetrofitManager.create().SectionMessageQuery("46", "2020-02-20")
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<SectionMessageDataBean>bindToLifecycle())
                .subscribe(new CommonObserver<SectionMessageDataBean>(refreshLayout) {
                    @Override
                    public void onSuccess(SectionMessageDataBean sectionMessageDataBean) {
                        if (sectionMessageDataBean.getCode() == Constant.CODE_SUCC) {
                            List<SectionMessageBean> messageBeanList = sectionMessageDataBean.getReferer();
                            if (messageBeanList != null && messageBeanList.size() > 0) {
                                sectionEntityList = new ArrayList<>();
                                sectionEntityList.add(new MySectionEntity(true, "最新课程", false));
                                PostsArticleBaseBean bean1 = new PostsArticleBaseBean();
                                bean1.setObject_id(headerItem1.getObject_id());
                                bean1.setPost_title(headerItem1.getPost_title());
                                bean1.setPost_price(headerItem1.getPost_price());
                                bean1.setPost_type("2");
                                bean1.setSmeta(headerItem1.getSmeta());
                                sectionEntityList.add(new MySectionEntity(bean1));
                                sectionEntityList.add(new MySectionEntity(true, "最新求助", false));
                                PostsArticleBaseBean bean2 = new PostsArticleBaseBean();
                                bean2.setObject_id(headerItem2.getObject_id());
                                bean2.setPost_title(headerItem2.getPost_title());
                                bean2.setPost_price(headerItem2.getPost_price());
                                bean2.setPost_excerpt(headerItem2.getPost_excerpt());
                                bean2.setPost_date(headerItem2.getPost_date());
                                bean2.setPost_type("3");
                                bean2.setPhotos_urls(headerItem2.getPhotos_urls());
                                sectionEntityList.add(new MySectionEntity(bean2));
                                for (SectionMessageBean messageBean : messageBeanList) {
                                    sectionEntityList.add(new MySectionEntity(true, messageBean.getQuery_date() + "消息", false));
                                    List<PostsArticleBaseBean> postsList = messageBean.getPost_array();
                                    if (postsList != null && postsList.size() > 0) {
                                        for (PostsArticleBaseBean postsArticleBaseBean : postsList) {
                                            sectionEntityList.add(new MySectionEntity(postsArticleBaseBean));
                                        }
                                    }
                                }
                            }
                        }
                        sectionAdapter.setNewData(sectionEntityList);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("Info", "onResult:" + requestCode + " onResult:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadMessageData();
    }


}
