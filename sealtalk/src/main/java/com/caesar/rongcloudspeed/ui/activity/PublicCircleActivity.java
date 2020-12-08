package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.allen.library.SuperTextView;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.BaiduTextBean;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.common.NoScrollGridView;
import com.caesar.rongcloudspeed.config.Config;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.adapter.NinePicturesAdapter;
import com.caesar.rongcloudspeed.util.ToastUitl;
import com.caesar.rongcloudspeed.util.ToastUtils;
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
 * @author HIAPAD
 */
public class PublicCircleActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "PublicCircleActivity";
    TextView post_title_add;
    EditText post_edit_title;
    NoScrollGridView post_image_gridview;
    private NinePicturesAdapter ninePicturesAdapter;
    private int REQUEST_CODE = 120;
    public static final int RESULT_OK = -1;
    private String postTitle;
    private String photos_url[] = new String[9];
    private String uidString;
    private List<String> pathStringList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String baiduToken;
    /**
     * 默认是发布出售
     */
    public static int CODE_SUCC = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_circle_detail);
        post_title_add = (TextView) this.findViewById(R.id.post_title_add);
        post_edit_title = (EditText) this.findViewById(R.id.post_edit_title);
        post_image_gridview = (NoScrollGridView) this.findViewById(R.id.post_image_gridview);
        uidString = UserInfoUtils.getAppUserId(this);
        baiduToken = UserInfoUtils.getBaiduToken(this);
        ninePicturesAdapter = new NinePicturesAdapter(this, 9, new NinePicturesAdapter.OnClickAddListener() {
            @Override
            public void onClickAdd(int positin) {
                choosePhoto();
            }
        });
        post_image_gridview.setAdapter(ninePicturesAdapter);
        post_title_add.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在处理，请稍后...");
    }


    public void postBackAction(View view) {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        uidString = UserInfoUtils.getAppUserId(PublicCircleActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_title_add:
                postTitle = post_edit_title.getText().toString();
                if (TextUtils.isEmpty(postTitle)) {
                    ToastUtils.showShortToast("内容不能为空！");
                } else {
                    hideInput();
                    List<String> imageList = ninePicturesAdapter.getData();
                    pathStringList = new ArrayList<>();
                    for (int i = 0; i < imageList.size(); i++) {
                        String path = imageList.get(i);
                        if (path.length() > 12) {
                            pathStringList.add(path);
                        }
                    }
                    if (pathStringList.size() > 0) {
                        progressDialog.show();
                        circleHandler.sendEmptyMessageDelayed(0, 500);
                    } else {
                        if (postTitle.length() > 12) {
                            progressDialog.show();
                            circleHandler.sendEmptyMessage(0);
                        } else {
                            ToastUtils.showShortToast("无图片描述建议12个字符以上！");
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler circleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitBaiduApi().getBaiduTextCheck(postTitle, baiduToken),
                            new NetworkCallback<BaiduTextBean>() {
                                @Override
                                public void onSuccess(BaiduTextBean baiduTextBean) {
                                    int conclusionType = baiduTextBean.getConclusionType();
                                    if (conclusionType == 1) {
                                        if (pathStringList.size() > 0) {
                                            circleHandler.sendEmptyMessage(1);
                                        } else {
                                            circleHandler.sendEmptyMessage(2);
                                        }

                                    } else {
                                        progressDialog.dismiss();
                                        BaiduTextBean.BaiduTextData baiduTextData = baiduTextBean.getData().get(0);
                                        ToastUitl.showToastWithImg(baiduTextData.getMsg(), R.drawable.ic_warm);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PublicCircleActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
                case 1:
                    upLoadAllFeedBackImg(pathStringList);
                    break;
                case 2:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().uploadFriendCircle(uidString, "41", postTitle, photos_url),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    if (NetworkResultUtils.isSuccess(baseData)) {
                                        Toast.makeText(PublicCircleActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PublicCircleActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(PublicCircleActivity.this, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                    break;
                default:
                    break;
            }
        }
    };

    private void upLoadAllFeedBackImg(List<String> pathList) {
        QiniuUtils.getUploadManagerInstance();
        //已经上传过的不上传
        photos_url = new String[pathList.size()];
        for (int i = 0; i < pathList.size(); i++) {
            String fileName = pathList.get(i);
            Bitmap bitmap = BastiGallery.getimage(fileName);
            Log.d("upLoadBitmap", i + "bitmap.size = " + bitmap.getByteCount());
            byte[] img = compressBitmap(bitmap, 128);
            int finalI = i;
            Log.d("upLoadBitmap", i + "img.size = " + img.length);
            QiniuUtils.uploadImg(PublicCircleActivity.this, img, QiniuUtils.createImageKey(UserInfoUtils.getPhone(PublicCircleActivity.this)), new UpLoadImgCallback() {
                @Override
                public void onSuccess(String imgUrl) {
                    photos_url[finalI] = imgUrl;
                    Log.d("uploadSuccess", finalI + "imgUrl = " + imgUrl);
                    if (finalI == pathList.size() - 1) {
                        circleHandler.sendEmptyMessage(2);
                    }
                }

                @Override
                public void onFailure() {
                    Log.e("uploadFailure", "imgUrl = " + finalI);
                    ToastUtils.showShortToast("第" + finalI + "图片上传失败，请重试");
                }
            });
        }
    }


    public static byte[] compressBitmap(Bitmap bitmap, float size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (BastiGallery.Bitmap2Bytes(bitmap).length <= size * 1024) {

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }

        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);//如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
        while (baos.toByteArray().length / 1024f > size) {
            quality = quality - 4;// 每次都减少4
            baos.reset();// 重置baos即清空baos
            if (quality <= 0) {
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            Log.d(TAG, "------quality--------" + baos.toByteArray().length / 1024f);
        }
        return baos.toByteArray();
    }

    /*
   添加全屏斜着45度的文字
   */
    public static Bitmap drawCenterLable(Context context, Bitmap bmp, String text) {
        float scale = context.getResources().getDisplayMetrics().density;
        //创建一样大小的图片
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        //创建画布
        Canvas canvas = new Canvas(newBmp);
        canvas.drawBitmap(bmp, 0, 0, null);  //绘制原始图片
        canvas.save();
        canvas.rotate(45); //顺时针转45度
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.argb(50, 255, 255, 255)); //白色半透明
        paint.setTextSize(100 * scale);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect rectText = new Rect();  //得到text占用宽高， 单位：像素
        paint.getTextBounds(text, 0, text.length(), rectText);
        double beginX = (bmp.getHeight() / 2 - rectText.width() / 2) * 1.4;  //45度角度值是1.414
        double beginY = (bmp.getWidth() / 2 - rectText.width() / 2) * 1.4;
        canvas.drawText(text, (int) beginX, (int) beginY, paint);
        canvas.restore();
        return newBmp;
    }

    /**
     * 开启图片选择器
     */
    private void choosePhoto() {
        ImgSelConfig config = new ImgSelConfig.Builder(loader)
                // 是否多选
                .multiSelect(true)
                // 确定按钮背景色
                .btnBgColor(Color.TRANSPARENT)
                .titleBgColor(ContextCompat.getColor(PublicCircleActivity.this, R.color.colorAccent))
                // 使用沉浸式状态栏
                .statusBarColor(ContextCompat.getColor(PublicCircleActivity.this, R.color.colorAccent))
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

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            ImageLoaderUtils.display(context, imageView, path);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE) {
                List<String> resultList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
                if (ninePicturesAdapter != null) {
                    ninePicturesAdapter.addAll(resultList);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
