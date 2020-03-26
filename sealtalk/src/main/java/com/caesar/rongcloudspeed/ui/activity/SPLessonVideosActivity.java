package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.MoreLessonesAdapter;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.player.PLVideoViewActivity;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.pili.pldroid.player.AVOptions;
import com.tencent.smtt.sdk.TbsVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.caesar.rongcloudspeed.quick.QiniuLabConfig.ADVERT_SERVICE_VIDEO;

public class SPLessonVideosActivity extends MultiStatusActivity {

    private static final String TAG = "SPLessonVideosActivity";

    @BindView(R.id.lessonPosterImageView)
    ImageView lessonPosterImageView;
    @BindView(R.id.lesson_video_title)
    TextView lesson_video_title;
    @BindView(R.id.detail_text_tag)
    TextView detail_text_tag;
    @BindView(R.id.lesson_videos_recyclerView)
    RecyclerView lesson_videos_recyclerView;
    private String lesson_id;
    private boolean lesson_status;
    private String lessonVideoString;
    private String lesson_name;
    private String lesson_price;
    private String lesson_smeta;
    private String uidString;
    private View lessonHeadView;
    private LinearLayout lesson_selected_contact_container;
    private LayoutInflater mInflater;
    private Button lesson_more_btn;
    private MoreLessonesAdapter moreLessonesAdapter;
    private List<PostsArticleBaseBean> dataArray = new ArrayList<PostsArticleBaseBean>();
    private String thumbString = null;
    private JSONArray videoArray = null;
    private String userType = "2";
    private List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();
    private List<TextView> textViews = new ArrayList<TextView>();
    private String advertVideoUrl;
    private String[] advertVideoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        userType = UserInfoUtils.getUserType(this);
        lesson_id = getIntent().getExtras().getString("lesson_id");
        lesson_status = getIntent().getExtras().getBoolean("lesson_status");
        lesson_name = getIntent().getExtras().getString("lesson_name");
        lesson_price = getIntent().getExtras().getString("lesson_price");
        lesson_smeta = getIntent().getExtras().getString("lesson_smeta");
        lessonVideoString = getIntent().getExtras().getString("videoPath");
        try {
            JSONObject jsonSmeta = new JSONObject(lesson_smeta);
            thumbString = jsonSmeta.getString("thumb");
            JSONArray photoArray = jsonSmeta.getJSONArray("photo");
            if (photoArray != null && photoArray.length() > 0) {
                videoArray = photoArray;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (thumbString != null && !thumbString.startsWith("http://")) {
            thumbString = Constant.THINKCMF_PATH + thumbString;
        }
        if (thumbString != null && thumbString.length() > 32) {
            Glide.with(this).load(thumbString).into(lessonPosterImageView);
        }
        initTitleBarView(titlebar, "课程视频");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        Glide.with(this).load(thumbVideoString+"?vframe/jpg/offset/1").into(convenientBanner);
        uidString = UserInfoUtils.getAppUserId(this);
        Set<String> set = UserInfoUtils.getAdvertVideoList(this);
        if (set != null && set.size() > 0) {
            advertVideoArray = set.toArray(new String[set.size()]);
        }
        lessonHeadView = getLayoutInflater().inflate(R.layout.left_lesson_videos_header, (ViewGroup) lesson_videos_recyclerView.getParent(), false);
        lesson_selected_contact_container = (LinearLayout) lessonHeadView.findViewById(R.id.lesson_selected_contact_container);
        mInflater = LayoutInflater.from(this);
        if (videoArray != null && videoArray.length() > 0) {
            linearLayouts = new ArrayList<LinearLayout>();
            textViews = new ArrayList<TextView>();
            for (int i = 0; i < videoArray.length(); i++) {
                View view = mInflater.inflate(R.layout.activity_index_gallery_item,
                        lesson_selected_contact_container, false);
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.id_index_gallery_item_layout);
                TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
                txt.setText(String.valueOf(i + 1));
                int finalI = i;
                String urlString = lessonVideoString;
                String altString = lesson_name;
                try {
                    JSONObject photoObj = videoArray.getJSONObject(finalI);
                    urlString = photoObj.getString("url");
                    altString = photoObj.getString("alt");
                    if (!urlString.startsWith("http://")) {
                        urlString = Constant.THINKCMF_PATH + urlString;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (i == 0) {
                    txt.setEnabled(false);
                    layout.setEnabled(false);
                    lessonVideoString = urlString;
                    Glide.with(SPLessonVideosActivity.this).load(lessonVideoString + "?vframe/jpg/offset/1").into(lessonPosterImageView);
                }
                String finalUrlString = urlString;
                String finalAltString = altString;
                view.setOnClickListener(view1 -> {
                    for (int j = 0; j < linearLayouts.size(); j++) {
                        TextView text = (TextView) textViews.get(j);
                        LinearLayout layouts = (LinearLayout) linearLayouts.get(j);
                        text.setEnabled(finalI != j);
                        layouts.setEnabled(finalI != j);
                    }
                    lessonVideoString = finalUrlString;
                    Glide.with(SPLessonVideosActivity.this).load(finalUrlString + "?vframe/jpg/offset/1").into(lessonPosterImageView);
                    lesson_video_title.setText(finalAltString);
                    ToastUtils.showToast(finalAltString, Toast.LENGTH_LONG);
                });
                textViews.add(txt);
                linearLayouts.add(layout);
                lesson_selected_contact_container.addView(view);
            }
        } else {
            View view = mInflater.inflate(R.layout.activity_index_gallery_item,
                    lesson_selected_contact_container, false);
            TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
            txt.setText(String.valueOf(1));
            view.setOnClickListener(view12 -> ToastUtils.showToast(String.valueOf(1)));
            lesson_selected_contact_container.addView(view);
        }

        lesson_more_btn = lessonHeadView.findViewById(R.id.lesson_more_btn);
        lesson_more_btn.setOnClickListener(view -> ToastUtils.showToast("More"));

        lessonPosterImageView.setOnClickListener(view -> {
            if (userType.equals("6")) {
                if (TbsVideo.canUseTbsPlayer(SPLessonVideosActivity.this)) {
                    TbsVideo.openVideo(SPLessonVideosActivity.this, lessonVideoString);
                }
            } else {
                if (advertVideoArray != null && advertVideoArray.length > 0) {
                    int number = (int) (Math.random() * advertVideoArray.length);
                    advertVideoUrl = advertVideoArray[number];
                } else {
                    advertVideoUrl = ADVERT_SERVICE_VIDEO;
                }
                Intent intent = new Intent(this, PLVideoViewActivity.class);
                intent.putExtra("videoPath", advertVideoUrl);
                intent.putExtra("lessonVideoString", lessonVideoString);
                intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
                intent.putExtra("liveStreaming", 1);
                intent.putExtra("cache", false);
                intent.putExtra("loop", false);
                intent.putExtra("video-data-callback", false);
                intent.putExtra("audio-data-callback", false);
                intent.putExtra("disable-log", false);
                intent.putExtra("lesson_id", lesson_id);
                intent.putExtra("lesson_name", lesson_name);
                intent.putExtra("lesson_price", lesson_price);
                intent.putExtra("lesson_smeta", lesson_smeta);
                startActivity(intent);
            }

        });

        moreLessonesAdapter = new MoreLessonesAdapter(this, dataArray);
        moreLessonesAdapter.openLoadAnimation();
        moreLessonesAdapter.setNotDoAnimationCount(4);
//        lessonRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        moreLessonesAdapter.setOnItemClickListener((adapter, view, position) -> {
            PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
            String lessonID = postsArticleBaseBean.getObject_id();
            String lessonName = postsArticleBaseBean.getPost_title();
            String lessonPrice = postsArticleBaseBean.getPost_price();
            String lessonContent = postsArticleBaseBean.getPost_excerpt();
            String lessonSmeta = postsArticleBaseBean.getSmeta();
            String lessonSource = postsArticleBaseBean.getPost_source();
            String thumbVideoString = postsArticleBaseBean.getThumb_video();
            if (!(thumbVideoString.startsWith("http://") || thumbVideoString.startsWith("https://"))) {
                thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
            }
            Intent intent = new Intent(SPLessonVideosActivity.this, SPLessonDetailActivity.class);
            intent.putExtra("videoPath", thumbVideoString);
            intent.putExtra("lesson_id", lessonID);
            intent.putExtra("lesson_name", lessonName);
            intent.putExtra("lesson_price", lessonPrice);
            intent.putExtra("lesson_smeta", lessonSmeta);
            intent.putExtra("lesson_content", lessonContent);
            intent.putExtra("lesson_source", lessonSource);
            startActivity(intent);
        });
        lesson_videos_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        moreLessonesAdapter.addHeaderView(lessonHeadView);
        lesson_videos_recyclerView.setAdapter(moreLessonesAdapter);
        loadMoreLessonesData();
        lesson_video_title.setText(lesson_name);
        if (userType.equals("6")) {
            detail_text_tag.setText("VIP会员");
            detail_text_tag.setVisibility(View.VISIBLE);
        } else {
            if (lesson_status) {
                detail_text_tag.setText("已购买");
                detail_text_tag.setVisibility(View.VISIBLE);
            } else {
                detail_text_tag.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userType = UserInfoUtils.getUserType(this);
        if (userType.equals("6")) {
            detail_text_tag.setText("VIP会员");
            detail_text_tag.setVisibility(View.VISIBLE);
        }
    }

    private void loadMoreLessonesData() {
//        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas("4"),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        dataArray = homeDataBean.getReferer().getPosts();
                        moreLessonesAdapter.setNewData(dataArray);
//                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
//                        dismissLoadingDialog();
                        Toast.makeText(SPLessonVideosActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_lesson_videos_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
