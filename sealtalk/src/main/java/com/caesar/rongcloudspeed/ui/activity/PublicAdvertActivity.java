package com.caesar.rongcloudspeed.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.BaiduTextBean;
import com.caesar.rongcloudspeed.bean.QiniuBaseBean;
import com.caesar.rongcloudspeed.bean.QiniuBean;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.config.Config;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.quick.QiniuLabConfig;
import com.caesar.rongcloudspeed.util.ToastUitl;
import com.caesar.rongcloudspeed.utils.GlideEngine;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.Tools;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.luck.picture.lib.PictureExternalPreviewActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.utils.AsyncRun;
import com.yiw.circledemo.utils.BastiGallery;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.caesar.rongcloudspeed.utils.QiniuUtils.zone2;
import static com.caesar.rongcloudspeed.utils.ToastUtils.showToast;

/**
 * @author HIAPAD
 */
public class PublicAdvertActivity extends Activity implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PublicAdvertActivityLog";
    private static final int REQUEST_CODE_SELECT_INDUSTRY = 1001;
    private static final int REQUEST_CODE_SELECT_PROFESSION = 1002;
    private static final int REQUEST_CODE_SELECT_SOFT = 1003;
    private static final int REQUEST_CODE_SELECT_PAY = 1000;
    TextView post_title_text;
    EditText post_edit_title;
    EditText post_edit_mobile;
    EditText post_edit_content;
    Button post_commit_btn;
    private ImageView post_advert_image;
    private ImageView post_advert_del;
    private String mParam1;
    private String mParam2;
    private int REQUEST_CODE = 120;
    public static final int RESULT_OK = -1;
    private String postTitle;
    private String phoneNumber;
    private String postContent;
    private String thumbVideoString;
    private String uidString;
    private int themeId;
    /**
     * 默认是发布出售
     */
    public static int CODE_SUCC = 101;
    private PictureParameterStyle mPictureParameterStyle;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private ItemTouchHelper mItemTouchHelper;
    private TextView tv_right;

    private ProgressDialog mProgressDialog;

    private LinearLayout uploadStatusLayout;
    private ProgressBar uploadProgressBar;
    private TextView uploadSpeedTextView;
    private TextView uploadFileLengthTextView;
    private TextView uploadPercentageTextView;
    private LocalMedia localMedia;
    private String[] images;
    private String numberCount;

    SuperTextView post_industy_btn;
    SuperTextView post_profession_btn;
    SuperTextView post_soft_btn;
    private String industryIDString;
    private String professionIDString;
    private String softIDString;
    private String baiduToken;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_advert_detail);
        this.uploadProgressBar = (ProgressBar) this
                .findViewById(R.id.advert_video_upload_progressbar);
        this.uploadProgressBar.setMax(100);
        this.uploadStatusLayout = (LinearLayout) this
                .findViewById(R.id.advert_video_upload_status_layout);
        this.uploadSpeedTextView = (TextView) this
                .findViewById(R.id.advert_video_upload_speed_textview);
        this.uploadFileLengthTextView = (TextView) this
                .findViewById(R.id.advert_video_upload_file_length_textview);
        this.uploadPercentageTextView = (TextView) this
                .findViewById(R.id.advert_video_upload_percentage_textview);
        post_title_text = (TextView) this.findViewById(R.id.post_title_text);
        post_edit_title = (EditText) this.findViewById(R.id.post_edit_title);
        post_advert_del = (ImageView) this.findViewById(R.id.post_advert_del);
        post_advert_image = (ImageView) this.findViewById(R.id.post_advert_image);

        post_industy_btn = (SuperTextView) this.findViewById(R.id.post_industy_btn);
        post_profession_btn = (SuperTextView) this.findViewById(R.id.post_profession_btn);
        post_soft_btn = (SuperTextView) this.findViewById(R.id.post_soft_btn);

        post_edit_mobile = (EditText) this.findViewById(R.id.post_edit_mobile);
        post_edit_content = (EditText) this.findViewById(R.id.post_edit_content);
        post_commit_btn = (Button) this.findViewById(R.id.post_commit_btn);
        uidString = UserInfoUtils.getAppUserId(this);
        baiduToken = UserInfoUtils.getBaiduToken(this);
        phoneNumber = UserInfoUtils.getPhone(this);
        if (uidString.equals("0")) {
            post_edit_mobile.setText("");
        } else {
            post_edit_mobile.setText(phoneNumber);
        }
        post_edit_title.setHint("在此输入广告标语");
        post_edit_content.setHint("请详细描述您所发布的广告内容");
        post_title_text.setText("同行广告发布");
        post_commit_btn.setOnClickListener(this);
        post_advert_image.setOnClickListener(this);
        QiniuUtils.getUploadManagerInstance();
        themeId = R.style.picture_default_style;
        getDefaultStyle();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);

        post_industy_btn.setOnClickListener(this);
        post_profession_btn.setOnClickListener(this);
        post_soft_btn.setOnClickListener(this);
    }

    public Context getContext() {
        return this;
    }

    private void getDefaultStyle() {
        // 相册主题
        mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = false;
        // 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_grey);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa632d);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa632d);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_grey);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(getContext(), R.color.app_color_white);
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");
//        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
//        mPictureParameterStyle.pictureCompleteText = "";
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";
//
//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;

        // 裁剪主题
        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(getContext(), R.color.app_color_grey),
                ContextCompat.getColor(getContext(), R.color.app_color_grey),
                Color.parseColor("#393a3e"),
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                mPictureParameterStyle.isChangeStatusBarFontColor);
    }


    public void postBackAction(View view) {
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        uidString = UserInfoUtils.getAppUserId(PublicAdvertActivity.this);
        phoneNumber = UserInfoUtils.getPhone(PublicAdvertActivity.this);
        if (uidString.equals("0")) {
            post_edit_mobile.setText("");
        } else {
            post_edit_mobile.setText(phoneNumber);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_SELECT_INDUSTRY) {
                industryIDString = data.getStringExtra("industryIDString");
                post_industy_btn.setRightString(data.getStringExtra("industryNameString"));
            } else if (requestCode == REQUEST_CODE_SELECT_PROFESSION) {
                professionIDString = data.getStringExtra("professionIDString");
                post_profession_btn.setRightString(data.getStringExtra("professionNameString"));
            } else if (requestCode == REQUEST_CODE_SELECT_SOFT) {
                softIDString = data.getStringExtra("softIDString");
                post_soft_btn.setRightString(data.getStringExtra("softNameString"));
            } else if (requestCode == REQUEST_CODE_SELECT_PAY) {
                String mimeType = data.getStringExtra("mimeType");
                if (mimeType.equals("image")) {
                    String photos_url = data.getStringExtra("photos_url");
                    images = new String[]{photos_url};
                    purchaseHandler.sendEmptyMessage(1);
                } else if (mimeType.equals("video")) {
                    thumbVideoString = data.getStringExtra("thumb_video");
                    purchaseHandler.sendEmptyMessage(3);
                }
                showProgressBar(true, R.string.upload);
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_advert_image:
                if (localMedia == null) {
                    // 进入相册 以下是例子：不需要的api可以不写
                    PictureSelector.create(PublicAdvertActivity.this)
                            .openGallery(PictureConfig.TYPE_ALL)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                            .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                            .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                            .isWeChatStyle(false)// 是否开启微信图片选择风格
                            .isUseCustomCamera(false)// 是否使用自定义相机
                            .setLanguage(LanguageConfig.CHINESE)// 设置语言，默认中文
                            .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                            .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                            .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                            .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                            .maxSelectNum(1)// 最大图片选择数量
                            .minSelectNum(1)// 最小选择数量
                            .maxVideoSelectNum(1) // 视频最大选择数量，如果没有单独设置的需求则可以不设置，同用maxSelectNum字段
                            //.minVideoSelectNum(1)// 视频最小选择数量，如果没有单独设置的需求则可以不设置，同用minSelectNum字段
                            .imageSpanCount(3)// 每行显示个数
                            .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                            //.isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && enableCrop(false);有效,默认处理
                            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                            .isOriginalImageControl(false)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                            //.bindCustomPlayVideoCallback(callback)// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                            //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                            //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
                            //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
                            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                            .isSingleDirectReturn(false)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                            .previewImage(true)// 是否可预览图片
                            .previewVideo(true)// 是否可预览视频
                            //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                            .enablePreviewAudio(true) // 是否可播放音频
                            .isCamera(true)// 是否显示拍照按钮
                            //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                            //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                            .enableCrop(false)// 是否裁剪
                            //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                            .compress(false)// 是否压缩
                            //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                            //.compressSavePath(getPath())//压缩图片保存地址
                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                            //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                            .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                            .isGif(false)// 是否显示gif图片
                            .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                            .circleDimmedLayer(false)// 是否圆形裁剪
                            //.setCircleDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置圆形裁剪背景色值
                            //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                            //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                            .openClickSound(false)// 是否开启点击声音
                            .selectionMedia(null)// 是否传入已选图片
                            //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                            //.videoMinSecond(10)
                            //.videoMaxSecond(15)
                            //.recordVideoSecond(10)//录制视频秒数 默认60s
                            //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                            //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                            .cutOutQuality(90)// 裁剪输出质量 默认100
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                            //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                            //.rotateEnabled(false) // 裁剪是否可旋转图片
                            //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                            //.videoQuality()// 视频录制质量 0 or 1
                            //.videoSecond()//显示多少秒以内的视频or音频也可适用
                            //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                            .forResult(new OnResultCallbackListener() {
                                @Override
                                public void onResult(List<LocalMedia> result) {
                                    for (LocalMedia media : result) {
                                        Log.i(TAG, "是否压缩:" + media.isCompressed());
                                        Log.i(TAG, "压缩:" + media.getCompressPath());
                                        Log.i(TAG, "原图:" + media.getPath());
                                        Log.i(TAG, "是否裁剪:" + media.isCut());
                                        Log.i(TAG, "裁剪:" + media.getCutPath());
                                        Log.i(TAG, "是否开启原图:" + media.isOriginal());
                                        Log.i(TAG, "原图路径:" + media.getOriginalPath());
                                        Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
                                        Log.d(TAG, "文件时长:" + media.getDuration());
                                        Log.d(TAG, "文件类型:" + media.getMimeType());
                                        localMedia = media;
                                        if (media.getMimeType().equals("image/jpeg")) {
                                            uploadStatusLayout.setVisibility(LinearLayout.VISIBLE);
                                        } else if (media.getMimeType().equals("video/mp4")) {
                                            if (media.getSize() <= 20 * 1024 * 1024 && media.getDuration() <= 30 * 1000) {
                                                uploadStatusLayout.setVisibility(LinearLayout.VISIBLE);
                                            } else {
                                                uploadStatusLayout.setVisibility(LinearLayout.VISIBLE);
                                            }
                                        }
                                        Object imageModel = media.getPath().startsWith("content://") && !media.isCut() && !media.isCompressed() ? Uri.parse(media.getPath())
                                                : media.getPath();
                                        Glide.with(getContext())
                                                .load(imageModel)
                                                .centerCrop()
                                                .placeholder(R.color.app_color_f6)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(post_advert_image);
                                        post_advert_del.setVisibility(View.VISIBLE);
                                        post_advert_del.setOnClickListener(view -> {
                                            localMedia = null;
                                            post_advert_del.setVisibility(View.INVISIBLE);
                                            post_advert_image.setImageResource(R.drawable.addphoto);
                                        });
                                    }
                                }

                                @Override
                                public void onCancel() {
                                    Log.i(TAG, "PictureSelector Cancel");
                                }
                            });
                } else {
                    String mimeType = localMedia.getMimeType();
                    int mediaType = PictureMimeType.getMimeType(mimeType);
                    switch (mediaType) {
                        case PictureConfig.TYPE_VIDEO:
                            // 预览视频
                            PictureSelector.create(PublicAdvertActivity.this)
                                    .themeStyle(R.style.picture_default_style)
                                    .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                    .externalPictureVideo(localMedia.getPath());
                            break;
                        default:
                            // 预览图片 可自定长按保存路径
                            List<LocalMedia> medias = new ArrayList<LocalMedia>();
                            medias.add(localMedia);
                            Intent intent = new Intent(PublicAdvertActivity.this, PictureExternalPreviewActivity.class);
                            intent.putParcelableArrayListExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST,
                                    (ArrayList<? extends Parcelable>) medias);
                            intent.putExtra(PictureConfig.EXTRA_POSITION, 0);
                            startActivity(intent);
                            overridePendingTransition(com.luck.picture.lib.R.anim.picture_anim_enter, com.luck.picture.lib.R.anim.picture_anim_fade_in);
                            break;
                    }
                }
                break;
            case R.id.post_industy_btn:
                startActivityForResult(new Intent(this, IndustrySelectActivity.class), REQUEST_CODE_SELECT_INDUSTRY);
                break;
            case R.id.post_profession_btn:
                startActivityForResult(new Intent(this, ProfessionSelectActivity.class), REQUEST_CODE_SELECT_PROFESSION);
                break;
            case R.id.post_soft_btn:
                startActivityForResult(new Intent(this, SoftSelectActivity.class), REQUEST_CODE_SELECT_SOFT);
                break;
            case R.id.post_commit_btn:
                postTitle = post_edit_title.getText().toString();
                phoneNumber = post_edit_mobile.getText().toString();
                postContent = post_edit_content.getText().toString();
                if (uidString.equals("0")) {
                    Intent loginIntent = new Intent(PublicAdvertActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    if (TextUtils.isEmpty(postTitle) && postTitle.length() > 5) {
                        ToastUitl.showToastWithImg(getString(R.string.circle_publish_empty), R.drawable.ic_warm);
                    } else {
                        if (TextUtils.isEmpty(phoneNumber)) {
                            ToastUitl.showToastWithImg(getString(R.string.circle_mobile_empty), R.drawable.ic_warm);
                        } else {
                            if (TextUtils.isEmpty(industryIDString) || TextUtils.isEmpty(professionIDString) || TextUtils.isEmpty(softIDString)) {
                                ToastUitl.showToastWithImg("广告分类不能为空", R.drawable.ic_warm);
                            } else {
                                if (localMedia != null) {
                                    if (localMedia.getMimeType().startsWith("video")) {
                                        if (!(localMedia.getSize() <= 20 * 1024 * 1024 && localMedia.getDuration() <= 30 * 1000)) {
                                            ToastUitl.showToastWithImg("视频内容一般不超过30秒，不超过20MB", R.drawable.ic_warm);
                                            return;
                                        }
                                    }
                                    mProgressDialog.show();
                                    purchaseHandler.sendEmptyMessage(4);
//                                showProgressBar(true, R.string.upload);
                                } else {
                                    ToastUitl.showToastWithImg(getString(R.string.circle_image_empty), R.drawable.ic_warm);
                                }
                            }
                        }

                    }
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler purchaseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    boolean isAndroidQ = SdkVersionUtils.checkedAndroid_Q();
                    byte[] img = BastiGallery.Bitmap2Bytes(BastiGallery.getimage(isAndroidQ?localMedia.getAndroidQToPath():localMedia.getPath()));
                    //已经上传过的不上传
                    QiniuUtils.uploadImg(PublicAdvertActivity.this, img, QiniuUtils.createImageKey(phoneNumber), new UpLoadImgCallback() {
                        @Override
                        public void onSuccess(String imgUrl) {
//                            images = new String[]{imgUrl};
                            showProgressBar(false);
                            Intent intent = new Intent(PublicAdvertActivity.this, SeekHelperOrderActivity.class);
                            intent.putExtra("seek_name", postTitle);
                            intent.putExtra("seek_price", "100");
                            intent.putExtra("seek_type", "4");
                            intent.putExtra("mimeType", "image");
                            intent.putExtra("photos_url", imgUrl);
                            intent.putExtra("industryIDString", industryIDString);
                            intent.putExtra("professionIDString", professionIDString);
                            intent.putExtra("softIDString", softIDString);
                            startActivityForResult(intent, REQUEST_CODE_SELECT_PAY);
//                            purchaseHandler.sendEmptyMessage(1);
                        }

                        @Override
                        public void onFailure() {
                            showProgressBar(false);
                            ToastUtils.s(getContext(), "图片上传失败，请重试");
                        }
                    });
                    break;
                case 1:
                    if (TextUtils.isEmpty(postContent)) {
                        postContent = postTitle;
                    }
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().AddPostCartArticle(uidString, "42", postTitle, phoneNumber, images, postContent),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    showProgressBar(false);
                                    if (NetworkResultUtils.isSuccess(baseData)) {
                                        Toast.makeText(PublicAdvertActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(PublicAdvertActivity.this, "上传失败", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    showProgressBar(false);
                                    Toast.makeText(PublicAdvertActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });
                    break;
                case 2:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getQiniuToken(uidString),
                            new NetworkCallback<QiniuBaseBean>() {
                                @Override
                                public void onSuccess(QiniuBaseBean qiniuBaseBean) {
                                    if (qiniuBaseBean.getCode() == Constant.CODE_SUCC) {
                                        String uploadToken = qiniuBaseBean.getReferer();
                                        uploadData(uploadToken, QiniuLabConfig.REMOTE_SERVICE_SERVER);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(PublicAdvertActivity.this, R.string.qiniu_get_upload_token_failed, Toast.LENGTH_LONG).show();
                                }
                            });
                    break;
                case 3:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().AddPostAdvertType(uidString, "42", postTitle, phoneNumber, thumbVideoString, postContent, "4"),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    showProgressBar(false);
                                    if (NetworkResultUtils.isSuccess(baseData)) {
                                        Toast.makeText(PublicAdvertActivity.this, "信息发布成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(PublicAdvertActivity.this, "信息发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    showProgressBar(false);
                                    Toast.makeText(PublicAdvertActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                                }
                            });

                    break;
                case 4:
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitBaiduApi().getBaiduTextCheck(postTitle, baiduToken),
                            new NetworkCallback<BaiduTextBean>() {
                                @Override
                                public void onSuccess(BaiduTextBean baiduTextBean) {
                                    int conclusionType = baiduTextBean.getConclusionType();
                                    if (conclusionType == 1) {
                                        if (localMedia.getMimeType().startsWith("image")) {
                                            purchaseHandler.sendEmptyMessage(0);
                                        } else if (localMedia.getMimeType().startsWith("video")) {
                                            purchaseHandler.sendEmptyMessage(2);
                                        }
//                                        Intent intent = new Intent(PublicAdvertActivity.this, SeekHelperOrderActivity.class);
//                                        intent.putExtra("seek_name", postTitle);
//                                        intent.putExtra("seek_price", "100");
//                                        intent.putExtra("seek_type", "4");
//                                        intent.putExtra("industryIDString", industryIDString);
//                                        intent.putExtra("professionIDString", professionIDString);
//                                        intent.putExtra("softIDString", softIDString);
//                                        startActivityForResult(intent, REQUEST_CODE_SELECT_PAY);
//                                        purchaseHandler.sendEmptyMessage(0);
                                    } else {
                                        mProgressDialog.dismiss();
                                        BaiduTextBean.BaiduTextData baiduTextData = baiduTextBean.getData().get(0);
                                        ToastUitl.showToastWithImg(baiduTextData.getMsg(), R.drawable.ic_warm);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(PublicAdvertActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
                default:

                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mProgressDialog.setMessage("正在处理，请稍后...");
                    mProgressDialog.show();
                    NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().AddPostCartArticle(uidString, "42", postTitle, phoneNumber, thumbVideoString, postContent),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    if (NetworkResultUtils.isSuccess(baseData)) {
                                        post_edit_title.setText("");
                                        post_edit_content.setText("");
                                        Toast.makeText(PublicAdvertActivity.this, "信息发布成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PublicAdvertActivity.this, "信息发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                    mProgressDialog.dismiss();
                                    setResult(RESULT_OK, getIntent());
                                    finish();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(PublicAdvertActivity.this, "信息发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                    mProgressDialog.dismiss();
                                }
                            });

                    break;
                default:
                    break;
            }
        }
    };

    private void upLoadAllFeedBackImg(String pathString) {
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "upLoadAllFeedBackImg.onCancel");
            }
        });
        mProgressDialog.setMessage("正在上传");
        mProgressDialog.show();
        Bitmap bitmap = BastiGallery.getimage(pathString);
        byte[] img = compressBitmap(bitmap, 128);
        QiniuUtils.uploadImg(PublicAdvertActivity.this, img, QiniuUtils.createImageKey(UserInfoUtils.getPhone(PublicAdvertActivity.this)), new UpLoadImgCallback() {
            @Override
            public void onSuccess(String imgUrl) {

                Log.e("111111111111", "imgUrl = " + imgUrl);
                mProgressDialog.dismiss();
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure() {
                mProgressDialog.dismiss();
            }
        });

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void showProgressBar(boolean show, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    public void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    protected void showProgressBar(boolean show, int message) {
        String s = getString(message);
        showProgressBar(show, s);
    }

    public void showProgressBar(int type) {
        switch (type) {
            case Config.download:
                showProgressBar(true, R.string.searching);
                break;
            case Config.upload:
                showProgressBar(true, R.string.upload);
                break;
            case Config.login:
                showProgressBar(true, R.string.logining);
                break;
            case Config.delete:
                showProgressBar(true, R.string.deleting);
                break;
            case Config.check:
                showProgressBar(true, R.string.checking);
                break;
            case Config.cancel:
                showProgressBar(true, R.string.canceling);
                break;
            case Config.logout:
                showProgressBar(true, R.string.logout_account);
                break;
            case Config.scaning:
                showProgressBar(true, R.string.scaning);
                break;
            case Config.update:
                showProgressBar(true, R.string.update);
                break;
            case Config.create:
                showProgressBar(true, R.string.create);
                break;
            default:
                break;
        }
    }


    private UploadManager uploadManager;

    private void uploadData(final String uploadToken, final String domain) {
        if (this.uploadManager == null) {
            Configuration config = new Configuration.Builder()
                    .chunkSize(1024 * 1024)        // 分片上传时，每片的大小。 默认256K
                    .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                    .connectTimeout(10)           // 链接超时。默认10秒
                    .responseTimeout(60)          // 服务器响应超时。默认60秒
                    //.recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
                    //.recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                    .zone(zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                    .build();
            this.uploadManager = new UploadManager(config);
        }
        boolean isAndroidQ = SdkVersionUtils.checkedAndroid_Q();
        File uploadFile = new File(isAndroidQ ? localMedia.getAndroidQToPath() : localMedia.getPath());
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {
                        updateStatus(percent);
                    }
                }, null);

        final long startTime = System.currentTimeMillis();
        final long fileLength = uploadFile.length();
        this.uploadFileLength = fileLength;
        this.uploadLastTimePoint = startTime;
        this.uploadLastOffset = 0;
        // prepare status
        AsyncRun.run(new Runnable() {
            @Override
            public void run() {
                //clear old status
                uploadPercentageTextView.setText("0 %");
                uploadSpeedTextView.setText("0 KB/s");
                uploadFileLengthTextView.setText(Tools.formatSize(fileLength));
                uploadStatusLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        this.uploadManager.put(uploadFile, null, uploadToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo respInfo,
                                         JSONObject jsonData) {
                        // reset status
                        AsyncRun.run(new Runnable() {
                            @Override
                            public void run() {
                                uploadStatusLayout
                                        .setVisibility(LinearLayout.VISIBLE);
                                uploadProgressBar.setProgress(0);
                            }
                        });
                        long lastMillis = System.currentTimeMillis()
                                - startTime;
                        if (respInfo.isOK()) {
                            try {
                                String fileKey = jsonData.getString("key");
                                final String persistentId = jsonData.getString("hash");
                                final String videoUrl = domain + "/" + fileKey;
                                AsyncRun.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "hash:" + persistentId);
                                        Log.d(TAG, "videoUrl:" + videoUrl);

                                        showToast(R.string.qiniu_upload_success, Toast.LENGTH_LONG);
                                    }
                                });
                                showProgressBar(false);
                                thumbVideoString = videoUrl;
                                Intent intent = new Intent(PublicAdvertActivity.this, SeekHelperOrderActivity.class);
                                intent.putExtra("seek_name", postTitle);
                                intent.putExtra("seek_price", "100");
                                intent.putExtra("seek_type", "4");
                                intent.putExtra("mimeType", "video");
                                intent.putExtra("thumb_video", videoUrl);
                                intent.putExtra("industryIDString", industryIDString);
                                intent.putExtra("professionIDString", professionIDString);
                                intent.putExtra("softIDString", softIDString);
                                startActivityForResult(intent, REQUEST_CODE_SELECT_PAY);
//                                purchaseHandler.sendEmptyMessage(3);
                            } catch (JSONException e) {
                                Toast.makeText(
                                        getContext(),
                                        getContext().getString(R.string.qiniu_upload_file_response_parse_error),
                                        Toast.LENGTH_LONG).show();
                                Log.e(QiniuLabConfig.LOG_TAG, e.getMessage());
                                showProgressBar(false);
                            }
                        } else {
                            Toast.makeText(
                                    getContext(),
                                    getContext().getString(R.string.qiniu_upload_file_failed),
                                    Toast.LENGTH_LONG).show();
                            Log.e(QiniuLabConfig.LOG_TAG, respInfo.toString());
                            showProgressBar(false);
                        }
                    }

                }, uploadOptions);
    }

    private long uploadLastTimePoint;
    private long uploadLastOffset;
    private long uploadFileLength;

    private void updateStatus(final double percentage) {
        long now = System.currentTimeMillis();
        long deltaTime = now - uploadLastTimePoint;
        long currentOffset = (long) (percentage * uploadFileLength);
        long deltaSize = currentOffset - uploadLastOffset;
        if (deltaTime <= 100) {
            return;
        }

        final String speed = Tools.formatSpeed(deltaSize, deltaTime);
        // update
        uploadLastTimePoint = now;
        uploadLastOffset = currentOffset;

        AsyncRun.run(new Runnable() {
            @Override
            public void run() {
                int progress = (int) (percentage * 100);
                uploadProgressBar.setProgress(progress);
                uploadPercentageTextView.setText(progress + " %");
                uploadSpeedTextView.setText(speed);
            }
        });
    }

}
