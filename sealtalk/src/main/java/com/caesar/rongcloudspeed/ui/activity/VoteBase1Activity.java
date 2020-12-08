package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.player.PLVideoViewActivity;
import com.caesar.rongcloudspeed.quick.QuickStartVideoExampleActivity;
import com.caesar.rongcloudspeed.ui.BaseActivity;
import com.caesar.rongcloudspeed.ui.adapter.AnimationAdapter3;
import com.caesar.rongcloudspeed.ui.view.SealTitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.AVOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VoteBase1Activity extends BaseActivity {
    private SealTitleBar titleBar;
    private String catID="4";
    private AnimationAdapter3 animationAdapter3;
    private RecyclerView recyclerview;
    private List<PostsArticleBaseBean> dataArray=new ArrayList<PostsArticleBaseBean>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        titleBar = findViewById(R.id.title_bar);
        recyclerview = findViewById(R.id.order_recyclerView);
        catID=getIntent().getStringExtra("catID");
        if(catID.equals("4")){
            titleBar.setTitle(R.string.vote_circle1);
        }else if(catID.equals("5")){
            titleBar.setTitle(R.string.vote_circle2);
        }else if(catID.equals("6")){
            titleBar.setTitle(R.string.vote_circle3);
        }
        setTitleBarType(SealTitleBar.Type.NORMAL);
        getTitleBar().setRightImage(R.drawable.add);
        getTitleBar().setOnBtnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getTitleBar().setOnBtnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(VoteBase1Activity.this, QuickStartVideoExampleActivity.class);
            }
        });

        initanimationAdapter();
        loadData();
    }

    private void loadData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas(catID),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        dataArray=homeDataBean.getReferer().getPosts();
                        animationAdapter3.setNewData(dataArray);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(VoteBase1Activity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public SealTitleBar getTitleBar() {
        return titleBar;
    }

    public void setTitleBarType(SealTitleBar.Type type) {
        titleBar.setType(type);
    }

    @Override
    public void finish() {
        super.finish();
        hideInputKeyboard();
    }

    private void initanimationAdapter() {
        animationAdapter3 = new AnimationAdapter3(this,dataArray,catID);
        animationAdapter3.openLoadAnimation();
        animationAdapter3.setNotDoAnimationCount(3);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        animationAdapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostsArticleBaseBean postsArticleBaseBean=dataArray.get( position );
//                Bundle bundle = new Bundle();
                String postID=postsArticleBaseBean.getObject_id();
//                String cid=postsArticleBaseBean.getTerm_id();
//                String webString="http://thinkcmf.91bim.net/index.php?g=&m=article&a=index&id="+postID+"&cid="+cid;
//                bundle.putString("url", webString);
//                bundle.putString("title" , postsArticleBaseBean.getPost_title());
//                bundle.putString("postID" , postID);
//                ActivityUtils.startActivity(bundle, VoteBaseActivity.this, WebViewActivity.class);
                String thumbString = null;
                try {
                    JSONObject jsonSmeta = new JSONObject( postsArticleBaseBean.getSmeta() );
                    thumbString = jsonSmeta.getString( "thumb" );
                } catch (JSONException e) {
                    e.printStackTrace();
                 }
                Intent intent = new Intent(VoteBase1Activity.this, PLVideoViewActivity.class);
                intent.putExtra("videoPath", thumbString);
                intent.putExtra("postID" , postID);
                intent.putExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
                intent.putExtra("liveStreaming", 1);
                intent.putExtra("cache", true);
                intent.putExtra("loop", true);
                intent.putExtra("video-data-callback", false);
                intent.putExtra("audio-data-callback", false);
                intent.putExtra("disable-log", false);
                startActivity(intent);
            }
        });
        recyclerview.setAdapter(animationAdapter3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDatas(catID),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        dataArray=homeDataBean.getReferer().getPosts();
                        animationAdapter3.setNewData(dataArray);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(VoteBase1Activity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }


}
