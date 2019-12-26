package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import com.jrmf360.rylib.common.http.ModelHttpCallBack;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.http.a;
import com.jrmf360.rylib.rp.http.model.d;
import com.jrmf360.rylib.rp.http.model.h;
import com.jrmf360.rylib.rp.ui.PayTypeActivity;

public abstract class CaesarBaseSendActivity extends CaesarBaseActivity {
    private String envelopesID = "";
    protected String envelopeMessage = "";
    protected String envelopeName = "";
    protected String summary = "";
    protected EditText et_peak_message;
    protected String maxLimitMoney = "200";
    protected int maxCount = 100;
    protected EditText et_amount;

    public CaesarBaseSendActivity() {
    }

    protected void getRpInfo() {
        a.a(this.userid, rongCloudToken, this.username, this.usericon, new ModelHttpCallBack<d>() {
            public void onSuccess(d var1) {
                if (!CaesarBaseSendActivity.this.context.isFinishing()) {
                    if (var1 != null && var1.isSuccess()) {
                        if (q.c(var1.maxLimitMoney)) {
                            CaesarBaseSendActivity.this.maxLimitMoney = var1.maxLimitMoney;
                        }

                        if (var1.maxCount > 0) {
                            CaesarBaseSendActivity.this.maxCount = var1.maxCount;
                        }

                        if (q.c(var1.summary)) {
                            CaesarBaseSendActivity.this.summary = var1.summary;
                        }

                        if (q.c(var1.envelopeName)) {
                            CaesarBaseSendActivity.this.envelopeName = var1.envelopeName;
                        }

                        CaesarBaseSendActivity.this.et_peak_message.setHint(CaesarBaseSendActivity.this.summary);
                    }

                }
            }

            public void onFail(String var1) {
            }
        });
    }

    public void jumpPayTypeActivity(h var1, String var2, int var3, boolean var4) {
        this.jumpPayTypeActivity(var1, var2, var3, "1", var4);
    }

    public void jumpPayTypeActivity(h var1, String var2, int var3, String var4, boolean var5) {
        this.envelopesID = var1.envelopeId;
        Intent var6 = new Intent(this, CaesarPayTypeActivity.class);
        Bundle var7 = new Bundle();
        var7.putSerializable("temp", var1);
        var6.putExtras(var7);
        var6.putExtra("envelopestype", var3);
        var6.putExtra("amount", var2);
        var6.putExtra("number", var4);
        var6.putExtra("rp_name", this.envelopeName);
        var6.putExtra("isVailPwd", var5);
        this.startActivityForResult(var6, 999);
    }

    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int var1, int var2, Intent var3) {
        if (var1 == 999) {
            if (var2 == -1) {
                Intent var4 = new Intent();
                var4.putExtra("envelopesID", this.envelopesID);
                var4.putExtra("envelopeMessage", this.envelopeMessage);
                var4.putExtra("envelopeName", this.envelopeName);
                this.setResult(-1, var4);
                this.finish();
            } else if (var2 == 0) {
            }
        }

    }

    public static boolean isNumber(String var0) {
        return !q.a(var0) ? var0.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$") : false;
    }

    protected class InputMoney implements InputFilter {
        protected InputMoney() {
        }

        public CharSequence filter(CharSequence var1, int var2, int var3, Spanned var4, int var5, int var6) {
            if (var1.toString().equals(".") && var5 == 0 && var6 == 0) {
                CaesarBaseSendActivity.this.et_amount.setText("0" + var1 + var4);
                CaesarBaseSendActivity.this.et_amount.setSelection(2);
            }

            if (var5 >= 8) {
                return "";
            } else {
                return var4.toString().indexOf(".") != -1 && var4.length() - var4.toString().indexOf(".") > 2 && var4.length() - var5 < 3 ? "" : null;
            }
        }
    }
}

