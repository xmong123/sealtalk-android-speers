package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.caesar.rongcloudspeed.R;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.c.h.a;
import com.jrmf360.rylib.common.http.ModelHttpCallBack;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.i;
import com.jrmf360.rylib.common.util.o;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.gridpwdview.GridPasswordView;
import com.jrmf360.rylib.rp.gridpwdview.GridPasswordView.OnPasswordChangedListener;
import com.jrmf360.rylib.rp.http.model.c;
import com.jrmf360.rylib.rp.http.model.e;
import com.jrmf360.rylib.rp.http.model.m;
import com.jrmf360.rylib.rp.ui.AddCardActivity;
import com.jrmf360.rylib.rp.ui.FindPwdByInfoActivity;
import com.jrmf360.rylib.rp.ui.RealNameActivity;
import com.jrmf360.rylib.rp.ui.RealNameActivity.AuthType;
import com.jrmf360.rylib.rp.ui.ResetPswdActivity;
import com.jrmf360.rylib.rp.ui.SettingPswdActivity;
import com.jrmf360.rylib.rp.widget.LimitDialog;
import com.jrmf360.rylib.rp.widget.PswdErrorDialog;
import com.jrmf360.rylib.rp.widget.TransPayTypeCheckPopWindow;
import com.jrmf360.rylib.rp.widget.TransPayTypeCheckPopWindow.OnClickListener;

public class CaesarTransPayActivity extends CaesarBaseActivity implements a {
    private boolean isVailPwd;
    private boolean hasBindCard;
    private TextView tv_pay_title;
    private ImageView iv_exit;
    private TextView tv_redenvelope_name;
    private TextView tv_redenvelope_amount;
    private LinearLayout layout_paytype;
    private ImageView iv_paytype_icon;
    private TextView tv_paytype_name;
    private Button btn_pay;
    private TransPayTypeCheckPopWindow menuWindow;
    private int payTypeTag;
    private double balanceNum;
    private GridPasswordView gpv_pswd;
    private TextView tv_pswd_tips;
    private TextView tv_forget_pswd;
    private String tradeId;
    private CaesarTransPayActivity.MyCount mc;
    private View rootView;
    private int lastCardIndex = 0;
    private m transModel;
    private String targetId;
    private String mAmount;
    private boolean firstSelctNewCard = true;
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler = new Handler() {
        public void handleMessage(Message var1) {
            switch(var1.what) {
                case 1:
                    com.jrmf360.rylib.b.a var2 = new com.jrmf360.rylib.b.a((String)var1.obj);
                    String var3 = var2.b();
                    String var4 = var2.a();
                    if (TextUtils.equals(var4, "9000")) {
                        CaesarTransPayActivity.this.enterFinishActivity();
                    } else if (TextUtils.equals(var4, "8000")) {
                        ToastUtil.showToast(CaesarTransPayActivity.this, CaesarTransPayActivity.this.getString(string.pay_waiting));
                    } else if ("6001".equals(var4)) {
                        ToastUtil.showToast(CaesarTransPayActivity.this.context, CaesarTransPayActivity.this.getString(string.jrmf_rp_pay_quit));
                    } else {
                        Toast.makeText(CaesarTransPayActivity.this, CaesarTransPayActivity.this.getString(string.pay_failure), Toast.LENGTH_SHORT).show();
                    }
                default:
            }
        }
    };

    public CaesarTransPayActivity() {
    }

    public static void intent(Activity var0, String var1, m var2, String var3) {
        Intent var4 = new Intent(var0, CaesarTransPayActivity.class);
        Bundle var5 = new Bundle();
        var5.putSerializable("TRANS_MODEL", var2);
        var5.putString("TargetId", var1);
        var5.putString("AMOUNT", var3);
        var4.putExtras(var5);
        var0.startActivity(var4);
    }

    public int getLayoutId() {
        return R.layout.jrmf_rp_activity_trans_pay;
    }

    public void initView() {
        this.rootView = this.findViewById(R.id.layout);
        this.iv_exit = (ImageView)this.findViewById(R.id.iv_exit);
        this.tv_redenvelope_name = (TextView)this.findViewById(R.id.tv_redenvelope_name);
        this.tv_redenvelope_amount = (TextView)this.findViewById(R.id.tv_redenvelope_amount);
        this.layout_paytype = (LinearLayout)this.findViewById(R.id.layout_paytype);
        this.iv_paytype_icon = (ImageView)this.findViewById(R.id.iv_paytype_icon);
        this.tv_paytype_name = (TextView)this.findViewById(R.id.tv_paytype_name);
        this.btn_pay = (Button)this.findViewById(R.id.btn_pay);
        this.gpv_pswd = (GridPasswordView)this.findViewById(R.id.gpv_pswd);
        this.tv_pswd_tips = (TextView)this.findViewById(R.id.tv_pswd_tips);
        this.tv_forget_pswd = (TextView)this.findViewById(R.id.tv_forget_pswd);
        this.tv_pay_title = (TextView)this.findViewById(R.id.tv_pay_title);
    }

    public void initListener() {
        this.iv_exit.setOnClickListener(this);
        this.layout_paytype.setOnClickListener(this);
        this.btn_pay.setOnClickListener(this);
        this.tv_forget_pswd.setOnClickListener(this);
        this.gpv_pswd.setOnPasswordChangedListener(new OnPasswordChangedListener() {
            public void onTextChanged(String var1) {
            }

            public void onInputFinish(String var1) {
                CaesarTransPayActivity.this.exePay();
                i.b(CaesarTransPayActivity.this);
            }
        });
    }

    protected void onNewIntent(Intent var1) {
        super.onNewIntent(var1);
        LogUtil.i("onNewIntent");
        this.initData(var1.getExtras());
    }

    protected void initData(Bundle var1) {
        if (var1 != null) {
            this.transModel = (m)var1.getSerializable("TRANS_MODEL");
            this.targetId = var1.getString("TargetId");
            this.isVailPwd = "1".equals(this.transModel.isVailPwd);
            this.balanceNum = q.i(this.transModel.balance);
//            if (this.transModel.myBankcards != null && this.transModel.myBankcards.size() > 0) {
                this.hasBindCard = true;
//            }

            this.mAmount = var1.getString("AMOUNT");
            this.tv_redenvelope_amount.setText(q.h(this.mAmount));
            String var2 = CurrentUser.getNameById(this.targetId);
            this.tv_redenvelope_name.setText(String.format(this.getString(string.jrmf_trans_to), q.o(var2)));
        }

        this.setPayTypeView(this.getDefaultPayType(), 0, false);
    }

    public void onClick(View var1) {
        if (var1 == this.iv_exit) {
            i.b(this.context);
            this.finish();
        } else if (var1 == this.layout_paytype) {
            this.showPayTypeViewDialog();
        } else if (var1 == this.btn_pay) {
            if (!com.jrmf360.rylib.common.util.m.a()) {
                if (this.payTypeTag == 5) {
                    this.setOldCardInputPwd();
                } else if (this.payTypeTag == 4) {
                    this.showNewCard();
                } else {
                    this.exePay();
                }
            }
        } else if (var1 == this.tv_forget_pswd) {
            if (this.payTypeTag != 0 && this.payTypeTag != 4) {
                if (this.payTypeTag == 5) {
                    this.jdsendCode();
                }
            } else if (this.payTypeTag == 0 || this.payTypeTag == 4) {
                if (this.hasBindCard) {
                    ResetPswdActivity.intent(this);
                } else {
                    FindPwdByInfoActivity.intent(this);
                }
            }
        }

    }

    private void toggleInputPwd(boolean var1) {
        if (var1) {
            this.btn_pay.setVisibility(View.GONE);
            this.gpv_pswd.setFocusable(true);
            this.gpv_pswd.setEnabled(true);
            this.gpv_pswd.setVisibility(View.VISIBLE);
            this.tv_pswd_tips.setVisibility(View.VISIBLE);
            this.tv_forget_pswd.setVisibility(View.VISIBLE);
            i.a(this.gpv_pswd.getEditText());
        } else {
            this.btn_pay.setVisibility(View.VISIBLE);
            this.gpv_pswd.setVisibility(View.GONE);
            this.gpv_pswd.setFocusable(false);
            this.gpv_pswd.setEnabled(false);
            this.tv_pswd_tips.setVisibility(View.GONE);
            this.tv_forget_pswd.setVisibility(View.GONE);
            i.b(this);
        }

    }

    private void setOldCardInputPwd() {
        this.toggleInputPwd(true);
//        String var1 = ((com.jrmf360.rylib.rp.http.model.a)this.transModel.myBankcards.get(this.lastCardIndex)).mobileNo;
//        var1 = var1.substring(0, 3) + "****" + var1.substring(7, 11);
//        this.tv_pswd_tips.setText(String.format(this.getString(string.input_verify_code), var1));
        this.tv_forget_pswd.setText(this.getString(string.send_code));
        this.jdsendCode();
        this.tv_pay_title.setText(this.getString(string.input_verify_code_title));
    }

    private void setPayTypeView(int var1, int var2, boolean var3) {
        this.rootView.setVisibility(View.VISIBLE);
        this.gpv_pswd.setPasswordVisibility(false);
        this.btn_pay.setTag((Object)null);
        Drawable var4 = null;
        String var5 = "";
        if (var1 == 5) {
            this.gpv_pswd.setPasswordVisibility(true);
            this.tv_pay_title.setText(this.getString(string.please_pay));
            this.toggleInputPwd(false);
            this.lastCardIndex = var2;
//            this.btn_pay.setTag(((com.jrmf360.rylib.rp.http.model.a)this.transModel.myBankcards.get(this.lastCardIndex)).bankCardNo);
            var5 =  "零钱 (" + "余额 ￥" +this.transModel.balance+ ")";
            this.tv_paytype_name.setText(var5);
//            h.a().a(this.iv_paytype_icon, ((com.jrmf360.rylib.rp.http.model.a)this.transModel.myBankcards.get(this.lastCardIndex)).logo_url);
        } else if (var1 == 4) {
            if (this.firstSelctNewCard && !var3) {
                this.toggleInputPwd(false);
                var4 = this.getResources().getDrawable(drawable.ic_card);
                var5 = this.getString(string.add_card_pay);
                this.iv_paytype_icon.setImageDrawable(var4);
                this.tv_paytype_name.setText(var5);
            } else {
                this.showNewCard();
            }

            this.firstSelctNewCard = false;
        } else {
            if (this.isVailPwd && var1 != 2 && var1 != 3) {
                this.toggleInputPwd(true);
            } else {
                this.toggleInputPwd(false);
            }

            if (var1 == 0) {
                var4 = this.getResources().getDrawable(drawable._ic_charge);
                var5 = String.format(this.getString(string.jrmf_rp_balance), this.transModel.balance);
                this.tv_pswd_tips.setText(this.getString(string.input_pwd));
                this.tv_forget_pswd.setClickable(true);
                this.tv_forget_pswd.setText(this.getString(string.forget_pwd));
                if (this.isVailPwd) {
                    this.tv_pay_title.setText(this.getString(string.input_pwd));
                } else {
                    this.tv_pay_title.setText(this.getString(string.please_pay));
                }
            } else if (var1 == 3) {
                var4 = this.getResources().getDrawable(drawable._ic_wx);
                var5 = this.getString(string.jrmf_we_chat_pay);
            } else if (var1 == 2) {
                var4 = this.getResources().getDrawable(drawable._ic_alipay);
                var5 = this.getString(string.alipay);
                this.btn_pay.setVisibility(View.VISIBLE);
                this.gpv_pswd.setVisibility(View.GONE);
                this.tv_pswd_tips.setVisibility(View.GONE);
                this.tv_forget_pswd.setVisibility(View.GONE);
                this.tv_pay_title.setText(this.getString(string.please_pay));
            }

            this.iv_paytype_icon.setImageDrawable(var4);
            this.tv_paytype_name.setText(var5);
        }
    }

    private void showNewCard() {
        if (this.transModel.isAuthentication == 1) {
            this.startActivity((new Intent(this, AddCardActivity.class)).putExtra("realname", this.transModel.realName));
            this.finish();
        } else if (this.transModel.isComplete == 1) {
            if (this.transModel.hasPwd == 1) {
                this.toggleInputPwd(true);
                Drawable var1 = this.getResources().getDrawable(drawable.ic_card);
                this.iv_paytype_icon.setImageDrawable(var1);
                String var2 = this.getString(string.add_card_pay);
                this.tv_paytype_name.setText(var2);
                this.tv_pswd_tips.setText(this.getString(string.input_pwd));
                this.tv_forget_pswd.setClickable(true);
                this.tv_forget_pswd.setText(this.getString(string.forget_pwd));
                this.tv_pay_title.setText(this.getString(string.input_pwd));
            } else {
                this.startActivity((new Intent(this, AddCardActivity.class)).putExtra("realname", this.transModel.realName));
                this.finish();
            }
        } else {
            RealNameActivity.intent(this.context, AuthType.BIND_CARD);
            this.finish();
        }

    }

    private int getDefaultPayType() {
        int var1 = o.a().b(this, "lastPayType", -1);
        if (var1 == 5) {
            o.a().c(this);
            return this.payTypeTag = 5;
        } else {
            double var2 = q.i(this.tv_redenvelope_amount.getText().toString().trim());
            this.payTypeTag = 0;
            if (var2 <= this.balanceNum) {
                this.payTypeTag = 0;
            } else if (this.hasBindCard) {
                this.payTypeTag = 5;
            } else if ("1".equals(this.transModel.isSupportAliPay)) {
                this.payTypeTag = 2;
            } else if ("1".equals(this.transModel.isSupportWeChatPay)) {
                this.payTypeTag = 3;
            } else {
                this.payTypeTag = 4;
            }

            return this.payTypeTag;
        }
    }

    private void jdsendCode() {
        i.b(this);
        com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.waiting));
        com.jrmf360.rylib.rp.http.a.b(CurrentUser.getUserId(), rongCloudToken, this.transModel.transferOrderNo, (String)this.btn_pay.getTag(), this.tradeId, new ModelHttpCallBack<c>() {
            public void onSuccess(c var1) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransPayActivity.this.context);
                if (var1.isSuccess()) {
                    Toast.makeText(CaesarTransPayActivity.this, CaesarTransPayActivity.this.getString(string.verify_code_suss), Toast.LENGTH_LONG).show();
                    CaesarTransPayActivity.this.tradeId = var1.trade_id;
                    if (CaesarTransPayActivity.this.mc != null) {
                        CaesarTransPayActivity.this.mc.cancel();
                        CaesarTransPayActivity.this.mc = null;
                    }

                    CaesarTransPayActivity.this.mc = CaesarTransPayActivity.this.new MyCount(60000L, 1000L);
                    CaesarTransPayActivity.this.mc.start();
                } else {
                    ToastUtil.showToast(CaesarTransPayActivity.this.context, var1.respmsg);
                }

            }

            public void onFail(String var1) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransPayActivity.this.context);
                ToastUtil.showToast(CaesarTransPayActivity.this.context, CaesarTransPayActivity.this.getString(string.net_error_l));
            }
        });
    }

    private void exePay() {
        if (this.payTypeTag == 0) {
            if (this.isVailPwd) {
                this.payCaesarMFKJ();
            } else if (this.transModel.isAuthentication == 1) {
                SettingPswdActivity.intentAuth(this.context);
            } else if (this.transModel.isComplete == 1) {
                SettingPswdActivity.intentComplete(this.context);
            } else {
                this.showDialog();
            }
        } else {
            this.payMFKJ();
        }

    }

    public void showDialog() {
        this.rootView.setVisibility(View.INVISIBLE);
        com.jrmf360.rylib.c.h var1 = com.jrmf360.rylib.c.a.getInstance().dialogLeftAndRight(this, this.getString(string.jrmf_rp_identity_table), this.getString(string.jrmf_rp_identity_left), this.getString(string.jrmf_rp_identity_right), this);
        var1.show(this.getSupportFragmentManager(), this.getClass().getSimpleName());
    }

    public void onLeft() {
        this.finish();
    }

    public void onRight() {
        RealNameActivity.intent(this.context, AuthType.FAKE_AUTH);
        this.finish();
    }

    private void payMFKJ() {
        com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.waiting), false);
        int var1 = this.payTypeTag;
        String var2 = (String)this.btn_pay.getTag();
        com.jrmf360.rylib.rp.http.a.a(CurrentUser.getUserId(), rongCloudToken, var1, this.transModel.transferOrderNo, var2, this.gpv_pswd.getPassWord(), this.tradeId, new ModelHttpCallBack<e>() {
            public void onSuccess(e var1) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransPayActivity.this.context);
                CaesarTransPayActivity.this.gpv_pswd.clearPassword();
                if (var1.isSuccess()) {
                    if (CaesarTransPayActivity.this.payTypeTag == 4) {
                        if (var1.hasCompInfo != null && var1.hasCompInfo.equals("1")) {
                            CaesarTransPayActivity.this.startActivity((new Intent(CaesarTransPayActivity.this, AddCardActivity.class)).putExtra("realname", var1.realName));
                        } else {
                            RealNameActivity.intent(CaesarTransPayActivity.this.context, AuthType.BIND_CARD);
                        }

                        CaesarTransPayActivity.this.finish();
                    } else if (CaesarTransPayActivity.this.payTypeTag == 2) {
                        CaesarTransPayActivity.this.alipay(var1.paramStr);
                    } else if (CaesarTransPayActivity.this.payTypeTag != 3) {
                        CaesarTransPayActivity.this.enterFinishActivity();
                    }
                } else if (var1.respstat != null && var1.respstat.equals("R0012")) {
                    PswdErrorDialog var3 = new PswdErrorDialog(CaesarTransPayActivity.this, CaesarTransPayActivity.this.hasBindCard, var1.respmsg);
                    var3.show();
                    CaesarTransPayActivity.this.gpv_pswd.clearPassword();
                } else if (var1.respstat != null && var1.respstat.equals("R0015")) {
                    LimitDialog var2 = new LimitDialog(CaesarTransPayActivity.this, var1.limitMoney);
                    var2.show();
                    CaesarTransPayActivity.this.gpv_pswd.clearPassword();
                } else if (var1.respstat != null && var1.respstat.equals("TP010")) {
                    ToastUtil.showToast(CaesarTransPayActivity.this, var1.respmsg);
                } else {
                    ToastUtil.showToast(CaesarTransPayActivity.this, var1.respmsg);
                }

            }

            public void onFail(String var1) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransPayActivity.this.context);
                ToastUtil.showToast(CaesarTransPayActivity.this, CaesarTransPayActivity.this.getString(string.network_error));
            }
        });
    }

    private void payCaesarMFKJ() {
        CaesarTransPayActivity.this.enterFinishActivity();
    }

    private void showPayTypeViewDialog() {
        i.b(this);
        this.createMenuWindow();
        this.menuWindow.setOnClickListener(new OnClickListener() {
            public void onClick(int var1, int var2) {
                CaesarTransPayActivity.this.payTypeTag = var1;
                CaesarTransPayActivity.this.setPayTypeView(var1, var2, true);
            }

            public void onDismisss() {
                CaesarTransPayActivity.this.rootView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createMenuWindow() {
        boolean var1 = q.i(this.transModel.balance) >= q.i(this.tv_redenvelope_amount.getText().toString().trim());
        if (this.menuWindow == null) {
            this.menuWindow = new TransPayTypeCheckPopWindow(this, this.transModel, this.transModel.balance, var1);
        }

        this.rootView.setVisibility(View.INVISIBLE);
        this.menuWindow.showAtLocation(this.rootView, 17, 0, 0);
    }

    public void alipay(final String var1) {
        Runnable var2 = new Runnable() {
            public void run() {
                PayTask var1x = new PayTask(CaesarTransPayActivity.this);
                String var2 = var1x.pay(var1, true);
                Message var3 = new Message();
                var3.what = 1;
                var3.obj = var2;
                CaesarTransPayActivity.this.mHandler.sendMessage(var3);
            }
        };
        Thread var3 = new Thread(var2);
        var3.start();
    }

    public void enterFinishActivity() {
        CaesarTransSuccActivity.intent(this.context, CurrentUser.getNameById(this.targetId), this.mAmount);
        this.finish();
    }

    public void onBackPressed() {
        if (this.menuWindow != null && this.menuWindow.isShowing()) {
            this.menuWindow.dismiss();
        }

        super.onBackPressed();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mc != null) {
            this.mc.cancel();
            this.mc = null;
        }

    }

    public class MyCount extends CountDownTimer {
        public MyCount(long var2, long var4) {
            super(var2, var4);
        }

        public void onTick(long var1) {
            if (CaesarTransPayActivity.this.payTypeTag == 5) {
                CaesarTransPayActivity.this.tv_forget_pswd.setText(String.format(CaesarTransPayActivity.this.getString(string.jrmf_wait_resend_code), var1 / 1000L));
                CaesarTransPayActivity.this.tv_forget_pswd.setClickable(false);
            }

        }

        public void onFinish() {
            if (CaesarTransPayActivity.this.payTypeTag == 5) {
                CaesarTransPayActivity.this.tv_forget_pswd.setText(CaesarTransPayActivity.this.getString(string.re_send_code));
                CaesarTransPayActivity.this.tv_forget_pswd.setClickable(true);
            }

        }
    }
}

