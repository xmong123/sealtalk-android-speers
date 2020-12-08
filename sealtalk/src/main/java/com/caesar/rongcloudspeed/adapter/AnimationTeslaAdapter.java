package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.network.Api;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.util.ToastUtils;
import com.caesar.rongcloudspeed.util.Utils;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class AnimationTeslaAdapter extends BaseQuickAdapter<PostsArticleBaseBean, BaseViewHolder> {
    private Context context;
    private String type;
    String uidString = SPUtils.getInstance().getString( "uid", "0" );
    String user_type = SPUtils.getInstance().getString( "user_type", "0" );

    public AnimationTeslaAdapter(Context context, List data) {
        super(R.layout.fragment_thinkcmf_recyclerview_tesla_item, data);
        this.context = context;
    }

    public AnimationTeslaAdapter(Context context, List data, String type) {
        super(R.layout.fragment_thinkcmf_recyclerview_tesla_item, data);
        this.context = context;
        this.type = type;
    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            ToastUtils.showShortToast("事件触发了 landscapes and nedes");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Utils.getContext().getResources().getColor(R.color.clickspan_color));
            ds.setUnderlineText(true);
        }
    };

    @Override
    protected void convert(BaseViewHolder helper, PostsArticleBaseBean bean) {
        String titleString = bean.getPost_title();
        String priceString = bean.getPost_price();
        SpannableString spannableString = new SpannableString("单价："+priceString+"元/吨");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 3,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tweetName1, titleString);
        helper.setText(R.id.tweetText1, spannableString);
        helper.setText(R.id.tweetText2, "地点："+bean.getPost_area());
        helper.setText(R.id.tweetText3, "数量："+bean.getStore_count()+"吨");
        helper.setText(R.id.tweetText4, "批次：TXS042"+bean.getTid());
        helper.setText(R.id.tweetText5, "日期：" + bean.getPost_date());
        helper.setText(R.id.tweetText6, "提货：" + bean.getPost_delivery());
        helper.setText(R.id.tweetText7, "货物描述：" + bean.getPost_excerpt());

        helper.setOnClickListener(R.id.tweetName2, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uidString.equals( "0" )) {
                    ToastUtils.showShortToast("此功能需要用户登录后操作");
                }else{
                    if(user_type.equals("2")){
                        onShowVIPDialog(context,R.mipmap.ic_launcher);
                    }else{
                        RetrofitManager.create().DoFavoriteMobile(uidString,bean.getPost_title(),"posts",bean.getTid(),bean.getPost_mobile())
                                .observeOn( AndroidSchedulers.mainThread())
                                .subscribeOn( Schedulers.io())
                                .subscribe(new CommonObserver<CommonResonseBean>() {
                                    @Override
                                    public void onSuccess(CommonResonseBean value) {
                                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.getPost_mobile()));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        Toast.makeText(context, R.string.network_err, Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                }

            }
        });
    }
    
}
