package com.caesar.rongcloudspeed.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ActivityUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationAdapter1;
import com.caesar.rongcloudspeed.adapter.AnimationPayAdapter;
import com.caesar.rongcloudspeed.bean.PersonalCountData;
import com.caesar.rongcloudspeed.bean.PersonalPayListData;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.common.BaseShopActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.ui.fragment.PersonalSeekHelperFragment;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.caesar.rongcloudspeed.view.TranspanentTitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;
import static com.google.android.material.tabs.TabLayout.MODE_FIXED;


public class PersonalWalletActivity extends BaseShopActivity implements View.OnClickListener {

    @BindView(R.id.titleBar)
    TranspanentTitleBar titleBar;
    @BindView(R.id.walletRecyclerview)
    RecyclerView walletRecyclerview;
    @BindView(R.id.user_wallet_text2)
    TextView user_wallet_text2;
    private String uidString;
    private AnimationPayAdapter animationPayAdapter;
    private List<PersonalPayListData.PayItem> payItemList;
    private float payCount = 0.00f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_wallet);
        ButterKnife.bind(this);
        uidString = UserInfoUtils.getAppUserId(this);
        titleBar.getBack().setOnClickListener(this);
        animationPayAdapter = new AnimationPayAdapter(payItemList);
        walletRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        walletRecyclerview.setAdapter(animationPayAdapter);
        RetrofitManager.create().get_user_paylist(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<PersonalPayListData>bindToLifecycle())
                .subscribe(new CommonObserver<PersonalPayListData>() {
                    @Override
                    public void onSuccess(PersonalPayListData personalPayListData) {
                        if (personalPayListData.getCode() == CODE_SUCC) {
                            payItemList = personalPayListData.getReferer();
                            if (payItemList.size() > 0) {
                                animationPayAdapter.setNewData(payItemList);
                                for (PersonalPayListData.PayItem item : payItemList) {
                                    float itemCount = Float.valueOf(item.getPayamount());
                                    payCount += itemCount;
                                }
                                user_wallet_text2.setText("¥" + payCount);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
