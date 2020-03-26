package com.caesar.rongcloudspeed.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.db.model.FriendShipInfo;
import com.caesar.rongcloudspeed.model.Resource;
import com.caesar.rongcloudspeed.model.Status;
import com.caesar.rongcloudspeed.model.VersionInfo;
import com.caesar.rongcloudspeed.ui.view.NPMTopTabGroupView;
import com.caesar.rongcloudspeed.ui.view.NPMTopTabItem;
import com.caesar.rongcloudspeed.ui.widget.DragPointView;
import com.caesar.rongcloudspeed.ui.widget.TabGroupView;
import com.caesar.rongcloudspeed.ui.widget.TabItem;
import com.caesar.rongcloudspeed.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

import static com.caesar.rongcloudspeed.utils.ToastUtils.showToast;

public class MainNpmTalkFragment extends Fragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewPager npmViewPager;
    private NPMTopTabGroupView tabGroupView;
    public MainViewModel mainViewModel;
    /**
     * 各个 Fragment 界面
     */
    private List<Fragment> fragments = new ArrayList<>();

    /**
     * tab 项枚举
     */
    public enum NPM {
        /**
         * 消息
         */
        MESSAGE(0),
        /**
         * 通讯录
         */
        CONTACT(1),
        /**
         * 加好友
         */
        ADDFRIEND(2);

        private int value;

        NPM(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static NPM getType(int value) {
            for (NPM npm : NPM.values()) {
                if (value == npm.getValue()) {
                    return npm;
                }
            }
            return null;
        }
    }

    public static MainNpmTalkFragment newInstance(String param1, String param2) {
        MainNpmTalkFragment fragment = new MainNpmTalkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_npm_sealtalk, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewModel();
    }

    private void initView() {
        tabGroupView = getActivity().findViewById(R.id.npm_top_tabs);
        npmViewPager = getActivity().findViewById(R.id.npmViewPager);
        // 初始化底部 tabs
        initTabs();
        // 初始化 fragment 的 viewpager
        initFragmentViewPager();
        tabGroupView.setSelected(0);

    }

    /**
     * 初始化 Tabs
     */
    private void initTabs() {
        // 初始化 tab
        List<TabItem> items = new ArrayList<>();
        String[] stringArray = new String[]{"消息","通讯录","加好友"};
//        String[] stringArray = getResources().getStringArray(R.array.tab_names);
        for (NPM npm : NPM.values()) {
            TabItem tabItem = new TabItem();
            tabItem.id = npm.getValue();
            tabItem.text = stringArray[npm.getValue()];
            items.add(tabItem);
        }

        tabGroupView.initView(items, new TabGroupView.OnTabSelectedListener() {
            @Override
            public void onSelected(View view, TabItem item) {
                // 当点击 tab 的后， 也要切换到正确的 fragment 页面
                int currentItem = npmViewPager.getCurrentItem();
                if (currentItem != item.id) {
                    // 切换布局
                    npmViewPager.setCurrentItem(item.id);
                    // 如果是我的页面， 则隐藏红点
                    if (item.id == NPM.ADDFRIEND.getValue()) {
                        ((NPMTopTabItem) tabGroupView.getView(NPM.ADDFRIEND.getValue())).setRedVisibility(View.GONE);
                    }
                }
            }
        });

        tabGroupView.setOnTabDoubleClickListener(new NPMTopTabGroupView.OnTabDoubleClickListener() {
            @Override
            public void onDoubleClick(TabItem item, View view) {
                // 双击定位到某一个未读消息位置
                if (item.id == NPM.MESSAGE.getValue()) {
                    MainConversationListFragment fragment = (MainConversationListFragment) fragments.get(NPM.MESSAGE.getValue());
                    fragment.focusUnreadItem();
                }
            }
        });

        // 未读数拖拽
        ((NPMTopTabItem) tabGroupView.getView(NPM.MESSAGE.getValue())).setTabUnReadNumDragListener(new DragPointView.OnDragListencer() {

            @Override
            public void onDragOut() {
                ((NPMTopTabItem) tabGroupView.getView(NPM.MESSAGE.getValue())).setNumVisibility(View.GONE);
                showToast(getString(R.string.seal_main_toast_unread_clear_success));
                clearUnreadStatus();
            }
        });
        ((NPMTopTabItem) tabGroupView.getView(NPM.MESSAGE.getValue())).setNumVisibility(View.VISIBLE);
    }

    /**
     * 初始化 initFragmentViewPager
     * fragments.add(new BookStoreHomeFragment());
     */
    private void initFragmentViewPager() {
        fragments.add(new MainConversationListFragment());
        fragments.add(new MainContactsListFragment());
        fragments.add(new MainDiscoveryFragment());

        // ViewPager 的 Adpater
        FragmentStatePagerAdapter fragmentPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        npmViewPager.setAdapter(fragmentPagerAdapter);
        npmViewPager.setOffscreenPageLimit(fragments.size());
        // 设置页面切换监听
        npmViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 当页面切换完成之后， 同时也要把 tab 设置到正确的位置
                tabGroupView.setSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化ViewModel
     */
    private void initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // 未读消息
        mainViewModel.getUnReadNum().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                NPMTopTabItem chatTab = (NPMTopTabItem) tabGroupView.getView(NPM.MESSAGE.getValue());
                if (count == 0) {
                    chatTab.setNumVisibility(View.GONE);
                } else if (count > 0 && count < 100) {
                    chatTab.setNumVisibility(View.VISIBLE);
                    chatTab.setNum(String.valueOf(count));
                } else {
                    chatTab.setVisibility(View.VISIBLE);
                    chatTab.setNum(getString(R.string.seal_main_chat_tab_more_read_message));
                }
            }
        });

        // 同行快聊
        mainViewModel.getNewFriendNum().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                NPMTopTabItem chatTab = tabGroupView.getView(NPM.CONTACT.getValue());
                if (count > 0) {
                    chatTab.setRedVisibility(View.VISIBLE);
                } else {
                    chatTab.setRedVisibility(View.GONE);
                }
            }
        });

        mainViewModel.getPrivateChatLiveData().observe(this, new Observer<FriendShipInfo>() {
            @Override
            public void onChanged(FriendShipInfo friendShipInfo) {
                RongIM.getInstance().startPrivateChat(getActivity(),
                        friendShipInfo.getUser().getId(),
                        TextUtils.isEmpty(friendShipInfo.getDisplayName()) ?
                                friendShipInfo.getUser().getNickname() : friendShipInfo.getDisplayName());
            }
        });

    }


    /**
     * 清理未读消息状态
     */
    private void clearUnreadStatus() {
        if (mainViewModel != null) {
            mainViewModel.clearMessageUnreadStatus();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
