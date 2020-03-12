package com.caesar.rongcloudspeed.ui.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caesar.rongcloudspeed.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by yiw on 2016/1/6.
 */
public class ImageViewPreviewActivity extends Activity{
    private static final String INTENT_IMGURLS = "imgurls";
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview_layout);
        imageView = (ImageView) findViewById(R.id.imageview_preview);
        String imageurl=getIntent().getStringExtra(INTENT_IMGURLS);
        ImageLoader.getInstance().displayImage(imageurl, imageView);
    }

}

