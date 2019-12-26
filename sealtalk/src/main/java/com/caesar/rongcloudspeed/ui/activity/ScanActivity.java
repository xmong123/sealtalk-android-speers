package com.caesar.rongcloudspeed.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.qrcode.SealQrCodeUISelector;
import com.caesar.rongcloudspeed.ui.dialog.PayPassDialog;
import com.caesar.rongcloudspeed.ui.dialog.PayPassView;
import com.caesar.rongcloudspeed.ui.view.SealTitleBar;
import com.caesar.rongcloudspeed.utils.PhotoUtils;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.utils.log.SLog;
import com.caesar.rongcloudspeed.utils.qrcode.QRCodeUtils;
import com.caesar.rongcloudspeed.utils.qrcode.barcodescanner.BarcodeResult;
import com.caesar.rongcloudspeed.utils.qrcode.barcodescanner.CaptureManager;
import com.caesar.rongcloudspeed.utils.qrcode.barcodescanner.DecoratedBarcodeView;


/**
 * 扫一扫界面
 */
public class ScanActivity extends TitleBaseActivity implements View.OnClickListener {
    private final static String TAG = "ScanActivity";
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private TextView lightControlTv;
    private TextView selectPicTv;
    private TextView tipsTv;
    private PhotoUtils photoUtils;

    private boolean isCameraLightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SealTitleBar titleBar = getTitleBar();
        titleBar.setTitle(R.string.seal_main_title_scan);
        titleBar.setOnBtnRightClickListener(getString(R.string.common_album), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromAlbum();
            }
        });

        initView(savedInstanceState);

        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                String result = QRCodeUtils.analyzeImage(uri.getPath());
                handleQrCode(result);
            }

            @Override
            public void onPhotoCancel() {
            }
        });
    }

    private void initView(Bundle savedInstanceState) {
        barcodeScannerView = initializeContent();
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.setOnCaptureResultListener(new CaptureManager.OnCaptureResultListener() {
            @Override
            public void onCaptureResult(BarcodeResult result) {
                handleQrCode(result.toString());
            }
        });

        barcodeScannerView.getViewFinder().networkChange(!NetworkUtils.isNetWorkAvailable(this));
        if (!NetworkUtils.isNetWorkAvailable(this)) {
            capture.stopDecode();
        } else {
            capture.decode();
        }
        barcodeScannerView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                lightControlTv.setText(R.string.zxing_close_light);
                isCameraLightOn = true;
            }

            @Override
            public void onTorchOff() {
                lightControlTv.setText(R.string.zxing_open_light);
                isCameraLightOn = false;
            }
        });
        lightControlTv = findViewById(R.id.zxing_open_light);
        lightControlTv.setOnClickListener(this);
        selectPicTv = findViewById(R.id.zxing_select_pic);
        selectPicTv.setOnClickListener(this);
        tipsTv = findViewById(R.id.zxing_user_tips);
    }

    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.zxing_capture);
        return (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
    }

    /**
     * 切换摄像头照明
     */
    private void switchCameraLight() {
        if (isCameraLightOn) {
            barcodeScannerView.setTorchOff();
        } else {
            barcodeScannerView.setTorchOn();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zxing_open_light:
                switchCameraLight();
                break;
            case R.id.zxing_select_pic:
                scanFromAlbum();
                break;
        }
    }

    /**
     * 从相册中选中
     */
    public void scanFromAlbum() {
        photoUtils.selectPicture(this);
    }

    /**
     * 处理二维码结果，并跳转到相应界面
     *
     * @param qrCodeText
     */
    private void handleQrCode(String qrCodeText) {
        if (TextUtils.isEmpty(qrCodeText)) {
            SLog.d(TAG, "scanner result is null");
            ToastUtils.showToast(R.string.zxing_qr_can_not_recognized);
            return;
        }
       if(qrCodeText.startsWith("https://a.feigecb.com/?:for")){
            final EditText et = new EditText(ScanActivity.this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(ScanActivity.this).setTitle("请设置付款金额")
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String user_sum_add=et.getText().toString();
                            String [] stringArr= qrCodeText.split("/?:for");
                            String sum_touserID=stringArr[1];
                            String sum_foruserID= UserInfoUtils.getAppUserId(ScanActivity.this);
                            PayPassDialog payPassDialog=new PayPassDialog(ScanActivity.this);
                            payPassDialog.getPayViewPass().setPayClickListener(new PayPassView.OnPayClickListener() {
                                @Override
                                public void onPassFinish(String passContent) {
                                    String payPassWord= UserInfoUtils.getPayPassWord(ScanActivity.this);
                                    if(payPassWord.equals(passContent)){
                                        payPassDialog.dismiss();
                                        //按下确定键后的事件
                                        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getCodeToUser(user_sum_add,sum_foruserID,sum_touserID),
                                                new NetworkCallback<UserSumResult>() {
                                                    @Override
                                                    public void onSuccess(UserSumResult userSumResult) {
                                                        UserSumUrl userSumUrl=userSumResult.getUrl();
                                                        String sumString=String.valueOf(userSumUrl.getUser_sum());
                                                        UserInfoUtils.setUserSum(sumString,ScanActivity.this);
                                                        Toast.makeText(ScanActivity.this, "交易成功，付款金额为:"+user_sum_add+"元",Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        Toast.makeText(ScanActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                });
                                    }else{
                                        payPassDialog.dismiss();
                                        Toast.makeText(ScanActivity.this, "支付密码错误", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onPayClose() {
                                    payPassDialog.dismiss();
                                }

                                @Override
                                public void onPayForget() {
                                    payPassDialog.dismiss();
                                }
                            });

                        }
                    }).setNegativeButton("取消",null).show();
           return;
        }else if(qrCodeText.startsWith("https://a.feigecb.com/?:to")){
            final EditText et = new EditText(ScanActivity.this);
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(ScanActivity.this).setTitle("请设置收款金额")
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String user_sum_add=et.getText().toString();
                            String [] stringArr= qrCodeText.split("/?:to");
                            String sum_touserID=stringArr[1];
                            String sum_foruserID= UserInfoUtils.getAppUserId(ScanActivity.this);
                            PayPassDialog payPassDialog=new PayPassDialog(ScanActivity.this);
                            payPassDialog.getPayViewPass().setPayClickListener(new PayPassView.OnPayClickListener() {
                                @Override
                                public void onPassFinish(String passContent) {
                                    String payPassWord=UserInfoUtils.getPayPassWord(ScanActivity.this);
                                    if(payPassWord.equals(passContent)){
                                        payPassDialog.dismiss();
                                        //按下确定键后的事件
                                        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getCodeForUser(user_sum_add,sum_touserID,sum_foruserID),
                                                new NetworkCallback<UserSumResult>() {
                                                    @Override
                                                    public void onSuccess(UserSumResult userSumResult) {
                                                        UserSumUrl userSumUrl=userSumResult.getUrl();
                                                        String sumString=String.valueOf(userSumUrl.getUser_sum());
                                                        UserInfoUtils.setUserSum(sumString,ScanActivity.this);
                                                        Toast.makeText(ScanActivity.this, "交易成功，收款金额为:"+user_sum_add+"元",Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        Toast.makeText(ScanActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                                        finish();
                                                    }
                                                });
                                    }else{
                                        payPassDialog.dismiss();
                                        Toast.makeText(ScanActivity.this, "支付密码错误", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onPayClose() {
                                    payPassDialog.dismiss();
                                }

                                @Override
                                public void onPayForget() {
                                    payPassDialog.dismiss();
                                }
                            });
                        }
                    }).setNegativeButton("取消",null).show();
           return;
        }

        // 处理二维码结果
        SealQrCodeUISelector uiSelector = new SealQrCodeUISelector(this);
        LiveData<Resource<String>> resourceLiveData = uiSelector.handleUri(qrCodeText);
        resourceLiveData.observeForever(new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                if(resource.status != Status.LOADING) {
                    resourceLiveData.removeObserver(this);
                }

                if(resource.status == Status.SUCCESS){
                    finish();
                } else if(resource.status == Status.ERROR){
                    ToastUtils.showToast(resource.data);
                    barcodeScannerView.getViewFinder().setAllowScanAnimation(false);
                    lightControlTv.setVisibility(View.INVISIBLE);
                    tipsTv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        photoUtils.onActivityResult(this, requestCode, resultCode ,data);
    }
}
