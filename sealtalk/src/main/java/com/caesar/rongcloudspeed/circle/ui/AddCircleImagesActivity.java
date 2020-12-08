package com.caesar.rongcloudspeed.circle.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.callback.OnItemClickListener;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.circle.adapter.AddImgAdapter;
import com.caesar.rongcloudspeed.common.NoScrollGridView;
import com.caesar.rongcloudspeed.config.Config;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.HolderImg;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.PublicSeekActivity;
import com.caesar.rongcloudspeed.ui.adapter.NinePicturesAdapter;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.yiw.circledemo.utils.BastiGallery;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mathum on 2017/9/20.
 */

public class AddCircleImagesActivity extends BaseActivity {
    private static final String TAG = "AddCircleImagesActivity";
    EditText edContent;
    NoScrollGridView post_image_gridview;
    private ImageView back;
    private TextView put;
    private NinePicturesAdapter ninePicturesAdapter;
    private String contentString;
    private String[] photos_url= new String[9];
    private int REQUEST_CODE = 120;
    private List<String> imagePathList = new ArrayList<>();

    @Override
    protected int provideContentView() {
        return R.layout.activity_add_circle_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        edContent = findViewById(R.id.ed_content);
        post_image_gridview = (NoScrollGridView) this.findViewById(R.id.post_image_gridview);
        back = findViewById(R.id.back);
        put = findViewById(R.id.add);
        back.setOnClickListener(view -> {
            finish();
        });
        put.setOnClickListener(view -> {
            contentString = edContent.getText().toString();
            if (TextUtils.isEmpty(contentString)) {
                showInfo("内容不能为空！");
            } else {
                List<String> imageList = ninePicturesAdapter.getData();
                for (int i = 0; i < imageList.size(); i++) {
                    String path = imageList.get(i);
                    if (path.length() > 5) {
                        imagePathList.add(path);
                    }
                }
                if (imagePathList.size() > 0) {
                    addCircleHandler.sendEmptyMessage(0);
                }else{
                    if(contentString.length()>12){
                        showProgressBar(Config.upload);
                        addCircleHandler.sendEmptyMessage(1);
                    }else{
                        showInfo("无图片描述建议12个字符以上！");
                    }
                }
            }
        });

    }

    @SuppressLint("HandlerLeak")
    Handler addCircleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    upLoadAllFeedBackImg(imagePathList);
                    break;
                case 1:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().uploadFriendCircle(
                            UserInfoUtils.getAppUserId(AddCircleImagesActivity.this), "41", contentString, photos_url),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    showProgressBar(false);
                                    if (NetworkResultUtils.isSuccess(baseData)) {
                                        showInfo("上传成功");
                                    } else {
                                        showInfo("上传失败");
                                    }
                                    finish();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    showProgressBar(false);
                                    Toast.makeText(AddCircleImagesActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });
                    break;
                default:
                    break;
            }
        }
    };


    private void initAdapter() {
        ninePicturesAdapter = new NinePicturesAdapter(AddCircleImagesActivity.this, 9, new NinePicturesAdapter.OnClickAddListener() {
            @Override
            public void onClickAdd(int positin) {
                choosePhoto();
            }
        });
        post_image_gridview.setAdapter(ninePicturesAdapter);

    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            ImageLoaderUtils.display(context, imageView, path);
        }
    };

    /**
     * 开启图片选择器
     */
    private void choosePhoto() {
        ImgSelConfig config = new ImgSelConfig.Builder(loader)
                // 是否多选
                .multiSelect(true)
                // 确定按钮背景色
                .btnBgColor(Color.TRANSPARENT)
                .titleBgColor(ContextCompat.getColor(AddCircleImagesActivity.this, R.color.colorAccent))
                // 使用沉浸式状态栏
                .statusBarColor(ContextCompat.getColor(AddCircleImagesActivity.this, R.color.colorAccent))
                // 返回图标ResId
                .backResId(R.drawable.ic_arrow_back)
                .title("图片")
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(9 - ninePicturesAdapter.getPhotoCount())
                .build();
        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE) {
                List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                if (ninePicturesAdapter != null) {
                    ninePicturesAdapter.addAll(pathList);
                }
            }

        }
    }

    private void upLoadAllFeedBackImg(List<String> pathList) {
        hideInput();
        final ProgressDialog pd = new ProgressDialog(AddCircleImagesActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "EMClient.getInstance().onCancel");
            }
        });
        pd.setMessage("正在上传");
        pd.show();
        QiniuUtils.getUploadManagerInstance();
        //已经上传过的不上传
        final int[] count = {0};
        photos_url = new String[pathList.size()];
        for (String fileName : pathList) {
            Bitmap bitmap = BastiGallery.getimage(fileName);
            byte[] img = compressBitmap(bitmap, 128);
            QiniuUtils.uploadImg(AddCircleImagesActivity.this, img, QiniuUtils.createImageKey(UserInfoUtils.getPhone(AddCircleImagesActivity.this)), new UpLoadImgCallback() {
                @Override
                public void onSuccess(String imgUrl) {
                    photos_url[count[0]] = imgUrl;
                    count[0]++;
                    Log.e("111111111111", "imgUrl = " + imgUrl);
                    if (count[0] == pathList.size()) {
                        pd.dismiss();
                        addCircleHandler.sendEmptyMessage(1);
                    }
                }

                @Override
                public void onFailure() {
                    count[0]++;
                    if (count[0] == pathList.size()) {
                        pd.dismiss();
                        addCircleHandler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static byte[] compressBitmap(Bitmap bitmap, float size) {
        if (bitmap == null || BastiGallery.Bitmap2Bytes(bitmap).length <= size * 1024) {
            return null;//如果图片本身的大小已经小于这个大小了，就没必要进行压缩
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);//如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
        while (baos.toByteArray().length / 1024f > size) {
            quality = quality - 4;// 每次都减少4
            baos.reset();// 重置baos即清空baos
            if (quality <= 0) {
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            //Log.e(TAG,"------质量--------"+baos.toByteArray().length/1024f);
        }
        return baos.toByteArray();
    }

}
