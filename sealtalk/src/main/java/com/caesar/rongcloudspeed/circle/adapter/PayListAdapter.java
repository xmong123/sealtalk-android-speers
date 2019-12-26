package com.caesar.rongcloudspeed.circle.adapter;

/*
 Suneee Android Client, NoticeItemAdapter
 Copyright (c) 2014 Suneee Tech Company Limited
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.data.PayDataItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiw
 * @ClassName: CommentAdapter
 * @Description: 评论的adapter
 * @date 2015-12-28 下午3:40:29
 */
public class PayListAdapter extends BaseAdapter {

    private List<PayDataItem> datasource = new ArrayList<PayDataItem>();
    private Context mContext;

    public PayListAdapter(Context context) {
        mContext = context;
    }

    /**
     * @param context
     */
    public PayListAdapter(Activity context, List<PayDataItem> datasource) {
        this.datasource = datasource;
    }

    public void setDatasource(List<PayDataItem> datasource) {
        if (datasource != null) {
            this.datasource = datasource;
        }
    }

    class ViewHolder {
        TextView payItemTv;
        TextView payItemTv1;
        TextView payItemTv2;
        TextView payItemTv3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.user_pay_item, null);
            holder.payItemTv = (TextView) convertView.findViewById(R.id.payItemTv);
            holder.payItemTv1 = (TextView) convertView.findViewById(R.id.payItemTv1);
            holder.payItemTv2 = (TextView) convertView.findViewById(R.id.payItemTv2);
            holder.payItemTv3 = (TextView) convertView.findViewById(R.id.payItemTv3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PayDataItem bean = datasource.get(position);
        String payforname = bean.getPayforname();
        String paytoname = bean.getPaytoname();
        String payamount = bean.getPayamount();
        String paydatatime = bean.getPaydatatime();
        holder.payItemTv.setText("收款方："+payforname);
        holder.payItemTv1.setText("付款方："+paytoname);
        holder.payItemTv2.setText("交易金额："+payamount);
        holder.payItemTv3.setText(paydatatime);
        return convertView;
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int arg0) {
        return datasource.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }



}
