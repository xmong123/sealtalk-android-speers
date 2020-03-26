package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.AddressDefaultBean;
import com.caesar.rongcloudspeed.bean.AddressItemBean;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.PersonalInvoiceBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddPersonalInvoiceActivity extends MultiStatusActivity {

    @BindView(R.id.invoice_name)
    EditText invoiceNameEdit;
    @BindView(R.id.invoice_identify)
    EditText invoiceIdentifyEdit;
    @BindView(R.id.invoice_address)
    EditText invoiceAddressEdit;
    @BindView(R.id.invoice_phone)
    EditText invoicePhoneEdit;
    @BindView(R.id.invoice_bank)
    EditText invoiceBankEdit;
    @BindView(R.id.invoice_account)
    EditText invoiceAccountEdit;
    private String uidString;
    private String invoiceNameString;
    private String invoiceIdentifyString;
    private String invoiceAddressString;
    private String invoicePhoneString;
    private String invoiceBankString;
    private String invoiceAccountString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBarView(titlebar, "设置增值税发票资质");
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        loadPersonalInvoiceData();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_invoice;
    }

    @OnClick({R.id.invoice_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.invoice_save:
                invoiceNameString = invoiceNameEdit.getText().toString();
                invoiceIdentifyString = invoiceIdentifyEdit.getText().toString();
                invoiceAddressString = invoiceAddressEdit.getText().toString();
                invoicePhoneString = invoicePhoneEdit.getText().toString();
                invoiceBankString = invoiceBankEdit.getText().toString();
                invoiceAccountString = invoiceAccountEdit.getText().toString();
                if (TextUtils.isEmpty(invoiceNameString) || invoiceNameString.length() < 8) {
                    ToastUtils.showShort("请输入有效的单位名称");
                } else if (TextUtils.isEmpty(invoiceIdentifyString) || invoiceIdentifyString.length() < 8) {
                    ToastUtils.showShort("请输入有效的纳税人识别码");

                } else if (TextUtils.isEmpty(invoiceAddressString) || invoiceAddressString.length() < 8) {
                    ToastUtils.showShort("请输入有效的注册地址");

                } else if (TextUtils.isEmpty(invoicePhoneString) || invoicePhoneString.length() < 11) {
                    ToastUtils.showShort("请输入有效的注册电话");

                } else if (TextUtils.isEmpty(invoiceBankString) || invoiceBankString.length() < 8) {
                    ToastUtils.showShort("请输入有效的开户银行");

                } else if (TextUtils.isEmpty(invoiceAccountString) || invoiceAccountString.length() < 11) {
                    ToastUtils.showShort("请输入有效的银行账户");

                }
                invoiceSubmitData();
                break;
        }
    }

    private void loadPersonalInvoiceData() {
        multipleStatusView.showLoading();
        RetrofitManager.create()
                .getUserInvoiceData(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<PersonalInvoiceBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(PersonalInvoiceBean invoiceBean) {
                        if (invoiceBean.getCode() == 101) {
                            PersonalInvoiceBean.InvoiceBean bean=invoiceBean.getReferer();
                            invoiceNameEdit.setText(bean.getInvoice_name());
                            invoiceIdentifyEdit.setText(bean.getInvoice_identifi());
                            invoiceAddressEdit.setText(bean.getInvoice_address());
                            invoicePhoneEdit.setText(bean.getInvoice_phone());
                            invoiceBankEdit.setText(bean.getInvoice_bank());
                            invoiceAccountEdit.setText(bean.getInvoice_account());
                        }
                    }

                    @Override
                    public boolean onFailure(Throwable e) {
                        return super.onFailure(e);
                    }
                });


    }

    private void invoiceSubmitData() {
        multipleStatusView.showLoading();
        RetrofitManager.create()
                .editUserInvoiceData(uidString, invoiceNameString, invoiceIdentifyString, invoiceAddressString, invoicePhoneString, invoiceBankString, invoiceAccountString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<CommonResonseBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(CommonResonseBean value) {
                        if (value.getCode() == 101) {
                            ToastUtils.showShort("增值税发票资质设置成功");
                        }
                        finish();
                    }

                    @Override
                    public boolean onFailure(Throwable e) {
                        return super.onFailure(e);
                    }
                });


    }

    public static void openForResult(int requestCode) {
        ActivityUtils.
                getTopActivity().
                startActivityForResult(new Intent(ActivityUtils.getTopActivity(), AddPersonalInvoiceActivity.class), requestCode);
    }
}
