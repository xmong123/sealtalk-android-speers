package com.caesar.rongcloudspeed.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.allen.library.SuperTextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * 新消息提醒
 */
public class AccountRecruitEduActivity extends TitleBaseActivity {
    @BindView(R.id.recruit_edu_supert)
    SuperTextView recruit_edu_supert;
    @BindView(R.id.recruit_edu_supert1)
    SuperTextView recruit_edu_supert1;
    @BindView(R.id.recruit_edu_supert2)
    SuperTextView recruit_edu_supert2;
    @BindView(R.id.recruit_edu_excerpt)
    EditText recruit_edu_excerpt;
    @BindView(R.id.recruit_self_excerpt)
    EditText recruit_self_excerpt;
    private String uidString;
    private String recruit_grade = "0";
    private RecruitItemBean recruitItemBean;
    private String[] gradeItems = new String[]{"高职高中及以下", "大专院校", "全日制本科", "硕士研究生", "MBA", "博士及以上", "保密"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recruit_edu_message);
        getTitleBar().setTitle("基本信息");
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        //学历
        recruit_edu_supert.setOnClickListener(view -> {
            showSelectDialog(0);
        });
        //专业
        recruit_edu_supert1.setOnClickListener(view -> {
            showEditDialog(1);
        });
        recruit_edu_supert2.setOnClickListener(view -> {
            showEditDialog(2);
        });
        recruitItemBean = getIntent().getParcelableExtra("recruitItemBean");
        if (recruitItemBean != null) {
            recruit_grade = recruitItemBean.getRecruit_grade();
            recruit_edu_supert.setRightString(gradeItems[Integer.parseInt(recruit_grade)]);
            recruit_edu_supert1.setRightString(recruitItemBean.getRecruit_major());
            recruit_edu_supert2.setRightString(recruitItemBean.getRecruit_school());
            recruit_edu_excerpt.setText(recruitItemBean.getRecruit_education());
            recruit_self_excerpt.setText(recruitItemBean.getRecruit_expert1());
        }
    }

    @OnClick({R.id.post_edu_btn})
    public void onViewClicked(View view) {
        onPostUserrecruitMessage();
    }

    public void onPostUserrecruitMessage() {
        String recruit_major = recruit_edu_supert1.getRightString();
        String recruit_school = recruit_edu_supert2.getRightString();
        String recruit_education = recruit_edu_excerpt.getText().toString();
        String recruit_expert1 = recruit_self_excerpt.getText().toString();
        if (TextUtils.isEmpty(recruit_grade)) {
            showToast("学历不能为空");
            return;
        }
        if (TextUtils.isEmpty(recruit_major)) {
            showToast("专业不能为空");
            return;
        }
        if (TextUtils.isEmpty(recruit_school)) {
            showToast("毕业院校不能为空");
            return;
        }
        if (TextUtils.isEmpty(recruit_education)) {
            recruit_education = "bim学校-环球网校 在BIM环球网校推荐榜单前十名的专业学员";
        }
        if (TextUtils.isEmpty(recruit_expert1)) {
            recruit_expert1 = "　　本人忠实诚信从事BIM项目经理2年,精通BIM项目经理各项内容，并且讲原则，说到做到，决不推卸责任；有自制力，做事情始终坚持有始有终，从不半途而废；肯学习,有问题不逃避,愿意虚心向他人学习；自信但不自负,不以自我为中心；愿意以谦虚态度赞扬接纳优越者,权威者；会用100%的热情和精力投入到工作中；平易近人。为人诚恳,性格开朗,积极进取,适应力强、勤奋好学、脚踏实地，有较强的团队精神,工作积极进取,态度认真。";
        }
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateUserRecruitEducare(uidString, recruit_grade, recruit_major, recruit_school, recruit_education, recruit_expert1),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData lessonBaseBean) {
                        if (lessonBaseBean.getCode() == CODE_SUCC) {
                            Toast.makeText(AccountRecruitEduActivity.this, "您已成功提交同行简历", Toast.LENGTH_LONG).show();
                            setResult(200);
                            finish();
                        } else {
                            Toast.makeText(AccountRecruitEduActivity.this, lessonBaseBean.getInfo(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(AccountRecruitEduActivity.this, "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void showSelectDialog(int position) {
        String title = "请选择简历信息";
        int checkedItem = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.recruit_message);
        builder.setTitle(title);
        String[] finalItems = gradeItems;
        if (recruitItemBean != null) {
            checkedItem = Integer.valueOf(recruit_grade);

        }
        builder.setSingleChoiceItems(finalItems, checkedItem, (dialog, which) -> {
            recruit_grade = String.valueOf(which);
            recruit_edu_supert.setRightString(gradeItems[which]);
            dialog.dismiss();
        });
        builder.show();
    }

    public void showEditDialog(int position) {
        String title = "请填写求职职位";
        final EditText editText = new EditText(this);
        if (recruitItemBean != null) {
            if (position == 1) {
                editText.setText(recruitItemBean.getRecruit_major());
            } else {
                editText.setText(recruitItemBean.getRecruit_school());
            }

        }
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(R.drawable.recruit_message)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        if(position==1){
                            recruit_edu_supert1.setRightString(editText.getText().toString());
                        }else{
                            recruit_edu_supert2.setRightString(editText.getText().toString());
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

}
