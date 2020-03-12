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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.callback.UpLoadImgCallback;
import com.caesar.rongcloudspeed.common.NoScrollGridView;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkResultUtils;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.adapter.NinePicturesAdapter;
import com.caesar.rongcloudspeed.util.ToastUitl;
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
public class PublicGoodsActivity extends Activity implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PublicGoodsActivity";
    TextView post_title_text;
    EditText post_edit_title;
    NoScrollGridView post_image_gridview;
    EditText post_edit_mobile;
    EditText post_edit_content;
    Button post_commit_btn;
    private String mParam1;
    private String mParam2;
    private NinePicturesAdapter ninePicturesAdapter;
    private int REQUEST_CODE = 120;
    public static final int RESULT_OK = -1;
    private String postTitle;
    private String phoneNumber;
    private String postContent;
    private String photos_url[]=new String[9];
    private String uidString;

    /**
     * 默认是发布出售
     */
    public static int CODE_SUCC = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_post_goods_detail);
        post_title_text=(TextView)this.findViewById(R.id.post_title_text);
        post_edit_title=(EditText)this.findViewById(R.id.post_edit_title);
        post_image_gridview=(NoScrollGridView)this.findViewById(R.id.post_image_gridview);
        post_edit_mobile=(EditText)this.findViewById(R.id.post_edit_mobile);
        post_edit_content=(EditText)this.findViewById(R.id.post_edit_content);
        post_commit_btn=(Button) this.findViewById(R.id.post_commit_btn);
        uidString= UserInfoUtils.getAppUserId( PublicGoodsActivity.this);
        phoneNumber= UserInfoUtils.getPhone( PublicGoodsActivity.this);
        ninePicturesAdapter = new NinePicturesAdapter( PublicGoodsActivity.this, 9, new NinePicturesAdapter.OnClickAddListener() {
            @Override
            public void onClickAdd(int positin) {
                choosePhoto();
            }
        } );
        post_image_gridview.setAdapter( ninePicturesAdapter );
        if (uidString.equals( "0" )) {
            post_edit_mobile.setText("");
        }else{
            post_edit_mobile.setText(phoneNumber);
        }
        post_edit_title.setHint( "请输入信息标题" );
        post_edit_content.setHint( "请输入您信息的真实情况，包括标题，详情等" );
        post_title_text.setText("信息发布");
        post_commit_btn.setOnClickListener(this);
    }


    public void postBackAction(View view) {
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        uidString= UserInfoUtils.getAppUserId( PublicGoodsActivity.this);
        phoneNumber= UserInfoUtils.getPhone( PublicGoodsActivity.this);
        if (uidString.equals( "0" )) {
            post_edit_mobile.setText("");
        }else{
            post_edit_mobile.setText(phoneNumber);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_commit_btn:
                postTitle=post_edit_title.getText().toString();
                phoneNumber=post_edit_mobile.getText().toString();
                postContent=post_edit_content.getText().toString();
                if (uidString.equals( "0" )) {
                    Intent loginIntent = new Intent( PublicGoodsActivity.this, LoginActivity.class );
                    startActivity( loginIntent );
                } else {
                    if(TextUtils.isEmpty(postTitle)&&postTitle.length()>5) {
                        ToastUitl.showToastWithImg(getString(R.string.circle_publish_empty),R.drawable.ic_warm);
                    }else{
                        if(TextUtils.isEmpty(phoneNumber)) {
                            ToastUitl.showToastWithImg(getString(R.string.circle_mobile_empty),R.drawable.ic_warm);
                        }else{
                            List<String> imageList=ninePicturesAdapter.getData();
                            List<String> pathList=new ArrayList<>( );
                            for(int i=0;i<imageList.size();i++){
                                String path=imageList.get( i );
                                if (path.length()>5){
                                    pathList.add( path );
                                }
                            }
                            if (pathList.size() > 0) {
                                upLoadAllFeedBackImg(pathList);
                            }else{
                                ToastUitl.showToastWithImg(getString(R.string.circle_image_empty),R.drawable.ic_warm);
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
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    final ProgressDialog pd = new ProgressDialog( PublicGoodsActivity.this );
                    pd.setMessage( "正在处理，请稍后..." );
                    pd.show();
                    NetworkUtils.fetchInfo( AppNetworkUtils.initRetrofitApi().AddPostCartArticle(uidString,"42",postTitle,phoneNumber,photos_url,postContent),
                            new NetworkCallback<BaseData>() {
                                @Override
                                public void onSuccess(BaseData baseData) {
                                    if (NetworkResultUtils.isSuccess(baseData)) {
                                        post_edit_title.setText( "" );
                                        post_edit_content.setText( "" );
                                        Toast.makeText(PublicGoodsActivity.this, "信息发布成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(PublicGoodsActivity.this, "信息发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                    pd.dismiss();
                                    setResult(RESULT_OK,getIntent());
                                    finish();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(PublicGoodsActivity.this, "信息发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            });

                    break;
                default:
                    break;
            }
        }
    };

    private void upLoadAllFeedBackImg(List<String> pathList) {
        final ProgressDialog pd = new ProgressDialog( PublicGoodsActivity.this );
        pd.setCanceledOnTouchOutside( false );
        pd.setOnCancelListener( new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d( TAG, "EMClient.getInstance().onCancel" );
            }
        } );
        pd.setMessage( "正在上传" );
        pd.show();
        QiniuUtils.getUploadManagerInstance();
        //已经上传过的不上传
        final int[] count = {0};
        photos_url=new String[pathList.size()];
        for(String fileName:pathList){
            Bitmap bitmap= BastiGallery.getimage( fileName );
            //bitmap=drawCenterLable(PublicGoodsActivity.this,bitmap,"城库货源");
            //byte[] img = BastiGallery.Bitmap2Bytes( bitmap );
            byte[] img=compressBitmap(bitmap,128);
            QiniuUtils.uploadImg( PublicGoodsActivity.this, img, QiniuUtils.createImageKey( UserInfoUtils.getPhone( PublicGoodsActivity.this ) ), new UpLoadImgCallback() {
                @Override
                public void onSuccess(String imgUrl) {
                    photos_url[count[0]]=imgUrl;
                    count[0]++;
                    Log.e( "111111111111", "imgUrl = " + imgUrl );
                    if(count[0]==pathList.size()){
                        pd.dismiss();
                        handler.sendEmptyMessage( 0 );
                    }
                }

                @Override
                public void onFailure() {
                    count[0]++;
                    if(count[0]==pathList.size()){
                        pd.dismiss();
                        handler.sendEmptyMessage( 0 );
                    }
                }
            } );
        }
    }


    public static byte[] compressBitmap(Bitmap bitmap, float size){
        if(bitmap==null|| BastiGallery.Bitmap2Bytes(bitmap).length<=size*1024){
            return null;//如果图片本身的大小已经小于这个大小了，就没必要进行压缩
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality=100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);//如果签名是png的话，则不管quality是多少，都不会进行质量的压缩
        while (baos.toByteArray().length / 1024f>size) {
            quality=quality-4;// 每次都减少4
            baos.reset();// 重置baos即清空baos
            if(quality<=0){
                break;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            //Log.e(TAG,"------质量--------"+baos.toByteArray().length/1024f);
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
    paint.setTextSize(100*scale);
    paint.setDither(true);
    paint.setFilterBitmap(true);
    Rect rectText = new Rect();  //得到text占用宽高， 单位：像素
    paint.getTextBounds(text, 0, text.length(), rectText);
    double beginX = (bmp.getHeight()/2 - rectText.width()/2) * 1.4;  //45度角度值是1.414
    double beginY = (bmp.getWidth()/2 - rectText.width()/2) * 1.4;
    canvas.drawText(text, (int)beginX, (int)beginY, paint);
    canvas.restore();
    return newBmp;
  }

    /**
     * 开启图片选择器
     */
    private void choosePhoto() {
        ImgSelConfig config = new ImgSelConfig.Builder( loader )
                // 是否多选
                .multiSelect( true )
                // 确定按钮背景色
                .btnBgColor( Color.TRANSPARENT )
                .titleBgColor( ContextCompat.getColor( PublicGoodsActivity.this, R.color.main_color ) )
                // 使用沉浸式状态栏
                .statusBarColor( ContextCompat.getColor( PublicGoodsActivity.this, R.color.main_color ) )
                // 返回图标ResId
                .backResId( R.drawable.ic_arrow_back )
                .title( "图片" )
                // 第一个是否显示相机
                .needCamera( true )
                // 最大选择图片数量
                .maxNum( 1 - ninePicturesAdapter.getPhotoCount() )
                .build();
        ImgSelActivity.startActivity( this, config, REQUEST_CODE );
    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            ImageLoaderUtils.display( context, imageView, path );
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra( ImgSelActivity.INTENT_RESULT );
            if (ninePicturesAdapter != null) {
                ninePicturesAdapter.addAll( pathList );
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
