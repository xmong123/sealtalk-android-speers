package com.caesar.rongcloudspeed.circle.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.callback.OnItemClickListener;
import com.caesar.rongcloudspeed.circle.adapter.MyAlbumAdapter;
import com.yiw.circledemo.utils.BastiGallery;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/5/3.
 */

public class MyAlbumActivity extends BaseActivity implements MyAlbumAdapter.ISelectPhoto {

    public static final String[] IMAGE_COLUMN = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE};
    RecyclerView rvMyAlbum;
    TextView tvPhotoNum;
    TextView tvConfirm;

    private MyAlbumAdapter adapter;
    private List<String> photos;
    private boolean isSingle;   //只能选取单张
    private int limitNum;


    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initAdapter();
        getPhoto();
    }

    @Override
    protected int provideContentView() {
        return R.layout.activity_myalbum;
    }

    private void initView() {
        rvMyAlbum = findViewById(R.id.rv_my_album);
        tvPhotoNum = findViewById(R.id.tv_photo_num);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(view -> {
            if (selectedPhotos == null || selectedPhotos.size() == 0) {
                Toast.makeText(this, "请选择要提交的相片", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent();
            intent.putStringArrayListExtra("photos", (ArrayList<String>) selectedPhotos);
            setResult(0x888, intent);
            finish();
        });
    }

    private void initData() {
        limitNum = getIntent().getIntExtra("limitNum", 9);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initAdapter() {
        photos = new ArrayList<>();
        photos.add("");
        adapter = new MyAlbumAdapter(photos, limitNum);
        adapter.setISelectPhoto(this);
        rvMyAlbum.setLayoutManager(new GridLayoutManager(this, 3));
        rvMyAlbum.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (position == 0) {
                    //拍照回调
                    BastiGallery.openCamera(MyAlbumActivity.this, 0);
                } else {
                    //图片预览
                    Intent intent = new Intent(MyAlbumActivity.this, ImagePreviewActivity.class);
                    intent.putExtra("imageUrl", photos.get(position));
                    intent.putExtra("show", false);
                    intent.putExtra("local", true);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String url = BastiGallery.getmCurrentPhotoPath();
            String fileName = BastiGallery.getPathFromCamera(data, url);
            if (TextUtils.isEmpty(fileName)) {
                Toast.makeText(this, "相册异常", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("fileName", fileName);
            setResult(0x777, intent);
            finish();
        }
    }

    private void getPhoto() {
        try {
            String path;
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_COLUMN, null, null, null);
            if (cursor != null) {
                cursor.moveToLast();
                path = cursor.getString(cursor.getColumnIndex(IMAGE_COLUMN[0]));
                photos.add(path);
                while (cursor.moveToPrevious()) {
                    path = cursor.getString(cursor.getColumnIndex(IMAGE_COLUMN[0]));
                    photos.add(path);
                }
                if (photos.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void overNum() {

        Toast.makeText(this, "最多还能选择?张照片".replace("?", String.valueOf(limitNum)), Toast.LENGTH_LONG).show();
    }

    private List<String> selectedPhotos;

    @Override
    public void numTips(List<String> list) {
        selectedPhotos = list;
        if (list.size() > 0) {
            tvPhotoNum.setVisibility(View.VISIBLE);
            tvPhotoNum.setText(String.valueOf(list.size()));
        } else {
            tvPhotoNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
