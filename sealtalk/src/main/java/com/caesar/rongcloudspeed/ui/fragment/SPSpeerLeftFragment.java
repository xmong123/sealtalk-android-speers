package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.common.LogTag;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.model.ChatRoomAction;
import com.caesar.rongcloudspeed.model.ChatRoomResult;
import com.caesar.rongcloudspeed.ui.activity.AddFriendActivity;
import com.caesar.rongcloudspeed.ui.activity.ScanActivity;
import com.caesar.rongcloudspeed.ui.activity.SealSearchActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectCreateGroupActivity;
import com.caesar.rongcloudspeed.ui.activity.SelectSingleFriendActivity;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.log.SLog;
import com.caesar.rongcloudspeed.viewmodel.AppViewModel;

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
    @BindView(R.id.lessonContent)
    TextView lessonContent;
    @BindView(R.id.lessonTeacher)
    TextView lessonTeacher;
    private String lesson_id;
    private String lesson_name;
    private String lesson_price;
    private String lesson_content;
    private String lesson_source;

    @Override
    protected int getLayoutResId() {
        return R.layout.user_fragment_speer_shop_list;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        lesson_id = intent.getExtras().getString( "lesson_id" );
        lesson_name = intent.getExtras().getString( "lesson_name" );
        lesson_price = intent.getExtras().getString( "lesson_price" );
        lesson_content = intent.getExtras().getString( "lesson_content" );
        lesson_source = intent.getExtras().getString( "lesson_source" );
        lessonTitle.setText(lesson_name);
        if(lesson_price.startsWith( "0.0" )){
            lessonMoney.setText("课程免费");
        }else{
            lessonMoney.setText("￥"+lesson_price+"元");
        }
        lessonContent.setText("课程简介：\n"+lesson_content);
        lessonTeacher.setText("讲师简介：\n"+lesson_source);
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {

    }
}
