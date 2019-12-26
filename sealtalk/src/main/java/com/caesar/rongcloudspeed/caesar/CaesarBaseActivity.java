package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import androidx.fragment.app.FragmentActivity;
import com.jrmf360.rylib.common.util.i;
import com.jrmf360.rylib.common.util.o;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.d.a;
import com.jrmf360.rylib.rp.widget.ActionBarView;
import io.rong.imkit.RongConfigurationManager;

public abstract class CaesarBaseActivity extends FragmentActivity implements a {
    public Activity context;
    public ActionBarView actionBarView;
    protected String userid;
    protected String username;
    protected String usericon;
    public static String rongCloudToken;

    public CaesarBaseActivity() {
    }

    protected void onCreate(Bundle var1) {
        this.requestWindowFeature(1);
        String var2 = this.getClass().getSimpleName();
        if (!"PayTypeActivity".equals(var2) && !"TransPayActivity".equals(var2)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(var1);
        com.jrmf360.rylib.wallet.c.a.a().a(this);
        this.setContentView(this.getLayoutId());
        this.context = this;
        this.userid = this.getIntent().getStringExtra("user_id");
        this.username = this.getIntent().getStringExtra("user_name");
        this.usericon = this.getIntent().getStringExtra("user_icon");
        if (q.b(this.userid)) {
            o.a().a(this.context, "userId", this.userid);
        }

        this.initView();
        this.initListener();
        this.initData(this.getIntent().getExtras());
    }

    protected void attachBaseContext(Context var1) {
        Context var2 = RongConfigurationManager.getInstance().getConfigurationContext(var1);
        super.attachBaseContext(var2);
    }

    public abstract int getLayoutId();

    public void initView() {
    }

    public void initListener() {
    }

    protected void initData(Bundle var1) {
    }

    public void onClick(int var1) {
    }

    public void onClick(View var1) {
        this.onClick(var1.getId());
    }

    public boolean dispatchTouchEvent(MotionEvent var1) {
        if (var1.getAction() == 0) {
            View var2 = this.getCurrentFocus();
            if (this.isShouldHideKeyboard(var2, var1)) {
                i.a(this, var2.getWindowToken());
            }
        }

        return super.dispatchTouchEvent(var1);
    }

    private boolean isShouldHideKeyboard(View var1, MotionEvent var2) {
        if (var1 != null && var1 instanceof EditText) {
            int[] var3 = new int[]{0, 0};
            var1.getLocationInWindow(var3);
            int var4 = var3[0];
            int var5 = var3[1];
            int var6 = var5 + var1.getHeight();
            int var7 = var4 + var1.getWidth();
            return var2.getX() <= (float)var4 || var2.getX() >= (float)var7 || var2.getY() <= (float)var5 || var2.getY() >= (float)var6;
        } else {
            return false;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        com.jrmf360.rylib.wallet.c.a.a().b(this);
    }
}

