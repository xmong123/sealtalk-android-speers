package com.caesar.rongcloudspeed.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caesar.rongcloudspeed.R;

import java.util.List;

public class HorizontalScrollViewAdapter {

    private Context mContext;
    private LayoutInflater mInflate;
    private List<Integer> mDatas;

    public HorizontalScrollViewAdapter(Context context, List<Integer> mDatas) {
        this.mContext = context;
        mInflate = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int positon) {
        return mDatas.get(positon);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflate.inflate(R.layout.activity_index_gallery_item, parent, false);
            viewHolder.mText = (TextView) convertView.findViewById(R.id.id_index_gallery_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mText.setText("some info ");

        return convertView;
    }

    private class ViewHolder
    {
        TextView mText;
    }
}


