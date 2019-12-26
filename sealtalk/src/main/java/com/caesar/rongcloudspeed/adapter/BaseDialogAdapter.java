package com.caesar.rongcloudspeed.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.callback.OnItemClickListener;
import com.caesar.rongcloudspeed.data.BaseDialogData;
import com.caesar.rongcloudspeed.viewholder.BaseDialogViewHolder;

import java.util.List;

/**
 * Created by 43053 on 2017/1/6.
 */

public class BaseDialogAdapter<D extends BaseDialogData> extends RecyclerView.Adapter<BaseDialogViewHolder> {

    List<D> dialogList;
    private OnItemClickListener onItemClickListener;
    int resId;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BaseDialogAdapter(List<D> dialogList, int resId) {
        this.dialogList = dialogList;
        this.resId = resId;
    }

    @Override
    public BaseDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
        return new BaseDialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseDialogViewHolder holder, int position) {

        D d = dialogList.get(position);

        holder.itemNameTv.setText(d.getItemName());
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener!= null){
                onItemClickListener.onItemClick(holder.getAdapterPosition(),view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dialogList == null ? 0 : dialogList.size();
    }
}
