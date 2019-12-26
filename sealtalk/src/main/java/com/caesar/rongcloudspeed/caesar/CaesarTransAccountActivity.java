package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.jrmf360.rylib.R.color;
import com.jrmf360.rylib.R.id;
import com.jrmf360.rylib.R.layout;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.common.http.ModelHttpCallBack;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.f;
import com.jrmf360.rylib.common.util.h;
import com.jrmf360.rylib.common.util.i;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.http.model.l;
import com.jrmf360.rylib.rp.http.model.m;
import com.jrmf360.rylib.rp.widget.ActionBarView;
import com.jrmf360.rylib.rp.widget.RoundImageView;
import com.jrmf360.rylib.wallet.e.a;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import java.net.URLEncoder;

public class CaesarTransAccountActivity extends CaesarBaseActivity {
    private TextView tv_name;
    private TextView tv_trans_tip;
    private TextView tv_update_tip;
    private EditText et_trans_money;
    private RoundImageView iv_header;
    private Button btn_trans_account;
    private String targetId;
    private AlertDialog transDesdialog;
    private AlertDialog transRepeatdialog;
    private boolean isFirstShowDialog = true;
    private String transferLimit = "20000.00";
    private m mTransModel;

    public CaesarTransAccountActivity() {
    }

    public int getLayoutId() {
        return layout.jrmf_rp_activity_trans_account;
    }

    public void initView() {
        this.actionBarView = (ActionBarView)this.findViewById(id.actionbar);
        this.iv_header = (RoundImageView)this.findViewById(id.iv_header);
        this.tv_name = (TextView)this.findViewById(id.tv_name);
        this.et_trans_money = (EditText)this.findViewById(id.et_trans_money);
        this.tv_trans_tip = (TextView)this.findViewById(id.tv_trans_tip);
        this.tv_update_tip = (TextView)this.findViewById(id.tv_update_tip);
        this.btn_trans_account = (Button)this.findViewById(id.btn_trans_account);
        f.a(this.btn_trans_account, false);
        i.a(this.et_trans_money);
    }

    public void initListener() {
        this.actionBarView.getIvBack().setOnClickListener(this);
        this.tv_trans_tip.setOnClickListener(this);
        this.tv_update_tip.setOnClickListener(this);
        this.btn_trans_account.setOnClickListener(this);
        this.et_trans_money.addTextChangedListener(new a() {
            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
                super.onTextChanged(var1, var2, var3, var4);
            }

            public void afterTextChanged(Editable var1) {
                super.afterTextChanged(var1);
                CaesarTransAccountActivity.this.checkButtomButtom(var1);
            }
        });
        this.et_trans_money.setFilters(new InputFilter[]{new CaesarTransAccountActivity.InputMoney()});
    }

    private void checkButtomButtom(Editable var1) {
        if (var1 != null && !q.a(var1.toString()) && !var1.toString().startsWith(".")) {
            f.a(this.btn_trans_account, true);
        } else {
            f.a(this.btn_trans_account, false);
        }

    }

    protected void initData(Bundle var1) {
        if (var1 != null) {
            this.targetId = var1.getString("TargetId");
            String var2 = var1.getString("TargetName");
            String var3 = var1.getString("TargetIcon");
            this.tv_name.setText(var2);
            if (q.b(var3)) {
                h.a().a(this.iv_header, var3);
            }
        }

        this.checkToken(0);
    }

    private void getTransInfo() {
        com.jrmf360.rylib.rp.http.a.a(rongCloudToken, CurrentUser.getUserId(), CurrentUser.getName(), CurrentUser.getUserIcon(), this.targetId, CurrentUser.getNameById(this.targetId), CurrentUser.getUserIconById(this.targetId), new ModelHttpCallBack<l>() {
            public void onSuccess(l var1) {
                if (var1.isSuccess()) {
                    CaesarTransAccountActivity.this.transferLimit = var1.transferLimit;
                } else {
                    ToastUtil.showToast(CaesarTransAccountActivity.this.context, var1.respmsg);
                }

            }

            public void onFail(String var1) {
                LogUtil.e("获取转账信息失败：" + var1);
            }
        });
    }

    private void checkToken(final int var1) {
        if (q.a(rongCloudToken)) {
            if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                RongIMClient.getInstance().getVendorToken(new ResultCallback<String>() {
                    public void onSuccess(String var1x) {
                        try {
                            if (q.c(var1x)) {
                                CaesarBaseActivity.rongCloudToken = URLEncoder.encode(var1x, "UTF-8");
                                if (var1 == 0) {
                                    CaesarTransAccountActivity.this.getTransInfo();
                                } else {
                                    CaesarTransAccountActivity.this.transAccount();
                                }
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
        } else if (var1 == 0) {
            this.getTransInfo();
        } else {
            this.transAccount();
        }

    }

    public void onClick(int var1) {
        if (var1 == id.iv_back) {
            i.b(this);
            this.finish();
        } else if (var1 == id.tv_trans_tip) {
            this.showTransTipDialog();
        } else if (var1 == id.tv_update_tip) {
            this.showTransTipDialog();
        } else if (var1 == id.btn_trans_account && !com.jrmf360.rylib.common.util.m.a()) {
            this.checkToken(1);
        }

    }

    private void transAccount() {
        final String var1 = this.et_trans_money.getText().toString();
        if (q.i(var1) <= 0.0D) {
            ToastUtil.showToast(this.context, this.getResources().getString(string.trans_little_tip));
        } else {
            String var2;
            if (q.i(var1) > q.i(this.transferLimit)) {
                var2 = String.format(this.getString(string.jrmf_trans_over_limit), this.transferLimit);
                ToastUtil.showToast(this.context, var2);
            } else {
                var2 = this.isFirstShowDialog ? "" : this.tv_trans_tip.getText().toString().trim();
                com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.loading), false);
                String sum_foruserName=CurrentUser.getNameById(this.targetId);
                NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().transferCodeToUserName(var1,UserInfoUtils.getAppUserId(this),sum_foruserName),
                        new NetworkCallback<UserSumResult>() {
                            @Override
                            public void onSuccess(UserSumResult userSumResult) {
                                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransAccountActivity.this.context);
                                UserSumUrl userSumUrl=userSumResult.getUrl();
                                String sumString=String.valueOf(userSumUrl.getUser_sum());
                                UserInfoUtils.setUserSum(sumString,CaesarTransAccountActivity.this);
                                String var2 = userSumUrl.getOrder();
                                m var1x=new m();
                                var1x.balance= UserInfoUtils.getUserSum(CaesarTransAccountActivity.this);
                                var1x.isVailPwd="1";
                                var1x.isWarn=false;
                                var1x.transferOrderNo=var2;
                                var1x.realName=var2;
                                var1x.count=var1;
                                CaesarTransAccountActivity.this.mTransModel = var1x;
                                CaesarTransPayActivity.intent(CaesarTransAccountActivity.this.context, CaesarTransAccountActivity.this.targetId, var1x, var1);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransAccountActivity.this.context);
                                Toast.makeText(CaesarTransAccountActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

//                com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.loading));
//                com.jrmf360.rylib.rp.http.a.a(CurrentUser.getUserId(), rongCloudToken, this.targetId, var1, var2, new ModelHttpCallBack<m>() {
//                    public void onSuccess(m var1x) {
//                        if (!CaesarTransAccountActivity.this.isFinishing()) {
//                            com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransAccountActivity.this.context);
//                            CaesarTransAccountActivity.this.mTransModel = var1x;
//                            if (var1x.isSuccess()) {
//                                if (var1x.isWarn) {
//                                    CaesarTransAccountActivity.this.showTransRepeatDialog(var1x);
//                                } else {
//                                    CaesarTransPayActivity.intent(CaesarTransAccountActivity.this.context, CaesarTransAccountActivity.this.targetId, var1x, var1);
//                                }
//                            } else {
//                                ToastUtil.showToast(CaesarTransAccountActivity.this.context, var1x.respmsg);
//                            }
//
//                        }
//                    }
//
//                    public void onFail(String var1x) {
//                        com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransAccountActivity.this.context);
//                        ToastUtil.showToast(CaesarTransAccountActivity.this.context, CaesarTransAccountActivity.this.getString(string.net_error_l));
//                    }
//                });
            }
        }
    }

    private void showTransTipDialog() {
        Builder var1 = new Builder(this);
        View var2 = View.inflate(this, layout.jrmf_rp_trans_tip_dialog, (ViewGroup)null);
        var1.setView(var2);
        var1.setCancelable(false);
        final EditText var3 = (EditText)var2.findViewById(id.cet_trans_des);
        if (!this.isFirstShowDialog) {
            var3.setText(this.tv_trans_tip.getText().toString().trim());
        }

        TextView var4 = (TextView)var2.findViewById(id.tv_quit);
        TextView var5 = (TextView)var2.findViewById(id.tv_confirm);
        var4.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                CaesarTransAccountActivity.this.transDesdialog.dismiss();
            }
        });
        var5.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                String var2 = var3.getText().toString().trim();
                if (q.b(var2)) {
                    CaesarTransAccountActivity.this.tv_trans_tip.setText(var2.trim());
                    CaesarTransAccountActivity.this.tv_trans_tip.setTextColor(CaesarTransAccountActivity.this.getResources().getColor(color.color_888888));
                    CaesarTransAccountActivity.this.tv_update_tip.setVisibility(View.VISIBLE);
                    if (CaesarTransAccountActivity.this.isFirstShowDialog) {
                        CaesarTransAccountActivity.this.isFirstShowDialog = false;
                    }
                } else {
                    CaesarTransAccountActivity.this.isFirstShowDialog = true;
                    CaesarTransAccountActivity.this.tv_trans_tip.setText(CaesarTransAccountActivity.this.getResources().getString(string.add_trans_tip));
                    CaesarTransAccountActivity.this.tv_trans_tip.setTextColor(CaesarTransAccountActivity.this.getResources().getColor(color.title_bar_color));
                    CaesarTransAccountActivity.this.tv_update_tip.setVisibility(View.VISIBLE);
                }

                CaesarTransAccountActivity.this.transDesdialog.dismiss();
            }
        });
        this.transDesdialog = var1.create();
        this.transDesdialog.show();
        i.a(var3);
    }

    private void showTransRepeatDialog(final m var1) {
        Builder var2 = new Builder(this);
        View var3 = View.inflate(this, layout.jrmf_rp_trans_repeat_dialog, (ViewGroup)null);
        var2.setView(var3);
        var2.setCancelable(false);
        TextView var4 = (TextView)var3.findViewById(id.tv_count);
        if (q.b(var1.count)) {
            var4.setText(String.format(this.getString(string.trans_repeat_tip), var1.count));
        }

        TextView var5 = (TextView)var3.findViewById(id.tv_continue_trans);
        TextView var6 = (TextView)var3.findViewById(id.tv_quit);
        var6.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
                CaesarTransAccountActivity.this.transRepeatdialog.dismiss();
            }
        });
        var5.setOnClickListener(new OnClickListener() {
            public void onClick(View var1x) {
                CaesarTransPayActivity.intent(CaesarTransAccountActivity.this.context, CaesarTransAccountActivity.this.targetId, var1, CaesarTransAccountActivity.this.et_trans_money.getText().toString().trim());
                CaesarTransAccountActivity.this.transRepeatdialog.dismiss();
            }
        });
        this.transRepeatdialog = var2.create();
        this.transRepeatdialog.show();
    }

    public void finishWithResult() {
        Intent var1 = new Intent();
        var1.putExtra("transferOrder", this.mTransModel.transferOrderNo);
        var1.putExtra("transferAmount", this.et_trans_money.getText().toString().trim());
        var1.putExtra("transferDesp", this.isFirstShowDialog ? "" : this.tv_trans_tip.getText().toString().trim());
        this.setResult(-1, var1);
        this.finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        i.b(this);
    }

    private class InputMoney implements InputFilter {
        private InputMoney() {
        }

        public CharSequence filter(CharSequence var1, int var2, int var3, Spanned var4, int var5, int var6) {
            if (var1.toString().equals(".") && var5 == 0 && var6 == 0) {
                CaesarTransAccountActivity.this.et_trans_money.setText("0" + var1 + var4);
                CaesarTransAccountActivity.this.et_trans_money.setSelection(2);
            }

            return var4.toString().indexOf(".") != -1 && var4.length() - var4.toString().indexOf(".") > 2 && var4.length() - var5 < 3 ? "" : null;
        }
    }
}

