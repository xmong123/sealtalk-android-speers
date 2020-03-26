package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AbsRecycleAdapter;
import com.caesar.rongcloudspeed.bean.AdminInBean;
import com.caesar.rongcloudspeed.bean.AdminIndustryBean;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.adapter.BrandAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 账号设置
 */
public class AdminProfessionActivity extends TitleBaseActivity implements View.OnClickListener {
    private RecyclerView adminIndustryRecyclerView;
    private BrandAdapter adminIndustryAdapter;
    private List<AdminIndustryBean> adminIndustryArray = new ArrayList<AdminIndustryBean>();
    private Button industryConfirmBtn;
    private String professionIDString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_industry);
        initView();
    }

    private void initView() {
        getTitleBar().setTitle("专业分类");
        professionIDString = getIntent().getStringExtra("professionIDString");
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
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().AdminProfessionBeanDatas(),
                new NetworkCallback<AdminInBean>() {
                    @Override
                    public void onSuccess(AdminInBean adminInBean) {
                        adminIndustryArray = adminInBean.getReferer();
                        AdminIndustryBean baseBean = new AdminIndustryBean(false, "0", "全部专业", "0");
                        adminIndustryArray.add(baseBean);
                        adminIndustryAdapter.setData(adminIndustryArray);
                        adminIndustryRecyclerView.setAdapter(adminIndustryAdapter);
                        if (professionIDString != null) {
                            String idArray[] = professionIDString.split(",");
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
                        Toast.makeText(AdminProfessionActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.industry_confirm:
                SparseBooleanArray mCheckStates = adminIndustryAdapter.getCheckedItemPositions();
                if (mCheckStates.size() > 0) {
                    StringBuffer industryIDBuffer = new StringBuffer();
                    StringBuffer industryNameBuffer = new StringBuffer();
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
                        String professionIDString = industryIDBuffer.toString();
                        String professionNameString = industryNameBuffer.toString();
                        getIntent().putExtra("professionIDString", professionIDString);
                        getIntent().putExtra("professionNameString", professionNameString);
                        setResult(RESULT_OK, getIntent());
                        finish();
                    } else {
                        Toast.makeText(this, "请选择相关专业", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, "请选择相关专业", Toast.LENGTH_LONG).show();
                }
                break;
            default:

                break;

        }
    }


}
