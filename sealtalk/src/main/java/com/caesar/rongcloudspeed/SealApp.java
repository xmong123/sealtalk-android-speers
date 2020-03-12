package com.caesar.rongcloudspeed;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;

import com.caesar.rongcloudspeed.common.ErrorCode;
import com.caesar.rongcloudspeed.contact.PhoneContactManager;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.utils.SearchUtils;
import com.caesar.rongcloudspeed.wx.WXManager;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;

import io.rong.imlib.ipc.RongExceptionHandler;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

public class SealApp extends MultiDexApplication {
    private static SealApp appInstance;
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "CircleDemo" + File.separator + "Images"
            + File.separator;
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.init(this);
        appInstance = this;
        Fresco.initialize(this);
        // 初始化 bugly BUG 统计
        //CrashReport.initCrashReport(getApplicationContext());
        Utils.init(this);
        ErrorCode.init(this);

        /*
         * 以上部分在所有进程中会执行
         */
        if (!getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            return;
        }
        /*
         * 以下部分仅在主进程中进行执行
         */
        // 初始化融云IM SDK，初始化 SDK 仅需要在主进程中初始化一次
        IMManager.getInstance().init(this);
        Stetho.initializeWithDefaults(this);

        SearchUtils.init(this);

        Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

        // 微信分享初始化
        WXManager.getInstance().init(this);

        PhoneContactManager.getInstance().init(this);

        initImageLoader();

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + b);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };

        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        QiniuUtils.getUploadManagerInstance();
    }

    public static SealApp getApplication(){
        return appInstance;
    }

    /** 初始化imageLoader */
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(com.yiw.circledemo.R.color.bg_no_photo)
                .showImageOnFail(com.yiw.circledemo.R.color.bg_no_photo).showImageOnLoading(com.yiw.circledemo.R.color.bg_no_photo).cacheInMemory(true)
                .cacheOnDisk(true).build();

        File cacheDir = new File(DEFAULT_SAVE_IMAGE_PATH);
        ImageLoaderConfiguration imageconfig = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(200)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(imageconfig);
    }

    public static SealApp getInstance()
    {
        if (appInstance == null) {
            appInstance = new SealApp();
        }
        return appInstance;
    }

    public static Context getAppContext() {
        return appInstance;
    }

}
