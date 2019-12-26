package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.player.PLVideoViewActivity;
import com.caesar.rongcloudspeed.ui.adapter.BookAdapter;
import com.caesar.rongcloudspeed.ui.adapter.LessonAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.AVOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面子界面-发现界面
 */
public class HomeDiscoveryFragment extends BaseFragment {
    private RecyclerView lessonRecyclerView;
    private RecyclerView bookRecyclerView;
    private LessonAdapter lessonAdapter;
    private BookAdapter bookAdapter;
    private List<PostsArticleBaseBean> dataArray=new ArrayList<PostsArticleBaseBean>();

    @Override
    protected int getLayoutResId() {
        return R.layout.main_fragment_discovery_home;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        findView(R.id.discovery_ll_chat_room_1, true);
        findView(R.id.discovery_ll_chat_room_2, true);

        lessonRecyclerView = getActivity().findViewById(R.id.lesson_recyclerView);
        lessonAdapter = new LessonAdapter(getActivity(),dataArray);
        lessonAdapter.openLoadAnimation();
        lessonAdapter.setNotDoAnimationCount(4);
        lessonRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        lessonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostsArticleBaseBean postsArticleBaseBean=dataArray.get( position );
                String postID=postsArticleBaseBean.getObject_id();
                String thumbVideoString = postsArticleBaseBean.getThumb_video();
                if (!thumbVideoString.startsWith( "http://" )) {
                    thumbVideoString = Constant.THINKCMF_PATH + thumbVideoString;
                }
                Intent intent = new Intent(getActivity(), PLVideoViewActivity.class);
                intent.putExtra("videoPath", thumbVideoString);
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
        lessonRecyclerView.setAdapter(lessonAdapter);


        bookRecyclerView = getActivity().findViewById(R.id.book_recyclerView);
        bookAdapter = new BookAdapter(getActivity(),dataArray);
        bookAdapter.openLoadAnimation();
        bookAdapter.setNotDoAnimationCount(4);
        bookRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        bookRecyclerView.setAdapter(bookAdapter);
        loadData();
    }

    private void loadData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchVoteListDataLimit("3","4"),
                new NetworkCallback<HomeDataBean>() {
                    @Override
                    public void onSuccess(HomeDataBean homeDataBean) {
                        dataArray=homeDataBean.getReferer().getPosts();
                        lessonAdapter.setNewData(dataArray);
                        bookAdapter.setNewData(dataArray);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            case R.id.discovery_ll_chat_room_1:
                enterChatRoom(0, getString(R.string.discovery_chat_room_one));
                break;
            case R.id.discovery_ll_chat_room_2:
                enterChatRoom(1, getString(R.string.discovery_chat_room_two));
                break;
            default:
                break;
        }
    }

    /**
     * 进入聊天室
     *
     * @param roomIndex
     * @param roomTitle
     */
    private void enterChatRoom(int roomIndex, String roomTitle) {

    }
}
