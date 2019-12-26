package com.caesar.rongcloudspeed.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caesar.rongcloudspeed.R;

import java.io.File;

import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.ImageLoader;

import static io.rong.imageloader.core.assist.ImageScaleType.IN_SAMPLE_POWER_OF_2;

public class ImageLoaderUtils {


    private static DisplayImageOptions privateOptions;
    private static DisplayImageOptions groupOptions;

    static {
        privateOptions = createDefaultDisplayOptions(R.drawable.rc_default_portrait);
        groupOptions = createDefaultDisplayOptions(R.drawable.rc_default_group_portrait);
    }

    public static void displayUserPortraitImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView, privateOptions);
//        ImageLoader.getInstance().displayImage(uri, imageView, privateOptions, null);
    }


    public static void displayGroupPortraitImage(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView, groupOptions, null);
    }


    private static DisplayImageOptions createDefaultDisplayOptions(int defaultImgId) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnFail(defaultImgId == 0 ? R.drawable.rc_default_portrait : defaultImgId) //设置加载失败的图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(IN_SAMPLE_POWER_OF_2)
                .considerExifParams(true)
                .build();
        return builder.build();
    }

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeholder)
                .error(error).into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy( DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy( DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }
    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy( DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .thumbnail(0.5f)
                .into(imageView);
    }
    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .format( DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy( DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }
    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy( DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }
    public static void displayRound(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
//        Glide.with(context).load(url)
//                .diskCacheStrategy( DiskCacheStrategy.ALL)
//                .error(R.drawable.toux2)
//                .centerCrop().transform(new GlideRoundTransformUtil(context)).into(imageView);
    }
    public static void displayRound(Context context, ImageView imageView, int resId) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
//        Glide.with(context).load(resId)
//                .diskCacheStrategy( DiskCacheStrategy.ALL)
//                .error(R.drawable.toux2)
//                .centerCrop().transform(new GlideRoundTransformUtil(context)).into(imageView);
    }
}
