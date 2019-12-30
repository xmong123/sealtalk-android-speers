package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.just.agentweb.AgentWeb;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.GoodInfoDetail;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.holders.LocalImageHolderView;
import com.caesar.rongcloudspeed.util.ToastUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

//import com.jaydenxiao.common.imagePager.BigImagePagerActivity;

public class TSShopDetailActivity extends MultiStatusActivity {

    public PromptDialog prompDialog;
    private String tid;
    private String term_id;
    private String postID;
    private GoodInfoDetail goodInfo;
    private AgentWeb mAgentWeb;
    private String post_title;
    private String store_count;
    private String post_authorname;
    private String post_area;
    private String post_price;
    private String post_date;
    private String photos_urls;
    private String photos_content;
    @BindView(R.id.detail_convenientBanner)
    ConvenientBanner detail_convenientBanner;
    @BindView(R.id.detail_text1)
    TextView detail_text1;
    @BindView(R.id.detail_text2)
    TextView detail_text2;
    @BindView(R.id.detail_text3)
    TextView detail_text3;
    @BindView(R.id.detail_text4)
    TextView detail_text4;
    @BindView(R.id.detail_text5)
    TextView detail_text5;
    @BindView(R.id.detail_text6)
    TextView detail_text6;
    @BindView(R.id.detail_text10)
    TextView detail_text10;
    @BindView(R.id.detail_text11)
    TextView detail_text11;
    @BindView(R.id.detail_text12)
    TextView detail_text12;
    @BindView(R.id.detail_text13)
    TextView detail_text13;
    @BindView(R.id.detail_text14)
    TextView detail_text14;
    @BindView(R.id.detail_text20)
    TextView detail_text20;
    @BindView(R.id.detail_text21)
    TextView detail_text21;
    @BindView(R.id.detail_text22)
    TextView detail_text22;
    private String uidString = SPUtils.getInstance().getString( "uid", "0" );
    private String user_type = SPUtils.getInstance().getString( "user_type", "0" );
    private String nickName;
    private String mobileString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        nickName = UserInfoUtils.getNickName(this);
        tid = getIntent().getStringExtra("tid");
        term_id = getIntent().getStringExtra("term_id");
        postID = getIntent().getStringExtra("post_id");
        post_title = getIntent().getStringExtra("post_title");
        store_count = getIntent().getStringExtra("store_count");
        post_authorname = getIntent().getStringExtra("post_authorname");
        post_area = getIntent().getStringExtra("post_area");
        post_price = getIntent().getStringExtra("post_price");
        if (post_price.equals("0.00")) {
            post_price = "价格可议";
        }
        post_date = getIntent().getStringExtra("post_date");
        photos_urls = getIntent().getStringExtra("photos_urls");
        mobileString = getIntent().getStringExtra("mobile");
        photos_content = getIntent().getStringExtra("photos_content");
        initTitleBarView(titlebar, "商品详情");
        detail_text4.setText(post_title);
        if(post_price.equals("价格可议")){
            detail_text1.setText(post_price);
        }else{
            detail_text1.setText("￥"+post_price+"元/吨");
        }
        detail_text21.setText(post_date);
        detail_text22.setText(post_area);
        detail_text12.setText(store_count+"吨");
        detail_text13.setText(post_area);
        detail_text14.setText(photos_content);
        if (term_id.equals("3")) {
            if (!TextUtils.isEmpty(photos_urls) && photos_urls.length() > 32) {
                try {
                    JSONArray imageArray = new JSONArray(photos_urls);
                    if (imageArray != null && imageArray.length() > 0) {
                        ArrayList<String> viewpageDatas = new ArrayList<>();
                        for (int i = 0; i < imageArray.length(); i++) {
                            String str = imageArray.getString(i);
                            if (!str.startsWith("http://")) {
                                str = Constant.THINKCMF_PATH + str ;
                            }else{
                                str +=QiniuUtils.watermark ;
                            }
                            viewpageDatas.add(str);
                        }
                        setViewPagerData(viewpageDatas);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            detail_text2.setText("求购");

        }

    }

    @Override
    public int getContentView() {
        return R.layout.activity_shop_tesla_detail;
    }

    @OnClick({R.id.detail_text3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detail_text3:
                if (uidString.equals( "0" )) {
                    ToastUtils.showShortToast("此功能需要用户登录后操作");
                }else{
                    if(user_type.equals("2")){
                        onShowReviewDialog();
                    }else{
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileString));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setViewPagerData(List<String> data) {
        detail_convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, data)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.indicator_normal, R.mipmap.indicator_focus})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        detail_convenientBanner.startTurning(5000);
        detail_convenientBanner.setScrollDuration(3000);
        try {
            Class<?> clazz = Class.forName("com.bigkoo.convenientbanner.ConvenientBanner");
            Field loPageTurningPoint = clazz.getDeclaredField("loPageTurningPoint");
            loPageTurningPoint.setAccessible(true);
            ViewGroup o = (ViewGroup) loPageTurningPoint.get(detail_convenientBanner);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) o.getLayoutParams();
            layoutParams.bottomMargin = SizeUtils.dp2px(36);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        detail_convenientBanner.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
//                startImagePagerActivity(TSShopDetailActivity.this, data, position);
            }
        });

    }
}
