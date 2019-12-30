package com.caesar.rongcloudspeed.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wuhenzhizao.titlebar.utils.AppUtils;

import com.caesar.rongcloudspeed.R;

/**
 * Created by mac on 2018/4/1.
 */

public class TranspanentTitleBar extends FrameLayout {

    private String title = "";
    private boolean isShowBack;
    private int statusBarHeight;
    private View back;
    private TextView rightItem;
    private int imageBackResource;

    public TranspanentTitleBar(@NonNull Context context) {
        super(context);

    }

    public TranspanentTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public TranspanentTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TranspanentTitleBar);
        title = typedArray.getString(R.styleable.TranspanentTitleBar_msg);
        imageBackResource = typedArray.getResourceId(R.styleable.TranspanentTitleBar_backImage, -1);
        isShowBack = typedArray.getBoolean(R.styleable.TranspanentTitleBar_isShowBack, true);
        statusBarHeight = AppUtils.getStatusBarHeight(getContext());
        typedArray.recycle();

        addView();
    }

    private void addView() {
        /* <TextView
                style="@style/title_style"
        android:text="关于我们" />

            <ImageView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:layout_marginLeft="@dimen/layoutMargin16dp"
        android:background="@drawable/comm_titlebar_back_normal" />*/

        View view = LayoutInflater.from(getContext()).inflate(R.layout.transpanent_title_bar, null);
        LayoutParams layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams1.topMargin = statusBarHeight;
        this.addView(view, layoutParams1);
        ((TextView) view.findViewById(R.id.titleBar)).setText(title);
        back = view.findViewById(R.id.back);
        rightItem = (TextView) view.findViewById(R.id.righttitleItem);
        if (imageBackResource != -1) {
            ((ImageView) back).setImageResource(imageBackResource);
        }
    }

    public View getBack() {
        return back;
    }

    public TextView getRightItem() {
        return rightItem;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        float dimension = getResources().getDimension(R.dimen.titleBarHeight) + statusBarHeight;

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) dimension);
    }
}
