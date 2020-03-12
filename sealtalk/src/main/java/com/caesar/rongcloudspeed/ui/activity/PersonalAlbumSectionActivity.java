package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AlbumSectionAdapter;
import com.caesar.rongcloudspeed.adapter.MessageSectionAdapter;
import com.caesar.rongcloudspeed.bean.PersonalPhotoBean;
import com.caesar.rongcloudspeed.bean.SectionAlbumBean;
import com.caesar.rongcloudspeed.bean.SectionPersonalAlbumDataBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.entity.AlbumSectionEntity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
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
public class PersonalAlbumSectionActivity extends MultiStatusActivity implements OnRefreshListener {
    private List<AlbumSectionEntity> sectionEntityList = new ArrayList<>();
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview_message_view)
    RecyclerView recyclerview_message_view;
    private AlbumSectionAdapter sectionAdapter;
    private CircleHeaderItem headerItem1;
    private CircleHeaderItem headerItem2;
    private String uidString;

    @Override
    public int getContentView() {
        return R.layout.activity_factory_mesage_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitleBarView(titlebar, "您的相册");
        uidString = UserInfoUtils.getAppUserId(this);
        initView();
        loadMPersonalAlbumData();
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshListener(this);
        recyclerview_message_view.setLayoutManager(new GridLayoutManager(this,4));
//        headView = getLayoutInflater().inflate(R.layout.factory_extension_view, (ViewGroup) recyclerview_message_view.getParent(), false);

//        recyclerview_message_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

//        mRecyclerView.addItemDecoration(new GridSectionAverageGapItemDecoration(10,10,20,15));

        sectionAdapter = new AlbumSectionAdapter(R.layout.fragment_album_recyclerview_item, R.layout.personal_album_section_head, sectionEntityList);

        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlbumSectionEntity mySection = sectionEntityList.get(position);
                if (mySection.isHeader) {
                    Toast.makeText(PersonalAlbumSectionActivity.this, mySection.header, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PersonalAlbumSectionActivity.this, mySection.t.getTitle(), Toast.LENGTH_LONG).show();
                }
            }
        });
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(PersonalAlbumSectionActivity.this, "onItemChildClick" + position, Toast.LENGTH_LONG).show();
            }
        });
        recyclerview_message_view.setAdapter(sectionAdapter);
//        sectionAdapter.addHeaderView(headView);
    }

    private void loadMPersonalAlbumData() {
        LogUtils.e("loadMPersonalAlbumData");
        RetrofitManager.create().getSectionPersonalPhoto(uidString, "2020-02-20")
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<SectionPersonalAlbumDataBean>bindToLifecycle())
                .subscribe(new CommonObserver<SectionPersonalAlbumDataBean>(refreshLayout) {
                    @Override
                    public void onSuccess(SectionPersonalAlbumDataBean sectionMessageDataBean) {
                        if (sectionMessageDataBean.getCode() == Constant.CODE_SUCC) {
                            List<SectionAlbumBean> albumBeanList = sectionMessageDataBean.getReferer();
                            if (albumBeanList != null && albumBeanList.size() > 0) {
                                sectionEntityList = new ArrayList<>();
                                for (SectionAlbumBean albumBean : albumBeanList) {
                                    sectionEntityList.add(new AlbumSectionEntity(true, albumBean.getQuery_date(), false));
                                    List<PersonalPhotoBean> photoArray = albumBean.getPost_array();
                                    if (photoArray != null && photoArray.size() > 0) {
                                        for (PersonalPhotoBean photoBean : photoArray) {
                                            sectionEntityList.add(new AlbumSectionEntity(photoBean));
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
        loadMPersonalAlbumData();
    }


}
