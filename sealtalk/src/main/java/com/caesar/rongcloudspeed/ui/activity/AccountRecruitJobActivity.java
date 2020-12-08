package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.allen.library.SuperTextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * 新消息提醒
 */
public class AccountRecruitJobActivity extends TitleBaseActivity {
    @BindView(R.id.recruit_job_supert)
    SuperTextView recruit_job_supert;
    @BindView(R.id.recruit_job_supert1)
    SuperTextView recruit_job_supert1;
    @BindView(R.id.recruit_job_supert2)
    SuperTextView recruit_job_supert2;
    @BindView(R.id.recruit_job_supert3)
    SuperTextView recruit_job_supert3;
    @BindView(R.id.recruit_job_excerpt)
    EditText recruit_job_excerpt;
    private String uidString;
    private int recruitState;
    private DBManager dbManager;
    private OptionsPickerView cityCustomOptions;
    private List<City> options1Items = new ArrayList<>();
    private List<ArrayList<City>> options2Items = new ArrayList<>();
    private String recruit_place="100000";
    private String recruit_salary="6";
    private RecruitItemBean recruitItemBean;
    private String recruit_work="2";
    private String[] salaryItems = new String[]{"2000元/月以下", "2000～3000元/月", "3000～4000元/月", "4000～5000元/月", "5000～8000元/月", "8000元/月以上", "面议"};
    private String[] jobItems = new String[]{"1年及以下工作经验", "2年工作经验", "3年工作经验", "4年工作经验", "5年工作经验", "6年工作经验", "7年工作经验", "8年及以上工作经验"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recruit_job_message);
        getTitleBar().setTitle("求职意向");
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        recruitState = getIntent().getIntExtra("state", 0);
        //求职职位
        recruit_job_supert.setOnClickListener(view -> {
            showEditDialog(0);
        });
        //求职地点
        recruit_job_supert1.setOnClickListener(view -> {
            cityCustomOptions.show();
        });
        //期望薪资
        recruit_job_supert2.setOnClickListener(view -> {
            showSelectDialog(2);
        });
        recruit_job_supert3.setOnClickListener(view -> {
            showSelectDialog(3);
        });
        getOptionData();
        recruitItemBean = getIntent().getParcelableExtra("recruitItemBean");
        if (recruitItemBean != null) {
            recruit_salary=recruitItemBean.getRecruit_salary();
            recruit_work=recruitItemBean.getRecruit_workingyears();
            int salaryInt=Integer.valueOf(recruit_salary);
            int workInt = Integer.valueOf(recruit_work);
            recruit_place = recruitItemBean.getRecruit_place();
            String placeString = dbManager.getAreaNames(recruit_place);
            recruit_job_supert.setRightString(recruitItemBean.getRecruit_job());
            recruit_job_supert1.setRightString(placeString);
            recruit_job_supert2.setRightString(salaryItems[salaryInt]);
            recruit_job_supert3.setRightString(jobItems[workInt]);
            recruit_job_excerpt.setText(recruitItemBean.getRecruit_experience());
        }
    }

    @OnClick({R.id.post_job_btn})
    public void onViewClicked(View view) {
        onPostUserrecruitMessage();
    }

    public void onPostUserrecruitMessage() {
        String recruit_job = recruit_job_supert.getRightString();
        String recruit_experience = recruit_job_excerpt.getText().toString();
        if (TextUtils.isEmpty(recruit_job)) {
            showToast("求职职位不能为空");
            return;
        }
        if (TextUtils.isEmpty(recruit_experience)) {
            recruit_experience="2年以上工作经验 工作能力突出 能够应对各种突发挑战";
        }
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateUserRecruitJob(uidString, recruit_job, recruit_place, recruit_salary,recruit_work,recruit_experience),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData lessonBaseBean) {
                        if (lessonBaseBean.getCode() == CODE_SUCC) {
                            Toast.makeText(AccountRecruitJobActivity.this, "您已成功提交同行简历", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(AccountRecruitJobActivity.this, lessonBaseBean.getInfo(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(AccountRecruitJobActivity.this, "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void showEditDialog(int position) {
        String title = "请填写求职职位";
        final EditText editText = new EditText(this);
        if (recruitItemBean != null) {
            editText.setText(recruitItemBean.getRecruit_job());
        }
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(R.drawable.recruit_message)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        recruit_job_supert.setRightString(editText.getText().toString());
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void showSelectDialog(int position) {
        String title = "请选择简历信息";
        int checkedItem = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.recruit_message);
        builder.setTitle(title);
        String[] finalItems = salaryItems;
        if (position == 1) {
            finalItems = jobItems;
        }
        if (recruitItemBean != null) {
            if(position==0){
                checkedItem=Integer.valueOf(recruit_salary);
            }else{
                checkedItem=Integer.valueOf(recruit_work);
            }

        }
        builder.setSingleChoiceItems(finalItems, checkedItem, (dialog, which) -> {
            if (position == 0) {
                recruit_salary = String.valueOf(which);
                recruit_job_supert2.setRightString(salaryItems[which]);
            } else {
                recruit_work = String.valueOf(which);
                recruit_job_supert3.setRightString(jobItems[which]);
            }
            dialog.dismiss();
        });
        builder.show();
    }

    private void getOptionData() {
        dbManager = new DBManager(this);
        options1Items = dbManager.getAllProvince();
        options2Items = dbManager.getAllProvinceCities();
        cityCustomOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String provinceStr = options1Items.get(options1).getName();
                // 设置在你组件上的 选取后得到的内容
                String cityStr = options2Items.get(options1).get(option2).getName();
                recruit_place = options2Items.get(options1).get(option2).getCode();
                recruit_job_supert1.setRightString(provinceStr + "-" + cityStr);
            }
        })
                //* 使用系统自带的Color文件 也可用自己的值 但是这里的值要注意 颜色值=（65536*Red)+（256*Green)+(Blue) 不能用R.color的形式
                .setTitleText("城市选择")
                .setContentTextSize(20)                     //设置滚轮文字大小
                .setDividerColor(Color.GRAY)              //设置分割线的颜色
                .setSelectOptions(0, 0)                  //默认选中项
                .setBgColor(Color.WHITE)                // 设置下方选择框的背景颜色
                .setTitleBgColor(getResources().getColor(R.color.home_title_color))           // 设置标题处整个背景的背景颜色
                .setTitleColor(Color.WHITE)           // 设置标题颜色
                .setCancelColor(Color.WHITE)         // 设置左边取消按钮颜色
                .setSubmitColor(Color.WHITE)        // 设置右边确定按钮颜色
                .setTextColorCenter(getResources().getColor(R.color.home_title_color))   //  设置下方滚轮选中条目的字体颜色
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                // 只能设为三级  不管显示多少级
                .setBackgroundId(0x66000000) //设置外部遮罩颜色 只能用16进制的方式 这里设置为无颜色 只是透明度修改
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        cityCustomOptions.setPicker(options1Items, options2Items); //二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/

    }

}
