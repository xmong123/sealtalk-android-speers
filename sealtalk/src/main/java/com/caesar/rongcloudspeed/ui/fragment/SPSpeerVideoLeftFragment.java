package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.caesar.rongcloudspeed.R;
import com.flyco.tablayout.SlidingTabLayout;

import butterknife.BindView;

/**
 * 主界面子界面-发现界面
 */
public class SPSpeerVideoLeftFragment extends BaseFragment {
    private static final String TAG = "SPSpeerVideoLeftFragment";
    @BindView(R.id.lesson_video_recyclerView)
    RecyclerView lessonVideoRecyclerView;
    private String lesson_id;
    private String thumbVideoString;
    private String lesson_name;
    private String lesson_price;
    private View lessonHeadView;
    private View bookHeadView;
    private SlidingTabLayout leftVideoTL1;
    private final String[] mTitles = {
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.user_fragment_speer_video_list;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        lesson_id = intent.getExtras().getString( "lesson_id" );
        lesson_name = intent.getExtras().getString( "lesson_name" );
        lesson_price = intent.getExtras().getString( "lesson_price" );
        lessonHeadView = getLayoutInflater().inflate(R.layout.left_lesson_video_header, (ViewGroup) lessonVideoRecyclerView.getParent(), false);
        bookHeadView = getLayoutInflater().inflate(R.layout.left_lesson_book_header, (ViewGroup) lessonVideoRecyclerView.getParent(), false);
        leftVideoTL1 = lessonHeadView.findViewById(R.id.left_video_tl_1);
        ViewPager viewPager=new ViewPager(getActivity());
        leftVideoTL1.setViewPager(viewPager, mTitles);
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {

    }
}
