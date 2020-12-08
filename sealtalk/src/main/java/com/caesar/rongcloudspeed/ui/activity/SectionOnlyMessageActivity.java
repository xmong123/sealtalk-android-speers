package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
public class SectionOnlyMessageActivity extends MultiStatusActivity implements OnRefreshListener {
    private List<MySectionEntity> sectionEntityList = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview_message_view)
    RecyclerView recyclerview_message_view;
    private MessageSectionAdapter sectionAdapter;

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
                Toast.makeText(SectionOnlyMessageActivity.this, mySection.header, Toast.LENGTH_LONG).show();
            }
        });
        sectionAdapter.setOnItemChildClickListener((adapter, view, position) -> Toast.makeText(SectionOnlyMessageActivity.this, "onItemChildClick" + position, Toast.LENGTH_LONG).show());
        recyclerview_message_view.setAdapter(sectionAdapter);
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
