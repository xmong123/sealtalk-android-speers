package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.ui.widget.ClearWriteEditText;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

public class UpdateRecruitMessageActivity extends TitleBaseActivity {

    private ClearWriteEditText updateRecruitCet;
    private String uidString;
    private String newMeaage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recruit_message);
        uidString = UserInfoUtils.getAppUserId(this);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {

        getTitleBar().setTitle(R.string.seal_update_name);
        getTitleBar().setOnBtnRightClickListener(getString(R.string.seal_update_name_save_update), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMeaage = updateRecruitCet.getText().toString().trim();
                if (!TextUtils.isEmpty(newMeaage)) {
                    setResult(1,getIntent());
                    finish();
                } else {
                    showToast("信息不能为空");
                    updateRecruitCet.setShakeAnimation();
                }
            }
        });

        updateRecruitCet = findViewById(R.id.cet_update_recruit_edit);
    }

}
