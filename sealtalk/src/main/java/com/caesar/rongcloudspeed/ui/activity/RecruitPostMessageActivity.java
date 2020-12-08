package com.caesar.rongcloudspeed.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.allen.library.SuperTextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.dialog.SelectPictureBottomDialog;
import com.caesar.rongcloudspeed.utils.AccountValidatorUtil;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
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
public class RecruitPostMessageActivity extends TitleBaseActivity {
    @BindView(R.id.recruit_post_supert1)
    SuperTextView recruit_post_supert1;
    @BindView(R.id.recruit_post_supert2)
    SuperTextView recruit_post_supert2;
    @BindView(R.id.recruit_post_supert3)
    SuperTextView recruit_post_supert3;
    @BindView(R.id.recruit_post_supert4)
    SuperTextView recruit_post_supert4;
    @BindView(R.id.recruit_post_supert5)
    SuperTextView recruit_post_supert5;
    @BindView(R.id.recruit_post_supert6)
    SuperTextView recruit_post_supert6;
    @BindView(R.id.post_recruit_excerpt)
    EditText post_recruit_excerpt;
    private String uidString;
    private String phoneNumber;
    private DBManager dbManager;
    private OptionsPickerView cityCustomOptions;
    private List<City> options1Items = new ArrayList<>();
    private List<ArrayList<City>> options2Items = new ArrayList<>();
    private String post_area_code = "100000";
    private String post_area_name = "全国";
    private String post_grade = "2";
    private String post_salary = "6";
    private String post_job = "2";
    private String[] salaryItems = new String[]{"2000元/月以下", "2000～3000元/月", "3000～4000元/月", "4000～5000元/月", "5000～8000元/月", "8000元/月以上", "面议"};
    private String[] gradeItems = new String[]{"高职高中及以下", "大专院校", "全日制本科", "硕士研究生", "MBA", "博士及以上", "保密"};
    private String[] jobItems = new String[]{"1年及以下工作经验", "2年工作经验", "3年工作经验", "4年工作经验", "5年工作经验", "6年工作经验", "7年工作经验", "8年及以上工作经验"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_post_layout);
        getTitleBar().setTitle("招聘信息发布");
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        phoneNumber = UserInfoUtils.getPhone(this);
        //招聘信息
        recruit_post_supert1.setOnClickListener(view -> {
            showEditDialog(1);
        });
        //招聘薪资
        recruit_post_supert2.setOnClickListener(view -> {
            showSelectDialog(2);
        });
        //招聘地址
        recruit_post_supert3.setOnClickListener(view -> {
            cityCustomOptions.show();
        });
        //招聘学历
        recruit_post_supert4.setOnClickListener(view -> {
            showSelectDialog(4);
        });
        //工作年限
        recruit_post_supert5.setOnClickListener(view -> {
            showSelectDialog(5);
        });
        //联系方式
        recruit_post_supert6.setOnClickListener(view -> {
            showEditDialog(6);
        });
        recruit_post_supert2.setRightString(salaryItems[6]);
        recruit_post_supert3.setRightString(post_area_name);
        recruit_post_supert4.setRightString(gradeItems[2]);
        recruit_post_supert5.setRightString(jobItems[2]);
        recruit_post_supert6.setRightString(phoneNumber);
        getOptionData();
    }

    @OnClick({R.id.recruit_post_btn})
    public void onViewClicked(View view) {
        onPostAddRecruitMessage();
    }

    public void onPostAddRecruitMessage() {
        String post_name = recruit_post_supert1.getRightString();
        phoneNumber = recruit_post_supert6.getRightString();
        String excerptString = post_recruit_excerpt.getText().toString();
        if (TextUtils.isEmpty(post_name)) {
            showToast("招聘职位不能为空");
            return;
        }
        if (TextUtils.isEmpty(post_area_code)) {
            showToast("招聘地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("手机号不能为空");
            return;
        }
        if (!AccountValidatorUtil.isMobile(phoneNumber)) {
            showToast("手机格式错误");
            return;
        }
        if (TextUtils.isEmpty(excerptString)) {
            excerptString = post_name;
        }
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().addRecruitPost(uidString, post_name, post_area_code, post_area_name, post_salary, post_grade, post_job, phoneNumber, excerptString),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData lessonBaseBean) {
                        if (lessonBaseBean.getCode() == CODE_SUCC) {
                            Toast.makeText(RecruitPostMessageActivity.this, "您已成功发布同行招聘", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(RecruitPostMessageActivity.this, lessonBaseBean.getInfo(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(RecruitPostMessageActivity.this, "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void showEditDialog(int position) {
        String title = "请录入招聘信息";
        final EditText editText = new EditText(this);
        if (position == 6) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(R.drawable.recruit_finding)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        if (position == 1) {
                            recruit_post_supert1.setRightString(editText.getText().toString());
                        } else {
                            recruit_post_supert6.setRightString(editText.getText().toString());
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void showSelectDialog(int position) {
        String title = "请选择招聘信息";
        int checkedItem = 2;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.recruit_finding);
        builder.setTitle(title);
        String[] finalItems = salaryItems;
        if (position == 4) {
            finalItems = gradeItems;
        } else if (position == 5) {
            finalItems = jobItems;
        }
        builder.setSingleChoiceItems(finalItems, checkedItem, (dialog, which) -> {
            if (position == 2) {
                post_salary = String.valueOf(which);
                recruit_post_supert2.setRightString(salaryItems[which]);
            } else if (position == 4) {
                post_grade = String.valueOf(which);
                recruit_post_supert4.setRightString(gradeItems[which]);
            } else {
                post_job = String.valueOf(which);
                recruit_post_supert5.setRightString(jobItems[which]);
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
                post_area_name = options2Items.get(options1).get(option2).getName();
                post_area_code = options2Items.get(options1).get(option2).getCode();
                recruit_post_supert3.setRightString(provinceStr + "-" + post_area_name);
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
