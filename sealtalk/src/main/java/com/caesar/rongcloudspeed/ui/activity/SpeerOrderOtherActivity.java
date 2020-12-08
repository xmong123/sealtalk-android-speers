package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.MemberSpeerAdapter;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.GoodsOrderBaseBean;
import com.caesar.rongcloudspeed.bean.MemberSpeerBean;
import com.caesar.rongcloudspeed.bean.WechatPayBaseBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.common.VerificationCodeHelper;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.wx.WXManager.APP_ID;

public class SpeerOrderOtherActivity extends MultiStatusActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.lesson_title)
    TextView lessonTitle;
    @BindView(R.id.lesson_price)
    TextView lessonPrice;
    @BindView(R.id.member_recyclerview)
    RecyclerView memberRecyclerview;
    @BindView(R.id.weixin_speer_paybox)
    CheckBox weixinBox;
    @BindView(R.id.alipay_speer_paybox)
    CheckBox alipayBox;
    @BindView(R.id.codeVerifiEdit)
    EditText codeVerifiEdit;
    @BindView(R.id.getSpeerVerifiCode)
    TextView getSpeerVerifiCode;
    @BindView(R.id.speerLessonMoney)
    TextView speerLessonMoney;
    private String uidString;
    private String payPriceString = "16.88";
    private String payNameString = "《连续包月》VIP服务费";
    private MemberSpeerAdapter memberSpeerAdapter;
    private List<MemberSpeerBean> memberSpeerBeanList = new ArrayList<MemberSpeerBean>();
    private boolean isRread = true;
    private String pay_code = "alipay";
    private String order_type = "6";
    private String out_trade_no;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBarView(titlebar, "确认订单");
        ButterKnife.bind(this);
        weixinBox.setOnCheckedChangeListener(this);
        alipayBox.setOnCheckedChangeListener(this);
        initData();
        uidString = UserInfoUtils.getAppUserId(this);
        memberSpeerAdapter = new MemberSpeerAdapter(this, memberSpeerBeanList);
        memberSpeerAdapter.openLoadAnimation();
        memberSpeerAdapter.setNotDoAnimationCount(4);
        memberRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        memberRecyclerview.setAdapter(memberSpeerAdapter);
        MemberSpeerBean bean = new MemberSpeerBean();
        bean.setMember_id("1001");
        bean.setMember_title("连续包月");
        bean.setMember_price("16.88");
        MemberSpeerBean bean1 = new MemberSpeerBean();
        bean1.setMember_id("1002");
        bean1.setMember_title("月度VIP");
        bean1.setMember_price("24.88");
        MemberSpeerBean bean2 = new MemberSpeerBean();
        bean2.setMember_id("1003");
        bean2.setMember_title("季度VIP");
        bean2.setMember_price("58.88");
        MemberSpeerBean bean3 = new MemberSpeerBean();
        bean3.setMember_id("1004");
        bean3.setMember_title("年度VIP");
        bean3.setMember_price("218.88");
        memberSpeerBeanList.add(bean);
        memberSpeerBeanList.add(bean1);
        memberSpeerBeanList.add(bean2);
        memberSpeerBeanList.add(bean3);
        memberSpeerAdapter.setNewData(memberSpeerBeanList);
        memberSpeerAdapter.setOnItemClickListener((adapter, view, position) -> {
            boolean isFlag = memberSpeerBeanList.get(position).isFlag();
            String member_title = memberSpeerBeanList.get(position).getMember_title();
            String member_price = memberSpeerBeanList.get(position).getMember_price();
            for (int i = 0; i < memberSpeerBeanList.size(); i++) {
                if (isFlag) {
                    memberSpeerBeanList.get(i).setFlag(false);
                    payPriceString = "16.88";
                    payNameString = "《连续包月》VIP服务费";
                } else {
                    if (i == position) {
                        memberSpeerBeanList.get(i).setFlag(true);
                    } else {
                        memberSpeerBeanList.get(i).setFlag(false);
                    }
                    payPriceString = member_price;
                    payNameString = "《" + member_title + "》VIP服务费";
                }
                adapter.notifyDataSetChanged();
                lessonTitle.setText(payNameString);
                lessonPrice.setText("¥ " + payPriceString);
                speerLessonMoney.setText("¥ " + payPriceString);
            }

        });
        lessonTitle.setText(payNameString);
        lessonPrice.setText("¥ " + payPriceString);
        speerLessonMoney.setText("¥ " + payPriceString);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
    }

    private void initData() {

        new VerificationCodeHelper(getSpeerVerifiCode) {
            @Override
            public void onClick(View v) {
                final VerificationCodeHelper temp = this;
                String phone = UserInfoUtils.getPhone(SpeerOrderOtherActivity.this);
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

    @SuppressLint("HandlerLeak")
    Handler purchaseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    prompDialog.showLoading("请等待");
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().cartLessonOrder(uidString, "0", payPriceString, pay_code, order_type, payNameString),
                            new NetworkCallback<GoodsOrderBaseBean>() {
                                @Override
                                public void onSuccess(GoodsOrderBaseBean goodsOrderBaseBean) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SpeerOrderOtherActivity.this, "您提交了订单，请等待系统确认", Toast.LENGTH_SHORT).show();
                                    out_trade_no = goodsOrderBaseBean.getReferer().getOrder_sn();
//                                    showPayDialog();
                                    purchaseHandler.sendEmptyMessage(1);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SpeerOrderOtherActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });

                    break;
                case 1:
                    float totalPrice = Float.valueOf(payPriceString) * 100f;
                    String priceString = String.valueOf((int) totalPrice);
                    prompDialog.showLoading("请等待");
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().WechatAppPaySign(uidString, out_trade_no, priceString, payNameString),
                            new NetworkCallback<WechatPayBaseBean>() {
                                @Override
                                public void onSuccess(WechatPayBaseBean baseBean) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SpeerOrderOtherActivity.this, "您正在发起支付，请稍后...", Toast.LENGTH_SHORT).show();
                                    WechatPayBaseBean.WechatPayBean wechatPayBean = baseBean.getReferer();
                                    PayReq req = new PayReq();
                                    req.appId = wechatPayBean.getAppid();
                                    req.partnerId = wechatPayBean.getPartnerid();
                                    req.prepayId = wechatPayBean.getPrepayid();
                                    req.nonceStr = wechatPayBean.getNoncestr();
                                    req.timeStamp = wechatPayBean.getTimestamp();
                                    req.packageValue = "Sign=WXPay";
                                    req.sign = wechatPayBean.getSign();
                                    req.extData = "app data"; // optional
                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                    api.sendReq(req);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SpeerOrderOtherActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };

    private void showPayDialog() {
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(android.R.color.transparent)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)  // or any custom width ie: 300
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentHolder(new ViewHolder(R.layout.orderconfirm_dialog_layout))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        View holderView = dialog.getHolderView();
        holderView.findViewById(R.id.morder_confirm_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                purchaseHandler.sendEmptyMessage(1);
            }
        });
        holderView.findViewById(R.id.morder_confirm_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_lessones_order;
    }


    @OnClick({R.id.speerProtoBtn, R.id.speer_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.speerProtoBtn:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", "file:///android_asset/webpage/speer_agree.html");
                intent.putExtra("title", "《同行快线普通服务协议》");
                startActivity(intent);
                break;
            case R.id.speer_pay:
                if (!alipay && !wechatpay) {
                    ToastUtils.showShort("请选择支付方式");
                } else if (TextUtils.isEmpty(getSpeerVerifiCode.getText().toString())) {
                    ToastUtils.showShort("请输入验证码");

                } else {

                    if (!isRread) {
                        ToastUtils.showShort("请阅读并同意同行快线普通服务协议");
                        return;
                    }

                    if (alipay) {
                        //支付宝支付
                        //拿到支付需要的基本参数
                        purchaseHandler.sendEmptyMessage(0);

                    } else if (wechatpay) {
                        //立即支付

                        purchaseHandler.sendEmptyMessage(0);

                    }

                }


                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uidString = UserInfoUtils.getAppUserId(this);
        boolean isWechatPay = UserInfoUtils.getWechatInfoData(this);
        if (isWechatPay) {
            UserInfoUtils.setUserType("6", SpeerOrderOtherActivity.this);
            UserInfoUtils.setWechatInfoData(false, this);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

        }
    }

    boolean alipay = false;
    boolean wechatpay = false;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        alipayBox.setOnCheckedChangeListener(null);
        weixinBox.setOnCheckedChangeListener(null);
        if (buttonView.getId() == R.id.alipay_speer_paybox) {
            alipay = isChecked;
            if (wechatpay) {
                weixinBox.setChecked(false);
                wechatpay = false;
            }
            pay_code = "alipay";
        } else if (buttonView.getId() == R.id.weixin_speer_paybox) {
            wechatpay = isChecked;
            if (alipay) {
                alipay = false;
                alipayBox.setChecked(false);
            }
            pay_code = "wechatpay";
        }

        alipayBox.setOnCheckedChangeListener(this);
        weixinBox.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
