package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.bean.RecruitJobBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.AccountValidatorUtil;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.wx.WXManager;
import com.zaaach.citypicker.db.DBManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class PersonnelDetailActivity extends MultiStatusActivity {

    @BindView(R.id.user_recruit_supert1)
    SuperTextView user_recruit_supert1;
    @BindView(R.id.user_recruit_supert2)
    SuperTextView user_recruit_supert2;
    @BindView(R.id.user_recruit_supert3)
    SuperTextView user_recruit_supert3;
    @BindView(R.id.user_recruit_supert4)
    SuperTextView user_recruit_supert4;
    @BindView(R.id.user_recruit_supert5)
    SuperTextView user_recruit_supert5;
    @BindView(R.id.user_recruit_supert6)
    SuperTextView user_recruit_supert6;
    @BindView(R.id.user_recruit_supert7)
    SuperTextView user_recruit_supert7;
    @BindView(R.id.user_recruit_supert8)
    SuperTextView user_recruit_supert8;
    @BindView(R.id.user_recruit_supert9)
    SuperTextView user_recruit_supert9;
    private String titleString = "简历详情";
    private String uidString,emailString;
    private DBManager dbManager;
    private RecruitItemBean recruitItemBean;
    private String[] salaryItems = new String[]{"2000元/月以下", "2000～3000元/月", "3000～4000元/月", "4000～5000元/月", "5000～8000元/月", "8000元/月以上", "面议"};
    private String[] gradeItems = new String[]{"高职高中及以下", "大专院校", "全日制本科", "硕士研究生", "MBA", "博士及以上", "保密"};
    private String[] jobItems = new String[]{"1年及以下工作经验", "2年工作经验", "3年工作经验", "4年工作经验", "5年工作经验", "6年工作经验", "7年工作经验", "8年及以上工作经验"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        dbManager = new DBManager(this);
        uidString = UserInfoUtils.getAppUserId(this);
        emailString = UserInfoUtils.getUserEmail(this);
        recruitItemBean=getIntent().getParcelableExtra("recruitItemBean");
        initTitleBarView(titlebar, titleString);
        int salaryInt=Integer.valueOf(recruitItemBean.getRecruit_salary());
        int gradeInt=Integer.valueOf(recruitItemBean.getRecruit_grade());
        int jobInt=Integer.valueOf(recruitItemBean.getRecruit_workingyears());
        String cityString = dbManager.getAreaNames(recruitItemBean.getRecruit_native());
        user_recruit_supert1.setLeftString(recruitItemBean.getRecruit_name());
        user_recruit_supert1.setLeftBottomString(jobItems[jobInt]);
        user_recruit_supert1.setRightString(cityString);
        ImageLoaderUtils.displayUserPortraitImage(recruitItemBean.getRecruit_avatar(), user_recruit_supert1.getLeftIconIV());
        user_recruit_supert2.setLeftString(recruitItemBean.getRecruit_mobile());
        user_recruit_supert3.setLeftString(recruitItemBean.getRecruit_email());
        user_recruit_supert5.setCenterString(recruitItemBean.getRecruit_job());
        user_recruit_supert6.setCenterString(cityString);
        user_recruit_supert7.setCenterString(salaryItems[salaryInt]);
        user_recruit_supert8.setCenterString(gradeItems[gradeInt]);
        user_recruit_supert9.setCenterString(jobItems[jobInt]);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_user_recruit_layout;
    }

    @OnClick({R.id.user_recruit_card1, R.id.user_recruit_card2, R.id.user_recruit_card3, R.id.resume_post_connect, R.id.resume_post_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_recruit_card1:

                break;
            case R.id.user_recruit_card2:

                break;
            case R.id.user_recruit_card3:

                break;
            case R.id.resume_post_connect:
                RongIM.getInstance().startPrivateChat(this, recruitItemBean.getRongid(), recruitItemBean.getRecruit_name());
                break;
            case R.id.resume_post_save:
                if(TextUtils.isEmpty(emailString)){
                    ToastUtils.showToast("请先完善个人邮箱地址信息");
                    startActivity(new Intent(this, MyAccountActivity.class));
                }else{
                    if (AccountValidatorUtil.isEmail(emailString)) {
                        sendEmailHandler.sendEmptyMessage(0);
                    }else{
                        ToastUtils.showToast("邮箱格式错误");
                    }
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler sendEmailHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().testPhpWordMail(uidString, recruitItemBean.getRecruit_id()),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    if(baseData.getCode()==CODE_SUCC){
                                        ToastUtils.showToast("发送简历成功");
                                    }else{
                                        ToastUtils.showToast("发送简历失败."+baseData.getInfo());
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    ToastUtils.showToast("发送简历失败");
                                }
                            });
                    break;
            }
        }
    };


}
