package com.caesar.rongcloudspeed.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.ui.adapter.models.SearchModel;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.BaseViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchConversationViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchDivViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchFriendViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchGroupViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchLessonViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchMessageViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchOtherViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchShowMoreViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.SearchTitleViewHolder;
import com.caesar.rongcloudspeed.ui.interfaces.OnChatItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnCircleItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnContactItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnGroupItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnLessonItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnMessageRecordClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnRecruitItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnSeekItemClickListener;
import com.caesar.rongcloudspeed.ui.interfaces.OnShowMoreClickListener;

import java.util.ArrayList;
import java.util.List;

public class SearchOtherAdapter extends RecyclerView.Adapter<BaseViewHolder<PostsArticleBaseBean>> {
    private List<PostsArticleBaseBean> data;
    private OnLessonItemClickListener onLessonItemClickListener;
    private OnCircleItemClickListener onCircleItemClickListener;
    private OnRecruitItemClickListener onRecruitItemClickListener;
    private OnSeekItemClickListener onSeekItemClickListener;

    public SearchOtherAdapter(OnLessonItemClickListener lessontemClickListener,
                              OnCircleItemClickListener circleItemClickListener,
                              OnRecruitItemClickListener recruitItemClickListener,
                              OnSeekItemClickListener seekItemClickListener) {
        this.onLessonItemClickListener = lessontemClickListener;
        this.onCircleItemClickListener = circleItemClickListener;
        this.onRecruitItemClickListener = recruitItemClickListener;
        this.onSeekItemClickListener = seekItemClickListener;
        data = new ArrayList<>();
    }

    public void updateData(List<PostsArticleBaseBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder<PostsArticleBaseBean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(viewType, null, false);
        switch (viewType) {
            case R.layout.serach_fragment_recycler_friend_item:
                viewHolder = new SearchLessonViewHolder(itemView, onLessonItemClickListener);
                break;
            case R.layout.serach_fragment_recycler_conversation_item:
                viewHolder = new SearchLessonViewHolder(itemView, onLessonItemClickListener);
                break;
            case R.layout.serach_fragment_recycler_group_item:
                viewHolder = new SearchLessonViewHolder(itemView, onLessonItemClickListener);
                break;
            case R.layout.search_frament_show_more_item:
                viewHolder = new SearchLessonViewHolder(itemView, onLessonItemClickListener);
                break;
            case R.layout.search_fragment_recycler_div_layout:
                viewHolder = new SearchDivViewHolder(itemView);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<PostsArticleBaseBean> holder, int position) {
        holder.update(data.get(position));
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
