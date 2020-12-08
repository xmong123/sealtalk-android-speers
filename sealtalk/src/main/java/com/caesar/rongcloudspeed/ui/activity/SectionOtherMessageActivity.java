package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.MessageSectionAdapter;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.SectionMessageBean;
import com.caesar.rongcloudspeed.bean.SectionMessageDataBean;
import com.caesar.rongcloudspeed.circle.ui.FriendTimeLineActivity;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.entity.MySectionEntity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
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
public class SectionOtherMessageActivity extends MultiStatusActivity implements OnRefreshListener {
    private List<MySectionEntity> sectionEntityList = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview_message_view)
    RecyclerView recyclerview_message_view;
    private MessageSectionAdapter sectionAdapter;
    private int cid;

    @Override
    public int getContentView() {
        return R.layout.activity_factory_mesage_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        cid = getIntent().getIntExtra("cid", 43);
        initTitleBarView(titlebar, "最新" + (cid == 43 ? "求助" : "课程"));
        initView();
        loadMessageData();


    }

    @SuppressLint("WrongConstant")
    private void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);

        recyclerview_message_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        sectionAdapter = new MessageSectionAdapter(R.layout.fragment_message_recyclerview_item, R.layout.message_section_head, sectionEntityList);

        sectionAdapter.setOnItemClickListener((adapter, view, position) -> {
            MySectionEntity mySection = sectionEntityList.get(position);
            if (mySection.isHeader) {
                Toast.makeText(SectionOtherMessageActivity.this, mySection.header, Toast.LENGTH_LONG).show();
            } else {
                PostsArticleBaseBean postsArticleBaseBean = mySection.t;
                if (cid == 43) {
                    String seekID = postsArticleBaseBean.getObject_id();
                    String rongID = postsArticleBaseBean.getRongid();
                    String userNiceName = postsArticleBaseBean.getUser_nicename();
                    String seekTitle = postsArticleBaseBean.getPost_title();
                    String seekPrice = postsArticleBaseBean.getPost_price();
                    String seekContent = postsArticleBaseBean.getPost_excerpt();
                    String seekDate = postsArticleBaseBean.getPost_date();
                    String photos_urls = postsArticleBaseBean.getPhotos_urls();
                    String post_author = postsArticleBaseBean.getPost_author();
                    Intent intent = new Intent(SectionOtherMessageActivity.this, SeekHelperDetailActivity.class);
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
                } else {
                    String lessonID = postsArticleBaseBean.getObject_id();
                    String lessonName = postsArticleBaseBean.getPost_title();
                    String lessonPrice = postsArticleBaseBean.getPost_price();
                    String lessonSmeta = postsArticleBaseBean.getSmeta();
                    String lessonContent = postsArticleBaseBean.getPost_excerpt();
                    String lessonSource = postsArticleBaseBean.getPost_source();
                    Intent intent = new Intent(SectionOtherMessageActivity.this, SPLessonDetailActivity.class);
                    intent.putExtra("lesson_id", lessonID);
                    intent.putExtra("lesson_name", lessonName);
                    intent.putExtra("lesson_price", lessonPrice);
                    intent.putExtra("lesson_smeta", lessonSmeta);
                    intent.putExtra("lesson_content", lessonContent);
                    intent.putExtra("lesson_source", lessonSource);
                    startActivity(intent);
                }
            }
        });
        recyclerview_message_view.setAdapter(sectionAdapter);
    }

    private void loadMessageData() {
        LogUtils.e("indexJsonSectionMessage");
        RetrofitManager.create().SectionMessageQuery(String.valueOf(cid), "2020-12-20")
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<SectionMessageDataBean>bindToLifecycle())
                .subscribe(new CommonObserver<SectionMessageDataBean>(refreshLayout) {
                    @Override
                    public void onSuccess(SectionMessageDataBean sectionMessageDataBean) {
                        if (sectionMessageDataBean.getCode() == Constant.CODE_SUCC) {
                            List<SectionMessageBean> messageBeanList = sectionMessageDataBean.getReferer();
                            if (messageBeanList != null && messageBeanList.size() > 0) {
                                sectionEntityList = new ArrayList<>();
                                for (SectionMessageBean messageBean : messageBeanList) {
                                    sectionEntityList.add(new MySectionEntity(true, messageBean.getQuery_date() + (cid == 43 ? "求助" : "课程"), false));
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
