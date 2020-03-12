package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.MemberSpeerAdapter;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.MemberSpeerBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.common.VerificationCodeHelper;
import com.caesar.rongcloudspeed.config.Config;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.result.TargetNumberData;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.zaaach.citypicker.db.DBManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SeekHelperOrderActivity extends MultiStatusActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.seek_tag)
    TextView seekTagText;
    @BindView(R.id.seek_title_text)
    TextView seekTitleText;
    @BindView(R.id.seek_price_text)
    TextView seekPriceText;
    @BindView(R.id.seek_troduce_text)
    TextView seekTroduceText;
    @BindView(R.id.seek_troduce_message)
    TextView seekMessageText;
    @BindView(R.id.seek_troduce_number)
    TextView seekNumberText;
    @BindView(R.id.weixin_speer_paybox)
    CheckBox weixinBox;
    @BindView(R.id.alipay_speer_paybox)
    CheckBox alipayBox;
    @BindView(R.id.seek_layout)
    LinearLayout seek_layout;
    @BindView(R.id.advert_layout)
    LinearLayout advert_layout;
    @BindView(R.id.normal_invoice_btn)
    Button normal_invoice_btn;
    @BindView(R.id.value_invoice_btn)
    Button value_invoice_btn;
    @BindView(R.id.codeVerifiEdit)
    EditText codeVerifiEdit;
    @BindView(R.id.getSpeerVerifiCode)
    TextView getSpeerVerifiCode;
    @BindView(R.id.speerSeekMoneyText)
    TextView speerSeekMoney;
    private String uidString;
    private String seek_name;
    private String seek_price;
    private boolean isRread = true;
    private String actionString = null;
    private String pay_code = "alipay";
    private String order_type = "3";
    private String seek_type = "3";
    private String industryIDString;
    private String professionIDString;
    private String softIDString;
    private int targetNumber=1;
    private float totalPrice=10.00f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBarView(titlebar, "确认订单");
        ButterKnife.bind(this);
        actionString = getIntent().getAction();
        seek_name = getIntent().getExtras().getString("seek_name");
        seek_price = getIntent().getExtras().getString("seek_price");
        seek_type = getIntent().getExtras().getString("seek_type");
        industryIDString = getIntent().getExtras().getString("industryIDString");
        professionIDString = getIntent().getExtras().getString("professionIDString");
        softIDString = getIntent().getExtras().getString("softIDString");
        weixinBox.setOnCheckedChangeListener(this);
        alipayBox.setOnCheckedChangeListener(this);
        initData();
        uidString = UserInfoUtils.getAppUserId(this);
        seekTitleText.setText(seek_name);
        seekPriceText.setText("¥" + seek_price);
        if (seek_type.equals("4")) {
            seekTagText.setText("广告");
            seekTroduceText.setText("求助信息发布费用说明：");
            seekMessageText.setText("广告信息发布费用为人民币0.05元/人/次。");
            seek_layout.setVisibility(View.GONE);
            advert_layout.setVisibility(View.VISIBLE);
        }
        purchaseHandler.sendEmptyMessage(0);
    }


    private void initData() {

        new VerificationCodeHelper(getSpeerVerifiCode) {
            @Override
            public void onClick(View v) {
                final VerificationCodeHelper temp = this;
                String phone = UserInfoUtils.getPhone(SeekHelperOrderActivity.this);
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
                    prompDialog.showLoading("正在查询");
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getNumberForTarget(industryIDString, professionIDString, softIDString),
                            new NetworkCallback<TargetNumberData>() {
                                @Override
                                public void onSuccess(TargetNumberData baseData) {
                                    prompDialog.dismiss();
                                    if (NetworkResultUtils.isSuccess(baseData)) {
                                        targetNumber=Integer.parseInt(baseData.getReferer());
                                        seekNumberText.setText("潜在用户数量："+baseData.getReferer()+"人");
                                        totalPrice+=targetNumber*0.05f;
                                        speerSeekMoney.setText("¥"+String.valueOf(totalPrice));
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SeekHelperOrderActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });
                    break;
                case 1:
                    prompDialog.showLoading("请等待");
                    String priceString=String.valueOf(totalPrice);
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().cartSeekOrder(uidString, "1", priceString, pay_code, order_type),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData data) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SeekHelperOrderActivity.this, "您提交了订单，请等待系统确认", Toast.LENGTH_SHORT).show();
                                    showPayDialog();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SeekHelperOrderActivity.this, "网络异常", Toast.LENGTH_LONG).show();
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
                setResult(RESULT_OK, getIntent());
                finish();
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
        return R.layout.activity_seek_order;
    }


    @OnClick({R.id.seekProtoBtn, R.id.speer_pay,R.id.normal_invoice_btn,R.id.value_invoice_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.normal_invoice_btn:
                Intent nIntent = new Intent(this, SeekInvoiceOrderActivity.class);
                startActivity(nIntent);
                break;
            case R.id.value_invoice_btn:
                break;
            case R.id.seekProtoBtn:
                Intent webIntent = new Intent(this, WebViewActivity.class);
                webIntent.putExtra("url", "file:///android_asset/webpage/memaiagree.html");
                webIntent.putExtra("title", "《同行快线普通服务协议》");
                startActivity(webIntent);
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
                        if (actionString != null) {
                            finish();
                        } else {
                            purchaseHandler.sendEmptyMessage(1);
                        }

                    } else if (wechatpay) {
                        //立即支付

                        if (actionString != null) {
                            finish();
                        } else {
                            purchaseHandler.sendEmptyMessage(1);
                        }

                    }

                }


                break;
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
    public void onDestroy() {
        super.onDestroy();
    }

}
