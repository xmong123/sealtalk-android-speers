package com.caesar.rongcloudspeed.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        Log.d("expandSpec","widthMeasureSpec:"+widthMeasureSpec+",expandSpec:"+expandSpec);
        Log.i("tag", "onMeasure:widthMea="+MeasureSpec.getSize(widthMeasureSpec));
        Log.i("tag", "onMeasure:heightMeasureSpec="+MeasureSpec.getSize(expandSpec));
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
