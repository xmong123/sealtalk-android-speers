package com.caesar.rongcloudspeed.easypop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationCityAdapter;
import com.caesar.rongcloudspeed.adapter.AnimationProAdapter;
import com.caesar.rongcloudspeed.implement.SelectTabCityListener;
import com.caesar.rongcloudspeed.implement.SelectTabSecondCityListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.ProCityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyyoona7 on 2017/8/7.
 */

public class CityPopup extends BasePopup<CityPopup> {

    private RecyclerView mProRecyclerView;
    private RecyclerView mCityRecyclerView;
    private AnimationProAdapter proAdapter;
    private AnimationCityAdapter mComplexAdapter;
    private static Context mContext;
    private List<ProCityBean> proCityBeanList = new ArrayList<>();
    private List<City> cityArrayList = new ArrayList<>();
    private int sposition=0;
    private int section=0;

    private static SelectTabCityListener tabCityListener;

    private static SelectTabSecondCityListener tabSecondCityListener;

    public static CityPopup create(Context context,SelectTabCityListener stabCityListener) {
        mContext=context;
        tabCityListener = stabCityListener;
        return new CityPopup();
    }

    public static CityPopup create(Context context, SelectTabSecondCityListener stabSecondCityListener) {
        mContext=context;
        tabSecondCityListener = stabSecondCityListener;
        return new CityPopup();
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_gift_city);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusAndOutsideEnable(true);
    }

    public void setNewData(View view, List<ProCityBean> proCityBeans, int position, int secition) {
        proCityBeanList=proCityBeans;
        sposition=position;
        section=secition;
        cityArrayList=proCityBeanList.get(sposition).getCityList();
        proAdapter.setNewData(proCityBeanList);
        mComplexAdapter.setNewData(cityArrayList);
        self().showAtAnchorView(view, YGravity.BELOW, XGravity.LEFT);
    }

    @Override
    protected void initViews(View view, CityPopup basePopup) {
        mProRecyclerView = findViewById(R.id.rv_gift_pro);
        mCityRecyclerView = findViewById(R.id.rv_gift_city);
        mProRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mCityRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        proAdapter = new AnimationProAdapter(proCityBeanList);
        mComplexAdapter = new AnimationCityAdapter(cityArrayList);
        mProRecyclerView.setAdapter(proAdapter);
        mCityRecyclerView.setAdapter(mComplexAdapter);
        proAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                cityArrayList=proCityBeanList.get(position).getCityList();
                mComplexAdapter.setNewData(cityArrayList);
                sposition=position;
                for (int i = 0; i < proCityBeanList.size(); i++) {
                    if (i == position) {
                        proCityBeanList.get( i ).setFlag( true );
                    } else {
                        proCityBeanList.get( i ).setFlag( false );
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mComplexAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                City city = cityArrayList.get(position);
                if (tabCityListener != null) {
//                    ToastUtils.showShort("搜索地区位置："+sposition);
                    if(section==0){
                        tabCityListener.onSelectTabCityListener(city.getCode(),sposition);
                    }else{
                        tabSecondCityListener.onSelectTabCityListener(city.getCode(),sposition);
                    }
                }
                self().dismiss();
            }
        });
    }

}
