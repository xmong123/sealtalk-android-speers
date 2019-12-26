package com.caesar.rongcloudspeed.circle.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.caesar.rongcloudspeed.util.ImageLoader;
import com.caesar.rongcloudspeed.util.L;
import com.caesar.rongcloudspeed.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;


/**
 * Created by 43053 on 2016/9/23.
 */
public class ImagePreviewActivity extends BaseActivity {

    SimpleDraweeView vinImg;
    private String imageUrl = "";
    private Uri imageUri;
    private String tag;
    private final static String FRAGMENT_TAG = "fragment";

    //是否显示编辑菜单：删除重拍按钮
    private boolean showEditMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataFromLastActivity();
    }


    @Override
    protected int provideContentView() {
        return R.layout.activity_image_preview;
    }

    private void showImageView() {

        //reCameraTv.setVisibility(showEditMenu ? View.VISIBLE : View.INVISIBLE);
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = "";
        }
        L.l("imageurl", imageUrl);
        vinImg = findViewById(R.id.vin_img);

        ImageLoader.setImage(vinImg, 800, 640, imageUrl);
    }

    private void showImageViewByUri() {
        //reCameraTv.setVisibility(showEditMenu ? View.VISIBLE : View.INVISIBLE);
        if (TextUtils.isEmpty(imageUrl)) {
            imageUrl = "";
        }
        L.l("imageurl", imageUrl);
        ImageLoader.setImage(vinImg, 800, 640, imageUri);
    }

    private void getDataFromLastActivity() {
        tag = getIntent().getStringExtra("tag");
        if (!getIntent().getBooleanExtra("local", false)) {
            imageUrl = getIntent().getStringExtra("imageUrl");
            showEditMenu = getIntent().getBooleanExtra("show", true);
            showImageView();
        } else {
            imageUrl = "file://".concat(getIntent().getStringExtra("imageUrl"));
            showEditMenu = getIntent().getBooleanExtra("show", true);
            imageUri = Uri.fromFile(new File(imageUrl));
            showImageView();
        }
    }


}
