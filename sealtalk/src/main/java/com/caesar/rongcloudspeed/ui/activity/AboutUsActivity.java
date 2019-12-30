package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.caesar.rongcloudspeed.common.BaseShopActivity;
import com.caesar.rongcloudspeed.view.TranspanentTitleBar;
import com.just.agentweb.AgentWeb;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.manager.RetrofitManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseShopActivity {

    @BindView(R.id.titleBar)
    TranspanentTitleBar titleBar;
    @BindView(R.id.browerContainer)
    LinearLayout browerContainer;
    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        titleBar.getBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        loadData();
    }

    private void loadData() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(browerContainer, new LinearLayout.LayoutParams(-1, -1))
                .closeIndicator()
                .createAgentWeb()
                .ready()
                .go(RetrofitManager.server_address + "about");

    }
}
