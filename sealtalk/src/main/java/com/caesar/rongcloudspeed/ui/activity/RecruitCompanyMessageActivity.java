package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.allen.library.SuperTextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.CompanyBaseBean;
import com.caesar.rongcloudspeed.bean.CompanyItemBean;
import com.caesar.rongcloudspeed.bean.RecruitApplyBaseBean;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.ui.dialog.SelectPictureBottomDialog;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * 新消息提醒
 */
public class RecruitCompanyMessageActivity extends TitleBaseActivity {
    @BindView(R.id.recruit_company_supert1)
    SuperTextView recruit_company_supert1;
    @BindView(R.id.recruit_company_supert2)
    SuperTextView recruit_company_supert2;
    @BindView(R.id.recruit_company_supert3)
    SuperTextView recruit_company_supert3;
    @BindView(R.id.recruit_company_supert4)
    SuperTextView recruit_company_supert4;
    @BindView(R.id.recruit_company_supert5)
    SuperTextView recruit_company_supert5;
    @BindView(R.id.recruit_company_supert6)
    SuperTextView recruit_company_supert6;
    @BindView(R.id.recruit_company_supert7)
    SuperTextView recruit_company_supert7;
    private String uidString;
    private String phoneNumber;
    private String company_licence;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_company_layout);
        getTitleBar().setTitle("招聘单位认证信息");
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        phoneNumber = UserInfoUtils.getPhone(this);
        //简历头像
        recruit_company_supert1.setOnClickListener(view -> {
            showEditDialog(1);
        });
        //简历名称
        recruit_company_supert2.setOnClickListener(view -> {
            showEditDialog(2);
        });
        //简历性别
        recruit_company_supert3.setOnClickListener(view -> {
            showEditDialog(3);
        });
        //简历生日
        recruit_company_supert4.setOnClickListener(view -> {
            showSelectPictureDialog();
        });
        //手机号码
        recruit_company_supert5.setOnClickListener(view -> {
            showEditDialog(5);
        });
        //简历邮箱
        recruit_company_supert6.setOnClickListener(view -> {
            showEditDialog(6);
        });
        //简历户籍
        recruit_company_supert7.setOnClickListener(view -> {
            showEditDialog(7);
        });
        getRecruitCompanyData();
    }

    private void getRecruitCompanyData() {
        RetrofitManager.create().getRecruitCompany(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<CompanyBaseBean>() {
                    @Override
                    public void onSuccess(CompanyBaseBean companyBaseBean) {
                        if (companyBaseBean.getCode() == Constant.CODE_SUCC) {
                            CompanyItemBean companyItemBean=companyBaseBean.getReferer();
                            recruit_company_supert1.setRightString(companyItemBean.getCompany_name());
                            recruit_company_supert2.setRightString(companyItemBean.getCompany_nature());
                            recruit_company_supert3.setRightString(companyItemBean.getCompany_size());
                            String licenceString=companyItemBean.getCompany_licence();
                            if(licenceString!=null&&licenceString.length()>32){
                                ImageLoaderUtils.displayUserPortraitImage(companyItemBean.getCompany_licence(), recruit_company_supert4.getRightIconIV());
                            }
                            recruit_company_supert5.setRightString(companyItemBean.getCompany_address());
                            recruit_company_supert6.setRightString(companyItemBean.getCompany_contact());
                            recruit_company_supert7.setRightString(companyItemBean.getCompany_phone());
                        } else {
                            Toast.makeText(RecruitCompanyMessageActivity.this, "请您认证单位信息", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(RecruitCompanyMessageActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @OnClick({R.id.post_company_btn})
    public void onViewClicked(View view) {
        onPostAddCompanyMessage();
    }

    public void onPostAddCompanyMessage() {
        String company_name = recruit_company_supert1.getRightString();
        String company_nature = recruit_company_supert2.getRightString();
        String company_size = recruit_company_supert3.getRightString();
        String company_address = recruit_company_supert5.getRightString();
        String company_contact = recruit_company_supert6.getRightString();
        String company_phone = recruit_company_supert7.getRightString();
        if (TextUtils.isEmpty(company_name)) {
            showToast("公司名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(company_nature)) {
            showToast("公司性质不能为空");
            return;
        }
        if (TextUtils.isEmpty(company_size)) {
            showToast("单位规模不能为空");
            return;
        }
        if (TextUtils.isEmpty(company_address)) {
            showToast("公司地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(company_contact)) {
            showToast("单位联系人");
            return;
        }
        if (TextUtils.isEmpty(company_phone)) {
            showToast("单位电话联系人");
            return;
        }
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().addRecruitCompany(uidString, company_name, company_nature, company_size, company_licence, company_address, company_contact, company_phone),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData lessonBaseBean) {
                        if (lessonBaseBean.getCode() == CODE_SUCC) {
                            Toast.makeText(RecruitCompanyMessageActivity.this, "您已成功提交同行简历", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(RecruitCompanyMessageActivity.this, lessonBaseBean.getInfo(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(RecruitCompanyMessageActivity.this, "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                    }
                });
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
                    company_licence = imgUrl;
                    ImageLoaderUtils.displayUserPortraitImage(imgUrl, recruit_company_supert4.getRightIconIV());
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
        String title = "请录入公司信息";
        final EditText editText = new EditText(this);
        if (position == 7) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(R.drawable.recruit_message)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        if (position == 1) {
                            recruit_company_supert1.setRightString(editText.getText().toString());
                        } else if (position == 2) {
                            recruit_company_supert2.setRightString(editText.getText().toString());
                        } else if (position == 3) {
                            recruit_company_supert3.setRightString(editText.getText().toString());
                        } else if (position == 5) {
                            recruit_company_supert5.setRightString(editText.getText().toString());
                        } else if (position == 6) {
                            recruit_company_supert6.setRightString(editText.getText().toString());
                        } else {
                            recruit_company_supert7.setRightString(editText.getText().toString());
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

}
