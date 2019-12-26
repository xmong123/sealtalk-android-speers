package com.caesar.rongcloudspeed.circle.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.callback.OnItemClickListener;
import com.caesar.rongcloudspeed.util.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class MyAlbumAdapter extends RecyclerView.Adapter<MyAlbumAdapter.Holder> {

    private List<String> photos;
    private List<String> selectedPhotos;
    private OnItemClickListener itemClickListener;
    private int limitNum;

    public MyAlbumAdapter(List<String> photos, int limitNum) {
        this.photos = photos;
        selectedPhotos = new ArrayList<>();
        this.limitNum = limitNum;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(holder.getAdapterPosition(), view);
                }
            }
        });
        holder.ivPhoto.setVisibility(View.VISIBLE);
        holder.ivPhoto1.setVisibility(View.GONE);
        if (TextUtils.isEmpty(photos.get(position))) {
            holder.ivChosed.setVisibility(View.GONE);
            holder.ivPhoto.setVisibility(View.GONE);
            holder.ivPhoto1.setVisibility(View.VISIBLE);
            holder.ivPhoto1.setImageResource(R.drawable.img_chat_camera);
            return;
        }
        if (holder.ivChosed.getVisibility() == View.GONE) {
            holder.ivChosed.setVisibility(View.VISIBLE);
        }
        Uri uri = Uri.fromFile(new File(photos.get(position)));
        ImageLoader.setImage(holder.ivPhoto, 110, 110, uri);

        holder.ivChosed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (selectedPhotos.size() >= limitNum && isChecked) {
                    holder.ivChosed.setChecked(false);
                    iSelectPhoto.overNum();
                    return;
                } else if (selectedPhotos.size() >= limitNum && !isChecked) {
                    selectedPhotos.remove(photos.get(position));
                    iSelectPhoto.numTips(selectedPhotos);
                } else if (selectedPhotos.size() < limitNum && isChecked) {
                    selectedPhotos.add(photos.get(position));
                    iSelectPhoto.numTips(selectedPhotos);
                } else {
                    selectedPhotos.remove(photos.get(position));
                    iSelectPhoto.numTips(selectedPhotos);
                }
            }
        });
    }

    ISelectPhoto iSelectPhoto;

    public void setISelectPhoto(ISelectPhoto iSelectPhoto) {
        this.iSelectPhoto = iSelectPhoto;
    }

    public interface ISelectPhoto {
        void numTips(List<String> list);

        void overNum();
    }

    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        SimpleDraweeView ivPhoto;
        CheckBox ivChosed;
        ImageView ivPhoto1;

        public Holder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            ivChosed = itemView.findViewById(R.id.iv_chosed);
            ivPhoto1 = itemView.findViewById(R.id.iv_photo1);
        }
    }

}
