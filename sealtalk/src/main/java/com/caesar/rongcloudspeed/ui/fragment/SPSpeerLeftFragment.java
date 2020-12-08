package com.caesar.rongcloudspeed.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.LessonAdvBean;
import com.caesar.rongcloudspeed.common.LogTag;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.model.ChatRoomAction;
import com.caesar.rongcloudspeed.model.ChatRoomResult;
import com.caesar.rongcloudspeed.ui.activity.AddFriendActivity;
import com.caesar.rongcloudspeed.ui.activity.LessonAdvertDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.ScanActivity;
import com.caesar.rongcloudspeed.ui.activity.SealSearchActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectCreateGroupActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectSingleFriendActivity;
import com.caesar.rongcloudspeed.ui.activity.WebActivity;
import com.caesar.rongcloudspeed.util.ImageLoader;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.utils.log.SLog;
import com.caesar.rongcloudspeed.viewmodel.AppViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 主界面子界面-发现界面
 */
public class SPSpeerLeftFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SPSpeerLeftFragment";
    @BindView(R.id.lessonTitle)
    TextView lessonTitle;
    @BindView(R.id.lessonMoney)
    TextView lessonMoney;
    @BindView(R.id.expand_text_view)
    ExpandableTextView expand_text_view;
    @BindView(R.id.lessonTeacher)
    TextView lessonTeacher;
    @BindView(R.id.avTitleText)
    TextView avTitleText;
    @BindView(R.id.avTagText)
    TextView avTagText;
    @BindView(R.id.avImageView)
    ImageView avImageView;
    @BindView(R.id.advLessonLayout)
    RelativeLayout advLessonLayout;
    private String lesson_id;
    private String lesson_name;
    private String lesson_price;
    private String lesson_content;
    private String lesson_source;
    private String advertLessonstring;

    @Override
    protected int getLayoutResId() {
        return R.layout.user_fragment_speer_shop_list;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        advertLessonstring = UserInfoUtils.getAdvertLessonList(getActivity());
        lesson_id = intent.getExtras().getString("lesson_id");
        lesson_name = intent.getExtras().getString("lesson_name");
        lesson_price = intent.getExtras().getString("lesson_price");
        lesson_content = intent.getExtras().getString("lesson_content");
        lesson_source = intent.getExtras().getString("lesson_source");
        Gson gson = new Gson();
        lessonTitle.setText(lesson_name);
        if (lesson_price.startsWith("0.0")) {
            lessonMoney.setText("课程免费");
        } else {
            lessonMoney.setText("￥" + lesson_price + "元");
        }
        lessonTeacher.setText("讲师简介：" + lesson_source);
        expand_text_view.setText("课程简介：\n" + lesson_content);
        List<LessonAdvBean> lessonAdvBeanList = gson.fromJson(advertLessonstring, new TypeToken<List<LessonAdvBean>>() {
        }.getType());
        int number = (int) (Math.random() * lessonAdvBeanList.size());
        LessonAdvBean lessonAdvBean = lessonAdvBeanList.get(number);
        avTitleText.setText(lessonAdvBean.getPost_title());
        avTagText.setText("广告\u3000" + lessonAdvBean.getPost_source());
        String thumbString = lessonAdvBean.getThumb();
        if (!thumbString.startsWith("http")) {
            thumbString = Constant.THINKCMF_PATH + thumbString;
        }
        Glide.with(this).load(thumbString).into(avImageView);
        String mimeTypeString = lessonAdvBean.getPost_mime_type();
        String finalThumbString = thumbString;
        advLessonLayout.setOnClickListener(view -> {
            if (mimeTypeString.equals("0")) {
                Intent advIntent = new Intent(getActivity(), LessonAdvertDetailActivity.class);
                advIntent.putExtra("post_title", lessonAdvBean.getPost_title());
                advIntent.putExtra("post_author", lessonAdvBean.getPost_source());
                advIntent.putExtra("post_image", finalThumbString);
                advIntent.putExtra("post_expert", lessonAdvBean.getPost_excerpt());
                startActivity(advIntent);
            } else if (mimeTypeString.equals("1")) {
                Intent webIntent = new Intent(getActivity(), WebActivity.class);
                webIntent.putExtra("title", lessonAdvBean.getPost_title());
                webIntent.putExtra("webString", lessonAdvBean.getPost_excerpt());
                startActivity(webIntent);
            }
        });

    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {

    }
}
