package com.caesar.rongcloudspeed.circle.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.callback.OnItemClickListener;
import com.caesar.rongcloudspeed.circle.adapter.MyAlbumAdapter;
import com.yiw.circledemo.utils.BastiGallery;

import java.io.File;
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
            showProgressBar(true);

            Intent intent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri;
            File f = new File(url);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//24
                uri = FileProvider.getUriForFile(MyAlbumActivity.this, "com.caesar.rongcloudspeed.FileProvider", f);
                ContentResolver cR = MyAlbumActivity.this.getContentResolver();
                if (uri != null && uri.toString().length() > 0) {
                    String fileType = cR.getType(uri);
                    if (fileType.length() > 0) {
                        if (fileType.contains("video/")) {
                            uri = getVideoContentUri(MyAlbumActivity.this, f);
                        } else if (fileType.contains("image/")) {
                            uri = getImageContentUri(MyAlbumActivity.this, f);
                        } else if (fileType.contains("audio/")) {
                            uri = getAudioContentUri(MyAlbumActivity.this, f);
                        }
                    }
                }
                Log.d("***uri***", uri.toString());
            } else {
                uri = Uri.fromFile(f);
            }
            intent.setData(uri);
            sendBroadcast(intent);
//            Intent intent = new Intent();
//            intent.putExtra("fileName", fileName);
//            setResult(0x777, intent);
//            finish();
            photoHandler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler photoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ContentResolver resolver = getContentResolver();
                    Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_COLUMN, null, null, null);
                    if (cursor != null) {
                        cursor.moveToLast();
                        String path = cursor.getString(cursor.getColumnIndex(IMAGE_COLUMN[0]));
                        photos.add(1, path);
                        adapter.notifyDataSetChanged();
                    }
                    photoHandler.sendEmptyMessageDelayed(1,1000);
                    break;
                case 1:
                    showProgressBar(false);
                    break;
            }
        }
    };

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

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param videoFile
     * @return content Uri
     */
    public static Uri getVideoContentUri(Context context, File videoFile) {
        String filePath = videoFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media._ID}, MediaStore.Video.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/video/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (videoFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Video.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param audioFile
     * @return content Uri
     */
    public static Uri getAudioContentUri(Context context, File audioFile) {
        String filePath = audioFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID}, MediaStore.Audio.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/audio/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (audioFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
