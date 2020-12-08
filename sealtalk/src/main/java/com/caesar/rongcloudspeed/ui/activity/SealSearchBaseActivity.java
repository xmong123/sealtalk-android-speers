package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.ui.interfaces.SearchableInterface;
import com.caesar.rongcloudspeed.utils.log.SLog;

import static com.caesar.rongcloudspeed.ui.view.SealTitleBar.Type.SEARCH;

public class SealSearchBaseActivity extends TitleBaseActivity implements TextWatcher, SearchableInterface {
    private static final String TAG = "SealSearchBaseActivity";
    protected String search; //当前关键字
    private TextView fl_content_textview_1;
    private TextView fl_content_textview_2;
    private TextView fl_content_textview_3;
    private TextView fl_content_textview_4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setType(SEARCH);
        getTitleBar().addSeachTextChangedListener(this);
        setContentView(R.layout.activity_select_base);
        fl_content_textview_1 = findViewById(R.id.fl_content_textview_1);
        fl_content_textview_2 = findViewById(R.id.fl_content_textview_2);
        fl_content_textview_3 = findViewById(R.id.fl_content_textview_3);
        fl_content_textview_4 = findViewById(R.id.fl_content_textview_4);
        getTitleBar().setOnBtnLeftClickListener(v -> onBackPressed());
        Intent intent=new Intent(this,SealOtherSearchActivity.class);
        fl_content_textview_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type",0);
                startActivity(intent);
            }
        });

        fl_content_textview_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        fl_content_textview_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });

        fl_content_textview_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        SLog.i(TAG, "afterTextChanged Editable = " + s);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                search = s.toString();
                if (TextUtils.isEmpty(search)) {
                    clear();
                } else {
                    search(search);
                }
            }
        }, 300);
    }

    @Override
    public void search(String match) {
        //子类实现自己搜索
    }

    @Override
    public void clear() {
        //子类实现清空搜索
    }

}
