package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.widget.ImageView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.caesar.rongcloudspeed.R;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.id;
import com.jrmf360.rylib.R.layout;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.c.h.a;
import com.jrmf360.rylib.common.http.callback.INetCallbackImpl;
import com.jrmf360.rylib.common.http.task.NetProcessTask;
import com.jrmf360.rylib.common.model.BaseModel;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.i;
import com.jrmf360.rylib.common.util.j;
import com.jrmf360.rylib.common.util.o;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.common.util.r;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.gridpwdview.GridPasswordView;
import com.jrmf360.rylib.rp.gridpwdview.GridPasswordView.OnPasswordChangedListener;
import com.jrmf360.rylib.rp.http.model.c;
import com.jrmf360.rylib.rp.http.model.e;
import com.jrmf360.rylib.rp.http.model.h;
import com.jrmf360.rylib.rp.ui.AddCardActivity;
import com.jrmf360.rylib.rp.ui.FindPwdByInfoActivity;
import com.jrmf360.rylib.rp.ui.RealNameActivity;
import com.jrmf360.rylib.rp.ui.RealNameActivity.AuthType;
import com.jrmf360.rylib.rp.ui.ResetPswdActivity;
import com.jrmf360.rylib.rp.ui.SettingPswdActivity;
import com.jrmf360.rylib.rp.widget.BasePayTypeCheckPopWindow;
import com.jrmf360.rylib.rp.widget.LimitDialog;
import com.jrmf360.rylib.rp.widget.PswdErrorDialog;
import com.jrmf360.rylib.rp.widget.BasePayTypeCheckPopWindow.OnClickListener;
import java.math.BigDecimal;

public class CaesarPayTypeActivity extends CaesarBaseActivity implements a {
    private boolean isVailPwd;
    private boolean hasBindCard;
    private ImageView iv_exit;
    private TextView tv_paytype_name;
    private TextView tv_pay_title;
    private TextView tv_redenvelope_name;
    private TextView tv_redenvelope_amount;
    private LinearLayout layout_paytype;
    private ImageView iv_paytype_icon;
    private Button btn_pay;
    private BasePayTypeCheckPopWindow menuWindow;
    private h redEnvelopeModel;
    private int payTypeTag;
    private int envelopesType;
    private double envelopesNum;
    private double balanceNum;
    private double envelopesAmount;
    private GridPasswordView gpv_pswd;
    private TextView tv_pswd_tips;
    private TextView tv_forget_pswd;
    private boolean jdCodeAlreadySend = false;
    private CaesarPayTypeActivity.MyCount mc;
    private View rootView;
    private String tradeId;
    private String lastPayCardNum;
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
                        CaesarPayTypeActivity.this.setResult(-1);
                        CaesarPayTypeActivity.this.finish();
                    } else if (TextUtils.equals(var4, "8000")) {
                        ToastUtil.showToast(CaesarPayTypeActivity.this, CaesarPayTypeActivity.this.getString(R.string.pay_waiting));
                    } else if ("6001".equals(var4)) {
                        ToastUtil.showToast(CaesarPayTypeActivity.this.context, CaesarPayTypeActivity.this.getString(R.string.jrmf_rp_pay_quit));
                    } else {
                        Toast.makeText(CaesarPayTypeActivity.this, CaesarPayTypeActivity.this.getString(R.string.pay_failure), Toast.LENGTH_SHORT).show();
                    }
                default:
            }
        }
    };

    public CaesarPayTypeActivity() {
    }

    public int getLayoutId() {
        return R.layout.caesar_activity_pay_type;
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
                CaesarPayTypeActivity.this.exePay();
                i.b(CaesarPayTypeActivity.this);
            }
        });
    }

    protected void initData(Bundle var1) {
        this.isVailPwd = this.getIntent().getBooleanExtra("isVailPwd", false);
        this.redEnvelopeModel = (h)this.getIntent().getSerializableExtra("temp");
        this.balanceNum = q.i(this.redEnvelopeModel.balance);
        this.envelopesType = this.getIntent().getIntExtra("envelopestype", -1);
        this.envelopesNum = q.i(this.getIntent().getStringExtra("number"));
        this.tv_redenvelope_name.setText(this.getIntent().getStringExtra("rp_name"));
        this.envelopesAmount = q.i(this.getIntent().getStringExtra("amount"));
        if (this.redEnvelopeModel.myBankcards != null && this.redEnvelopeModel.myBankcards.size() > 0) {
            this.hasBindCard = true;
        }

        if (this.envelopesType == 2) {
            this.tv_redenvelope_amount.setText(q.h(String.valueOf(this.envelopesAmount)));
        } else if (this.envelopesType == 1) {
            this.tv_redenvelope_amount.setText(q.h(String.valueOf(this.envelopesAmount)));
        } else if (this.envelopesType == 0) {
            BigDecimal var2 = new BigDecimal(this.envelopesAmount * this.envelopesNum);
            var2 = var2.setScale(2, 5);
            this.tv_redenvelope_amount.setText(var2 + "");
        }

        this.setPayTypeView(this.getDefaultPayType(), 0);
    }

    private void setPayTypeView(int var1, int var2) {
        this.rootView.setVisibility(View.VISIBLE);
        this.btn_pay.setTag((Object)null);
        Drawable var3 = null;
        String var4 = "";
        if (this.isVailPwd && var1 != 2 && var1 != 3) {
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

        this.gpv_pswd.setPasswordVisibility(false);
        if (var1 == 0) {
            var3 = this.getResources().getDrawable(drawable._ic_charge);
            this.tv_pswd_tips.setText(this.getString(string.input_pwd));
            this.tv_forget_pswd.setClickable(true);
            this.tv_forget_pswd.setText(this.getString(string.forget_pwd));
            if (this.isVailPwd) {
                this.tv_pay_title.setText(this.getString(string.input_pwd));
            } else {
                this.tv_pay_title.setText(this.getString(string.please_pay));
            }

            this.iv_paytype_icon.setImageDrawable(var3);
            this.tv_paytype_name.setText(String.format(this.getString(string.jrmf_rp_balance), this.redEnvelopeModel.balance));
        } else {
            if (var1 == 5) {
                this.gpv_pswd.setPasswordVisibility(true);
                if (q.a(this.lastPayCardNum)) {
                    this.btn_pay.setTag(((com.jrmf360.rylib.rp.http.model.a)this.redEnvelopeModel.myBankcards.get(var2)).bankCardNo);
                } else {
                    for(int var5 = 0; var5 < this.redEnvelopeModel.myBankcards.size(); ++var5) {
                        if (this.lastPayCardNum.equals(((com.jrmf360.rylib.rp.http.model.a)this.redEnvelopeModel.myBankcards.get(var5)).bankCardNo)) {
                            var2 = var5;
                            break;
                        }
                    }

                    this.btn_pay.setTag(this.lastPayCardNum);
                }

                com.jrmf360.rylib.common.util.h.a().a(this.iv_paytype_icon, ((com.jrmf360.rylib.rp.http.model.a)this.redEnvelopeModel.myBankcards.get(var2)).logo_url);
                if (j.b()) {
                    var4 = ((com.jrmf360.rylib.rp.http.model.a)this.redEnvelopeModel.myBankcards.get(var2)).enName + "(" + ((com.jrmf360.rylib.rp.http.model.a)this.redEnvelopeModel.myBankcards.get(var2)).bankCardNoDesc + ")";
                } else {
                    var4 = ((com.jrmf360.rylib.rp.http.model.a)this.redEnvelopeModel.myBankcards.get(var2)).bankName + "(" + ((com.jrmf360.rylib.rp.http.model.a)this.redEnvelopeModel.myBankcards.get(var2)).bankCardNoDesc + ")";
                }

                String var6 = ((com.jrmf360.rylib.rp.http.model.a)this.redEnvelopeModel.myBankcards.get(var2)).mobileNo;
                var6 = var6.substring(0, 3) + "****" + var6.substring(7, 11);
                this.tv_pswd_tips.setText(String.format(this.getString(string.input_verify_code), var6));
                this.tv_forget_pswd.setText(this.getString(string.send_code));
                if (!this.jdCodeAlreadySend) {
                    this.jdsendCode();
                }

                this.tv_pay_title.setText(this.getString(string.input_verify_code_title));
            } else if (var1 == 4) {
                var3 = this.getResources().getDrawable(drawable._ic_card);
                var4 = this.getString(string.add_card_pay);
                this.tv_pswd_tips.setText(this.getString(string.input_pwd));
                this.tv_forget_pswd.setClickable(true);
                this.tv_forget_pswd.setText(this.getString(string.forget_pwd));
                if (this.isVailPwd) {
                    this.tv_pay_title.setText(this.getString(string.input_pwd));
                } else {
                    this.tv_pay_title.setText(this.getString(string.please_pay));
                }
            } else if (var1 == 2) {
                var3 = this.getResources().getDrawable(drawable._ic_alipay);
                var4 = this.getString(string.alipay);
                this.tv_pay_title.setText(this.getString(string.please_pay));
            } else if (var1 == 3) {
                var3 = this.getResources().getDrawable(drawable._ic_wx);
                var4 = this.getString(string.jrmf_we_chat_pay);
                this.tv_pay_title.setText(this.getString(string.please_pay));
            }

            if (var1 != 5) {
                this.iv_paytype_icon.setImageDrawable(var3);
            }

            this.tv_paytype_name.setText(var4);
        }
    }

    private int getDefaultPayType() {
        int var1 = o.a().b(this, "lastPayType", -1);
        if (var1 == 5) {
            this.lastPayCardNum = o.a().b(this, "lastCardNum", "");
            o.a().c(this);
            return this.payTypeTag = 5;
        } else {
            double var2 = q.i(this.tv_redenvelope_amount.getText().toString().trim());
            this.payTypeTag = 0;
            if (var2 <= this.balanceNum) {
                this.payTypeTag = 0;
            } else if (this.hasBindCard) {
                this.payTypeTag = 5;
            } else if ("1".equals(this.redEnvelopeModel.isSupportAliPay)) {
                this.payTypeTag = 2;
            } else if ("1".equals(this.redEnvelopeModel.isSupportWeChatPay)) {
                this.payTypeTag = 3;
            } else {
                this.payTypeTag = 4;
            }

            return this.payTypeTag;
        }
    }

    public void onClick(View var1) {
        if (var1 == this.iv_exit) {
            i.b(this);
            this.finish();
        } else if (var1 == this.layout_paytype) {
            this.showPayTypeViewDialog();
        } else if (var1 == this.btn_pay) {
            this.exePay();
        } else if (var1 == this.tv_forget_pswd) {
            if (this.payTypeTag != 0 && this.payTypeTag != 4) {
                if (this.payTypeTag == 5) {
                    this.jdsendCode();
                }
            } else if (this.hasBindCard) {
                ResetPswdActivity.intent(this);
            } else {
                FindPwdByInfoActivity.intent(this);
            }
        }

    }

    private void jdsendCode() {
        NetProcessTask var1 = new NetProcessTask(this, new INetCallbackImpl() {
            public Object doInBackground(int var1, Object... var2) {
                com.jrmf360.rylib.c.a.getInstance().dialogLoading(CaesarPayTypeActivity.this.context, CaesarPayTypeActivity.this.getString(string.waiting));
                return com.jrmf360.rylib.rp.http.a.a(CurrentUser.getUserId(), CaesarBaseActivity.rongCloudToken, CaesarPayTypeActivity.this.redEnvelopeModel.envelopeId, (String)CaesarPayTypeActivity.this.btn_pay.getTag(), CaesarPayTypeActivity.this.tradeId);
            }

            public void onPostExecute(int var1, Object var2) {
                if (!CaesarPayTypeActivity.this.context.isFinishing()) {
                    com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarPayTypeActivity.this.context);
                    if (var2 != null && var2 instanceof c) {
                        c var3 = (c)var2;
                        if (var3.isSuccess()) {
                            Toast.makeText(CaesarPayTypeActivity.this, CaesarPayTypeActivity.this.getString(string.verify_code_suss), Toast.LENGTH_LONG).show();
                            CaesarPayTypeActivity.this.jdCodeAlreadySend = true;
                            CaesarPayTypeActivity.this.tradeId = var3.trade_id;
                            if (CaesarPayTypeActivity.this.mc != null) {
                                CaesarPayTypeActivity.this.mc.cancel();
                                CaesarPayTypeActivity.this.mc = null;
                            }

                            CaesarPayTypeActivity.this.mc = CaesarPayTypeActivity.this.new MyCount(60000L, 1000L);
                            CaesarPayTypeActivity.this.mc.start();
                        } else {
                            Toast.makeText(CaesarPayTypeActivity.this, var3.respmsg, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(CaesarPayTypeActivity.this, CaesarPayTypeActivity.this.getString(string.network_error), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, (Dialog)null);
        r.a(var1, new Object[0]);
    }

    private void exePay() {
        if (this.payTypeTag == 0) {
            if (this.isVailPwd) {
                this.payCaesarMFKJ();
            } else if (this.redEnvelopeModel.isAuthentication == 1) {
                SettingPswdActivity.intentAuth(this.context);
            } else if (this.redEnvelopeModel.isComplete == 1) {
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

    private void payCaesarMFKJ() {
        CaesarPayTypeActivity.this.setResult(-1);
        CaesarPayTypeActivity.this.finish();
    }

    private void payMFKJ() {
        com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.waiting), false);
        NetProcessTask var1 = new NetProcessTask(this, new INetCallbackImpl() {
            public Object doInBackground(int var1, Object... var2) {
                int var3 = CaesarPayTypeActivity.this.payTypeTag;
                String var4 = CaesarPayTypeActivity.this.redEnvelopeModel.envelopeId;
                String var5 = (String)CaesarPayTypeActivity.this.btn_pay.getTag();
                return com.jrmf360.rylib.rp.http.a.a(CurrentUser.getUserId(), CaesarBaseActivity.rongCloudToken, var3, var4, var5, CaesarPayTypeActivity.this.gpv_pswd.getPassWord(), CaesarPayTypeActivity.this.tradeId);
            }

            public void onPostExecute(int var1, Object var2) {
                if (!CaesarPayTypeActivity.this.context.isFinishing()) {
                    com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarPayTypeActivity.this.context);
                    if (var2 != null && var2 instanceof BaseModel) {
                        e var3 = (e)var2;
                        if (var3.isSuccess()) {
                            if (CaesarPayTypeActivity.this.payTypeTag == 4) {
                                if (var3.hasCompInfo != null && var3.hasCompInfo.equals("1")) {
                                    CaesarPayTypeActivity.this.startActivity((new Intent(CaesarPayTypeActivity.this, AddCardActivity.class)).putExtra("realname", var3.realName));
                                } else {
                                    RealNameActivity.intent(CaesarPayTypeActivity.this.context, AuthType.BIND_CARD);
                                }

                                CaesarPayTypeActivity.this.finish();
                            } else if (CaesarPayTypeActivity.this.payTypeTag == 2) {
                                CaesarPayTypeActivity.this.alipay(var3.paramStr);
                            } else if (CaesarPayTypeActivity.this.payTypeTag != 3) {
                                CaesarPayTypeActivity.this.setResult(-1);
                                CaesarPayTypeActivity.this.finish();
                            }
                        } else if (var3.respstat != null && var3.respstat.equals("R0012")) {
                            PswdErrorDialog var5 = new PswdErrorDialog(CaesarPayTypeActivity.this, CaesarPayTypeActivity.this.hasBindCard, var3.respmsg);
                            var5.show();
                            CaesarPayTypeActivity.this.gpv_pswd.clearPassword();
                        } else if (var3.respstat != null && var3.respstat.equals("R0015")) {
                            LimitDialog var4 = new LimitDialog(CaesarPayTypeActivity.this, var3.limitMoney);
                            var4.show();
                            CaesarPayTypeActivity.this.gpv_pswd.clearPassword();
                        } else if (var3.respstat != null && var3.respstat.equals("TP010")) {
                            ToastUtil.showToast(CaesarPayTypeActivity.this, var3.respmsg);
                        } else {
                            ToastUtil.showToast(CaesarPayTypeActivity.this, var3.respmsg);
                        }
                    } else {
                        ToastUtil.showToast(CaesarPayTypeActivity.this, CaesarPayTypeActivity.this.getString(string.network_error));
                    }

                }
            }
        }, (Dialog)null);
        r.a(var1, new Object[0]);
    }

    private void showPayTypeViewDialog() {
        i.b(this);
        this.createMenuWindow();
        this.menuWindow.setOnClickListener(new OnClickListener() {
            public void onClick(int var1, int var2) {
                CaesarPayTypeActivity.this.payTypeTag = var1;
                CaesarPayTypeActivity.this.setPayTypeView(var1, var2);
            }

            public void onDismisss() {
                CaesarPayTypeActivity.this.rootView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createMenuWindow() {
        boolean var1 = false;
        if (this.envelopesType == 2) {
            var1 = this.balanceNum >= this.envelopesAmount;
        } else if (this.envelopesType == 1) {
            var1 = this.balanceNum >= this.envelopesAmount;
        } else if (this.envelopesType == 0) {
            var1 = this.balanceNum >= this.envelopesAmount * this.envelopesNum;
        }

        if (this.menuWindow == null) {
            this.menuWindow = new BasePayTypeCheckPopWindow(this, this.redEnvelopeModel, this.redEnvelopeModel.balance, var1);
        }

        this.rootView.setVisibility(View.INVISIBLE);
        this.menuWindow.showAtLocation(this.rootView, 17, 0, 0);
    }

    public boolean onTouchEvent(MotionEvent var1) {
        return true;
    }

    public boolean onKeyDown(int var1, KeyEvent var2) {
        if (var1 == 4 && var2.getAction() == 0) {
            if (this.menuWindow != null && this.menuWindow.isShowing()) {
                this.menuWindow.dismiss();
            }
        } else {
            this.finish();
        }

        return super.onKeyDown(var1, var2);
    }

    public void alipay(final String var1) {
        Runnable var2 = new Runnable() {
            public void run() {
                PayTask var1x = new PayTask(CaesarPayTypeActivity.this);
                String var2 = var1x.pay(var1, true);
                Message var3 = new Message();
                var3.what = 1;
                var3.obj = var2;
                CaesarPayTypeActivity.this.mHandler.sendMessage(var3);
            }
        };
        Thread var3 = new Thread(var2);
        var3.start();
    }

    public void finishWithResult() {
        this.setResult(-1);
        this.finish();
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
            if (CaesarPayTypeActivity.this.payTypeTag == 5) {
                CaesarPayTypeActivity.this.tv_forget_pswd.setText(String.format(CaesarPayTypeActivity.this.getString(string.jrmf_wait_resend_code), var1 / 1000L));
                CaesarPayTypeActivity.this.tv_forget_pswd.setClickable(false);
            }

        }

        public void onFinish() {
            if (CaesarPayTypeActivity.this.payTypeTag == 5) {
                CaesarPayTypeActivity.this.tv_forget_pswd.setText(CaesarPayTypeActivity.this.getString(string.re_send_code));
                CaesarPayTypeActivity.this.tv_forget_pswd.setClickable(true);
            }

        }
    }
}

