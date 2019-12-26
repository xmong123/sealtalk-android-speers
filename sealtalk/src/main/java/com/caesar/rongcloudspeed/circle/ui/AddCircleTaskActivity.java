package com.caesar.rongcloudspeed.circle.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.circle.adapter.AddImgAdapter;
import com.caesar.rongcloudspeed.data.HolderImg;
import com.caesar.rongcloudspeed.data.UserInfo;
import com.caesar.rongcloudspeed.data.result.UserInfoResult;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.callback.OnItemClickListener;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.config.Config;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.yiw.circledemo.utils.BastiGallery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mathum on 2017/9/20.
 */

public class AddCircleTaskActivity extends BaseActivity {

    EditText edContent;
    RecyclerView rvPhoto;
    private ImageView back;
    private TextView put;
    private int groupId;
    private List<HolderImg> holderImgList;
    private AddImgAdapter adapter;
    private int selectImgPosition = 0;
    private List<String> urlList;


    @Override
    protected int provideContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        edContent = findViewById(R.id.ed_content);
        rvPhoto = findViewById(R.id.rv_photo);
        back = findViewById(R.id.back);
        put = findViewById(R.id.add);
        back.setOnClickListener(view -> {
            finish();
        });
        put.setOnClickListener(view -> {
            uploadImg();
        });

    }


    private void initAdapter() {
        holderImgList = new ArrayList<>();
        holderImgList.add(new HolderImg());
        adapter = new AddImgAdapter(holderImgList);
        rvPhoto.setLayoutManager(new GridLayoutManager(this, 3));
        rvPhoto.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                selectImgPosition = position;
                if (position == holderImgList.size() - 1) {
                    Intent intent = new Intent(AddCircleTaskActivity.this, MyAlbumActivity.class);
                    intent.putExtra("limitNum", 9 - holderImgList.size() + 1);
                    startActivityForResult(intent, 0x123);
                } else {
                    Intent intent = new Intent(AddCircleTaskActivity.this, ImagePreviewActivity.class);
                    intent.putExtra("imageUrl", holderImgList.get(position).getUrl());
                    intent.putExtra("show", true);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x123 && resultCode == 0x888 && data != null) {
            holderImgList.remove(holderImgList.size() - 1);
            try {
                ArrayList<String> list = data.getStringArrayListExtra("photos");
                for (int position = 0; position < list.size(); position++) {
                    byte[] img = BastiGallery.Bitmap2Bytes(BastiGallery.getimage(list.get(position)));
                    HolderImg holderImg = new HolderImg();
                    holderImg.setUrl("file://".concat(list.get(position)));
                    holderImg.setBytes(img);
                    holderImg.setHasUpload(false);
                    holderImgList.add(holderImg);
                }
                if (holderImgList.size() < 9) {
                    holderImgList.add(new HolderImg());
                }
                adapter.notifyDataSetChanged();
            } catch (NullPointerException e) {
                showInfo("请选择正确的图片");
            }
        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            //相机
            holderImgList.remove(holderImgList.size() - 1);
            String url = BastiGallery.getmCurrentPhotoPath();
            String fileName = BastiGallery.getPathFromCamera(data, url);
            byte[] img = BastiGallery.Bitmap2Bytes(BastiGallery.getimage(fileName));
            long length=img.length/1024;//读出图片的kb大小
            if(length>128){
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);//如
                img=baos.toByteArray();
            }
            holderImgList.get(selectImgPosition).setUrl("file://".concat(fileName));
            holderImgList.get(selectImgPosition).setBytes(img);
            holderImgList.get(selectImgPosition).setHasUpload(false);
            if (holderImgList.size() < 9) {
                holderImgList.add(new HolderImg());
            }
            adapter.notifyDataSetChanged();
        } else if (resultCode == 0x777 && data != null) {
            holderImgList.remove(holderImgList.size() - 1);
            String fileName = data.getStringExtra("fileName");
            byte[] img = BastiGallery.Bitmap2Bytes(BastiGallery.getimage(fileName));
            HolderImg holderImg = new HolderImg();
            holderImg.setUrl("file://".concat(fileName));
            holderImg.setBytes(img);
            holderImg.setHasUpload(false);
            holderImgList.add(holderImg);
            if (holderImgList.size() < 9) {
                holderImgList.add(new HolderImg());
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void initData() {
        urlList = new ArrayList<>();
        for(int i=0;i<9;i++){
            urlList.add("");
        }
        groupId = getIntent().getIntExtra("groupId", 0);
    }


    private void uploadImg() {
        if (TextUtils.isEmpty(holderImgList.get(0).getUrl()) && checkInputValid()) {
            showProgressBar(Config.upload);
            updateFriendCircle(edContent.getText().toString().trim());
        } else {
            showProgressBar(Config.upload);
            images = new String[holderImgList.size()];
            for (int i = 0; i < holderImgList.size(); i++) {
                HolderImg holderImg = holderImgList.get(i);
                images[i] = holderImg.getUrl();
                if (!TextUtils.isEmpty(holderImg.getUrl())) {
                    upLoadFeedBackImg(holderImg.getBytes(), i);
                }
            }
        }
    }

    private void upLoadFeedBackImg(byte[] img, final int indexP) {
        QiniuUtils.getUploadManagerInstance();
        int positionP=indexP+1;
        Log.d("图片上传大小提示TAG","img"+positionP+"size:"+String.valueOf(img.length));
        //已经上传过的不上传
        QiniuUtils.uploadImg(this, img, QiniuUtils.createImageKey(UserInfoUtils.getPhone(this)), new UpLoadImgCallback() {
            @Override
            public void onSuccess(String imgUrl) {
                holderImgList.get(indexP).setHasUpload(true);
                holderImgList.get(indexP).setUrl(imgUrl);
                urlList.set(indexP, imgUrl);
                if (allImgUploadSuccess() && checkInputValid()) {
                    updateFriendCircle(edContent.getText().toString().trim());
                }
            }

            @Override
            public void onFailure() {
                showProgressBar(false);
                showInfo("第"+positionP+"图片上传失败，请重试");
            }
        });
    }

    private boolean allImgUploadSuccess() {
        images = new String[holderImgList.size()];
        for (int i = 0; i < holderImgList.size(); i++) {
            HolderImg holderImg = holderImgList.get(i);
            images[i] = holderImg.getUrl();
            if (!TextUtils.isEmpty(holderImg.getUrl())) {
                if (!holderImg.isHasUpload()) {
                    return false;
                }
            }
        }
        return true;
    }


    private boolean checkInputValid() {
        if (TextUtils.isEmpty(edContent.getText().toString().trim())) {
            showInfo("内容不能为空！");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private String[] images;

    private void updateFriendCircle(String content) {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().updateFriendCircle(
                UserInfoUtils.getAppUserId(this), content, images),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData baseData) {
                        showProgressBar(false);
                        if (!NetworkResultUtils.isSuccess(baseData)) {
                            showInfo("上传失败");
                            return;
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        showProgressBar(false);
                        Toast.makeText(AddCircleTaskActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
