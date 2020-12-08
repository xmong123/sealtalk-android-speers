package com.caesar.rongcloudspeed.easypop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationComplexAdapter;
import com.caesar.rongcloudspeed.bean.CategoryBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyyoona7 on 2017/8/4.
 */

public class ComplexPopup extends BasePopup<ComplexPopup> {
    private static final String TAG = "ComplexPopup";

    private Button mOkBtn;
    private Button mCancelBtn;
    private RecyclerView mRecyclerView;
    private AnimationComplexAdapter mComplexAdapter;
    private Context mContext;
    private List<CategoryBean.ChildrenBean> cateList=new ArrayList<CategoryBean.ChildrenBean>(  );

    private String[] menuString=new String[]{"全部","散户","基地","钢厂"};

    public static ComplexPopup create(Context context) {
        return new ComplexPopup(context);
    }

    protected ComplexPopup(Context context) {
        mContext = context;
        setContext(context);
    }


    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_complex, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusAndOutsideEnable(false)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f);

    }

    @Override
    protected void initViews(View view, ComplexPopup basePopup) {
        mOkBtn = findViewById(R.id.btn_ok);
        mCancelBtn = findViewById(R.id.btn_cancel);
        mRecyclerView = findViewById(R.id.rv_complex);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        for(int i=0;i<menuString.length;i++){
            CategoryBean.ChildrenBean childrenBean=new CategoryBean.ChildrenBean();
            childrenBean.setName(menuString[i]);
            childrenBean.setId(String.valueOf(i));
            cateList.add(childrenBean);
        }
        mComplexAdapter = new AnimationComplexAdapter(cateList);
        mRecyclerView.setAdapter(mComplexAdapter);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mComplexAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShort(cateList.get(position).getName());
            }
        });

    }

    public void setAbc() {

    }

}
