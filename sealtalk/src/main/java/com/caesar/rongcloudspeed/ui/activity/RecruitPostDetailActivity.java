package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationAdapter1;
import com.caesar.rongcloudspeed.bean.HomeDataBaseBean;
import com.caesar.rongcloudspeed.bean.HomeDataUserBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.manager.RetrofitManageres;
import com.caesar.rongcloudspeed.network.Api;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.rong.imkit.RongIM;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class RecruitPostDetailActivity extends MultiStatusActivity {

    @BindView(R.id.recruit_company_name)
    TextView recruit_company_name;
    @BindView(R.id.recruit_post_title)
    TextView recruit_post_title;
    @BindView(R.id.recruit_post_salary)
    TextView recruit_post_salary;
    @BindView(R.id.recruit_post_grade)
    TextView recruit_post_grade;
    @BindView(R.id.recruit_post_date)
    TextView recruit_post_date;
    @BindView(R.id.recruit_post_hits)
    TextView recruit_post_hits;
    @BindView(R.id.recruit_post_area)
    TextView recruit_post_area;
    @BindView(R.id.recruit_post_expert)
    TextView recruit_post_expert;
    @BindView(R.id.recruit_post_apply)
    Button recruit_post_apply;
    private String titleString = "招聘详情";
    private String uidString;
    private RecruitJobBean recruitJobBean;
    private String[] salaryItems = new String[]{"2000元/月以下", "2000～3000元/月", "3000～4000元/月", "4000～5000元/月", "5000～8000元/月", "8000元/月以上", "面议"};
    private String[] gradeItems = new String[]{"高职高中及以下", "大专院校", "全日制本科", "硕士研究生", "MBA", "博士及以上", "不限"};
    private String[] jobItems = new String[]{"1年及以下工作经验", "2年工作经验", "3年工作经验", "4年工作经验", "5年工作经验", "6年工作经验", "7年工作经验", "8年及以上工作经验"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        recruitJobBean = getIntent().getParcelableExtra("recruitJobBean");
        initTitleBarView(titlebar, titleString);
        int salaryInt = Integer.valueOf(recruitJobBean.getPost_salary());
        int gradeInt = Integer.valueOf(recruitJobBean.getPost_grade());
        int levelInt = Integer.valueOf(recruitJobBean.getPost_level());
        String gradeString = gradeItems[gradeInt];
        String areaNameString = recruitJobBean.getPost_area_name();
        String jobString = jobItems[levelInt];
        recruit_company_name.setText(recruitJobBean.getCompany_name());
        recruit_post_title.setText(recruitJobBean.getPost_title());
        recruit_post_salary.setText(salaryItems[salaryInt]);
        recruit_post_grade.setText(gradeString + "｜" + areaNameString + "｜" + jobString + "｜招聘2人");
        recruit_post_date.setText("发布:" + recruitJobBean.getPost_date());
        recruit_post_hits.setText("浏览:" + recruitJobBean.getPost_hits() + "人");
        recruit_post_area.setText("工作地址:" + areaNameString);
        recruit_post_expert.setText(recruitJobBean.getPost_excerpt());
    }

    @SuppressLint("HandlerLeak")
    Handler recruitPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().applyRecruitPost(uidString,recruitJobBean.getPost_id()),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    if (baseData.getCode() == Constant.CODE_SUCC) {
                                        Toast.makeText(RecruitPostDetailActivity.this, "恭喜您成功申请职位", Toast.LENGTH_LONG).show();
                                    }else{
                                        String msg=baseData.getInfo();
                                        Toast.makeText(RecruitPostDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(RecruitPostDetailActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });
                    break;
            }
        }
    };

    @Override
    public int getContentView() {
        return R.layout.activity_recruit_job_layout;
    }

    @OnClick({R.id.recruit_post_connect, R.id.recruit_post_server, R.id.recruit_post_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recruit_post_connect:
                RongIM.getInstance().startPrivateChat(this, "gJtmUXgia", "同行快线客服");
                break;
            case R.id.recruit_post_server:
                RongIM.getInstance().startPrivateChat(this, recruitJobBean.getRongid(), recruitJobBean.getCompany_name());
                break;
            case R.id.recruit_post_apply:
                recruitPostHandler.sendEmptyMessage(0);
                break;
            default:
                break;
        }
    }


}
