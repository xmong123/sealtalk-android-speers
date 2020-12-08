package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.bean.PostsLessonBean;
import com.caesar.rongcloudspeed.bean.SectionLessonBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.rong.callkit.util.GlideRoundTransform;

/**
 * 文 件 名: AnimationAdapter
 * 创 建 人: Allen
 * 创建日期: 16/12/24 15:33
 * 邮   箱: AllenCoder@126.com
 * 修改时间：
 * 修改备注：
 */
public class LessonSectionAdapter extends BaseQuickAdapter<SectionLessonBaseBean.SectionLessonBean, BaseViewHolder> {
    private Context context;

    public LessonSectionAdapter(Context context, List data) {
        super(R.layout.item_section_lesson_view, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionLessonBaseBean.SectionLessonBean sectionLessonBean) {
        List<PostsLessonBean> lessonBeanList = sectionLessonBean.getPosts();
        helper.setText(R.id.section_term_name, sectionLessonBean.getName());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.rc_image_error)
                .error(R.drawable.rc_image_error)
                .transform(new GlideRoundTransform());
        helper.addOnClickListener(R.id.section_more_btn);
        if (lessonBeanList.size() == 4) {
            helper.setVisible(R.id.section_lesson_layout1, true);
            helper.setVisible(R.id.section_lesson_layout2, true);
            helper.setVisible(R.id.section_lesson_layout3, true);
        } else {
            if (lessonBeanList.size() == 3) {
                helper.setVisible(R.id.section_lesson_layout1, true);
                helper.setVisible(R.id.section_lesson_layout2, true);
                helper.setVisible(R.id.section_lesson_layout3, false);
            } else {
                if (lessonBeanList.size() == 2) {
                    helper.setVisible(R.id.section_lesson_layout1, true);
                    helper.setGone(R.id.section_lesson_layout2, false);
                    helper.setGone(R.id.section_lesson_layout3, false);
                } else if (lessonBeanList.size() == 1) {
                    helper.setVisible(R.id.section_lesson_layout1, false);
                    helper.setGone(R.id.section_lesson_layout2, false);
                    helper.setGone(R.id.section_lesson_layout3, false);
                }
            }
        }
        for (int i = 0; i < lessonBeanList.size(); i++) {
            PostsLessonBean postsLessonBean = lessonBeanList.get(i);
            String thumbString = null;
            try {
                JSONObject jsonSmeta = new JSONObject(postsLessonBean.getSmeta());
                thumbString = jsonSmeta.getString("thumb");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (thumbString != null && !thumbString.startsWith("http")) {
                thumbString = Constant.THINKCMF_PATH + thumbString;
            }
            if (i == 0) {
                helper.setText(R.id.section_lesson_title, postsLessonBean.getPost_title());
                if (thumbString != null && thumbString.length() > 32) {
                    Glide.with(context).load(thumbString).apply(options).into((ImageView) helper.getView(R.id.section_lesson_image));
                } else {
                    helper.setImageResource(R.id.section_lesson_image, R.drawable.votebasecon);
                }
                helper.addOnClickListener(R.id.section_lesson_layout);
            } else if (i == 1) {
                helper.setText(R.id.section_lesson_title1, postsLessonBean.getPost_title());
                if (thumbString != null && thumbString.length() > 32) {
                    Glide.with(context).load(thumbString).apply(options).into((ImageView) helper.getView(R.id.section_lesson_image1));
                } else {
                    helper.setImageResource(R.id.section_lesson_image1, R.drawable.votebasecon);
                }
                helper.addOnClickListener(R.id.section_lesson_layout1);
            } else if (i == 2) {
                helper.setText(R.id.section_lesson_title2, postsLessonBean.getPost_title());
                if (thumbString != null && thumbString.length() > 32) {
                    Glide.with(context).load(thumbString).apply(options).into((ImageView) helper.getView(R.id.section_lesson_image2));
                } else {
                    helper.setImageResource(R.id.section_lesson_image2, R.drawable.votebasecon);
                }
                helper.addOnClickListener(R.id.section_lesson_layout2);
            } else {
                helper.setText(R.id.section_lesson_title3, postsLessonBean.getPost_title());
                if (thumbString != null && thumbString.length() > 32) {
                    Glide.with(context).load(thumbString).apply(options).into((ImageView) helper.getView(R.id.section_lesson_image3));
                } else {
                    helper.setImageResource(R.id.section_lesson_image3, R.drawable.votebasecon);
                }
                helper.addOnClickListener(R.id.section_lesson_layout3);
            }
        }
    }
}
