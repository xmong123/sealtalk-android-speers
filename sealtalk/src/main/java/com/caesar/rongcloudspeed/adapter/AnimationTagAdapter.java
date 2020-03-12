package com.caesar.rongcloudspeed.adapter;

import android.content.Context;

import com.allen.library.SuperTextView;
import com.caesar.rongcloudspeed.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class AnimationTagAdapter extends BaseQuickAdapter<HashMap<String, Object>, BaseViewHolder> {
    private Context context;
    public AnimationTagAdapter(List data) {
        super(R.layout.fragment_tag_recyclerview_item, data);
    }

    public AnimationTagAdapter(Context context, List data) {
        super( R.layout.fragment_tag_recyclerview_item , data );
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HashMap<String, Object> tagMap) {
        SuperTextView superTextView = helper.getView(R.id.tag_super_text);
        String titleString = (String) tagMap.get("title");
        String tagString = (String) tagMap.get("tag");
        superTextView.setLeftString(titleString);
        superTextView.setRightString(tagString);
        helper.addOnClickListener(R.id.tag_super_text);
    }
}
