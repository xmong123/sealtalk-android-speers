package com.caesar.rongcloudspeed.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.caesar.rongcloudspeed.data.Qiniu;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.util.L;
import com.qiniu.android.common.ServiceAddress;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mathum on 2017/9/6.
 */

public class QiniuUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMDDHHmmss");

    private static UploadManager uploadManager;

    private static String token = "";
    private static String url = "http://qiniu.500-china.com/";

    //生成图片Key
    public static String createImageKey(String phone) {
        Date date = new Date();

        //  String dateString = simpleDateFormat.format(date);

        String s = String.valueOf(System.currentTimeMillis());

        return phone.concat(s);
    }

    public static String createVoiceKey(String phone) {
        Date date = new Date();

        //  String dateString = simpleDateFormat.format(date);

        String s = String.valueOf(System.currentTimeMillis());

        return phone.concat(s).concat(".amr");
    }

    public static void getUploadManagerInstance() {
        if (uploadManager == null) {
            synchronized (UploadManager.class) {
                if (uploadManager == null) {
                    Configuration config = new Configuration.Builder()
                            .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                            .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                            .connectTimeout(10)           // 链接超时。默认10秒
                            .responseTimeout(60)          // 服务器响应超时。默认60秒
                            //.recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
                            //.recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                            .zone(zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                            .build();
                    uploadManager = new UploadManager(config);
                }
            }
        }
    }

    /**
     * 华南机房, http
     */
    public static final Zone zone2 =
            createZone("upload-z2.qiniu.com", "up-z2.qiniu.com", "183.60.214.197", "14.152.37.7");

    private static Zone createZone(String upHost, String upHostBackup, String upIp, String upIp2) {
        String[] upIps = new String[]{upIp, upIp2};
        ServiceAddress up = new ServiceAddress("http://" + upHost, upIps);
        ServiceAddress upBackup = new ServiceAddress("http://" + upHostBackup, upIps);
        return new Zone(up, upBackup);
    }




    public static void uploadImg(Context context, byte[] data, String key, UpLoadImgCallback callback) {
        synchronized (QiniuUtils.class) {
            if (TextUtils.isEmpty(token)) {
                //此时还没有token
                //先获取token
                fetchToken(data, key, context, callback);
            } else {
                uploadBitmap(data, key, context, callback);
            }
        }
    }

    public static void uploadFile(Context context, String fileName, String key, UpLoadImgCallback callback) {
        synchronized (QiniuUtils.class) {
            if (TextUtils.isEmpty(token) || TextUtils.isEmpty(url)) {
                //此时还没有token
                //先获取token
                fetchToken(fileName, key, context, callback);
            } else {
                uploadFile(fileName, key, context, callback);
            }
            //此时还没有token
            //先获取token
            fetchToken(fileName, key, context, callback);
        }
    }

    private static void fetchToken(final byte[] data, final String key, final Context context, final UpLoadImgCallback callback) {

        AppNetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchQiniuToken(), new NetworkCallback<Qiniu>() {
            @Override
            public void onSuccess(Qiniu qiniuResult) {
                if (NetworkResultUtils.isSuccess(qiniuResult)) {
                    token = qiniuResult.getReferer();
                    uploadBitmap(data, key, context, callback);
                } else {
                    Toast.makeText(context.getApplicationContext(), "发送图片失败，请重新发送", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //    Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private static void fetchToken(final String fileName, final String key, final Context context, final UpLoadImgCallback callback) {

        AppNetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchQiniuToken(), new NetworkCallback<Qiniu>() {
            @Override
            public void onSuccess(Qiniu qiniuResult) {
                if (NetworkResultUtils.isSuccess(qiniuResult)) {
                    token = qiniuResult.getReferer();

                    uploadFile(fileName, key, context, callback);
                } else {
                    Toast.makeText(context.getApplicationContext(), "发送语音失败，请重新发送", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //    Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void uploadBitmap(byte[] data, String key, Context context, final UpLoadImgCallback uploadImgCallback) {
        uploadManager.put(data, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key1, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    Log.i("qiniu", "Upload Success");

                    String imgurl = url.concat(key1);
                    if (uploadImgCallback != null) {
                        uploadImgCallback.onSuccess(imgurl);
                    }
                } else {
                    Log.i("qiniu", "Upload Fail");
                    if (uploadImgCallback != null) {
                        uploadImgCallback.onFailure();
                    }
                }
                Log.i("qiniu", key1 + ",\r\n " + info + ",\r\n " + response);
            }
        }, null);
    }

    private static void uploadFile(String fileName, String key, Context context, final UpLoadImgCallback uploadImgCallback) {
        uploadManager.put(fileName, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key1, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    Log.i("qiniu", "Upload Success");

                    String imgurl = url.concat(key1);
                    if (uploadImgCallback != null) {
                        uploadImgCallback.onSuccess(imgurl);
                    }
                } else {
                    Log.i("qiniu", "Upload Fail");
                    if (uploadImgCallback != null) {
                        uploadImgCallback.onFailure();
                    }
                }
                Log.i("qiniu", key1 + ",\r\n " + info + ",\r\n " + response);
            }
        }, null);
    }

}
