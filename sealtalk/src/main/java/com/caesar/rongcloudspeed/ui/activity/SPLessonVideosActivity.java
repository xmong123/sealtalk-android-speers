package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.caesar.rongcloudspeed.ui.adapter.HorizontalScrollViewAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.caesar.rongcloudspeed.ui.fragment.SPSpeerVideoLeftFragment;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.utils.X5WebView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

public class SPLessonVideosActivity extends MultiStatusActivity {

    @BindView(R.id.lessonPosterImageView)
    ImageView lessonPosterImageView;
    @BindView(R.id.lesson_video_title)
    TextView lesson_video_title;
    @BindView(R.id.lesson_videos_recyclerView)
    RecyclerView lesson_videos_recyclerView;
    private String lesson_id;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        lesson_id = getIntent().getExtras().getString("lesson_id");
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
        if (thumbString != null &&!thumbString.startsWith("http://")) {
            thumbString = Constant.THINKCMF_PATH + thumbString;
        }
        if (thumbString != null && thumbString.length() > 32) {
            Glide.with(this).load(thumbString).into(lessonPosterImageView);
        }
        initTitleBarView(titlebar, "课程视频");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        Glide.with(this).load(thumbVideoString+"?vframe/jpg/offset/1").into(convenientBanner);
        uidString = UserInfoUtils.getAppUserId(this);
        lessonHeadView = getLayoutInflater().inflate(R.layout.left_lesson_videos_header, (ViewGroup) lesson_videos_recyclerView.getParent(), false);
        lesson_selected_contact_container = (LinearLayout) lessonHeadView.findViewById(R.id.lesson_selected_contact_container);
        mInflater = LayoutInflater.from(this);
        if (videoArray != null && videoArray.length() > 0) {
            for (int i = 0; i < videoArray.length(); i++) {
                //InputStream is = getAssets().open(galleryDirectoryName + "/" + imageName);
                //final Bitmap bitmap = BitmapFactory.decodeStream(is);

                View view = mInflater.inflate(R.layout.activity_index_gallery_item,
                        lesson_selected_contact_container, false);
                TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
                txt.setText(String.valueOf(i + 1));
                int finalI = i;
                int finalI1 = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            JSONObject photoObj = videoArray.getJSONObject(finalI1);
                            String urlString = photoObj.getString("url");
                            String altString = photoObj.getString("alt");
//                            if (TbsVideo.canUseTbsPlayer(SPLessonVideosActivity.this)) {
//                                TbsVideo.openVideo(SPLessonVideosActivity.this, thumbVideoString);
//                            }
                            if (!urlString.startsWith("http://")) {
                                urlString = Constant.THINKCMF_PATH + urlString;
                            }
                            lessonVideoString=urlString;
                            Glide.with(SPLessonVideosActivity.this).load(urlString + "?vframe/jpg/offset/1").into(lessonPosterImageView);
                            lesson_video_title.setText(altString);
                            ToastUtils.showToast(altString, Toast.LENGTH_LONG);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                lesson_selected_contact_container.addView(view);
            }
        } else {
            View view = mInflater.inflate(R.layout.activity_index_gallery_item,
                    lesson_selected_contact_container, false);
            TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
            txt.setText(String.valueOf(1));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtils.showToast(String.valueOf(1));
                }
            });

            lesson_selected_contact_container.addView(view);
        }

        lesson_more_btn = lessonHeadView.findViewById(R.id.lesson_more_btn);
        lesson_more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast("More");
            }
        });

        lessonPosterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SPLessonVideosActivity.this,
//                        FullScreenActivity.class);
//                intent.putExtra("videoPath", lessonVideoString);
//                startActivity(intent);
                if (TbsVideo.canUseTbsPlayer(SPLessonVideosActivity.this)) {
                                TbsVideo.openVideo(SPLessonVideosActivity.this, lessonVideoString);
                            }
            }
        });

        moreLessonesAdapter = new MoreLessonesAdapter(this, dataArray);
        moreLessonesAdapter.openLoadAnimation();
        moreLessonesAdapter.setNotDoAnimationCount(4);
//        lessonRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        moreLessonesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
                String lessonID = postsArticleBaseBean.getObject_id();
                String lessonName = postsArticleBaseBean.getPost_title();
                String lessonPrice = postsArticleBaseBean.getPost_price();
                String thumbVideoString = postsArticleBaseBean.getThumb_video();
                if (!(thumbVideoString.startsWith("http://") || thumbVideoString.startsWith("https://"))) {
                    thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
                }
                Intent intent = new Intent(SPLessonVideosActivity.this, SPLessonDetailActivity.class);
                intent.putExtra("videoPath", thumbVideoString);
                intent.putExtra("lesson_id", lessonID);
                intent.putExtra("lesson_name", lessonName);
                intent.putExtra("lesson_price", lessonPrice);
                startActivity(intent);
//                Intent intent = new Intent(getActivity(),
//                        FullScreenActivity.class);
//                intent.putExtra("videoPath", thumbVideoString);
//                startActivity(intent);
            }
        });
        lesson_videos_recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        moreLessonesAdapter.addHeaderView(lessonHeadView);
        lesson_videos_recyclerView.setAdapter(moreLessonesAdapter);
        loadMoreLessonesData();
        lesson_video_title.setText(lesson_name);
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
