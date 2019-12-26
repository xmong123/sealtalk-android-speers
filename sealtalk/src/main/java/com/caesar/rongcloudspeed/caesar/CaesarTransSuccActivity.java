package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jrmf360.rylib.R.id;
import com.jrmf360.rylib.R.layout;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.widget.ActionBarView;
import com.jrmf360.rylib.wallet.c.a;

public class CaesarTransSuccActivity extends CaesarBaseActivity {
    private TextView tv_username;
    private TextView tv_trans_money;
    private Button btn_trans_finish;

    public CaesarTransSuccActivity() {
    }

    public static void intent(Activity var0, String var1, String var2) {
        Intent var3 = new Intent(var0, CaesarTransSuccActivity.class);
        Bundle var4 = new Bundle();
        var4.putString("receiptName", var1);
        var4.putString("transMoney", var2);
        var3.putExtras(var4);
        var0.startActivity(var3);
    }

    public int getLayoutId() {
        return layout.jrmf_rp_activity_trans_succ;
    }

    public void initView() {
        this.actionBarView = (ActionBarView)this.findViewById(id.actionbar);
        this.actionBarView.getIvBack().setVisibility(View.GONE);
        this.tv_username = (TextView)this.findViewById(id.tv_username);
        this.tv_trans_money = (TextView)this.findViewById(id.tv_trans_money);
        this.btn_trans_finish = (Button)this.findViewById(id.btn_trans_finish);
    }

    public void initListener() {
        this.btn_trans_finish.setOnClickListener(this);
    }

    protected void initData(Bundle var1) {
        if (var1 != null) {
            String var2 = var1.getString("receiptName");
            String var3 = var1.getString("transMoney");
            if (var2 != null && var2.length() > 10) {
                var2 = var2.substring(0, 10) + "...";
            }

            this.tv_username.setText(String.format(this.getString(string.jrmf_trans_to_who), var2));
            this.tv_trans_money.setText("￥" + q.h(var3));
        }

    }

    public void onClick(int var1) {
        if (var1 == id.btn_trans_finish) {
            CaesarTransAccountActivity var2 = (CaesarTransAccountActivity)a.a().a(CaesarTransAccountActivity.class);
            if (var2 != null) {
                var2.finishWithResult();
            }

            this.finish();
        }

    }

    public void onBackPressed() {
    }
}
