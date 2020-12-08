package com.caesar.rongcloudspeed.circle.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.circle.adapter.Circle1Adapter;
import com.caesar.rongcloudspeed.circle.adapter.CircleAdapter;
import com.caesar.rongcloudspeed.circle.contral.CirclePublicCommentController;
import com.caesar.rongcloudspeed.circle.widgets.MultiImageView;
import com.caesar.rongcloudspeed.data.result.CircleItemResult;
import com.caesar.rongcloudspeed.data.result.CircleItemResult1;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.yiw.circledemo.bean.CircleItem;
import com.yiw.circledemo.listener.SwipeListViewOnScrollListener;
import com.yiw.circledemo.utils.CommonUtils;
import com.yiw.circledemo.utils.HeightComputable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiw
 * @ClassName: FriendCircleActivity
 * @Description: ===TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午4:21:18
 */
public class FriendCircle1Activity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    protected static final String TAG = FriendCircle1Activity.class.getSimpleName();
    private ListView personalCircleLv;
    private Circle1Adapter mAdapter;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle1);
        initView();
        loadData();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        personalCircleLv = (ListView) findViewById(R.id.personalCircleLv);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });
        mAdapter = new Circle1Adapter(this);
        mAdapter.setOnMultiImageItemClickListener((parent, view, position) -> {
            MultiImageView multiImageView = (MultiImageView) parent;
            ImagePagerActivity.imageSize = new ImageSize(view.getWidth(), view.getHeight());
            ImagePagerActivity.startImagePagerActivity(FriendCircle1Activity.this, multiImageView.getImagesList(), position);
        });
        personalCircleLv.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 2000);

    }

    private void loadData() {
        String userid=getIntent().getStringExtra("userid");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchAblum(userid),
                new NetworkCallback<CircleItemResult1>() {
                    @Override
                    public void onSuccess(CircleItemResult1 circleItemResult1) {
                        mAdapter.setDatas(circleItemResult1.getReferer());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(FriendCircle1Activity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
