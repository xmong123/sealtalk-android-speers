/**
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月20日 下午7:52:58 
 * Description:Activity 基类
 * @version V1.0
 */
package com.caesar.rongcloudspeed.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.listener.SPIViewController;
import com.caesar.rongcloudspeed.ui.activity.SPShopDetailActivity;
import com.caesar.rongcloudspeed.utils.SPConfirmDialog;
import com.caesar.rongcloudspeed.utils.SPDialogUtils;
import com.caesar.rongcloudspeed.utils.SPLoadingDialog;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * @author 飞龙
 *
 */
public abstract class SPBaseActivity extends FragmentActivity {

	private String TAG = "SPBaseActivity";
	public final int TITLE_HOME = 1;
	public final int TITLE_DEFAULT = 0;
	public final int TITLE_CATEGORY = 2;

	public JSONObject mDataJson;		//包含网络请求所有结果
	public SPLoadingDialog mLoadingDialog;
	public boolean isCustomerTtitle ;	//是否自定义标题栏
	public boolean isBackShow ;			//是否显示返回箭头
	private String mTtitle ;				//标题栏

	private Button mBackBtn	;
	private TextView mTitleTxtv;

	FrameLayout mTitleBarLayout;
	FrameLayout mDefaultLayout;
	LinearLayout mHomeLayout;
	LinearLayout mCategoryLayout;
	SearchView homeSearch;
	SearchView categorySearch;
	RelativeLayout fragmentView;

	/**
	 * 是否自定义标题 , 该方法必须在子Activity的 super.onCreate()之前调用, 否则无效
	 * @param customerTtitle
	 */
	public void setCustomerTitle(boolean backShow, boolean customerTtitle , String title){
		isCustomerTtitle = customerTtitle;
		isBackShow = backShow;
		mTtitle = title;
	}

	public void setTitle(String title){
		mTtitle = title;
		if (mTitleTxtv!=null)mTitleTxtv.setText(mTtitle);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if (isCustomerTtitle){
			//自定义标题
			requestWindowFeature( Window.FEATURE_CUSTOM_TITLE);
		}
	}
	
	public void bindClickListener(View v , View.OnClickListener listener){
		if(v != null && listener != null){
			v.setOnClickListener(listener);
		}
		
	}
	
	/**
	 * activity初始化
	 * 
	 */
	public void init(){

		if (isCustomerTtitle){
			//设置标题为某个layout
			getWindow().setFeatureInt( Window.FEATURE_CUSTOM_TITLE, R.layout.sp_titlebar);
		}
		mBackBtn = (Button)findViewById(R.id.titlebar_back_btn);
		if (isBackShow){
			mBackBtn.setVisibility( View.VISIBLE);
			mBackBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SPBaseActivity.this.finish();
				}
			});
		}else{
			if(mBackBtn!=null)mBackBtn.setVisibility( View.GONE);
		}

		String title = this.getTitle().toString();

		mTitleTxtv = (TextView)findViewById(R.id.titlebar_title_txtv);
		if(mTitleTxtv!=null)mTitleTxtv.setText(mTtitle);
		//setSearchViewStyle(homeSearch);
		//setSearchViewStyle(categorySearch);
		initSubViews();
		initEvent();
		initData();
	}

	public void setTitleType(int type){
		int padding = getResources().getDimensionPixelSize(R.dimen.height_tab_bottom_item);
		fragmentView.setPadding(0,padding-10,0,padding);
		mTitleBarLayout.setBackgroundResource(R.color.bg_activity);
		if(type == TITLE_HOME){
			mHomeLayout.setVisibility( View.VISIBLE);
			mCategoryLayout.setVisibility( View.INVISIBLE);
			mDefaultLayout.setVisibility( View.INVISIBLE);
			fragmentView.setPadding(0,0,0,padding);
			mTitleBarLayout.setBackgroundResource(R.color.transparent);
		}else if(type == TITLE_CATEGORY){
			mHomeLayout.setVisibility( View.INVISIBLE);
			mCategoryLayout.setVisibility( View.VISIBLE);
			mDefaultLayout.setVisibility( View.INVISIBLE);
		}else{
			mHomeLayout.setVisibility( View.INVISIBLE);
			mCategoryLayout.setVisibility( View.INVISIBLE);
			mDefaultLayout.setVisibility( View.VISIBLE);
		}
	}

	public void showToast(String msg){
		SPDialogUtils.showToast(this, msg);
	}

	public void showLoadingToast(){
		showLoadingToast(null);
	}

	public void showLoadingToast(String title){
		mLoadingDialog = new SPLoadingDialog(this , title);
		mLoadingDialog.setCanceledOnTouchOutside(false);
		mLoadingDialog.show();
	}

	public void showConfirmDialog(String message , String title , final SPConfirmDialog.ConfirmDialogListener confirmDialogListener , final int actionType){
		SPConfirmDialog.Builder builder = new SPConfirmDialog.Builder(this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
				if(confirmDialogListener!=null)confirmDialogListener.clickOk(actionType);
			}
		});

		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	public void showConfirmDialog(String message , String title , final SPConfirmDialog.ConfirmDialogListener confirmDialogListener , String actionString){
		SPConfirmDialog.Builder builder = new SPConfirmDialog.Builder(this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
				int actionType=Integer.parseInt(actionString);
				if(confirmDialogListener!=null)confirmDialogListener.clickOk(actionType);
			}
		});

		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	public void hideLoadingToast(){
		if(mLoadingDialog !=null){
			mLoadingDialog.dismiss();
		}
	}

	/**
	 * 进入产品详情页
	 * @param orderID
	 */
	public void gotoProductDetail(String orderID){
		Intent detailIntent = new Intent(this  , SPShopDetailActivity.class);
		detailIntent.putExtra("orderId", orderID);
		startActivity(detailIntent);
	}

	/**
	 * 以下三个函数不需要再子类调用, 只需要在子类的
	 * onCrate()中调用:super.init()方法即可
	 * 基类函数,初始化界面
	 */
	abstract public void initSubViews();
	
	/**
	 * 基类函数, 初始化数据
	 */
	abstract public void initData();
	
	/**
	 * 基类函数, 绑定事件
	 */
	abstract public void initEvent();

	/**
	 * 处理网络加载过的数据
	 */
	public void dealModel(){}

}
