package com.yiw.circledemo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.RequiresPermission;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mathum on 2016/6/30.
 */
public class BastiGallery {
    private static RuntimePermissionUtils runtimePermissionUtils = new RuntimePermissionUtils();

    //打开相册
    public static void openGallery(final Context context, final int requestCode) {
        runtimePermissionUtils.getPermission(context, new PermissionCallback() {
            @Override
            public void onPermissionCallback(boolean isGranted) {
                if (isGranted) {
                    Log.i("TAG", "true");
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((FragmentActivity) context).startActivityForResult(i, requestCode);
                } else {
                    Log.i("TAG", "false");
                }
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    //打开摄像头
    public static String openCamera(final Context context, final int requestCode) {
        final String fileName = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        runtimePermissionUtils.getPermission(context, new PermissionCallback() {
            @Override
            public void onPermissionCallback(boolean isGranted) {
                if (isGranted) {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ((FragmentActivity) context).startActivityForResult(intent, requestCode);*/
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile(context);
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(context,
                                    "com.caesar.rongcloudspeed.FileProvider",
                                    photoFile);
                            mCurrentPhotoUri = photoURI;
                            //迷之bug
                            // @url http://stackoverflow.com/questions/33650632/fileprovider-not-working-with-camera/33652695#33652695
                            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolveInfo : resInfoList) {
                                String packageName = resolveInfo.activityInfo.packageName;
                                context.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            ((Activity) context).startActivityForResult(intent, requestCode);
                        }
                    }
                }
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return mCurrentPhotoPath;
    }

    static String mCurrentPhotoPath;
    static Uri mCurrentPhotoUri;

    public static String getmCurrentPhotoPath() {
        //mCurrentPhotoPath=amendRotatePhoto(mCurrentPhotoPath,false);
        return mCurrentPhotoPath;
    }

    public static Uri getmCurrentPhotoUri() {
        return mCurrentPhotoUri;
    }

    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("url", mCurrentPhotoPath);
        return image;
    }

    //打开相册回调
    public static String getPathFromGallery(Context context, Intent data) {
        Uri selectedImage = data.getData();
        if (selectedImage == null) {
            return "";
        }
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        if (cursor == null) {
            return "";
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);

        return picturePath;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 获取照片属性中的旋转角度
     *
     * @param path 图片的绝对路径
     * @return 照片属性中的旋转角度
     */
    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public static int getOrientationRotate(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其Exif信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 根据角度值旋转Bitmap
     *
     * @param bitmap
     * @param degree
     * @return
     */
    private static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {

        // 根据旋转角度，得到旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        // 将原始图片按照旋转矩阵进行旋转，得到新的图片
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            result = bitmap;
        }

        if (bitmap != result) {
            bitmap.recycle();
        }
        return result;
    }

    public static Bitmap getimage(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    @SuppressLint("MissingPermission")
    public static Bitmap getimage(String srcPath, int compress) {
        int degree = getOrientationRotate(srcPath);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = compress;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = compress;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        if (degree != 0) {
            bitmap = rotaingImageView(degree, bitmap);
        }
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    //打开摄像头回调
    public static String getPathFromCamera(Intent data, String vinImgUrl) {

        if (data == null) {
            //   Log.i("12312312", "1231231");
        }

        File picture = new File(vinImgUrl);
        Log.i("path", picture.getAbsolutePath());
        return picture.getAbsolutePath();

        /*String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile",
                    "SD card is not avaiable/writeable right now.");
            return "";

        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Loca}le.CHINA)) + ".jpg";
        // Toast.makeText(this, name, Toast.LENGTH_LONG).show();
        Bundle bundle = data.getExtras();
        // Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        Log.i("size", String.valueOf(bitmap.getByteCount()));

        FileOutputStream b = null;
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File temp = new File(file.getPath() + "/" + dirName + "/");

        temp.mkdirs();// 创建文件夹
        String fileName = temp.getPath() + "/" + name;

        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            Log.i("size", String.valueOf(bitmap.getByteCount()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";*/
    }

    /**
     * 处理旋转后的图片
     * 默认不压缩
     *
     * @param originpath    原图路径
     * @param isReplaceFile 是否替换之前的文件 true 替换 false 不替换 默认保存位置
     * @return 返回修复完毕后的图片路径
     */
    public static String amendRotatePhoto(String originpath, boolean isReplaceFile) {
        return amendRotatePhoto(originpath, false, isReplaceFile);
    }

    /**
     * 处理旋转后的图片
     * 默认不压缩
     * 默认替换原图路径下保存
     *
     * @param originpath
     * @return
     */
    public static String amendRotatePhoto(String originpath) {
        return amendRotatePhoto(originpath, false, true);
    }

    /**
     * 处理旋转后的图片
     *
     * @param originpath    原图路径
     * @param isCompress    是否压缩
     * @param isReplaceFile 是否替换之前的文件 true 替换 false 不替换 默认保存位置
     * @return 返回修复完毕后的图片路径
     */
    public static String amendRotatePhoto(String originpath, boolean isCompress, boolean isReplaceFile) {

        if (TextUtils.isEmpty(originpath)) return originpath;

        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);

        //是否压缩
        Bitmap bmp = null;
        if (isCompress) {
            // 把原图压缩后得到Bitmap对象
            bmp = getCompressPhoto(originpath);
        }

        if (bmp != null) {
            //处理旋转
            Bitmap bitmap = null;
            if (angle != 0) {
                // 修复图片被旋转的角度
                bitmap = rotaingImageView(angle, bmp);
            }
            if (bitmap != null) {

            }
            // 保存修复后的图片并返回保存后的图片路径
            return savePhotoToSD(bitmap, originpath, isReplaceFile);
        } else {
            Bitmap localBitmap = getLocalBitmap(originpath);
            if (localBitmap == null) return originpath;
            //处理旋转
            Bitmap bitmap = null;
            if (angle != 0) {
                // 修复图片被旋转的角度
                bitmap = rotaingImageView(angle, localBitmap);
            }
            if (bitmap != null) {
                return savePhotoToSD(bitmap, originpath, isReplaceFile);
            } else {
                return originpath;
            }
        }
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /* 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap       需要保存的Bitmap图片
     * @param originpath    文件的原路径
     * @param isReplaceFile 是否替换原文件
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public static String savePhotoToSD(Bitmap mbitmap, String originpath, boolean isReplaceFile) {
        FileOutputStream outStream = null;
        String fileName = "";
        if (mbitmap == null) return originpath;
        if (isReplaceFile) {
            fileName = mCurrentPhotoPath;
        } else {
            if (TextUtils.isEmpty(originpath)) return originpath;
            fileName = originpath;
        }
        try {
            outStream = new FileOutputStream(fileName);
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 10; // 图片的大小设置为原来的十分之一
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        options = null;
        return bmp;
    }

    private static Bitmap getLocalBitmap(String path) {
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
            /* try catch  可以解决OOM后出现的崩溃，然后采取相应的解决措施，如缩小图片，较少内存使用
             * 但这不是解决OOM的根本方法，因为这个地方是压缩骆驼的最后一颗稻草，
             * 解决方法是dump内存，找到内存异常原因。*/
        } catch (OutOfMemoryError error) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
        }
        return bitmap;
    }
}
