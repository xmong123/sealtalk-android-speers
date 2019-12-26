package com.caesar.rongcloudspeed.circle.ui;

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
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午4:21:18
 */
public class FriendCircle1Activity extends Activity implements SwipeRefreshLayout.OnRefreshListener, HeightComputable {

    protected static final String TAG = FriendCircle1Activity.class.getSimpleName();
    private ListView mCircleLv;
    private Circle1Adapter mAdapter;
    private LinearLayout mEditTextBody;
    private EditText mEditText;
    private TextView sendTv;
    private ImageView back;
    private ImageView img_bg;
    private ImageView photo;

    private int mScreenHeight;
    private int mEditTextBodyHeight;
    private CirclePublicCommentController mCirclePublicCommentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle1);
        initView();
        loadData();
    }

    private void initView() {
        mCircleLv = (ListView) findViewById(R.id.circleLv);
        back = (ImageView) findViewById(R.id.back);
        img_bg = (ImageView) findViewById(R.id.img_bg);
        photo = (ImageView) findViewById(R.id.photo);
        back.setOnClickListener(view -> {
            finish();
        });
        mCircleLv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mEditTextBody.getVisibility() == View.VISIBLE) {
                    mEditTextBody.setVisibility(View.GONE);
                    CommonUtils.hideSoftInput(FriendCircle1Activity.this, mEditText);
                    return true;
                }
                return false;
            }
        });
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mAdapter = new Circle1Adapter(this);
        mAdapter.setOnMultiImageItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                MultiImageView multiImageView = (MultiImageView) parent;
                ImagePagerActivity.imageSize = new ImageSize(view.getWidth(), view.getHeight());
                ImagePagerActivity.startImagePagerActivity(FriendCircle1Activity.this, multiImageView.getImagesList(), position);
            }
        });
        mCircleLv.setAdapter(mAdapter);
        mEditTextBody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        mEditText = (EditText) findViewById(R.id.circleEt);
        sendTv = (TextView) findViewById(R.id.sendTv);

        mCirclePublicCommentController = new CirclePublicCommentController(this, mEditTextBody, mEditText, sendTv);
        mCirclePublicCommentController.setListView(mCircleLv);
        mAdapter.setCirclePublicCommentController(mCirclePublicCommentController);

        setViewTreeObserver();

        Glide.with(this).load(UserInfoUtils.getAppUserUrl(this)).into(photo);
    }


    private void setViewTreeObserver() {

//        final ViewTreeObserver swipeRefreshLayoutVTO = mSwipeRefreshLayout.getViewTreeObserver();
//        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                Rect r = new Rect();
//                mSwipeRefreshLayout.getWindowVisibleDisplayFrame(r);
//                int screenH = mSwipeRefreshLayout.getRootView().getHeight();
//                int keyH = screenH - (r.bottom - r.top);
//                if (keyH == CommonUtils.keyboardHeight) {//有变化时才处理，否则会陷入死循环
//                    return;
//                }
//                Log.d(TAG, "keyH = " + keyH + " &r.bottom=" + r.bottom + " &top=" + r.top);
//                CommonUtils.keyboardHeight = keyH;
//                mScreenHeight = screenH;//应用屏幕的高度
//                mEditTextBodyHeight = mEditTextBody.getHeight();
//                if (mCirclePublicCommentController != null) {
//                    mCirclePublicCommentController.handleListViewScroll();
//                }
//            }
//        });
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
        List<CircleItem> datas = new ArrayList<>();
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

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getEditTextBodyHeight() {
        return mEditTextBodyHeight;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mEditTextBody != null && mEditTextBody.getVisibility() == View.VISIBLE) {
                mEditTextBody.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
