package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.common.http.callback.INetCallbackImpl;
import com.jrmf360.rylib.common.http.task.NetProcessTask;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.f;
import com.jrmf360.rylib.common.util.i;
import com.jrmf360.rylib.common.util.m;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.common.util.r;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.http.model.h;
import com.jrmf360.rylib.rp.ui.BaseActivity;
import com.jrmf360.rylib.rp.ui.SettingPswdActivity;
import com.jrmf360.rylib.rp.widget.ActionBarView;
import com.jrmf360.rylib.wallet.e.a;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import java.math.BigDecimal;
import java.net.URLEncoder;

public class CaesarSendSingleEnvelopesActivity extends CaesarBaseSendActivity {
    private TextView pop_message;
    private LinearLayout ll_amount_layout;
    private TextView tv_amount;
    private Button btn_putin;

    public CaesarSendSingleEnvelopesActivity() {
    }

    public int getLayoutId() {
        return R.layout.caesar_activity_send_single_peak;
    }

    public void initView() {
        this.actionBarView = (ActionBarView)this.findViewById(R.id.actionbar);
        this.pop_message = (TextView)this.findViewById(R.id.pop_message);
        this.pop_message.getBackground().mutate().setAlpha(80);
        this.pop_message.setVisibility(View.INVISIBLE);
        this.ll_amount_layout = (LinearLayout)this.findViewById(R.id.ll_amount_layout);
        this.et_amount = (EditText)this.findViewById(R.id.et_amount);
        this.et_peak_message = (EditText)this.findViewById(R.id.et_peak_message);
        this.tv_amount = (TextView)this.findViewById(R.id.tv_amount);
        this.tv_amount.setText("0.00");
        this.btn_putin = (Button)this.findViewById(R.id.btn_putin);
        i.a(this.et_amount);
        f.a(this.btn_putin, false);
    }

    public void initListener() {
        this.actionBarView.getIvBack().setOnClickListener(this);
        this.et_amount.addTextChangedListener(new a() {
            public void afterTextChanged(Editable var1) {
                super.afterTextChanged(var1);
                CaesarSendSingleEnvelopesActivity.this.checkAmount();
            }
        });
        this.et_amount.setFilters(new InputFilter[]{new InputMoney()});
        this.btn_putin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {
                if (!m.a()) {
                    CaesarSendSingleEnvelopesActivity.this.requestInfo();
                }

            }
        });
    }

    protected void initData(Bundle var1) {
        super.initData(var1);
        this.envelopeName = this.getString(R.string.profile_start_send_redpacket);
        this.summary = this.getString(string._bribery_message);
        this.getRpInfo();
    }

    public void onClick(int var1) {
        if (var1 == R.id.iv_back) {
            i.b(this);
            this.finish();
        }

    }

    protected void onStart() {
        super.onStart();
        this.et_peak_message.addTextChangedListener(new a() {
            public void afterTextChanged(Editable var1) {
                super.afterTextChanged(var1);
                if (!TextUtils.isEmpty(var1.toString())) {
                    if (var1.toString().length() == 25) {
                        ToastUtil.showToast(CaesarSendSingleEnvelopesActivity.this, CaesarSendSingleEnvelopesActivity.this.getString(string.red_envelope_blessing));
                    }

                }
            }
        });
    }

    private void checkAmount() {
        String var1 = this.et_amount.getText().toString();
        if (!q.a(var1)) {
            if (!var1.startsWith(".")) {
                BigDecimal var2 = new BigDecimal(var1);
                double var3 = var2.doubleValue();
                if (var3 == 0.0D) {
                    this.closeTips();
                    this.ll_amount_layout.setBackgroundResource(drawable._bg_white_round);
                } else if (var3 < 0.009999999776482582D) {
                    this.showTips(this.getString(string.jrmf_rp_min_amount));
                    this.ll_amount_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                    f.a(this.btn_putin, false);
                } else if (var3 > q.i(this.maxLimitMoney)) {
                    this.showTips(String.format(this.getString(string.jrmf_rp_single_max_amount), this.maxLimitMoney));
                    this.ll_amount_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                    f.a(this.btn_putin, false);
                } else {
                    this.closeTips();
                    this.ll_amount_layout.setBackgroundResource(drawable._bg_white_round);
                    f.a(this.btn_putin, true);
                }

                if (var3 > 0.0D) {
                    var2 = var2.setScale(2, 4);
                    this.tv_amount.setText("" + var2);
                } else {
                    this.tv_amount.setText("0.00");
                }
            } else {
                this.showTips(this.getString(string.jrmf_rp_amount_error));
                this.ll_amount_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                f.a(this.btn_putin, false);
            }
        } else {
            this.closeTips();
            f.a(this.btn_putin, false);
            this.ll_amount_layout.setBackgroundResource(drawable._bg_white_round);
            this.tv_amount.setText("0.00");
        }

    }

    private void requestInfo() {
        h var3=new h();
        var3.balance= UserInfoUtils.getUserSum(CaesarSendSingleEnvelopesActivity.this);
        var3.isVailPwd="1";
        var3.bSetPwd="0";
        var3.isAuthentication=0;
        var3.isComplete=0;
        var3.envelopeId="1";
        String var = CaesarSendSingleEnvelopesActivity.this.et_peak_message.getText().toString().trim();
        CaesarSendSingleEnvelopesActivity.this.envelopeMessage = q.b(var) ? var : CaesarSendSingleEnvelopesActivity.this.et_peak_message.getHint().toString().toString();
        CaesarSendSingleEnvelopesActivity.this.jumpPayTypeActivity(var3, CaesarSendSingleEnvelopesActivity.this.et_amount.getText().toString().trim(), 2, true);
    }

    private void requestInfos() {
        i.b(this);
        final NetProcessTask var1 = new NetProcessTask(this, new INetCallbackImpl() {
            public Object doInBackground(int var1, Object... var2) {
                com.jrmf360.rylib.c.a.getInstance().dialogLoading(CaesarSendSingleEnvelopesActivity.this.context, CaesarSendSingleEnvelopesActivity.this.getString(string.waiting));
                String var3 = CaesarSendSingleEnvelopesActivity.this.et_peak_message.getText().toString().trim();
                CaesarSendSingleEnvelopesActivity.this.envelopeMessage = q.b(var3) ? var3 : CaesarSendSingleEnvelopesActivity.this.et_peak_message.getHint().toString().toString();
                String var4 = CaesarSendSingleEnvelopesActivity.this.getIntent().getStringExtra("TargetId");
                return com.jrmf360.rylib.rp.http.a.a(CurrentUser.getUserId(), BaseActivity.rongCloudToken, CaesarSendSingleEnvelopesActivity.this.tv_amount.getText().toString().trim(), CaesarSendSingleEnvelopesActivity.this.envelopeMessage, var4, CaesarSendSingleEnvelopesActivity.this.envelopeName);
            }

            public void onPostExecute(int var1, Object var2) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarSendSingleEnvelopesActivity.this.context);
                if (!CaesarSendSingleEnvelopesActivity.this.isFinishing()) {
                    if (var2 != null && var2 instanceof h) {
                        h var3 = (h)var2;
                        if (var3.isSuccess()) {
                            if ("1".equals(var3.isVailPwd)) {
                                CaesarSendSingleEnvelopesActivity.this.jumpPayTypeActivity(var3, CaesarSendSingleEnvelopesActivity.this.et_amount.getText().toString().trim(), 2, true);
                            } else if ("1".equals(var3.bSetPwd)) {
                                SettingPswdActivity.intentAuth(CaesarSendSingleEnvelopesActivity.this.context);
                            } else {
                                CaesarSendSingleEnvelopesActivity.this.jumpPayTypeActivity(var3, CaesarSendSingleEnvelopesActivity.this.et_amount.getText().toString().trim(), 2, false);
                            }
                        } else {
                            ToastUtil.showToast(CaesarSendSingleEnvelopesActivity.this.context, var3.respmsg);
                        }
                    } else {
                        ToastUtil.showToast(CaesarSendSingleEnvelopesActivity.this.context, CaesarSendSingleEnvelopesActivity.this.getString(string.network_error));
                    }

                }
            }
        }, (Dialog)null);
        if (q.a(rongCloudToken)) {
            if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                RongIMClient.getInstance().getVendorToken(new ResultCallback<String>() {
                    public void onSuccess(String var1x) {
                        try {
                            if (q.c(var1x)) {
                                BaseActivity.rongCloudToken = URLEncoder.encode(var1x, "UTF-8");
                                r.a(var1, new Object[0]);
                            }
                        } catch (Exception var3) {
                            BaseActivity.rongCloudToken = "";
                            var3.printStackTrace();
                        }

                    }

                    public void onError(ErrorCode var1x) {
                        LogUtil.i("获得token失败" + var1x);
                        BaseActivity.rongCloudToken = "";
                    }
                });
            } else {
                ToastUtil.showToast(this.context, this.getString(string.net_error_l));
            }
        } else {
            r.a(var1, new Object[0]);
        }

    }

    private void showTips(String var1) {
        this.pop_message.setText(var1);
        this.pop_message.setVisibility(View.VISIBLE);
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
            }
        }, 2000L);
    }

    private void closeTips() {
        this.pop_message.setText("");
        this.pop_message.setVisibility(View.INVISIBLE);
    }

    public void onBackPressed() {
        super.onBackPressed();
        i.b(this);
    }
}

