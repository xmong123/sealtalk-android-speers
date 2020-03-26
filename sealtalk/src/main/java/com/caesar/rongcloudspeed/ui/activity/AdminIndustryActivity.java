package com.caesar.rongcloudspeed.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AbsRecycleAdapter;
import com.caesar.rongcloudspeed.adapter.AdminIndustryAdapter;
import com.caesar.rongcloudspeed.adapter.AnimationProAdapter;
import com.caesar.rongcloudspeed.bean.AdminInBean;
import com.caesar.rongcloudspeed.bean.AdminIndustryBean;
import com.caesar.rongcloudspeed.bean.LessonCateBean;
import com.caesar.rongcloudspeed.bean.LessonCategoryBean;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.adapter.BrandAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 账号设置
 */
public class AdminIndustryActivity extends TitleBaseActivity implements View.OnClickListener {
    private RecyclerView adminIndustryRecyclerView;
    private BrandAdapter adminIndustryAdapter;
    private List<AdminIndustryBean> adminIndustryArray = new ArrayList<AdminIndustryBean>();
    private Button industryConfirmBtn;
    private String industryIDString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_industry);
        initView();
    }

    private void initView() {
        getTitleBar().setTitle("行业分类");
        industryIDString = getIntent().getStringExtra("industryIDString");
        adminIndustryRecyclerView = findViewById(R.id.adminindustry_recyclerView);
        industryConfirmBtn = findViewById(R.id.industry_confirm);
        adminIndustryAdapter = new BrandAdapter();
        adminIndustryAdapter.setChoiceMode(AbsRecycleAdapter.CHOICE_MODE_MULTIPLE);
        adminIndustryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminIndustryRecyclerView.setHasFixedSize(true);
        industryConfirmBtn.setOnClickListener(this);
        loadAdminIndustryData();
    }

    private void loadAdminIndustryData() {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().AdminIndustryBeanDatas(),
                new NetworkCallback<AdminInBean>() {
                    @Override
                    public void onSuccess(AdminInBean adminInBean) {
                        adminIndustryArray = adminInBean.getReferer();
                        AdminIndustryBean baseBean = new AdminIndustryBean(false, "0", "全部行业", "0");
                        adminIndustryArray.add(baseBean);
                        adminIndustryAdapter.setData(adminIndustryArray);
                        adminIndustryRecyclerView.setAdapter(adminIndustryAdapter);
                        if (industryIDString != null) {
                            String idArray[] = industryIDString.split(",");
                            for (int i = 0; i < adminIndustryArray.size(); i++) {
                                AdminIndustryBean bean = adminIndustryArray.get(i);
                                String idString = bean.getId();
                                for (String str : idArray) {
                                    if (idString.equals(str)) {
                                        bean.setFlag(true);
                                        adminIndustryAdapter.setItemChecked(i, true);
                                    }
                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(AdminIndustryActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.industry_confirm:
                SparseBooleanArray mCheckStates = adminIndustryAdapter.getCheckedItemPositions();
                if (mCheckStates.size() > 0) {
                    StringBuilder industryIDBuffer = new StringBuilder();
                    StringBuilder industryNameBuffer = new StringBuilder();
                    for (int i = 0; i < mCheckStates.size(); i++) {
                        if (mCheckStates.valueAt(i)) {
                            int key = mCheckStates.keyAt(i);
                            String industryID = adminIndustryArray.get(key).getId();
                            String industryName = adminIndustryArray.get(key).getName();
                            industryIDBuffer.append(industryID + ",");
                            industryNameBuffer.append(industryName + ",");
                        }
                    }
                    if (industryIDBuffer.length() > 0) {
                        industryIDBuffer.deleteCharAt(industryIDBuffer.length() - 1);
                        industryNameBuffer.deleteCharAt(industryNameBuffer.length() - 1);
                        String industryIDString = industryIDBuffer.toString();
                        String industryNameString = industryNameBuffer.toString();
                        getIntent().putExtra("industryIDString", industryIDString);
                        getIntent().putExtra("industryNameString", industryNameString);
                        setResult(RESULT_OK, getIntent());
                        finish();
                    } else {
                        Toast.makeText(this, "请选择相关行业", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, "请选择相关行业", Toast.LENGTH_LONG).show();
                }
                break;
            default:

                break;

        }
    }


}
