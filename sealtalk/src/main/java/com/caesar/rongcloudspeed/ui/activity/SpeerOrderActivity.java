package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.BookShopAdapter;
import com.caesar.rongcloudspeed.adapter.MemberSpeerAdapter;
import com.caesar.rongcloudspeed.alipaysdk.PayResult;
import com.caesar.rongcloudspeed.bean.AddressDefaultBean;
import com.caesar.rongcloudspeed.bean.AddressItemBean;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.FenqiBean;
import com.caesar.rongcloudspeed.bean.GoodsDetailBean;
import com.caesar.rongcloudspeed.bean.GoodsOrderBaseBean;
import com.caesar.rongcloudspeed.bean.MemberSpeerBean;
import com.caesar.rongcloudspeed.bean.UserOrderBean;
import com.caesar.rongcloudspeed.bean.WechatPayBaseBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.common.VerificationCodeHelper;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.ui.dialog.PayPassDialog;
import com.caesar.rongcloudspeed.ui.dialog.PayPassView;
import com.caesar.rongcloudspeed.util.OrderInfoUtil2_0;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zaaach.citypicker.db.DBManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;
import static com.caesar.rongcloudspeed.quick.QiniuLabConfig.ALIAPPID;
import static com.caesar.rongcloudspeed.quick.QiniuLabConfig.RSA2_PRIVATE;
import static com.caesar.rongcloudspeed.quick.QiniuLabConfig.RSA_PRIVATE;
import static com.caesar.rongcloudspeed.quick.QiniuLabConfig.WXAPPID;

public class SpeerOrderActivity extends MultiStatusActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.lesson_title)
    TextView lessonTitle;
    @BindView(R.id.lesson_price)
    TextView lessonPrice;
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
    private String lesson_id;
    private String lesson_name;
    private String lesson_price;
    private String lesson_smeta;
    private String thumbVideoString;
    private boolean isRread = true;
    private String pay_code = "alipay";
    private String order_type = "2";
    private IWXAPI api;
    private String out_trade_no;
    private float totalPrice = 10.00f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBarView(titlebar, "确认订单");
        ButterKnife.bind(this);
        lesson_id = getIntent().getExtras().getString("lesson_id");
        lesson_name = getIntent().getExtras().getString("lesson_name");
        lesson_price = getIntent().getExtras().getString("lesson_price");
        lesson_smeta = getIntent().getExtras().getString("lesson_smeta");
        thumbVideoString = getIntent().getExtras().getString("videoPath");
        totalPrice = Float.valueOf(lesson_price) * 100f;
        weixinBox.setOnCheckedChangeListener(this);
        alipayBox.setOnCheckedChangeListener(this);
        initData();
        uidString = UserInfoUtils.getAppUserId(this);
        lessonTitle.setText(lesson_name);
        lessonPrice.setText("¥ " + lesson_price);
        speerLessonMoney.setText("¥ " + lesson_price);
        api = WXAPIFactory.createWXAPI(this, WXAPPID);
    }

    private void initData() {

        new VerificationCodeHelper(getSpeerVerifiCode) {
            @Override
            public void onClick(View v) {
                final VerificationCodeHelper temp = this;
                String phone = UserInfoUtils.getPhone(SpeerOrderActivity.this);
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
        String priceString = String.valueOf((int) totalPrice);
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    prompDialog.showLoading("请等待");
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().cartLessonOrder(uidString, lesson_id, lesson_price, pay_code, order_type, lesson_name),
                            new NetworkCallback<GoodsOrderBaseBean>() {
                                @Override
                                public void onSuccess(GoodsOrderBaseBean goodsOrderBaseBean) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SpeerOrderActivity.this, "您提交了订单，请等待系统确认", Toast.LENGTH_SHORT).show();
                                    if (goodsOrderBaseBean.getCode() == CODE_SUCC) {
                                        out_trade_no = goodsOrderBaseBean.getReferer().getOrder_sn();
                                        showPayDialog();
                                    } else {
                                        Toast.makeText(SpeerOrderActivity.this, "网络繁忙,请稍侯再试...", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SpeerOrderActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });

                    break;

                case 1:
                    prompDialog.showLoading("请等待");
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().WechatAppPaySign(uidString, out_trade_no, priceString, lesson_name),
                            new NetworkCallback<WechatPayBaseBean>() {
                                @Override
                                public void onSuccess(WechatPayBaseBean baseBean) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SpeerOrderActivity.this, "您正在发起支付，请稍后...", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(SpeerOrderActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });
                    break;
                case 2:
                    if(uidString.equals("2")){
                        priceString="0.01";
                    }
                    final String orderInfo = OrderInfoUtil2_0.payV2TradeNoParam(out_trade_no,lesson_name,lesson_price);
                    final Runnable payRunnable = () -> {
                        PayTask alipay = new PayTask(SpeerOrderActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Log.i("msp", result.toString());
                        Message msg1 = new Message();
                        msg1.what = 3;
                        msg1.obj = result;
                        purchaseHandler.sendMessage(msg1);
                    };

                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                    break;

                case 3:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    Log.d("SpeerOrderActivity",resultInfo);
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showAlert(SpeerOrderActivity.this, getString(R.string.pay_success));
                        Set<String> set = UserInfoUtils.getAppUserLessones(SpeerOrderActivity.this);
                        if (set == null) {
                            set = new HashSet<>();
                        }
                        set.add(lesson_id);
                        UserInfoUtils.setAppUserLessones(set, SpeerOrderActivity.this);
                        Intent videoIntent = new Intent(SpeerOrderActivity.this, SPLessonVideosActivity.class);
                        videoIntent.putExtra("lesson_status", true);
                        videoIntent.putExtra("lesson_id", lesson_id);
                        videoIntent.putExtra("lesson_name", lesson_name);
                        videoIntent.putExtra("lesson_price", lesson_price);
                        videoIntent.putExtra("lesson_smeta", lesson_smeta);
                        videoIntent.putExtra("videoPath", thumbVideoString);
                        startActivity(videoIntent);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(SpeerOrderActivity.this, getString(R.string.pay_failed));
                    }
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
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.orderconfirm_dialog_layout))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        View holderView = dialog.getHolderView();
        holderView.findViewById(R.id.morder_confirm_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (wechatpay) {
                    purchaseHandler.sendEmptyMessage(1);
                } else {
                    purchaseHandler.sendEmptyMessage(2);
                }
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
        return R.layout.activity_speer_order;
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

                    if (alipay || wechatpay) {
                        purchaseHandler.sendEmptyMessage(0);
                    } else {
                        ToastUtils.showShort("请选择支付方式");
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
            Set<String> set = UserInfoUtils.getAppUserLessones(SpeerOrderActivity.this);
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(lesson_id);
            UserInfoUtils.setAppUserLessones(set, SpeerOrderActivity.this);
            UserInfoUtils.setWechatInfoData(false, this);
            Intent videoIntent = new Intent(SpeerOrderActivity.this, SPLessonVideosActivity.class);
            videoIntent.putExtra("lesson_status", true);
            videoIntent.putExtra("lesson_id", lesson_id);
            videoIntent.putExtra("lesson_name", lesson_name);
            videoIntent.putExtra("lesson_price", lesson_price);
            videoIntent.putExtra("lesson_smeta", lesson_smeta);
            videoIntent.putExtra("videoPath", thumbVideoString);
            startActivity(videoIntent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "requestCode:" + requestCode + ", resultCode =>>> " + resultCode);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Set<String> set = UserInfoUtils.getAppUserLessones(SpeerOrderActivity.this);
            if (set == null) {
                set = new HashSet<>();
            }
            set.add(lesson_id);
            UserInfoUtils.setAppUserLessones(set, SpeerOrderActivity.this);
            Intent videoIntent = new Intent(SpeerOrderActivity.this, SPLessonVideosActivity.class);
            videoIntent.putExtra("lesson_status", true);
            videoIntent.putExtra("lesson_id", lesson_id);
            videoIntent.putExtra("lesson_name", lesson_name);
            videoIntent.putExtra("lesson_price", lesson_price);
            videoIntent.putExtra("lesson_smeta", lesson_smeta);
            videoIntent.putExtra("videoPath", thumbVideoString);
            startActivity(videoIntent);
            finish();
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
