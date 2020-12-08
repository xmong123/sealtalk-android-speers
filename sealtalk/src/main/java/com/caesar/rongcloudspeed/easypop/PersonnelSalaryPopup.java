package com.caesar.rongcloudspeed.easypop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationComplexAdapter;
import com.caesar.rongcloudspeed.bean.CategoryBean;
import com.caesar.rongcloudspeed.implement.SelectSecondItemListener;
import com.caesar.rongcloudspeed.implement.SelectTabItemListener;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyyoona7 on 2017/8/7.
 */

public class PersonnelSalaryPopup extends BasePopup<PersonnelSalaryPopup> {

    private RecyclerView mRecyclerView;
    private AnimationComplexAdapter mComplexAdapter;
    private Context mContext;
    private List<CategoryBean.ChildrenBean> cateList = new ArrayList<CategoryBean.ChildrenBean>();

    private String[] menuString = new String[]{"2000以下", "2000～3000", "3000～4000", "4000～5000", "5000～8000", "8000以上", "面议"};

    private static SelectSecondItemListener tabSecondListener;

    public static PersonnelSalaryPopup create(SelectSecondItemListener stabItemListener) {
        tabSecondListener = stabItemListener;
        return new PersonnelSalaryPopup();
    }


    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_gift);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusAndOutsideEnable(true);
    }

    public void setNewData(View view, String[] newString) {
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
    protected void initViews(View view, PersonnelSalaryPopup basePopup) {
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
                if (tabSecondListener != null) {
                    tabSecondListener.onSelectSecondItemListener(position);
                }
                self().dismiss();
            }
        });
    }

}
