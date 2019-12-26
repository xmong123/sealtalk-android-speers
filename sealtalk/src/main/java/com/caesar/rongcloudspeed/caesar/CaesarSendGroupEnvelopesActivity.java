package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jrmf360.rylib.R.color;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.id;
import com.jrmf360.rylib.R.layout;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.common.http.callback.INetCallbackImpl;
import com.jrmf360.rylib.common.http.task.NetProcessTask;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.f;
import com.jrmf360.rylib.common.util.i;
import com.jrmf360.rylib.common.util.j;
import com.jrmf360.rylib.common.util.m;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.common.util.r;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.http.model.h;
import com.jrmf360.rylib.rp.ui.SettingPswdActivity;
import com.jrmf360.rylib.rp.widget.ActionBarView;
import com.jrmf360.rylib.rp.widget.NoUnderClickableSpan;
import com.jrmf360.rylib.wallet.e.a;
import io.rong.imkit.RongIM.IGroupMemberCallback;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.UserInfo;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

public class CaesarSendGroupEnvelopesActivity extends CaesarBaseSendActivity {
    String TargetId = null;
    private int ENVELOPES_TYPE = 1;
    private TextView pop_message;
    private LinearLayout ll_peak_num_layout;
    private EditText et_peak_num;
    private TextView tv_group_member_num;
    private LinearLayout ll_peak_amount_layout;
    private TextView tv_peak_amount_icon;
    private TextView tv_peak_type;
    private TextView tv_amount_for_show;
    private Button btn_putin;
    private TextView tv_ge;
    private NoUnderClickableSpan peakTypeClick = new NoUnderClickableSpan() {
        public void onClick(View var1) {
            if (CaesarSendGroupEnvelopesActivity.this.ENVELOPES_TYPE == 1) {
                CaesarSendGroupEnvelopesActivity.this.ENVELOPES_TYPE = 0;
            } else if (CaesarSendGroupEnvelopesActivity.this.ENVELOPES_TYPE == 0) {
                CaesarSendGroupEnvelopesActivity.this.ENVELOPES_TYPE = 1;
            }

            CaesarSendGroupEnvelopesActivity.this.setPeakTypeStyle();
            CaesarSendGroupEnvelopesActivity.this.checkType();
        }
    };
    private int NUMBER = -1;
    private double AMOUNTMONEY = -1.0D;

    public CaesarSendGroupEnvelopesActivity() {
    }

    public int getLayoutId() {
        return layout._activity_send_group_peak;
    }

    public void initView() {
        this.actionBarView = (ActionBarView)this.findViewById(id.actionbar);
        this.pop_message = (TextView)this.findViewById(id.pop_message);
        this.ll_peak_num_layout = (LinearLayout)this.findViewById(id.ll_peak_num_layout);
        this.et_peak_num = (EditText)this.findViewById(id.et_peak_num);
        this.et_peak_num.setFocusable(true);
        this.et_peak_num.setFocusableInTouchMode(true);
        this.et_peak_num.requestFocus();
        this.et_peak_num.setSelection(this.et_peak_num.getText().length());
        this.et_peak_num.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        this.tv_group_member_num = (TextView)this.findViewById(id.tv_group_member_num);
        this.ll_peak_amount_layout = (LinearLayout)this.findViewById(id.ll_peak_amount_layout);
        this.tv_peak_amount_icon = (TextView)this.findViewById(id.tv_peak_amount_icon);
        this.et_amount = (EditText)this.findViewById(id.et_peak_amount);
        this.tv_peak_type = (TextView)this.findViewById(id.tv_peak_type);
        this.tv_ge = (TextView)this.findViewById(id.tv_ge);
        if (j.b()) {
            this.tv_ge.setVisibility(View.GONE);
        }

        this.et_peak_message = (EditText)this.findViewById(id.et_peak_message);
        this.tv_amount_for_show = (TextView)this.findViewById(id.tv_amount_for_show);
        this.btn_putin = (Button)this.findViewById(id.btn_putin);
        i.a(this.et_peak_num);
        this.setDefaultView();
    }

    public void initListener() {
        this.actionBarView.getIvBack().setOnClickListener(this);
        this.et_peak_num.addTextChangedListener(new a() {
            public void afterTextChanged(Editable var1) {
                CaesarSendGroupEnvelopesActivity.this.NUMBER = CaesarSendGroupEnvelopesActivity.this.checkNum();
                if (CaesarSendGroupEnvelopesActivity.this.NUMBER != -1) {
                    CaesarSendGroupEnvelopesActivity.this.AMOUNTMONEY = CaesarSendGroupEnvelopesActivity.this.checkAmount();
                }

                CaesarSendGroupEnvelopesActivity.this.checkForAllAmount();
                CaesarSendGroupEnvelopesActivity.this.checkForButton();
            }
        });
        this.et_amount.addTextChangedListener(new a() {
            public void afterTextChanged(Editable var1) {
                CaesarSendGroupEnvelopesActivity.this.NUMBER = CaesarSendGroupEnvelopesActivity.this.checkNum();
                if (CaesarSendGroupEnvelopesActivity.this.NUMBER != -1) {
                    CaesarSendGroupEnvelopesActivity.this.AMOUNTMONEY = CaesarSendGroupEnvelopesActivity.this.checkAmount();
                }

                CaesarSendGroupEnvelopesActivity.this.checkForAllAmount();
                CaesarSendGroupEnvelopesActivity.this.checkForButton();
            }
        });
        this.et_amount.setFilters(new InputFilter[]{new InputMoney()});
        this.btn_putin.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                if (!m.a()) {
                    CaesarSendGroupEnvelopesActivity.this.requestInfo();
                }

            }
        });
    }

    protected void initData(Bundle var1) {
        this.envelopeName = this.getString(string.jrmf_rp_default_name);
        this.summary = this.getString(string._bribery_message);
        this.TargetId = this.getIntent().getStringExtra("TargetId");
        this.getRpInfo();
        this.rongCloundNumber();
    }

    public void onClick(int var1) {
        if (var1 == id.iv_back) {
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
                        ToastUtil.showToast(CaesarSendGroupEnvelopesActivity.this.getApplicationContext(), CaesarSendGroupEnvelopesActivity.this.getString(string.red_envelope_blessing));
                    }

                }
            }
        });
    }

    private void rongCloundNumber() {
        if (RongMentionManager.getInstance().getGroupMembersProvider() != null) {
            RongMentionManager.getInstance().getGroupMembersProvider().getGroupMembers(this.TargetId, new IGroupMemberCallback() {
                public void onGetGroupMembersResult(List<UserInfo> var1) {
                    if (var1 != null && var1.size() > 0) {
                        CaesarSendGroupEnvelopesActivity.this.tv_group_member_num.setVisibility(View.GONE);
                        String var2 = CaesarSendGroupEnvelopesActivity.this.getResources().getString(string.group_number);
                        String var3 = String.format(var2, var1.size());
                        CaesarSendGroupEnvelopesActivity.this.tv_group_member_num.setText(var3);
                    }

                }
            });
        }

    }

    private void setDefaultView() {
        this.pop_message.setVisibility(View.INVISIBLE);
        this.pop_message.getBackground().mutate().setAlpha(80);
        this.setPeakTypeStyle();
        this.tv_amount_for_show.setText("0.00");
        f.a(this.btn_putin, false);
    }

    private void setPeakTypeStyle() {
        String var1 = "";
        if (this.ENVELOPES_TYPE == 1) {
            f.a(this.getResources().getDrawable(drawable._ic_pin), this.tv_peak_amount_icon);
            this.tv_peak_amount_icon.setText(this.getString(string.jrmf_rp_all_amount));
            var1 = this.getString(string.jrmf_rp_luck_to_normal);
        } else if (this.ENVELOPES_TYPE == 0) {
            f.a((Drawable)null, this.tv_peak_amount_icon);
            this.tv_peak_amount_icon.setText(this.getString(string.jrmf_rp_single_amount));
            var1 = this.getString(string.jrmf_rp_normal_to_luck);
        }

        int var2 = var1.indexOf("，") + 1;
        if (var2 >= 1) {
            SpannableString var3 = new SpannableString(var1);
            var3.setSpan(this.peakTypeClick, var2, var1.length(), 33);
            var3.setSpan(new ForegroundColorSpan(this.getResources().getColor(color.blue)), var2, var1.length(), 33);
            this.tv_peak_type.setText(var3);
            this.tv_peak_type.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void requestInfo() {
        final NetProcessTask var1 = new NetProcessTask(this, new INetCallbackImpl() {
            public Object doInBackground(int var1, Object... var2) {
                com.jrmf360.rylib.c.a.getInstance().dialogLoading(CaesarSendGroupEnvelopesActivity.this.context, CaesarSendGroupEnvelopesActivity.this.getString(string.waiting));
                String var3 = CaesarSendGroupEnvelopesActivity.this.getIntent().getStringExtra("TargetId");
                CaesarSendGroupEnvelopesActivity.this.envelopeMessage = q.a(CaesarSendGroupEnvelopesActivity.this.et_peak_message.getText().toString().trim()) ? CaesarSendGroupEnvelopesActivity.this.et_peak_message.getHint().toString().trim() : CaesarSendGroupEnvelopesActivity.this.et_peak_message.getText().toString().trim();
                return com.jrmf360.rylib.rp.http.a.a(CurrentUser.getUserId(), CaesarBaseActivity.rongCloudToken, CaesarSendGroupEnvelopesActivity.this.et_peak_num.getText().toString(), CaesarSendGroupEnvelopesActivity.this.et_amount.getText().toString(), CaesarSendGroupEnvelopesActivity.this.envelopeMessage, CaesarSendGroupEnvelopesActivity.this.ENVELOPES_TYPE, CaesarSendGroupEnvelopesActivity.this.envelopeName, var3);
            }

            public void onPostExecute(int var1, Object var2) {
                super.onPostExecute(var1, var2);
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarSendGroupEnvelopesActivity.this.context);
                if (!CaesarSendGroupEnvelopesActivity.this.isFinishing()) {
                    if (var2 != null && var2 instanceof h) {
                        h var3 = (h)var2;
                        if (var3.isSuccess()) {
                            if ("1".equals(var3.isVailPwd)) {
                                CaesarSendGroupEnvelopesActivity.this.jumpPayTypeActivity(var3, CaesarSendGroupEnvelopesActivity.this.et_amount.getText().toString().trim(), CaesarSendGroupEnvelopesActivity.this.ENVELOPES_TYPE, CaesarSendGroupEnvelopesActivity.this.et_peak_num.getText().toString().trim(), true);
                            } else if ("1".equals(var3.bSetPwd)) {
                                SettingPswdActivity.intentAuth(CaesarSendGroupEnvelopesActivity.this.context);
                            } else {
                                CaesarSendGroupEnvelopesActivity.this.jumpPayTypeActivity(var3, CaesarSendGroupEnvelopesActivity.this.et_amount.getText().toString().trim(), CaesarSendGroupEnvelopesActivity.this.ENVELOPES_TYPE, CaesarSendGroupEnvelopesActivity.this.et_peak_num.getText().toString().trim(), false);
                            }
                        } else {
                            Toast.makeText(CaesarSendGroupEnvelopesActivity.this, var3.respmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CaesarSendGroupEnvelopesActivity.this, CaesarSendGroupEnvelopesActivity.this.getString(string.network_error), Toast.LENGTH_LONG).show();
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
                                CaesarBaseActivity.rongCloudToken = URLEncoder.encode(var1x, "UTF-8");
                                r.a(var1, new Object[0]);
                            }
                        } catch (Exception var3) {
                            CaesarBaseActivity.rongCloudToken = "";
                            var3.printStackTrace();
                        }

                    }

                    public void onError(ErrorCode var1x) {
                        LogUtil.i("获得token失败" + var1x);
                        CaesarBaseActivity.rongCloudToken = "";
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
    }

    private void closeTips() {
        this.pop_message.setText("");
        this.pop_message.setVisibility(View.INVISIBLE);
    }

    private void checkForAllAmount() {
        int var1 = -1;
        String var2 = this.et_peak_num.getText().toString();
        if (!q.a(var2) && isNumber(var2)) {
            BigDecimal var3 = new BigDecimal(var2);
            var1 = var3.intValue();
        }

        float var7 = 0.0F;
        String var4 = this.et_amount.getText().toString();
        BigDecimal var5;
        if (!q.a(var4) && !var4.startsWith(".")) {
            var5 = new BigDecimal(var4);
            var7 = var5.floatValue();
        }

        if (var1 > 0 && var7 > 0.0F) {
            if (this.ENVELOPES_TYPE == 1) {
                var5 = new BigDecimal((double)var7);
                var5 = var5.setScale(2, 5);
                this.tv_amount_for_show.setText(var5 + "");
            } else if (this.ENVELOPES_TYPE == 0) {
                var5 = new BigDecimal((double)var7);
                BigDecimal var6 = var5.multiply(new BigDecimal(var1));
                var6 = var6.setScale(2, 5);
                this.tv_amount_for_show.setText(var6 + "");
            }
        } else {
            this.tv_amount_for_show.setText("0.00");
        }

    }

    private void checkForButton() {
        f.a(this.btn_putin, false);
        if (this.NUMBER > 0 && this.AMOUNTMONEY > 0.0D) {
            f.a(this.btn_putin, true);
        }

    }

    private void checkType() {
        String var1;
        if (this.ENVELOPES_TYPE == 1) {
            var1 = this.tv_amount_for_show.getText().toString();
            this.et_amount.setText(var1);
        } else if (this.ENVELOPES_TYPE == 0) {
            var1 = this.et_amount.getText().toString();
            String var2 = this.et_peak_num.getText().toString();
            if (var1 != null && !var1.isEmpty() && var2 != null && !var2.isEmpty()) {
                BigDecimal var3 = new BigDecimal(var1);
                BigDecimal var4 = new BigDecimal(var2);
                if (var4.floatValue() > 0.0F) {
                    BigDecimal var5 = var3.divide(var4, 3, 5);
                    this.et_amount.setText(q.a(var5.doubleValue()));
                }
            }
        }

    }

    private int checkNum() {
        String var1 = this.et_peak_num.getText().toString();
        if (!q.a(var1)) {
            if (isNumber(var1)) {
                BigDecimal var2 = new BigDecimal(var1);
                int var3 = var2.intValue();
                if (var3 == 0) {
                    this.showTips(this.getString(string.jrmf_rp_min_num));
                    this.ll_peak_num_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                    return -1;
                } else if (var3 > this.maxCount) {
                    this.showTips(String.format(this.getString(string.jrmf_rp_max_num), this.maxCount));
                    this.ll_peak_num_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                    return -1;
                } else {
                    this.ll_peak_num_layout.setBackgroundResource(drawable._bg_white_round);
                    this.closeTips();
                    return var3;
                }
            } else {
                this.showTips(this.getString(string.jrmf_rp_num_error));
                this.ll_peak_num_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                return -1;
            }
        } else {
            this.closeTips();
            this.ll_peak_num_layout.setBackgroundResource(drawable._bg_white_round);
            return -1;
        }
    }

    private double checkAmount() {
        String var1 = this.et_amount.getText().toString();
        if (!q.a(var1)) {
            if (!var1.startsWith(".")) {
                BigDecimal var2 = new BigDecimal(var1);
                double var3 = var2.doubleValue();
                if (var3 == 0.0D) {
                    this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round);
                    this.closeTips();
                    return -1.0D;
                } else if (var3 < 0.009999999776482582D) {
                    this.showTips(this.getString(string.jrmf_rp_min_amount));
                    this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                    return -1.0D;
                } else if (this.ENVELOPES_TYPE == 1) {
                    if (var3 > q.j(this.maxLimitMoney)) {
                        this.showTips(String.format(this.getString(string.jrmf_rp_max_amount), this.maxLimitMoney));
                        this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                        return -1.0D;
                    } else {
                        this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round);
                        this.closeTips();
                        return var3;
                    }
                } else if (this.ENVELOPES_TYPE == 0) {
                    String var5 = this.et_peak_num.getText().toString();
                    if (var5 != null && !var5.isEmpty()) {
                        double var6 = q.i(var5);
                        if (var6 != 0.0D && var6 * var3 > q.i(this.maxLimitMoney)) {
                            this.showTips(String.format(this.getString(string.jrmf_rp_max_amount), this.maxLimitMoney));
                            this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                            return -1.0D;
                        } else {
                            this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round);
                            this.closeTips();
                            return var3;
                        }
                    } else if (var3 > q.i(this.maxLimitMoney)) {
                        this.showTips(String.format(this.getString(string.jrmf_rp_max_amount), this.maxLimitMoney));
                        this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                        return -1.0D;
                    } else {
                        this.closeTips();
                        this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round);
                        return var3;
                    }
                } else {
                    return -1.0D;
                }
            } else {
                this.showTips(this.getString(string.jrmf_rp_amount_error));
                this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round_stroke);
                return -1.0D;
            }
        } else {
            this.closeTips();
            this.ll_peak_amount_layout.setBackgroundResource(drawable._bg_white_round);
            return -1.0D;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        i.b(this);
    }
}

