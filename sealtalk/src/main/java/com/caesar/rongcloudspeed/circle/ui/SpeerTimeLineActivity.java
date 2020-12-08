package com.caesar.rongcloudspeed.circle.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.circle.adapter.TimeLineAdapter;
import com.caesar.rongcloudspeed.circle.widgets.MultiImageView;
import com.caesar.rongcloudspeed.data.result.CircleHeaderResult;
import com.caesar.rongcloudspeed.data.result.CircleItemResult;
import com.caesar.rongcloudspeed.easypop.CmmtPopup;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.MainDiscoveryActivity;
import com.caesar.rongcloudspeed.ui.activity.SPLessonDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.SectionMessageActivity;
import com.caesar.rongcloudspeed.ui.activity.SeekHelperDetailActivity;
import com.caesar.rongcloudspeed.ui.dialog.LoadingDialog;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.yiw.circledemo.bean.CircleHeaderItem;
import com.yiw.circledemo.bean.CircleItem;
import com.yiw.circledemo.listener.SwipeListViewOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import me.corer.verticaldrawerlayout.VerticalDrawerLayout;

/**
 * @author yiw
 * @ClassName: FriendTimeLineActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午4:21:18
 */
public class SpeerTimeLineActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener {

    protected static final String TAG = SpeerTimeLineActivity.class.getSimpleName();
    private ListView friendCircleListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TimeLineAdapter timeLineAdapter;
    private ImageView back;
    private ImageView add;
    private LinearLayout drawerLayout;
    private VerticalDrawerLayout mDrawerLayout;
    private ImageView mArrow;
    private LoadingDialog dialog;
    private RelativeLayout peers_header_message;
    private RelativeLayout peers_header_lesson;
    private RelativeLayout peers_header_seek;
    private TextView messageMore,lessonMore,seekMore;
    private TextView messgaeText,lessonText,seeekText;
    private CircleHeaderItem headerItem1;
    private CircleHeaderItem headerItem2;
    private String userid;
    private String username;
    private CmmtPopup mCmmtPopup;
    private int circlePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speer_circle_layout);
        userid = UserInfoUtils.getAppUserId(this);
        username = UserInfoUtils.getNickName(this);
        initView();
        loadData();
        initCmmtPop();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mRefreshLayout);
        friendCircleListView = (ListView) findViewById(R.id.friend_circle_list);
        back = (ImageView) findViewById(R.id.back);
        add = (ImageView) findViewById(R.id.add);
        drawerLayout = (LinearLayout) findViewById(R.id.drawerLayout);
        mDrawerLayout = (VerticalDrawerLayout) findViewById(R.id.speerVertical);
        mArrow = (ImageView) findViewById(R.id.circleArrow);
        mDrawerLayout.setDrawerListener(new VerticalDrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mArrow.setRotation(slideOffset*180);
            }
        });
        drawerLayout.setOnClickListener(view -> {
            if (mDrawerLayout.isDrawerOpen()) {
                mDrawerLayout.closeDrawer();
            } else {
                mDrawerLayout.openDrawerView();
            }
        });
        back.setOnClickListener(view -> {
            finish();
        });
        add.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainDiscoveryActivity.class);
            startActivity(intent);
        });
        friendCircleListView.setOnScrollListener(new SwipeListViewOnScrollListener(mSwipeRefreshLayout));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        timeLineAdapter = new TimeLineAdapter(this);
        timeLineAdapter.setOnMultiImageItemClickListener((parent, view, position) -> {
            MultiImageView multiImageView = (MultiImageView) parent;
            ImagePagerActivity.imageSize = new ImageSize(view.getWidth(), view.getHeight());
            Log.d("view.getWidth():", String.valueOf(view.getWidth()) + ",view.getHeight():" + String.valueOf(view.getHeight()));
            ImagePagerActivity.startImagePagerActivity(SpeerTimeLineActivity.this, multiImageView.getImagesList(), position);
        });
        friendCircleListView.setAdapter(timeLineAdapter);

        peers_header_message = (RelativeLayout) findViewById(R.id.peers_header_message);
        peers_header_lesson = (RelativeLayout) findViewById(R.id.peers_header_lesson);
        peers_header_seek = (RelativeLayout) findViewById(R.id.peers_header_seek);
        messageMore = (TextView) findViewById(R.id.message_more);
        lessonMore = (TextView) findViewById(R.id.lesson_more);
        seekMore = (TextView) findViewById(R.id.seek_more);
        messgaeText = (TextView) findViewById(R.id.message_title);
        lessonText = (TextView) findViewById(R.id.lesson_title);
        seeekText = (TextView) findViewById(R.id.seek_title);

        peers_header_lesson.setOnClickListener(view -> {
            String lessonID = headerItem1.getObject_id();
            String lessonName = headerItem1.getPost_title();
            String lessonPrice = headerItem1.getPost_price();
            String lessonSmeta = headerItem1.getSmeta();
            String lessonContent = headerItem1.getPost_excerpt();
            String lessonSource = headerItem1.getPost_source();
            Intent intent = new Intent(SpeerTimeLineActivity.this, SPLessonDetailActivity.class);
            intent.putExtra("lesson_id", lessonID);
            intent.putExtra("lesson_name", lessonName);
            intent.putExtra("lesson_price", lessonPrice);
            intent.putExtra("lesson_smeta", lessonSmeta);
            intent.putExtra("lesson_content", lessonContent);
            intent.putExtra("lesson_source", lessonSource);
            startActivity(intent);
        });

        peers_header_seek.setOnClickListener(view -> {
            String seekID = headerItem2.getObject_id();
            String rongID = headerItem2.getRongid();
            String userNiceName = headerItem2.getUser_nicename();
            String seekTitle = headerItem2.getPost_title();
            String seekPrice = headerItem2.getPost_price();
            String seekContent = headerItem2.getPost_excerpt();
            String seekDate = headerItem2.getPost_date();
            String photos_urls = headerItem2.getPhotos_urls();
            String post_author = headerItem2.getPost_author();
            Intent intent = new Intent(SpeerTimeLineActivity.this, SeekHelperDetailActivity.class);
            intent.putExtra("seek_id", seekID);
            intent.putExtra("rong_id", rongID);
            intent.putExtra("user_nicename", userNiceName);
            intent.putExtra("seek_title", seekTitle);
            intent.putExtra("seek_date", seekDate);
            intent.putExtra("seek_price", seekPrice);
            intent.putExtra("seek_content", seekContent);
            intent.putExtra("photos_urls", photos_urls);
            intent.putExtra("post_author", post_author);
            startActivity(intent);
        });

        peers_header_message.setOnClickListener(view -> {

        });

        messageMore.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("headerItem1", headerItem1);
            bundle.putSerializable("headerItem2", headerItem2);
            ActivityUtils.startActivity(bundle, SpeerTimeLineActivity.this, SectionMessageActivity.class);
        });
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }

    private void loadData() {
        showLoadingDialog("");
        List<CircleItem> datas = new ArrayList<>();
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchFriendCircle(userid),
                new NetworkCallback<CircleItemResult>() {
                    @Override
                    public void onSuccess(CircleItemResult circleItemResult) {
                        if (circleItemResult != null && circleItemResult.getReferer() != null) {
                            timeLineAdapter.setDatas(circleItemResult.getReferer().getPosts());
                            timeLineAdapter.notifyDataSetChanged();
                        }
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(SpeerTimeLineActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().headerFriendCircle(),
                new NetworkCallback<CircleHeaderResult>() {
                    @Override
                    public void onSuccess(CircleHeaderResult circleHeaderResult) {
                        if (circleHeaderResult != null && circleHeaderResult.getReferer() != null) {
                            List<CircleHeaderItem> circleList = circleHeaderResult.getReferer();
                            for (CircleHeaderItem item : circleList) {
                                String termString = item.getTerm_id();
                                if (termString.equals("43")) {
                                    headerItem2 = item;
                                    seeekText.setText("\u3000\u3000\u3000"+item.getPost_title());
                                } else {
                                    headerItem1 = item;
                                    lessonText.setText("\u3000\u3000\u3000"+item.getPost_title());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("Throwable", String.valueOf(t));
                        Toast.makeText(SpeerTimeLineActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除所有
        handler.removeCallbacksAndMessages(null);
    }

    private long dialogCreateTime;
    private Handler handler = new Handler();

    /**
     * 显示加载 dialog
     *
     * @param msg
     */
    public void showLoadingDialog(String msg) {
        if (dialog == null || (dialog.getDialog() != null && !dialog.getDialog().isShowing())) {
            dialogCreateTime = System.currentTimeMillis();
            dialog = new LoadingDialog();
            dialog.setLoadingInformation(msg);
            dialog.show(getSupportFragmentManager(), "loading_dialog");
        }
    }

    private void initCmmtPop() {
        mCmmtPopup = CmmtPopup.create(this)
                .setOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCmmtPopup.isShowing()) {
                            //无法隐藏输入法。只有toggle方法起作用...
                            KeyboardUtils.hideSoftInput(SpeerTimeLineActivity.this);
                            mCmmtPopup.hideSoftInput()
                                    .dismiss();

                        }
                    }
                })
                .setOnOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCmmtPopup.isShowing()) {
                            String content = mCmmtPopup.mEditText.getText().toString();
                            if (TextUtils.isEmpty(content)) {
                                Toast.makeText(SpeerTimeLineActivity.this, "内容不能为空！", Toast.LENGTH_LONG).show();
                            } else {
                                timeLineAdapter.updateAddComment(circlePosition, username, content);
                                //无法隐藏输入法。只有toggle方法起作用...
                                mCmmtPopup.mEditText.setText("");
                                mCmmtPopup.hideSoftInput().dismiss();
                                hideInput();
                            }

                        }
                    }
                })
                .apply();

    }

    public void showCmmtPop(int position) {
        circlePosition = position;
        mCmmtPopup.showSoftInput()
                .showAtLocation(friendCircleListView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 显示加载 dialog
     *
     * @param msgResId
     */
    public void showLoadingDialog(int msgResId) {
        showLoadingDialog(getString(msgResId));
    }

    /**
     * 取消加载dialog
     */
    public void dismissLoadingDialog() {
        dismissLoadingDialog(null);
    }

    /**
     * 取消加载dialog. 因为延迟， 所以要延时完成之后， 再在 runnable 中执行逻辑.
     * <p>
     * 延迟关闭时间是因为接口有时返回太快。
     */
    public void dismissLoadingDialog(Runnable runnable) {
        if (dialog != null && dialog.getDialog() != null && dialog.getDialog().isShowing()) {
            // 由于可能请求接口太快，则导致加载页面一闪问题， 所有再次做判断，
            // 如果时间太快（小于 500ms）， 则会延时 1s，再做关闭。
            if (System.currentTimeMillis() - dialogCreateTime < 500) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (runnable != null) {
                            runnable.run();
                        }
                        dialog.dismiss();
                        dialog = null;
                    }
                }, 1000);

            } else {
                dialog.dismiss();
                dialog = null;
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
