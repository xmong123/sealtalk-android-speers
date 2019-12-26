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

import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.AddressDefaultBean;
import com.caesar.rongcloudspeed.bean.AddressItemBean;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.FenqiBean;
import com.caesar.rongcloudspeed.bean.UserOrderBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.common.VerificationCodeHelper;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.ui.dialog.PayPassDialog;
import com.caesar.rongcloudspeed.ui.dialog.PayPassView;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.zaaach.citypicker.db.DBManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ConfirmOrderActivity extends MultiStatusActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.timeTips)
    TextView timeTips;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.userCredit)
    CheckBox userCredit;
    @BindView(R.id.userAliPay)
    CheckBox userAliPay;
    @BindView(R.id.protoCv)
    CardView protoCv;
    @BindView(R.id.addAddressCv)
    CardView addAddressCv;
    @BindView(R.id.pay)
    Button pay;
    @BindView(R.id.yiyuqi)
    TextView yiyuqi;
    @BindView(R.id.goodTitle)
    TextView goodTitle;
    @BindView(R.id.goodMoney)
    TextView goodMoney;
    @BindView(R.id.goodImage)
    ImageView goodImage;
    @BindView(R.id.countMoneytv)
    TextView countMoneytv;
    @BindView(R.id.yanzhengmaEt)
    EditText yanzhengmaEt;
    @BindView(R.id.getVerificationCode)
    TextView getVerificationCode;
    @BindView(R.id.yanzhengmaEtLl)
    LinearLayout yanzhengmaEtLl;
    @BindView(R.id.yanzhengmaCv)
    CardView yanzhengmaCv;
    private String gid = "";
    private String money = "";
    private DialogPlus dialogPlus;
    private DBManager dbManager;
    private String uidString;
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
        money = getIntent().getExtras().getString("orderTotal");
        goodMoney.setText("¥" + money);
        countMoneytv.setText("总额: ¥" + money);
        userCredit.setOnCheckedChangeListener(this);
        userAliPay.setOnCheckedChangeListener(this);
        initData();
        uidString = UserInfoUtils.getAppUserId(this);
        loadData();
    }

    private void initData() {
//        time.setVisibility(View.INVISIBLE);
//        timeTips.setTextColor(getResources().getColor(R.color.textColorDark));
//        yiyuqi.setVisibility(View.INVISIBLE);

        new VerificationCodeHelper(getVerificationCode) {
            @Override
            public void onClick(View v) {
                final VerificationCodeHelper temp = this;
                String phone= UserInfoUtils.getPhone(ConfirmOrderActivity.this);
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

    private void loadData() {
        multipleStatusView.showLoading();
        RetrofitManager.create()
                .getDefaultAddressJson(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AddressDefaultBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(AddressDefaultBean value) {
                        if (value.getCode() == 101) {
                            AddressItemBean addressItem = value.getReferer();
                            String provinceString = dbManager.getAreaName(addressItem.getProvince());
                            String cityString = dbManager.getAreaName(addressItem.getCity());
                            userName.setText(addressItem.getConsignee() + "        " + addressItem.getMobile());
                            address.setText("收货地址 : " + provinceString + "-" + cityString + "-" + addressItem.getAddress());
                            addressitembean = addressItem;
                        } else {
                            ToastUtils.showShort("请先选中收货地址");
                        }
                    }

                    @Override
                    public boolean onFailure(Throwable e) {

                        return super.onFailure(e);
                    }
                });

    }

    @Override
    public int getContentView() {
        return R.layout.activity_confirm_order;
    }


    @OnClick({R.id.addAddressCv, R.id.protoCv, R.id.pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addAddressCv:

//                  startActivityForResult(new Intent(this, AddAddressActivity.class), 100);
                AddressListActivity.openForResult(100);

                break;
            case R.id.protoCv:
//                BrowerActivity.openURL(RetrofitManager.server_address + "shop_service", "购物服务协议", true);
                break;
            case R.id.pay:
                if (!alipay && !selfpay) {
                    ToastUtils.showShort("请选择支付方式");
                } else if (addressitembean == null) {
                    ToastUtils.showShort("请先选择收货地址");

                } else if (TextUtils.isEmpty(yanzhengmaEt.getText().toString())) {
                    ToastUtils.showShort("请输入验证码");

                } else {

                    if (!isRread) {
                        ToastUtils.showShort("请阅读并同意购物服务协议");
                        return;
                    }

                    if (alipay) {
                        //支付宝支付
                        //拿到支付需要的基本参数
                        submitData();

                    } else if (selfpay) {
                        //立即支付
//                        prompDialog.showLoading("请等待");
                        loadfenqimoney();

                    }

                }


                break;
        }
    }

    /**
     * 提交的支付宝支付订单
     */
    private void submitData() {
//        prompDialog.showLoading("请等待");
//        RetrofitManager.create().alipay_buy(SPUtils.getInstance().getString("uid"),
//                gid, addressitembean.getName(), addressitembean.getMobile(), addressitembean.getCity() + addressitembean.getAddress(), yanzhengmaEt.getText().toString()).compose(this.<ReBean>bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CommonObserver<ReBean>(prompDialog) {
//                    @Override
//                    public void onSuccess(ReBean value) {
//                        prompDialog.dismiss();
//                        if (value.getInfoData() != null && !TextUtils.isEmpty(value.getInfoData().getStr())) {
//                            mValue.setOrderId(value.getInfoData().getOrder_id());
//                            alipay(value.getInfoData().getStr());
//                        } else {
//                            ToastUtils.showShort("请求支付失败");
//                        }
//                    }
//                });
    }

    private void alipay(String re) {
        //1.创建支付宝支付订单的信息
        String rawAliOrderInfo = re;

        //3.发送支付宝支付请求
//        AliPayReq2 aliPayReq = new AliPayReq2.Builder()
//                .with(this)//Activity实例
//                .setRawAliPayOrderInfo(rawAliOrderInfo)//支付宝支付订单信息
//                .create()
//                .setOnAliPayListener(null);//
//        PayAPI.getInstance().sendPayRequest(aliPayReq);
//
//        //关于支付宝支付的回调
//        aliPayReq.setOnAliPayListener(new AliPayReq2.OnAliPayListener() {
//            @Override
//            public void onPaySuccess(String resultInfo) {
//
//                finishShopping();
//            }
//
//            @Override
//            public void onPayFailure(String resultInfo) {
//
//            }
//
//            @Override
//            public void onPayConfirmimg(String resultInfo) {
//
//            }
//
//            @Override
//            public void onPayCheck(String status) {
//
//            }
//        });
    }


    private void loadfenqimoney() {
        PayPassDialog payPassDialog=new PayPassDialog(ConfirmOrderActivity.this);
        payPassDialog.getPayViewPass().setPayClickListener(new PayPassView.OnPayClickListener() {
            @Override
            public void onPassFinish(String passContent) {
                String payPassWord= UserInfoUtils.getPayPassWord(ConfirmOrderActivity.this);
                if(payPassWord.equals(passContent)){
                    payPassDialog.dismiss();
                    multipleStatusView.showLoading();
                    RetrofitManager.create()
                            .addNewOrderJson(uidString,addressitembean.getAddress_id())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CommonObserver<UserOrderBean>(multipleStatusView) {
                                @Override
                                public void onSuccess(UserOrderBean userOrderBean) {
                                    if (userOrderBean.getCode() == 101) {
                                        ToastUtils.showShort("订单提交成功，请等待商家发货");
                                        Intent data = getIntent();
                                        setResult(RESULT_OK, data);
                                        finish();
                                    }else{
                                        ToastUtils.showShort(userOrderBean.getInfo()+",请稍后再试...");
                                    }
                                }

                                @Override
                                public boolean onFailure(Throwable e) {
                                    ToastUtils.showShort(e.getMessage());
                                    return super.onFailure(e);
                                }
                            });
                }else{
                    payPassDialog.dismiss();
                    Toast.makeText(ConfirmOrderActivity.this, "支付密码错误", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onPayClose() {
                payPassDialog.dismiss();
            }

            @Override
            public void onPayForget() {
                payPassDialog.dismiss();
            }
        });
//        prompDialog.showLoading("请等待");
//        RetrofitManager.create().stagepay_type(money, SPUtils.getInstance().getString("uid"))
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(this.<FenqiBean>bindToLifecycle())
//                .subscribe(new CommonObserver<FenqiBean>(ConfirmOrderActivity.this.prompDialog) {
//                    @Override
//                    public void onSuccess(FenqiBean value) {
//                        if (value.getCode() == Constant.CODE_SUCC) {
//
//                            showFenqiDialog(value);
//                        }
//                    }
//                });
    }

    public void showFenqiDialog(FenqiBean value) {
        // This will enable the expand feature, (similar to android L share dialog)
        dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.fenqidialog_layout))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .setContentBackgroundResource(android.R.color.transparent)
                .create();
        DialogPlus dialog = dialogPlus;
        View holderView = dialog.getHolderView();
        holderView.findViewById(R.id.confim_pay).setOnClickListener(this);
        LinearLayout ll = (LinearLayout) holderView.findViewById(R.id.container);
        addTextView(ll, value);
        defaultInfo = null;
        dialog.show();
    }


    private void addTextView(final LinearLayout ll, FenqiBean value) {
        List<FenqiBean.Info> info = value.getInfoData();
        int size = info.size();
        for (int i = 0; i < size; i++) {
            final FenqiBean.Info item = info.get(i);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int sizePx = (int) getResources().getDimension(R.dimen.layoutMargin16dp);
            if (i == size - 1) {
                layoutParams.bottomMargin = sizePx;
            }
            layoutParams.topMargin = sizePx;
            layoutParams.leftMargin = sizePx;
            layoutParams.rightMargin = sizePx;

            TextView textView = new TextView(this);
            textView.setPadding(0, sizePx, 0, sizePx);
            textView.setTextColor(getResources().getColor(R.color.textColorPrimary));
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            if ("1".equals(item.getTime())) {
                textView.setText("一次性付清 共" + item.getMoney_day() + "元");
                item.setPay_type("2");
            } else {
                item.setPay_type("1");
                String time = "0";
                if (item.getTime() != null) {
                    time = item.getTime();
                }
                textView.setText("限期" + time + "天 还款" + item.getMoney_day() + "元");
            }


            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.setBackgroundResource(R.drawable.shap_fenqi_textview_bg);
            frameLayout.setLayoutParams(layoutParams);


            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.choose);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            params.rightMargin = sizePx;
            imageView.setLayoutParams(params);
            imageView.setVisibility(View.INVISIBLE);
            frameLayout.addView(textView);
            frameLayout.addView(imageView);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    defaultInfo = item;
                    for (int i1 = 0; i1 < ll.getChildCount(); i1++) {
                        View fl = ll.getChildAt(i1);
                        View image = ((ViewGroup) fl).getChildAt(1);
                        if (fl != v) {
                            image.setVisibility(View.INVISIBLE);
                        } else {
                            image.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            ll.addView(frameLayout);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            AddressItemBean addressItem = data.getParcelableExtra("addressItem");
            String provinceString = dbManager.getAreaName(addressItem.getProvince());
            String cityString = dbManager.getAreaName(addressItem.getCity());
            userName.setText(addressItem.getConsignee() + "        " + addressItem.getMobile());
            address.setText("收货地址 : " + provinceString + "-" + cityString + "-" + addressItem.getAddress());
            addressitembean = addressItem;
        }
    }

    boolean alipay = false;
    boolean selfpay = false;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        userAliPay.setOnCheckedChangeListener(null);
        userCredit.setOnCheckedChangeListener(null);
        if (buttonView.getId() == R.id.userAliPay) {
            alipay = isChecked;
            if (selfpay) {
                userCredit.setChecked(false);
                selfpay = false;
            }
        } else if (buttonView.getId() == R.id.userCredit) {
            selfpay = isChecked;
            if (alipay) {
                alipay = false;
                userAliPay.setChecked(false);
            }
        }

        userAliPay.setOnCheckedChangeListener(this);
        userCredit.setOnCheckedChangeListener(this);
    }


    /**
     * 分期支付
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        //确认付款
        if (dialogPlus != null) {
            dialogPlus.dismiss();
            dialogPlus = null;
        }

        if (defaultInfo != null) {
            //立即支付
//            prompDialog.showLoading("请等待");
//            RetrofitManager.create().
//                    pay_stage(SPUtils.getInstance().getString("uid"),
//                            defaultInfo.getTime(),
//                            addressitembean.getName(),
//                            addressitembean.getMobile(),
//                            addressitembean.getCity() + addressitembean.getAddress(),
//                            defaultInfo.getPay_type(),
//                            gid, yanzhengmaEt.getText().toString()).
//                    map(new Function<FenqiBean2, FenqiBean2>() {
//                        @Override
//                        public FenqiBean2 apply(FenqiBean2 fenqibean2) throws Exception {
//                            Thread.sleep(1000);
//                            return fenqibean2;
//                        }
//                    }).
//                    compose(this.<FenqiBean2>bindToLifecycle()).
//                    observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new CommonObserver<FenqiBean2>(prompDialog) {
//                        @Override
//                        public void onSuccess(FenqiBean2 value) {
//                            if (value.getCode() == 101) {
//                                prompDialog.showSuccess("支付成功");
//                                mValue.setOrderId(value.getOrder_id());
//                                finishShopping();
//                            }
//
//                            if (value.getCode() == 103) {
//                                //提示用户升级额度
//                                showUpMoneyDialog();
//
//                            }
//                        }
//                    });
        } else {
            ToastUtils.showShort("请先选择分期数");
        }

    }

    private void finishShopping() {
        ToastUtils.showShort("购物成功");
        //通知更新额度
        finish();
    }

    /**
     * 升级用的额度dialog
     */
    private void showUpMoneyDialog() {
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setGravity(Gravity.CENTER)
                .setContentBackgroundResource(android.R.color.transparent)
                .setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT)  // or any custom width ie: 300
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentHolder(new ViewHolder(R.layout.dialog_view_upmoney))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        View holderView = dialog.getHolderView();
        holderView.findViewById(R.id.widthDraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        holderView.findViewById(R.id.giveUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
