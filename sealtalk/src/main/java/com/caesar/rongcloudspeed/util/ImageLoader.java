package com.caesar.rongcloudspeed.util;

import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图片加载库
 * Created by 43053 on 2016/6/15.
 */
public class ImageLoader {

    //显示图片
    public static void setImage(SimpleDraweeView simpleDraweeView, String url) {

        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        simpleDraweeView.setImageURI(url);
    }

    //清除缓存
    public static void clearCache() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearCaches();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();
    }

    //获取缓存大小
    public static long getCacheSize() {
        return Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
    }

    public static void setImageWithOutCorner(SimpleDraweeView imgView, int width, int height, String url) {
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        if (hasChinese(url)) {
            try {
                url = URLEncoder.encode(url, "UTF-8");
                url = url.replace("%3A", ":");
                url = url.replace("%2F", "/");
            } catch (Exception e) {
            }
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(width, height)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(imgView.getController())
                .setImageRequest(request).build();
        imgView.setController(controller);
    }


    public static void setImage(SimpleDraweeView imgView, int width, int height, String url) {
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        if (hasChinese(url)) {
            try {
                url = URLEncoder.encode(url, "UTF-8");
                url = url.replace("%3A", ":");
                url = url.replace("%2F", "/");
            } catch (Exception e) {
            }
        }
        if (!TextUtils.isEmpty(url)) {
            //初始化圆角圆形参数对象
            RoundingParams rp = new RoundingParams();
            //设置图像是否为圆形
            //rp.setRoundAsCircle(true);

            //获取GenericDraweeHierarchy对象
            GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(Resources.getSystem())
                    //设置圆形圆角参数
                    //.setRoundingParams(rp)
                    //设置圆角半径
                    .setRoundingParams(RoundingParams.fromCornersRadius(10))
                    //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
                    //.setRoundingParams(RoundingParams.asCircle())
                    //设置淡入淡出动画持续时间(单位：毫秒ms)
                    //.setFadeDuration(5000)
                    //构建
                    .build();

            //设置Hierarchy
            imgView.setHierarchy(hierarchy);
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(width, height)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(imgView.getController())
                .setImageRequest(request).build();
        imgView.setController(controller);
    }

    public static void setImage(SimpleDraweeView imgView, int width, int height, String url, String tag) {
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        if (hasChinese(url)) {
            try {
                url = URLEncoder.encode(url, "UTF-8");
                url = url.replace("%3A", ":");
                url = url.replace("%2F", "/");
            } catch (Exception e) {
            }
        }
        if (!TextUtils.isEmpty(url)) {
            //初始化圆角圆形参数对象
            RoundingParams rp = new RoundingParams();
            //设置图像是否为圆形
            //rp.setRoundAsCircle(true);

            //获取GenericDraweeHierarchy对象
            GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(Resources.getSystem())
                    //设置圆形圆角参数
                    //.setRoundingParams(rp)
                    //设置圆角半径
                    .setRoundingParams(RoundingParams.fromCornersRadius(10))
                    //设置圆形圆角参数；RoundingParams.asCircle()是将图像设置成圆形
                    //.setRoundingParams(RoundingParams.asCircle())
                    //设置淡入淡出动画持续时间(单位：毫秒ms)
                    //.setFadeDuration(5000)
                    //构建
                    .build();

            //设置Hierarchy
            imgView.setHierarchy(hierarchy);
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(width, height)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(imgView.getController())
                .setImageRequest(request).build();
        if (tag.equals(url)) {
            imgView.setController(controller);
        }
    }

    public static void setImage(SimpleDraweeView imgView, int width, int height, Uri uri) {
        if (uri == null) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height)).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder().setOldController(imgView.getController())
                .setImageRequest(request).build();
        imgView.setController(controller);
    }

    private static boolean hasChinese(String str) {
        String regex = "[\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        boolean flag = false;
        if (matcher.find()) {
            flag = true;
        }
        return flag;
    }

}
