package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.caesar.rongcloudspeed.R;

import butterknife.BindView;

/**
 * 主界面子界面-发现界面
 */
public class OrderInvoicePersonalFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "OrderInvoicePersonalFragment";
    @BindView(R.id.invoice_edit_title)
    EditText invoice_edit_title;

    private String lesson_id;

    @Override
    protected int getLayoutResId() {
        return R.layout.order_fragment_invoice_layout;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {


    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {

    }
}
