package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.MoreLessonesAdapter;
import com.caesar.rongcloudspeed.adapter.MoreLessonesIntroduceAdapter;
import com.caesar.rongcloudspeed.constants.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 主界面子界面-发现界面
 */
public class SPSpeerRightFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SPSpeerRightFragment";
    private MoreLessonesIntroduceAdapter moreLessonesIntroduceAdapter;
    @BindView(R.id.lessones_list_recyclerView)
    RecyclerView lessones_list_recyclerView;
    private String lesson_id;
    private String lesson_smeta;
    private String lesson_name;
    private String lesson_source;
    private JSONArray videoArray = null;
    private List<String> altList = new ArrayList<String>();

    @Override
    protected int getLayoutResId() {
        return R.layout.user_fragment_speer_lessones_list;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        lesson_id = intent.getExtras().getString( "lesson_id" );
        lesson_name = intent.getExtras().getString( "lesson_name" );
        lesson_smeta = intent.getExtras().getString("lesson_smeta");
        lesson_source = intent.getExtras().getString( "lesson_source" );
        try {
            JSONObject jsonSmeta = new JSONObject(lesson_smeta);
            JSONArray photoArray = jsonSmeta.getJSONArray("photo");
            if (photoArray != null && photoArray.length() > 0) {
                videoArray = photoArray;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (videoArray != null && videoArray.length() > 0) {
            for (int i = 0; i < videoArray.length(); i++) {
                int finalI = i;
                try {
                    JSONObject photoObj = videoArray.getJSONObject(finalI);
                    String altString = photoObj.getString("alt");
                    altList.add(lesson_name+"("+altString+")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            altList.add(lesson_name);
        }
        moreLessonesIntroduceAdapter = new MoreLessonesIntroduceAdapter(getActivity(), altList,lesson_source);
        lessones_list_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        lessones_list_recyclerView.setAdapter(moreLessonesIntroduceAdapter);
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {

    }
}
