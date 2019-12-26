package com.caesar.rongcloudspeed.ui.adapter.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.SealApp;
import com.caesar.rongcloudspeed.ui.interfaces.OnMessageRecordClickListener;
import com.caesar.rongcloudspeed.utils.CharacterParser;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.viewmodel.SearchMessageModel;
import io.rong.imkit.utils.RongDateUtils;

public class SearchMessageViewHolder extends BaseViewHolder<SearchMessageModel> {
    private ImageView ivPortrait;
    private TextView tvChatName;
    private TextView tvContent;
    private TextView tvDate;
    private OnMessageRecordClickListener listener;
    private SearchMessageModel model;

    public SearchMessageViewHolder(@NonNull View itemView, OnMessageRecordClickListener l) {
        super(itemView);
        ivPortrait = itemView.findViewById(R.id.item_iv_record_image);
        tvChatName = itemView.findViewById(R.id.item_tv_chat_name);
        tvContent = itemView.findViewById(R.id.item_tv_chatting_records_detail);
        tvDate = itemView.findViewById(R.id.item_tv_chatting_records_date);
        listener = l;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMessageRecordClick(model);
                }
            }
        });
    }

    @Override
    public void update(SearchMessageModel searchMessageModel) {
        model = searchMessageModel;
        tvChatName.setText(searchMessageModel.getName());
        tvContent.setText(CharacterParser.getColoredChattingRecord(searchMessageModel.getSearch(), searchMessageModel.getBean().getContent(), tvContent.getContext()));
        String sendTime = RongDateUtils.getConversationFormatDate(searchMessageModel.getBean().getSentTime(), SealApp.getApplication());
        tvDate.setText(sendTime);
        ImageLoaderUtils.displayUserPortraitImage(searchMessageModel.getPortiaitUrl(),ivPortrait);
    }

}
