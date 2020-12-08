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
import android.widget.CompoundButton;
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
import com.caesar.rongcloudspeed.ui.dialog.SelectPictureBottomDialog;
import com.caesar.rongcloudspeed.util.ToastUtils;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * 新消息提醒
 */
public class AccountRecruitMessageActivity extends TitleBaseActivity {
    @BindView(R.id.recruit_base_supert)
    SuperTextView recruit_base_supert;
    @BindView(R.id.recruit_base_supert1)
    SuperTextView recruit_base_supert1;
    @BindView(R.id.recruit_base_supert2)
    SuperTextView recruit_base_supert2;
    @BindView(R.id.recruit_base_supert3)
    SuperTextView recruit_base_supert3;
    @BindView(R.id.recruit_add_supert3)
    SuperTextView recruit_add_supert3;
    @BindView(R.id.recruit_base_supert4)
    SuperTextView recruit_base_supert4;
    @BindView(R.id.recruit_base_supert5)
    SuperTextView recruit_base_supert5;
    @BindView(R.id.recruit_base_supert6)
    SuperTextView recruit_base_supert6;
    @BindView(R.id.recruit_base_supert7)
    SuperTextView recruit_base_supert7;
    @BindView(R.id.recruit_base_supert8)
    SuperTextView recruit_base_supert8;
    @BindView(R.id.recruit_base_supert9)
    SuperTextView recruit_base_supert9;
    @BindView(R.id.recruit_base_supert10)
    SuperTextView recruit_base_supert10;
    @BindView(R.id.recruit_base_supert11)
    SuperTextView recruit_base_supert11;
    @BindView(R.id.recruit_base_supert12)
    SuperTextView recruit_base_supert12;
    @BindView(R.id.recruit_base_supert14)
    SuperTextView recruit_base_supert14;
    private String uidString;
    private String phoneNumber;
    private String recruit_avatar;
    private int recruitState;
    private DBManager dbManager;
    private OptionsPickerView cityCustomOptions;
    private List<City> options1Items = new ArrayList<>();
    private List<ArrayList<City>> options2Items = new ArrayList<>();
    private String postRegion="110000";
    private String recruit_sex = "0";
    private String recruit_marry = "0";
    private String recruit_show = "0";
    private String recruit_age = "21";
    private RecruitItemBean recruitItemBean;
    String[] sexItems = new String[]{"男", "女", "保密"};
    String[] marryItems = new String[]{"未婚", "已婚", "保密"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recruit_basemessage);
        getTitleBar().setTitle("基本信息");
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        phoneNumber = UserInfoUtils.getPhone(this);
        recruitState = getIntent().getIntExtra("state", 0);
        //简历头像
        recruit_base_supert.setOnClickListener(view -> {
            showSelectPictureDialog();
        });
        //简历名称
        recruit_base_supert1.setOnClickListener(view -> {
            showEditDialog(1);
        });
        //简历性别
        recruit_base_supert2.setOnClickListener(view -> {
            showSelectDialog(2);
        });
        //简历生日
        recruit_base_supert3.setOnClickListener(view -> {
            showDatePickerDialog();
        });
        //手机号码
        recruit_base_supert4.setOnClickListener(view -> {
            showEditDialog(4);
        });
        //简历邮箱
        recruit_base_supert8.setOnClickListener(view -> {
            showEditDialog(8);
        });
        //简历户籍
        recruit_base_supert10.setOnClickListener(view -> {
            cityCustomOptions.show();
        });
        //简历名族
        recruit_base_supert11.setOnClickListener(view -> {
            showEditDialog(11);
        });
        //简历婚姻
        recruit_base_supert12.setOnClickListener(view -> {
            showSelectDialog(12);
        });
        recruit_base_supert14.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                superTextView.setSwitchIsChecked(!superTextView.getSwitchIsChecked());
            }
        }).setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recruit_show = isChecked ? "1" : "0";
            }
        });
        getOptionData();
        recruitItemBean = getIntent().getParcelableExtra("recruitItemBean");
        if (recruitItemBean != null) {
            ImageLoaderUtils.displayUserPortraitImage(recruitItemBean.getRecruit_avatar(), recruit_base_supert.getRightIconIV());
            recruit_base_supert1.setRightString(recruitItemBean.getRecruit_name());
            postRegion = recruitItemBean.getRecruit_native();
            String nativeString = dbManager.getAreaNames(postRegion);
            recruit_sex = recruitItemBean.getRecruit_sex();
            if (recruit_sex.equals("0")) {
                recruit_base_supert2.setRightString("男");
            } else if (recruit_sex.equals("1")) {
                recruit_base_supert2.setRightString("女");
            } else {
                recruit_base_supert2.setRightString("保密");
            }
            recruit_age=recruitItemBean.getRecruit_age();
            recruit_base_supert3.setRightString(recruitItemBean.getRecruit_birthday());
            recruit_add_supert3.setRightString(recruit_age+"岁");
            recruit_base_supert4.setRightString(recruitItemBean.getRecruit_mobile());
            recruit_base_supert8.setRightString(recruitItemBean.getRecruit_email());
            recruit_base_supert10.setRightString(nativeString);
            recruit_base_supert11.setRightString(recruitItemBean.getRecruit_nation());
            recruit_marry = recruitItemBean.getRecruit_marry();
            if (recruit_marry.equals("0")) {
                recruit_base_supert12.setRightString("未婚");
            } else if (recruit_marry.equals("1")) {
                recruit_base_supert12.setRightString("已婚");
            } else {
                recruit_base_supert12.setRightString("保密");
            }
            recruit_show = recruitItemBean.getRecruit_show();
            recruit_base_supert14.setSwitchIsChecked(recruit_show.equals("1"));
        }
    }

    @OnClick({R.id.post_recruit_btn})
    public void onViewClicked(View view) {
        onPostUserrecruitMessage();
    }

    public void onPostUserrecruitMessage() {
        String recruit_name = recruit_base_supert1.getRightString();
        String recruit_birthday = recruit_base_supert3.getRightString();
        String recruit_mobile = recruit_base_supert4.getRightString();
        String recruit_email = recruit_base_supert8.getRightString();
        String recruit_natives = recruit_base_supert10.getRightString();
        String recruit_nation = recruit_base_supert11.getRightString();
        if (TextUtils.isEmpty(recruit_name)) {
            showToast("简历姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(recruit_birthday)) {
            showToast("出生日期不能为空");
            return;
        }
        if (TextUtils.isEmpty(recruit_mobile)) {
            showToast("手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(recruit_email)) {
            showToast("邮箱不能为空");
            return;
        }
        if (TextUtils.isEmpty(postRegion)) {
            showToast("户籍不能为空");
            return;
        }
        if (TextUtils.isEmpty(recruit_nation)) {
            showToast("民族不能为空");
            return;
        }
        if(recruitItemBean!=null){
            NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateUserRecruit(uidString, recruit_name, recruit_avatar, recruit_sex, recruit_birthday, recruit_age,recruit_mobile, recruit_email, recruit_natives,postRegion, recruit_nation, recruit_marry, recruit_show),
                    new NetworkCallback<BaseData>() {
                        @Override
                        public void onSuccess(BaseData lessonBaseBean) {
                            if (lessonBaseBean.getCode() == CODE_SUCC) {
                                Toast.makeText(AccountRecruitMessageActivity.this, "您已成功提交同行简历", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(AccountRecruitMessageActivity.this, lessonBaseBean.getInfo(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(AccountRecruitMessageActivity.this, "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().addUserRecruit(uidString, recruit_name, recruit_avatar, recruit_sex, recruit_birthday,recruit_age, recruit_mobile, recruit_email,recruit_natives, postRegion, recruit_nation, recruit_marry, recruit_show),
                    new NetworkCallback<BaseData>() {
                        @Override
                        public void onSuccess(BaseData lessonBaseBean) {
                            if (lessonBaseBean.getCode() == CODE_SUCC) {
                                Toast.makeText(AccountRecruitMessageActivity.this, "您已成功提交同行简历", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(AccountRecruitMessageActivity.this, lessonBaseBean.getInfo(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(AccountRecruitMessageActivity.this, "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }

    /**
     * 选择图片的 dialog
     */
    private void showSelectPictureDialog() {
        SelectPictureBottomDialog.Builder builder = new SelectPictureBottomDialog.Builder();
        builder.setOnSelectPictureListener(uri -> {
            //上传图片
            Log.d("resource.data:", String.valueOf(uri));
            QiniuUtils.uploadFile(this, uri.getPath(), QiniuUtils.createImageKey(phoneNumber), new UpLoadImgCallback() {
                @Override
                public void onSuccess(String imgUrl) {
                    recruit_avatar = imgUrl;
                    ImageLoaderUtils.displayUserPortraitImage(imgUrl, recruit_base_supert.getRightIconIV());
                }

                @Override
                public void onFailure() {
                    Log.e("uploadFailure", "imgUrl*");
                }
            });
        });
        SelectPictureBottomDialog dialog = builder.build();
        dialog.show(getSupportFragmentManager(), "select_picture_dialog");
    }

    public void showEditDialog(int position) {
        String title = "请录入简历信息";
        final EditText editText = new EditText(this);
        if (position == 4) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (position == 8) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        if (recruitItemBean != null) {
            editText.setText(recruitItemBean.getRecruit_name());
            if (position == 4) {
                editText.setText(recruitItemBean.getRecruit_mobile());
            } else if (position == 8) {
                editText.setText(recruitItemBean.getRecruit_email());
            } else if (position == 11) {
                editText.setText(recruitItemBean.getRecruit_nation());
            }
        }
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(R.drawable.recruit_message)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        if (position == 1) {
                            recruit_base_supert1.setRightString(editText.getText().toString());
                        } else if (position == 4) {
                            recruit_base_supert4.setRightString(editText.getText().toString());
                        } else if (position == 8) {
                            recruit_base_supert8.setRightString(editText.getText().toString());
                        } else if (position == 11) {
                            recruit_base_supert11.setRightString(editText.getText().toString());
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void showSelectDialog(int position) {
        String title = "请选择简历信息";
        int checkedItem = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.recruit_message);
        builder.setTitle(title);
        String[] finalItems = sexItems;
        if (position == 12) {
            finalItems = marryItems;
        }
        if (recruitItemBean != null) {
            checkedItem = Integer.valueOf(recruit_sex);
            if (position == 12) {
                finalItems = marryItems;
                checkedItem = Integer.valueOf(recruit_marry);
            }
        }
        builder.setSingleChoiceItems(finalItems, checkedItem, (dialog, which) -> {
            if (position == 2) {
                recruit_sex = String.valueOf(which);
                recruit_base_supert2.setRightString(sexItems[which]);
            } else if (position == 12) {
                recruit_marry = String.valueOf(which);
                recruit_base_supert12.setRightString(marryItems[which]);
            }
            dialog.dismiss();
        });
        builder.show();
    }

    @SuppressLint("DefaultLocale")
    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int yearString = calendar.get(Calendar.YEAR);
        int monthString = calendar.get(Calendar.MONTH);
        int dayString = calendar.get(Calendar.DAY_OF_MONTH);
        if (recruitItemBean != null) {
            String birthdayString = recruitItemBean.getRecruit_birthday();
            String dayArray[] = birthdayString.split("-");
            yearString = Integer.parseInt(dayArray[0]);
            monthString = Integer.parseInt(dayArray[1]) - 1;
            dayString = Integer.parseInt(dayArray[2]);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            String str_month = String.format("%02d", monthOfYear + 1);
            String str_day = String.format("%02d", dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            int NowYear = Integer.parseInt(format.format(new Date()));
            recruit_age=String.valueOf(NowYear - year);
            recruit_base_supert3.setRightString(year + "-" + str_month + "-" + str_day);
            recruit_add_supert3.setRightString( recruit_age + "岁");
        }, yearString, monthString, dayString);
        datePickerDialog.show();

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
                postRegion = options2Items.get(options1).get(option2).getCode();
                recruit_base_supert10.setRightString(provinceStr + "-" + cityStr);
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
