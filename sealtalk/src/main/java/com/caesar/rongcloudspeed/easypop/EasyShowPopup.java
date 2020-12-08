package com.caesar.rongcloudspeed.easypop;

import android.view.View;
import android.view.ViewGroup;

import com.caesar.rongcloudspeed.R;

/**
 * Created by zyyoona7 on 2017/8/7.
 */

public class EasyShowPopup extends BasePopup<EasyShowPopup> {

    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_easy_show);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusAndOutsideEnable(true);
    }

    @Override
    protected void initViews(View view, EasyShowPopup basePopup) {


    }

}
