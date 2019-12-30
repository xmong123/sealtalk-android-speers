package com.caesar.rongcloudspeed.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.network.Api;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.manager.RetrofitManageres;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.util.MyWebChromeClient;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

/**
 * Created by cenxiaozhong on 2017/5/26.
 * <p>
 * source code  https://github.com/Justson/AgentWeb
 */

public class BaseWebActivity extends AppCompatActivity {

    private String indexurl = "";
    private String indextitle = "";

    protected AgentWeb mAgentWeb;
    private RelativeLayout mLinearLayout;
    private Toolbar mToolbar;
    private TextView mTitleTextView;
    private AlertDialog mAlertDialog;
    private String uidString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_web );
        ButterKnife.bind(this);
        uidString= UserInfoUtils.getAppUserId( this);
        mLinearLayout = (RelativeLayout) this.findViewById( R.id.container );
        mToolbar = (Toolbar) this.findViewById( R.id.toolbar );
        mToolbar.setTitleTextColor( Color.WHITE );
        mToolbar.setTitle( "" );
        mTitleTextView = (TextView) this.findViewById( R.id.toolbar_title );
        this.setSupportActionBar( mToolbar );
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }
        mToolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        } );


        long p = System.currentTimeMillis();
        mAgentWeb = AgentWeb.with( this )
                .setAgentWebParent( mLinearLayout, new LinearLayout.LayoutParams( -1, -1 ) )
                .useDefaultIndicator()
                .setWebChromeClient( new MyWebChromeClient() )
                .setWebViewClient( mWebViewClient )
                .setMainFrameErrorView( R.layout.agentweb_error_page, -1 )
                .setSecurityType( AgentWeb.SecurityType.STRICT_CHECK )
//                .setWebLayout(new WebLayout(this))
                .setOpenOtherPageWays( DefaultWebClient.OpenOtherPageWays.ASK )//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go( getUrl() );

        //mAgentWeb.getUrlLoader().loadUrl(getUrl());

        long n = System.currentTimeMillis();
        Log.i( "Info", "init used time:" + (n - p) );
        String postID=getIntent().getStringExtra("postID");
        String title=getIntent().getStringExtra("title");
    }

    private void onPageStarted(String uidString,String indextitle,String table,String postID) {
        //do you  work
        Log.i( "Info", "BaseWebActivity onPageStarted" );
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if(url.equals("http://www.han80.com/mobile/user.php")){
                finish();
                return true;
            }else if(url.equals("http://www.han80.com/mobile/flow.php")){
                getIntent().putExtra("action", "flow"); //将计算的值回传回去
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(2, getIntent());
                finish();
                return true;
            }
            return super.shouldOverrideUrlLoading( view, request );
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i( "Info", "BaseWebActivity onPageStarted" );
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
//            Log.i("Info","onProgress:"+newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle( view, title );
            if (mTitleTextView != null) {
                mTitleTextView.setText( title );
            }
        }
    };

    public String getUrl() {
        indexurl=getIntent().getStringExtra("webString").toString();
//        indexurl = getIntent().getExtras().getString("webString");
        return indexurl;
    }


    private void showDialog() {

        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder( this )
                    .setMessage( "您确定要关闭该页面吗?" )
                    .setNegativeButton( "再逛逛", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    } )//
                    .setPositiveButton( "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                            BaseWebActivity.this.finish();
                        }
                    } ).create();
        }
        mAlertDialog.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent( keyCode, event )) {
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i( "Info", "onResult:" + requestCode + " onResult:" + resultCode );
        super.onActivityResult( requestCode, resultCode, data );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

}
