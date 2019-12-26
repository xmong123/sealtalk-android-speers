package com.caesar.rongcloudspeed.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.ui.adapter.models.ContactModel;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.BaseViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.CheckableContactViewHolder;
import com.caesar.rongcloudspeed.ui.adapter.viewholders.TitleViewHolder;
import com.caesar.rongcloudspeed.ui.interfaces.OnCheckContactClickListener;

public class SelectFriendAdapter extends RecyclerView.Adapter<BaseViewHolder<ContactModel>> {
    private List<ContactModel> data;
    private OnCheckContactClickListener checkableItemClickListener;

    public SelectFriendAdapter(OnCheckContactClickListener onContactItemClickListener) {
        this.data = new ArrayList<ContactModel>();
        this.checkableItemClickListener = onContactItemClickListener;
    }

    public void setData(List<ContactModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder<ContactModel> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(viewType, null, false);
        switch (viewType) {
            case R.layout.select_fragment_friend_item:
                viewHolder = new CheckableContactViewHolder(itemView, checkableItemClickListener);
                break;
            case R.layout.contact_friend_title:
                viewHolder = new TitleViewHolder(itemView);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ContactModel> holder, int position) {
        holder.update(data.get(position));
    }


    @Override
    public int getItemViewType(int position) {
        return data != null ? data.get(position).getType() : 0;
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}
