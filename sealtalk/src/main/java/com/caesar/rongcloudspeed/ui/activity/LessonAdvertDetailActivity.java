package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.ArticleCommentsAdapter;
import com.caesar.rongcloudspeed.bean.GoodInfoDetail;
import com.caesar.rongcloudspeed.circle.adapter.CommentAdapter;
import com.caesar.rongcloudspeed.circle.ui.FriendTimeLineActivity;
import com.caesar.rongcloudspeed.circle.ui.ImagePagerActivity;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.data.BaseData;
import com.caesar.rongcloudspeed.data.CommentsItemData;
import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.CommentsListData;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.easypop.CmmtPopup;
import com.caesar.rongcloudspeed.holders.LocalImageHolderView;
import com.caesar.rongcloudspeed.im.IMManager;
import com.caesar.rongcloudspeed.model.AddFriendResult;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.dialog.SimpleInputDialog;
import com.caesar.rongcloudspeed.util.ImageLoader;
import com.caesar.rongcloudspeed.util.ToastUtils;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.QiniuUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.viewmodel.SearchFriendNetViewModel;
import com.caesar.rongcloudspeed.viewmodel.UserDetailViewModel;
import com.just.agentweb.AgentWeb;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.contactcard.message.ContactSeekInfo;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import me.leefeng.promptlibrary.PromptDialog;

public class LessonAdvertDetailActivity extends MultiStatusActivity {

    @BindView(R.id.lesson_adv_image)
    ImageView lesson_adv_image;
    @BindView(R.id.lesson_adv_title)
    TextView lesson_adv_title;
    @BindView(R.id.lesson_adv_author)
    TextView lesson_adv_author;
    @BindView(R.id.lesson_adv_expert)
    TextView lesson_adv_expert;
    private String uidString;
    private String post_title;
    private String post_author;
    private String post_image;
    private String post_expert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        post_title = getIntent().getStringExtra("post_title");
        post_author = getIntent().getStringExtra("post_author");
        post_image = getIntent().getStringExtra("post_image");
        post_expert = getIntent().getStringExtra("post_expert");
        ImageLoaderUtils.display(this,lesson_adv_image,post_image);
        lesson_adv_title.setText(post_title);
        lesson_adv_author.setText(post_author);
        lesson_adv_expert.setText("\u3000\u3000\u3000"+post_expert);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_lesson_advert_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
