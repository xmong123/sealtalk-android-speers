package com.caesar.rongcloudspeed.easypop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.caesar.rongcloudspeed.R;

/**
 * Created by zyyoona7 on 2018/3/12.
 *
 * PopupWindow 中存在 EditText 隐藏键盘方法不起作用，只有 toggle 键盘方法才起作用
 * 注：建议由 EditText 需求的弹窗使用 DialogFragment
 */
public class CmmtPopup extends BasePopup<CmmtPopup> {

    private View.OnClickListener mCancelListener;
    private View.OnClickListener mOkListener;
    AppCompatTextView mCancelTv;
    AppCompatTextView mOkTv;
    public AppCompatEditText mEditText;

    public static CmmtPopup create(Context context) {
        return new CmmtPopup(context);
    }

    public CmmtPopup(Context context) {
        setContext(context);
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_cmmt, ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(150));
        setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setAnimationStyle( R.style.BottomPopAnim)
                .setDimValue(0.5f)
                .setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED)
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected void initViews(View view, CmmtPopup popup) {
        mCancelTv = findViewById(R.id.tv_cancel);
        mOkTv = findViewById(R.id.tv_ok);
        mEditText = findViewById(R.id.et_cmmt);
        mCancelTv.setOnClickListener(mCancelListener);
        mOkTv.setOnClickListener(mOkListener);
    }

    public CmmtPopup setOnCancelClickListener(View.OnClickListener listener) {
        mCancelListener = listener;
        return this;
    }

    public CmmtPopup setOnOkClickListener(View.OnClickListener listener) {
        mOkListener = listener;
        return this;
    }

    public CmmtPopup showSoftInput() {
        if (mEditText != null) {
            mEditText.post(new Runnable() {
                @Override
                public void run() {
                    KeyboardUtils.showSoftInput(mEditText);
                }
            });
        }
        return this;
    }

    public CmmtPopup hideSoftInput() {
        if (mEditText != null) {
            hideInput();
            mEditText.post(() -> KeyboardUtils.hideSoftInput(mEditText));
        }
        return this;
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}


