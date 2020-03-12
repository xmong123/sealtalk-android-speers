package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.GoodsDetailBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.holders.LocalImageHolderView;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.orhanobut.dialogplus.DialogPlus;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class SPShopDetailActivity extends MultiStatusActivity {

    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.shopTitle)
    TextView shopTitle;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.sfhsj)
    TextView sfhsj;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.webView)
    WebView webView;
    private String goods_id;
    private String photo;
    private String goods_name;
    private String goods_remark;
    private String shop_price;
    private String uidString;
    private String goods_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        ButterKnife.bind( this );
        goods_id = getIntent().getExtras().getString( "goods_id" );
        goods_name = getIntent().getExtras().getString( "goods_name" );
        photo = getIntent().getExtras().getString( "photo" );
        goods_remark = getIntent().getExtras().getString( "goods_remark" );
        goods_name = getIntent().getExtras().getString( "goods_name" );
        shop_price = getIntent().getExtras().getString( "shop_price" );
        goods_content = getIntent().getExtras().getString( "goods_content" );
        initTitleBarView( titlebar, "商品详情" );
        shopTitle.setText(goods_name);
        money.setText("¥ " + shop_price + "元");
        sfhsj.setText(goods_remark);
        try {
            JSONArray photoArray = new JSONArray( photo );
            if(photoArray!=null&&photoArray.length()>0){
                ArrayList<String> viewpageDatas = new ArrayList<>();
                for(int i=0;i<photoArray.length();i++){
                    JSONObject photoObj=photoArray.getJSONObject(i);
                    String photoString=photoObj.getString("url");
                    if (!photoString.startsWith( "http://" )) {
                        photoString = Constant.THINKCMF_PATH + photoString;
                    }
                    viewpageDatas.add( photoString );
                }
                setViewPagerData( viewpageDatas );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(goods_content!=null&&goods_content.length()>10){
            addWebView(goods_content);
        }
        uidString= UserInfoUtils.getAppUserId(this);
    }

    private void addWebView(String content) {
        webView.setWebViewClient( new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl( url );
                return true;
            }


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest( view, url );
            }

        } );
        webView.loadData( content, "text/html; charset=UTF-8", null );
//        webView.loadUrl( content );
        webView.setHorizontalFadingEdgeEnabled( false );
        webView.setVerticalFadingEdgeEnabled( false );
        webView.setHorizontalScrollBarEnabled( false );
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled( true );

//支持插件
//        webSettings.setPluginsEnabled(true);

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort( true ); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode( true ); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom( false ); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls( false ); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls( false ); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK ); //关闭webview中缓存
        webSettings.setAllowFileAccess( true ); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically( true ); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically( true ); //支持自动加载图片
        webSettings.setDefaultTextEncodingName( "utf-8" );//设置编码格式
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setTextZoom(200);

    }

    private void setViewPagerData(List<String> data) {
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, data )
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator( new int[]{R.mipmap.indicator_normal, R.mipmap.indicator_focus} )
                //设置指示器的方向
                .setPageIndicatorAlign( ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL );
        convenientBanner.startTurning( 5000 );
        convenientBanner.setScrollDuration( 3000 );
//        try {
//            Class<?> clazz = Class.forName( "com.bigkoo.convenientbanner.ConvenientBanner" );
//            Field loPageTurningPoint = clazz.getDeclaredField( "loPageTurningPoint" );
//            loPageTurningPoint.setAccessible( true );
//            ViewGroup o = (ViewGroup) loPageTurningPoint.get( convenientBanner );
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) o.getLayoutParams();
//            layoutParams.bottomMargin = SizeUtils.dp2px( 36 );
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_shop_detail;
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        String uidString= UserInfoUtils.getAppUserId(this);
        if(uidString.length()>0){
            if (goods_id != null) {
                createCarts(goods_id);
            }
        }else{
            Intent loginIntent = new Intent( SPShopDetailActivity.this, LoginActivity.class );
            startActivity( loginIntent );
        }

    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL( null, "", "text/html", "utf-8", null );
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView( webView );
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    private void createCarts(String goods_id) {
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().ajaxAddCart(uidString,goods_id,"1"),
                new NetworkCallback<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        showCartsDialog();
                        Toast.makeText(SPShopDetailActivity.this, "成功加入购物车: " + goods_name, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(SPShopDetailActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showCartsDialog() {
        final DialogPlus dialog = DialogPlus.newDialog( this )
                .setGravity( Gravity.CENTER )
                .setContentBackgroundResource( android.R.color.transparent )
                .setContentWidth( ViewGroup.LayoutParams.WRAP_CONTENT )  // or any custom width ie: 300
                .setContentHeight( ViewGroup.LayoutParams.WRAP_CONTENT )
                .setContentHolder( new com.orhanobut.dialogplus.ViewHolder( R.layout.cartscreate_dialog_layout ) )
                .setExpanded( false )  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        View holderView = dialog.getHolderView();
        holderView.findViewById( R.id.mcart_confirm_btn1 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );
        holderView.findViewById( R.id.mcart_confirm_btn2 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent loginIntent = new Intent( SPShopDetailActivity.this, SPShopCartActivity.class );
                startActivity( loginIntent );
            }
        } );

        dialog.show();
    }
}
