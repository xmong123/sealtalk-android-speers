package com.caesar.rongcloudspeed.easypop;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationComplexAdapter;
import com.caesar.rongcloudspeed.bean.CategoryBean;
import com.caesar.rongcloudspeed.implement.SelectSecondItemListener;
import com.caesar.rongcloudspeed.implement.SelectTabItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyyoona7 on 2017/8/7.
 */

public class GiftPopup extends BasePopup<GiftPopup> {

    private RecyclerView mRecyclerView;
    private AnimationComplexAdapter mComplexAdapter;
    private Context mContext;
    private List<CategoryBean.ChildrenBean> cateList = new ArrayList<CategoryBean.ChildrenBean>();

    private String[] menuString = new String[]{"全部", "散户", "基地", "钢厂"};

    private static SelectTabItemListener tabItemListener;

    private static SelectSecondItemListener tabSecondListener;

    private static String gifType = "1";

    public static GiftPopup create(SelectTabItemListener stabItemListener) {
        tabItemListener = stabItemListener;
        return new GiftPopup();
    }

    public static GiftPopup create(SelectSecondItemListener stabItemListener) {
        tabSecondListener = stabItemListener;
        return new GiftPopup();
    }

    public static GiftPopup create(SelectTabItemListener stabItemListener, String type) {
        gifType = type;
        tabItemListener = stabItemListener;
        return new GiftPopup();
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_gift);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusAndOutsideEnable(true);
    }

    public void setNewData(View view, String[] newString) {
        gifType = "4";
        cateList.clear();
        for (int i = 0; i < newString.length; i++) {
            CategoryBean.ChildrenBean childrenBean = new CategoryBean.ChildrenBean();
            childrenBean.setName(newString[i]);
            childrenBean.setId(String.valueOf(i));
            cateList.add(childrenBean);
        }
        mComplexAdapter.setNewData(cateList);
        self().showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT);
    }

    public void setNewData(View view, String[] newString, String type) {
        gifType = type;
        cateList.clear();
        for (int i = 0; i < newString.length; i++) {
            CategoryBean.ChildrenBean childrenBean = new CategoryBean.ChildrenBean();
            childrenBean.setName(newString[i]);
            childrenBean.setId(String.valueOf(i));
            cateList.add(childrenBean);
        }
        mComplexAdapter.setNewData(cateList);
        self().showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initViews(View view, GiftPopup basePopup) {
        mRecyclerView = findViewById(R.id.rv_gift);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        for (int i = 0; i < menuString.length; i++) {
            CategoryBean.ChildrenBean childrenBean = new CategoryBean.ChildrenBean();
            childrenBean.setName(menuString[i]);
            childrenBean.setId(String.valueOf(i));
            cateList.add(childrenBean);
        }
        mComplexAdapter = new AnimationComplexAdapter(cateList);
        mRecyclerView.setAdapter(mComplexAdapter);
        mComplexAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryBean.ChildrenBean childrenBean = cateList.get(position);
                if (gifType.equals("3")) {
                    if (tabItemListener != null) {
                        tabItemListener.onSelectTabItemListener(position, gifType);
                    }
                } else {
                    if (tabSecondListener != null) {
                        tabSecondListener.onSelectSecondItemListener(position);
                    }
                }
                self().dismiss();
            }
        });
    }

}
