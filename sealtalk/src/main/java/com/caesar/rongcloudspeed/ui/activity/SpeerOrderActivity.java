package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.BookShopAdapter;
import com.caesar.rongcloudspeed.adapter.MemberSpeerAdapter;
import com.caesar.rongcloudspeed.bean.AddressDefaultBean;
import com.caesar.rongcloudspeed.bean.AddressItemBean;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.FenqiBean;
import com.caesar.rongcloudspeed.bean.GoodsDetailBean;
import com.caesar.rongcloudspeed.bean.MemberSpeerBean;
import com.caesar.rongcloudspeed.bean.UserOrderBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.common.VerificationCodeHelper;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.ui.dialog.PayPassDialog;
import com.caesar.rongcloudspeed.ui.dialog.PayPassView;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.zaaach.citypicker.db.DBManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SpeerOrderActivity extends MultiStatusActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.member_recyclerview)
    RecyclerView memberRecyclerview;
    @BindView(R.id.weixin_speer_paybox)
    CheckBox weixinBox;
    @BindView(R.id.alipay_speer_paybox)
    CheckBox alipayBox;
    @BindView(R.id.getSpeerVerifiCode)
    TextView getSpeerVerifiCode;
    private DBManager dbManager;
    private String uidString;
    private MemberSpeerAdapter memberSpeerAdapter;
    private List<MemberSpeerBean> memberSpeerBeanList=new ArrayList<MemberSpeerBean>();
    /**
     * 收货地址
     */
    private AddressItemBean addressitembean = null;
    /**
     * 选中的分期数
     */
    private FenqiBean.Info defaultInfo = null;
    //    private ConfirmOrderBean mValue;
    private boolean isRread = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBarView(titlebar, "确认订单");
        ButterKnife.bind(this);
        dbManager = new DBManager(this);
        weixinBox.setOnCheckedChangeListener(this);
        alipayBox.setOnCheckedChangeListener(this);
        initData();
        uidString = UserInfoUtils.getAppUserId(this);
        memberSpeerAdapter = new MemberSpeerAdapter(this,memberSpeerBeanList);
        memberSpeerAdapter.openLoadAnimation();
        memberSpeerAdapter.setNotDoAnimationCount(4);
        memberRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        memberRecyclerview.setAdapter(memberSpeerAdapter);
        MemberSpeerBean bean=new MemberSpeerBean();
        bean.setMember_title("连续包月");
        bean.setMember_price("¥ 16.88");
        MemberSpeerBean bean1=new MemberSpeerBean();
        bean1.setMember_title("月度VIP");
        bean1.setMember_price("¥ 24.88");
        MemberSpeerBean bean2=new MemberSpeerBean();
        bean2.setMember_title("季度VIP");
        bean2.setMember_price("¥ 58.88");
        MemberSpeerBean bean3=new MemberSpeerBean();
        bean3.setMember_title("年度VIP");
        bean3.setMember_price("¥ 218.88");
        memberSpeerBeanList.add(bean);
        memberSpeerBeanList.add(bean1);
        memberSpeerBeanList.add(bean2);
        memberSpeerBeanList.add(bean3);
        memberSpeerAdapter.setNewData(memberSpeerBeanList);
    }

    private void initData() {

        new VerificationCodeHelper(getSpeerVerifiCode) {
            @Override
            public void onClick(View v) {
                final VerificationCodeHelper temp = this;
                String phone= UserInfoUtils.getPhone(SpeerOrderActivity.this);
                RetrofitManager.create()
                        .sendCode(phone)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<CommonResonseBean>() {
                            @Override
                            public void onSuccess(CommonResonseBean value) {
                                if (value.getCode() == 101) {
                                    temp.onSuccess();
                                } else {
                                    temp.onFailure();
                                }
                            }

                            @Override
                            public boolean onFailure(Throwable e) {
                                temp.onFailure();
                                return super.onFailure(e);
                            }
                        });

            }
        };
    }

    @Override
    public int getContentView() {
        return R.layout.activity_speer_order;
    }


    @OnClick({R.id.protoCv, R.id.speer_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.protoCv:
//                BrowerActivity.openURL(RetrofitManager.server_address + "shop_service", "购物服务协议", true);
                break;
            case R.id.speer_pay:
                if (!alipay && !selfpay) {
                    ToastUtils.showShort("请选择支付方式");
                } else if (addressitembean == null) {
                    ToastUtils.showShort("请先选择收货地址");

                } else if (TextUtils.isEmpty(getSpeerVerifiCode.getText().toString())) {
                    ToastUtils.showShort("请输入验证码");

                } else {

                    if (!isRread) {
                        ToastUtils.showShort("请阅读并同意购物服务协议");
                        return;
                    }

                    if (alipay) {
                        //支付宝支付
                        //拿到支付需要的基本参数


                    } else if (selfpay) {
                        //立即支付
//                        prompDialog.showLoading("请等待");


                    }

                }


                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            AddressItemBean addressItem = data.getParcelableExtra("addressItem");
            String provinceString = dbManager.getAreaName(addressItem.getProvince());
            String cityString = dbManager.getAreaName(addressItem.getCity());
            addressitembean = addressItem;
        }
    }

    boolean alipay = false;
    boolean selfpay = false;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        alipayBox.setOnCheckedChangeListener(null);
        weixinBox.setOnCheckedChangeListener(null);
        if (buttonView.getId() == R.id.userAliPay) {
            alipay = isChecked;
            if (selfpay) {
                weixinBox.setChecked(false);
                selfpay = false;
            }
        } else if (buttonView.getId() == R.id.userCredit) {
            selfpay = isChecked;
            if (alipay) {
                alipay = false;
                alipayBox.setChecked(false);
            }
        }

        alipayBox.setOnCheckedChangeListener(this);
        weixinBox.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}