package com.caesar.rongcloudspeed.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.caesar.rongcloudspeed.ui.widget.TabGroupView;
import com.caesar.rongcloudspeed.ui.widget.TabItem;

public class NPMTopTabGroupView extends TabGroupView {
    public NPMTopTabGroupView(Context context) {
        super(context);
    }

    public NPMTopTabGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NPMTopTabGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View createView(TabItem item) {
        NPMTopTabItem NPMTopTabItem = new NPMTopTabItem(getContext());
        NPMTopTabItem.setDrawable(item.drawable);
        NPMTopTabItem.setName(item.text);
        return NPMTopTabItem;
    }

    private long firstClick = 0;
    private OnTabDoubleClickListener doubleListener;

    @Override
    protected void onItemClick(TabItem item, OnTabSelectedListener listener, View view) {
        if (getSelectedItemId() == item.id) {
            if (firstClick == 0) {
                firstClick = System.currentTimeMillis();
            }  else {
                long secondClick = System.currentTimeMillis();
                if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
                    if (doubleListener != null) {
                        doubleListener.onDoubleClick(item, view);
                    }
                    firstClick = 0;
                } else {
                    firstClick = secondClick;
                }
            }

        } else {
            super.onItemClick(item, listener, view);
        }
    }

    /**
     * 设置双击监听
     * @param listener
     */
    public void setOnTabDoubleClickListener (OnTabDoubleClickListener listener) {
        this.doubleListener = listener;
    }


    /**
     * tab 双击监听
     */
    public interface OnTabDoubleClickListener {
        void onDoubleClick(TabItem item, View view);
    }
}
