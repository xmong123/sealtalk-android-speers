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
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.GoodInfoDetail;
import com.caesar.rongcloudspeed.circle.ui.ImagePagerActivity;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.holders.LocalImageHolderView;
import com.caesar.rongcloudspeed.util.ToastUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.just.agentweb.AgentWeb;
import com.nostra13.universalimageloader.core.assist.ImageSize;

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

public class SeekHelperDetailActivity extends MultiStatusActivity {

    public PromptDialog prompDialog;
    private String seekID;
    private String seek_title;
    private String seek_price;
    private String seek_date;
    private String photos_urls;
    private String seek_content;
    @BindView(R.id.seek_convenientBanner)
    ConvenientBanner seek_convenientBanner;
    @BindView(R.id.seek_price_text)
    TextView seek_price_text;
    @BindView(R.id.seek_contact_text)
    TextView seek_contact_text;
    @BindView(R.id.seek_title_text)
    TextView seek_title_text;
    @BindView(R.id.seek_tag1)
    TextView seek_tag1;
    @BindView(R.id.seek_tag2)
    TextView seek_tag2;
    @BindView(R.id.seek_content_text)
    TextView seek_content_text;
    @BindView(R.id.seek_text1)
    TextView seek_text1;
    @BindView(R.id.seek_text2)
    TextView seek_text2;
    @BindView(R.id.seek_text3)
    TextView seek_text3;
    private String uidString;
    private String user_type;
    private String mobileString = "13815067320";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        user_type = UserInfoUtils.getUserType(this);
        seekID = getIntent().getStringExtra("seek_id");
        seek_title = getIntent().getStringExtra("seek_title");
        seek_content = getIntent().getStringExtra("seek_content");
        seek_price = getIntent().getStringExtra("seek_price");
        if (seek_price.startsWith("0")) {
            seek_price = "价格可议";
        }
        seek_date = getIntent().getStringExtra("seek_date");
        String[] dateString = seek_date.split(" ");
        if(dateString.length>0){
            seek_date=dateString[0];
        }
        photos_urls = getIntent().getStringExtra("photos_urls");
        initTitleBarView(titlebar, "同行求助");
        seek_title_text.setText(seek_title);
        seek_price_text.setText(seek_price);
        if (seek_price.equals("价格可议")) {
            seek_price_text.setText(seek_price);
        } else {
            seek_price_text.setText("￥" + seek_price);
        }
        seek_content_text.setText("\u3000\u3000\u3000"+seek_content);
        seek_text2.setText(seek_date);
        if (!TextUtils.isEmpty(photos_urls) && photos_urls.length() > 32) {
            try {
                JSONArray imageArray = new JSONArray(photos_urls);
                if (imageArray.length() > 0) {
                    ArrayList<String> viewpageDatas = new ArrayList<>();
                    for (int i = 0; i < imageArray.length(); i++) {
                        String str = imageArray.getString(i);
                        if (!str.startsWith("http://")) {
                            str = Constant.THINKCMF_PATH + str;
                        }
                        viewpageDatas.add(str);
                    }
                    setSeekViewPagerData(viewpageDatas);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getContentView() {
        return R.layout.activity_seek_help_detail;
    }

    @OnClick({R.id.seek_contact_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.seek_contact_text:
                if (user_type.equals("2")) {
                    onShowReviewDialog();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileString));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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

    private void setSeekViewPagerData(List<String> data) {
        seek_convenientBanner.setPages(
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
        seek_convenientBanner.startTurning(5000);
        seek_convenientBanner.setScrollDuration(3000);
        try {
            Class<?> clazz = Class.forName("com.bigkoo.convenientbanner.ConvenientBanner");
            Field loPageTurningPoint = clazz.getDeclaredField("loPageTurningPoint");
            loPageTurningPoint.setAccessible(true);
            ViewGroup o = (ViewGroup) loPageTurningPoint.get(seek_convenientBanner);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) o.getLayoutParams();
            layoutParams.bottomMargin = SizeUtils.dp2px(36);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        seek_convenientBanner.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                ImagePagerActivity.imageSize = new ImageSize(seek_convenientBanner.getWidth(), seek_convenientBanner.getHeight());
                ImagePagerActivity.startImagePagerActivity(SeekHelperDetailActivity.this, data, position);
            }
        });

    }
}
