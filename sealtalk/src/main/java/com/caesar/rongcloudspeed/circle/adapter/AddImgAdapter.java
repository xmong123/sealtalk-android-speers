package com.caesar.rongcloudspeed.circle.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.data.HolderImg;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.caesar.rongcloudspeed.callback.OnItemClickListener;
import com.caesar.rongcloudspeed.util.ImageLoader;

import java.util.List;

/**
 * Created by mathum on 2017/9/2.
 */

public class AddImgAdapter extends RecyclerView.Adapter<AddImgAdapter.ImgHolder> {

    private List<HolderImg> list;
    public int[] resIds = {R.drawable.img_add_img};
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AddImgAdapter(List<HolderImg> list) {
        this.list = list;
    }

    @Override
    public ImgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_img, parent, false);
        return new ImgHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImgHolder holder, int position) {
        HolderImg holderImg = list.get(position);

        if (TextUtils.isEmpty(holderImg.getUrl())) {
            GenericDraweeHierarchy hierarchy = holder.holderImg.getHierarchy();
            int resId = resIds[0];
            hierarchy.setPlaceholderImage(resId);
        }


        if (TextUtils.isEmpty(holderImg.getUrl())) {
            ImageLoader.setImageWithOutCorner(holder.holderImg, 100, 100, holderImg.getUrl());
        } else {
            ImageLoader.setImage(holder.holderImg, 100, 100, holderImg.getUrl());
        }

        holder.holderImg.setOnClickListener((view) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(holder.getAdapterPosition(), view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImgHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView holderImg;

        public ImgHolder(View itemView) {
            super(itemView);
            holderImg = itemView.findViewById(R.id.holder_img);
        }
    }
}
