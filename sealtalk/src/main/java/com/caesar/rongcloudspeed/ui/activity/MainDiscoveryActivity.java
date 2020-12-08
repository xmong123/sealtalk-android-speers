package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.circle.ui.AddCircleImagesActivity;
import com.caesar.rongcloudspeed.circle.ui.AddCircleTaskActivity;
import com.caesar.rongcloudspeed.quick.QuickStartVideoExampleActivity;
import com.caesar.rongcloudspeed.ui.BaseActivity;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.PublicServiceProfile;

/**
 * 主界面子界面-发现界面
 */
public class MainDiscoveryActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_discovery);
        findViewById(R.id.btn_public_01).setOnClickListener(this);
        findViewById(R.id.btn_public_02).setOnClickListener(this);
        findViewById(R.id.btn_public_03).setOnClickListener(this);
        findViewById(R.id.btn_public_04).setOnClickListener(this);
        findViewById(R.id.btn_public_05).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_public_01:
                Intent intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_public_02:
                intent = new Intent(this, PublicCircleActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_public_03:
                intent = new Intent(this, QuickStartVideoExampleActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_public_04:
                intent = new Intent(this, PublicSeekActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_public_05:
                intent = new Intent(this, PublicAdvertActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

}
