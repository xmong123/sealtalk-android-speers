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
import com.caesar.rongcloudspeed.circle.ui.ImagePagerActivity;
import com.caesar.rongcloudspeed.circle.widgets.MultiImageView;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.decoration.GridSectionAverageGapItemDecoration;
import com.caesar.rongcloudspeed.entity.AlbumSectionEntity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nostra13.universalimageloader.core.assist.ImageSize;
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
    @BindView(R.id.recyclerview_album_view)
    RecyclerView recyclerview_album_view;
    private AlbumSectionAdapter sectionAdapter;
    private String uidString;

    @Override
    public int getContentView() {
        return R.layout.activity_personal_album_list;
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
        recyclerview_album_view.setLayoutManager(new GridLayoutManager(this,4));

        sectionAdapter = new AlbumSectionAdapter(this,R.layout.fragment_album_recyclerview_item, R.layout.personal_album_section_head, sectionEntityList);

        sectionAdapter.setOnItemClickListener((adapter, view, position) -> {
            AlbumSectionEntity mySection = sectionEntityList.get(position);
            if (mySection.isHeader) {
                Toast.makeText(PersonalAlbumSectionActivity.this, mySection.header, Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(PersonalAlbumSectionActivity.this, mySection.t.getTitle(), Toast.LENGTH_LONG).show();
                ImagePagerActivity.imageSize = new ImageSize(view.getWidth(), view.getHeight());
                ImagePagerActivity.startImagePagerActivity(PersonalAlbumSectionActivity.this, mySection.getImagesList(), mySection.getSectionNO());
            }
        });
        recyclerview_album_view.setAdapter(sectionAdapter);
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
                                    List<String> lists=new ArrayList<>();
                                    for(PersonalPhotoBean personalPhotoBean:photoArray){
                                        lists.add(personalPhotoBean.getImage());
                                    }
                                    if (photoArray != null && photoArray.size() > 0) {
                                        for (int i=0;i<photoArray.size();i++) {
                                            PersonalPhotoBean photoBean=photoArray.get(i);
                                            AlbumSectionEntity sectionEntity=new AlbumSectionEntity(photoBean);
                                            sectionEntity.setSectionNO(i);
                                            sectionEntity.setImagesList(lists);
                                            sectionEntityList.add(sectionEntity);
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
