package com.caesar.rongcloudspeed.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;


/**
 * RecyclerView样式Dialog中Viewholder的基类
 * <p>
 * Created by 43053 on 2017/1/6.
 */

public class BaseDialogViewHolder extends RecyclerView.ViewHolder {

    public TextView itemNameTv;
    public BaseDialogViewHolder(View itemView) {
        super(itemView);
        itemNameTv = (TextView) itemView.findViewById(R.id.text);
    }
}
