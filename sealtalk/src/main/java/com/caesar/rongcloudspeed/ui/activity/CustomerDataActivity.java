/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.caesar.rongcloudspeed.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.blankj.utilcode.util.SPUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.yuyh.library.imgsel.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * register screen
 */
public class CustomerDataActivity extends MultiStatusActivity {
    private static final String TAG = "CustomerDataActivity";

    @OnClick({R.id.myAccountStar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myAccountStar:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13961404086"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitleBarView(titlebar, "同行客服");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_customer_edit;
    }


    public void back(View view) {
        finish();
    }

}
