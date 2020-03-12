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
import com.caesar.rongcloudspeed.bean.MemberSpeerBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.common.VerificationCodeHelper;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
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
    private DBManager dbManager;
    private String uidString;
    private String lesson_id;
    private String lesson_name;
    private String lesson_price;
    private String lesson_smeta;
    private String thumbVideoString;
    private MemberSpeerAdapter memberSpeerAdapter;
    private List<MemberSpeerBean> memberSpeerBeanList = new ArrayList<MemberSpeerBean>();
    private boolean isRread = true;
    private String actionString = null;
    private String pay_code = "alipay";
    private String order_type = "2";
    private String stotal_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBarView(titlebar, "确认订单");
        ButterKnife.bind(this);
        actionString = getIntent().getAction();
        lesson_id = getIntent().getExtras().getString("lesson_id");
        lesson_name = getIntent().getExtras().getString("lesson_name");
        lesson_price = getIntent().getExtras().getString("lesson_price");
        lesson_smeta = getIntent().getExtras().getString("lesson_smeta");
        thumbVideoString = getIntent().getExtras().getString("videoPath");
        dbManager = new DBManager(this);
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
        bean.setMember_price("¥ 16.88");
        MemberSpeerBean bean1 = new MemberSpeerBean();
        bean1.setMember_id("1002");
        bean1.setMember_title("月度VIP");
        bean1.setMember_price("¥ 24.88");
        MemberSpeerBean bean2 = new MemberSpeerBean();
        bean2.setMember_id("1003");
        bean2.setMember_title("季度VIP");
        bean2.setMember_price("¥ 58.88");
        MemberSpeerBean bean3 = new MemberSpeerBean();
        bean3.setMember_id("1004");
        bean3.setMember_title("年度VIP");
        bean3.setMember_price("¥ 218.88");
        memberSpeerBeanList.add(bean);
        memberSpeerBeanList.add(bean1);
        memberSpeerBeanList.add(bean2);
        memberSpeerBeanList.add(bean3);
        memberSpeerAdapter.setNewData(memberSpeerBeanList);
        memberSpeerAdapter.setOnItemClickListener((adapter, view, position) -> {
            boolean isFlag = memberSpeerBeanList.get(position).isFlag();
            String member_id = memberSpeerBeanList.get(position).getMember_id();
            String member_price = memberSpeerBeanList.get(position).getMember_price();
            stotal_price = member_price;
            for (int i = 0; i < memberSpeerBeanList.size(); i++) {
                if (isFlag) {
                    memberSpeerBeanList.get(i).setFlag(false);
                    speerLessonMoney.setText("¥ " + lesson_price);
                } else {
                    if (i == position) {
                        memberSpeerBeanList.get(i).setFlag(true);
                    } else {
                        memberSpeerBeanList.get(i).setFlag(false);
                    }
                    speerLessonMoney.setText(member_price);
                }
                order_type = "6";
                adapter.notifyDataSetChanged();
            }

        });
        lessonTitle.setText(lesson_name);
        lessonPrice.setText("¥ " + lesson_price);
        stotal_price = lesson_price;
        speerLessonMoney.setText("¥ " + stotal_price);
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
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().cartLessonOrder(uidString, lesson_id, stotal_price, pay_code, order_type),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData data) {
                                    prompDialog.dismiss();
                                    Toast.makeText(SpeerOrderOtherActivity.this, "您提交了订单，请等待系统确认", Toast.LENGTH_SHORT).show();
                                    showPayDialog();
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
                if (order_type.equals("6")) {
                    UserInfoUtils.setUserType("6", SpeerOrderOtherActivity.this);
                } else {
                    Set<String> set = UserInfoUtils.getAppUserOrder(SpeerOrderOtherActivity.this);
                    if (set == null) {
                        set = new HashSet<>();
                    }
                    set.add(lesson_id);
                    UserInfoUtils.setAppUserOrder(set, SpeerOrderOtherActivity.this);
                }
                Intent videoIntent = new Intent(SpeerOrderOtherActivity.this, SPLessonVideosActivity.class);
                videoIntent.putExtra("lesson_status", true);
                videoIntent.putExtra("lesson_id", lesson_id);
                videoIntent.putExtra("lesson_name", lesson_name);
                videoIntent.putExtra("lesson_price", lesson_price);
                videoIntent.putExtra("lesson_smeta", lesson_smeta);
                videoIntent.putExtra("videoPath", thumbVideoString);
                startActivity(videoIntent);
            }
        });
        holderView.findViewById(R.id.morder_confirm_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent videoIntent = new Intent(SpeerOrderOtherActivity.this, SPLessonVideosActivity.class);
                videoIntent.putExtra("lesson_status", false);
                videoIntent.putExtra("lesson_id", lesson_id);
                videoIntent.putExtra("lesson_name", lesson_name);
                videoIntent.putExtra("lesson_price", lesson_price);
                videoIntent.putExtra("lesson_smeta", lesson_smeta);
                videoIntent.putExtra("videoPath", thumbVideoString);
                startActivity(videoIntent);
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
                intent.putExtra("url", "file:///android_asset/webpage/memaiagree.html");
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
                        if (actionString != null) {
                            finish();
                        } else {
                            purchaseHandler.sendEmptyMessage(0);
                        }

                    } else if (wechatpay) {
                        //立即支付

                        if (actionString != null) {
                            finish();
                        } else {
                            purchaseHandler.sendEmptyMessage(0);
//                            Intent videoIntent = new Intent(SpeerOrderActivity.this, SPLessonVideosActivity.class);
//                            videoIntent.putExtra("lesson_id", lesson_id);
//                            videoIntent.putExtra("lesson_name", lesson_name);
//                            videoIntent.putExtra("lesson_price", lesson_price);
//                            videoIntent.putExtra("lesson_smeta", lesson_smeta);
//                            videoIntent.putExtra("videoPath", thumbVideoString);
//                            startActivity(videoIntent);
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
    protected void onDestroy() {
        super.onDestroy();
    }

}
