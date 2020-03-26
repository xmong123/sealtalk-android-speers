package com.caesar.rongcloudspeed.wxapi;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xiaomi.mipush.sdk.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static com.caesar.rongcloudspeed.wx.WXManager.APP_ID;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_pay_result);
    	api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);

		//设置通知时间，默认为系统发出通知的时间，通常不用设置
		//.setWhen(System.currentTimeMillis());
		//通过builder.build()方法生成Notification对象,并发送通知,id=1
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@SuppressLint("LongLogTag")
	@Override
	public void onReq(BaseReq req) {
		Log.d(TAG, "onPayFinish, errCode =>>> " + req.toString());
	}

	@SuppressLint("LongLogTag")
	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode =>>> " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode== BaseResp.ErrCode.ERR_OK){
				UserInfoUtils.setWechatInfoData(true,WXPayEntryActivity.this);
				setResult(RESULT_OK, getIntent());
			}
			finish();
			//AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//builder.setTitle("提示");
			//builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
			//builder.show();
		}
	}
}