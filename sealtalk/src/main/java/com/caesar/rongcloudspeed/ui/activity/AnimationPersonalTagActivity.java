package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationTagAdapter;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.PersonalTagBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class AnimationPersonalTagActivity extends MultiStatusActivity implements OnRefreshListener {
    private static String TAG = "AnimationPersonalTagActivity";
    private static final int REQUEST_CODE_SELECT_INDUSTRY = 1001;
    private static final int REQUEST_CODE_SELECT_PROFESSION = 1002;
    private static final int REQUEST_CODE_SELECT_SOFT = 1003;
    @BindView(R.id.personal_tag_recyclerview)
    RecyclerView personalTagRecyclerview;
    @BindView(R.id.personal_tag_confirm)
    Button personalTagConfirm;
    private String titleString = "我的标签";
    private AnimationTagAdapter animationTagAdapter;
    private List<HashMap<String, Object>> tagList = new ArrayList<HashMap<String, Object>>();
    private String uidString;
    private String industryIDString = "1";
    private String professionIDString = "1";
    private String softIDString = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        initTitleBarView(titlebar, titleString);
        for (int i = 0; i < 3; i++) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("id", i);
            switch (i) {
                case 0:
                    hashMap.put("title", "关注行业");
                    hashMap.put("tag", "行业");
                    break;
                case 1:
                    hashMap.put("title", "关注专业");
                    hashMap.put("tag", "专业");
                    break;
                case 2:
                    hashMap.put("title", "关注软件");
                    hashMap.put("tag", "软件");
                    break;
            }
            tagList.add(hashMap);
        }
        animationTagAdapter = new AnimationTagAdapter(tagList);
        animationTagAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            switch (position) {
                case 0:
                    intent.setClass(this, AdminIndustryActivity.class);
                    intent.putExtra("industryIDString", industryIDString);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_INDUSTRY);
                    break;
                case 1:
                    intent.setClass(this, AdminProfessionActivity.class);
                    intent.putExtra("professionIDString", professionIDString);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_PROFESSION);
                    break;
                case 2:
                    intent.setClass(this, AdminSoftActivity.class);
                    intent.putExtra("softIDString", softIDString);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_SOFT);
                    break;
            }
        });
        animationTagAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemChildClick: ");
            Intent intent = new Intent();
            switch (position) {
                case 0:
                    intent.setClass(this, AdminIndustryActivity.class);
                    intent.putExtra("industryIDString", industryIDString);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_INDUSTRY);
                    break;
                case 1:
                    intent.setClass(this, AdminProfessionActivity.class);
                    intent.putExtra("professionIDString", professionIDString);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_PROFESSION);
                    break;
                case 2:
                    intent.setClass(this, AdminSoftActivity.class);
                    intent.putExtra("softIDString", softIDString);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_SOFT);
                    break;
            }
        });
        personalTagRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        personalTagRecyclerview.setHasFixedSize(true);
        personalTagRecyclerview.setAdapter(animationTagAdapter);
        personalTagConfirm.setOnClickListener(view -> {
            personalTagHandler.sendEmptyMessage(0);
        });
        loadPersonalTagData();
    }

    @SuppressLint("HandlerLeak")
    Handler personalTagHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    prompDialog.showLoading("请等待");
                    RetrofitManager.create().setPersonalTagData(uidString,industryIDString,professionIDString,softIDString)
                            .observeOn(AndroidSchedulers.mainThread())
                            .compose(AnimationPersonalTagActivity.this.bindToLifecycle())
                            .subscribe(new CommonObserver<CommonResonseBean>(multipleStatusView) {
                                @Override
                                public void onSuccess(CommonResonseBean personalTagBean) {
                                    prompDialog.dismiss();
                                    Toast.makeText(AnimationPersonalTagActivity.this, "您已成功修改个人标签", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    prompDialog.dismiss();
                                    Toast.makeText(AnimationPersonalTagActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };

    private void loadPersonalTagData() {
        RetrofitManager.create().getPersonalTagData(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(new CommonObserver<PersonalTagBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(PersonalTagBean personalTagBean) {
                        if (personalTagBean.getCode() == CODE_SUCC) {
                            List<PersonalTagBean.PersonTagItem> indusList = personalTagBean.getReferer().getUser_industry();
                            List<PersonalTagBean.PersonTagItem> proList = personalTagBean.getReferer().getUser_profession();
                            List<PersonalTagBean.PersonTagItem> softList = personalTagBean.getReferer().getUser_soft();
                            if (indusList.size() > 0) {
                                HashMap<String, Object> indusMap = tagList.get(0);
                                StringBuffer stringBuffer = new StringBuffer();
                                StringBuffer industryIDBuffer = new StringBuffer();
                                for (PersonalTagBean.PersonTagItem item : indusList) {
                                    stringBuffer.append(item.getName() + ",");
                                    industryIDBuffer.append(item.getId() + ",");
                                }
                                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                                industryIDBuffer.deleteCharAt(industryIDBuffer.length() - 1);
                                industryIDString = industryIDBuffer.toString();
                                String industryString = stringBuffer.toString();
                                indusMap.put("tag", industryString);
                                tagList.set(0, indusMap);
                                animationTagAdapter.setData(0, indusMap);
                            }
                            if (proList.size() > 0) {
                                HashMap<String, Object> proMap = tagList.get(1);
                                StringBuffer stringBuffer = new StringBuffer();
                                StringBuffer professionIDBuffer = new StringBuffer();
                                for (PersonalTagBean.PersonTagItem item : proList) {
                                    stringBuffer.append(item.getName() + ",");
                                    professionIDBuffer.append(item.getId() + ",");
                                }
                                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                                professionIDBuffer.deleteCharAt(professionIDBuffer.length() - 1);
                                professionIDString = professionIDBuffer.toString();
                                String proString = stringBuffer.toString();
                                proMap.put("tag", proString);
                                tagList.set(1, proMap);
                                animationTagAdapter.setData(1, proMap);
                            }
                            if (softList.size() > 0) {
                                HashMap<String, Object> softMap = tagList.get(2);
                                StringBuffer stringBuffer = new StringBuffer();
                                StringBuffer softIDBuffer = new StringBuffer();
                                for (PersonalTagBean.PersonTagItem item : softList) {
                                    stringBuffer.append(item.getName() + ",");
                                    softIDBuffer.append(item.getId() + ",");
                                }
                                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                                softIDBuffer.deleteCharAt(softIDBuffer.length() - 1);
                                softIDString = softIDBuffer.toString();
                                String softString = stringBuffer.toString();
                                softMap.put("tag", softString);
                                tagList.set(2, softMap);
                                animationTagAdapter.setData(2, softMap);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(AnimationPersonalTagActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_personal_taglayout;
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadPersonalTagData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_SELECT_INDUSTRY) {
                industryIDString = data.getStringExtra("industryIDString");
                HashMap<String, Object> indusMap = tagList.get(0);
                indusMap.put("tag", data.getStringExtra("industryNameString"));
                tagList.set(0, indusMap);
                animationTagAdapter.setData(0, indusMap);
            } else if (requestCode == REQUEST_CODE_SELECT_PROFESSION) {
                professionIDString = data.getStringExtra("professionIDString");
                HashMap<String, Object> proMap = tagList.get(1);
                proMap.put("tag", data.getStringExtra("professionNameString"));
                tagList.set(1, proMap);
                animationTagAdapter.setData(1, proMap);
            } else if (requestCode == REQUEST_CODE_SELECT_SOFT) {
                softIDString = data.getStringExtra("softIDString");
                HashMap<String, Object> softMap = tagList.get(2);
                softMap.put("tag", data.getStringExtra("softNameString"));
                tagList.set(2, softMap);
                animationTagAdapter.setData(2, softMap);
            }

        }
    }
}
