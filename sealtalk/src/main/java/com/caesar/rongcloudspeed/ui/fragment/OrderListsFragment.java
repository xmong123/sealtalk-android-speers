package com.caesar.rongcloudspeed.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.bean.HomeDataOtherBean;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.network.Api;
import com.caesar.rongcloudspeed.ui.activity.TSShopDetailActivity;
import com.caesar.rongcloudspeed.ui.activity.WebActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.classic.common.MultipleStatusView;
import com.facebook.stetho.common.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.ProCityBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.AnimationAdapter3;
import com.caesar.rongcloudspeed.adapter.AnimationComplexAdapter;
import com.caesar.rongcloudspeed.adapter.AnimationTeslaAdapter;
import com.caesar.rongcloudspeed.bean.CategoryBean;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.HomeDataUserBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.easypop.CityPopup;
import com.caesar.rongcloudspeed.easypop.GiftPopup;
import com.caesar.rongcloudspeed.implement.SelectTabCityListener;
import com.caesar.rongcloudspeed.implement.SelectTabItemListener;
import com.caesar.rongcloudspeed.manager.RetrofitManageres;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.rxlife.RxFragment;
import com.caesar.rongcloudspeed.util.KeyBordUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class OrderListsFragment extends RxFragment implements OnLoadmoreListener, OnRefreshListener, View.OnClickListener, SelectTabItemListener, SelectTabCityListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.menu_recyclerview)
    RecyclerView menu_recyclerview;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.appBarLayout_01)
    LinearLayout appBarLayout_01;
    @BindView(R.id.android_searchView)
    SearchView android_searchView;
    @BindView(R.id.search_btn_block)
    TextView search_btn_block;
    @BindView(R.id.search_btn_text)
    TextView search_btn_text;
    Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("allService -->", "onAttach" + rule);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("allService -->", "onStart_rule" + rule);
        LogUtil.d("allService -->", "onStart_type" + type);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("allService -->", "onResume_rule" + rule);
        LogUtil.d("allService -->", "onResume_type" + type);

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("allService -->", "onPause_rule" + rule);
        LogUtil.d("allService -->", "onPause_type" + type);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("allService -->", "onStop_rule" + rule);
        LogUtil.d("allService -->", "onStop_type" + type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("allService -->", "onDestroy_rule" + rule);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("allService -->", "onDetach" + rule);
    }

    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    //    private AnimationAdapter2 animationAdapter2;
    private AnimationAdapter3 animationAdapter3;
    private AnimationTeslaAdapter animationTeslaAdapter;
    private AnimationTeslaAdapter animationTeslaAdaptes;
    private String mParam1;
    private String mParam2;

    private AnimationComplexAdapter mComplexAdapter;
    private List<CategoryBean.ChildrenBean> cateList = new ArrayList<CategoryBean.ChildrenBean>();

    private List<PostsArticleBaseBean> dataArray = new ArrayList<PostsArticleBaseBean>();

    private GiftPopup mGiftPopup;
    private CityPopup mCityPopup;

    private void initGiftPop() {
        mGiftPopup = GiftPopup.create(this, type)
                .setContext(getActivity())
                .apply();
    }

    private void initCityPop() {
        mCityPopup = CityPopup.create(getActivity(), this)
                .setContext(getActivity())
                .apply();
    }

    private void setNewMenuData(String[] newString) {
        cateList.clear();
        for (int i = 0; i < newString.length; i++) {
            CategoryBean.ChildrenBean childrenBean = new CategoryBean.ChildrenBean();
            childrenBean.setName(newString[i]);
            childrenBean.setId(String.valueOf(i));
            cateList.add(childrenBean);
        }
        mComplexAdapter.setNewData(cateList);
        menu_recyclerview.setVisibility(View.VISIBLE);
    }

    @SuppressLint("WrongConstant")
    private void initMenuAdapter(String[] menuString) {

        menu_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        for (int i = 0; i < menuString.length; i++) {
            CategoryBean.ChildrenBean childrenBean = new CategoryBean.ChildrenBean();
            childrenBean.setName(menuString[i]);
            childrenBean.setId(String.valueOf(i));
            cateList.add(childrenBean);
        }
        mComplexAdapter = new AnimationComplexAdapter(cateList);
        menu_recyclerview.setAdapter(mComplexAdapter);
        mComplexAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtils.showShort(String.valueOf(position));
                switch (menuPosition) {
                    case 0:
                        rule = String.valueOf((position == 0) ? position : position + 2);
                        break;
                    case 1:
                        category = String.valueOf(position);
                        break;
                    case 2:
                        region = String.valueOf(position);
                        break;
                    default:
                        break;
                }
                LogUtil.d("allService -->", "onSelectTabItemListener_type" + type);
                LogUtil.d("allService -->", "onSelectTabItemListener_rule" + rule);
                loadQueryData();
                menu_recyclerview.setVisibility(View.GONE);
            }
        });
        menu_recyclerview.setVisibility(View.GONE);
    }

    private void initanimationAdapter() {
        if (type.equals("5")) {
            animationAdapter3 = new AnimationAdapter3(getActivity(), dataArray);
            animationAdapter3.openLoadAnimation();
            animationAdapter3.setNotDoAnimationCount(3);
            recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            animationAdapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
                    Bundle bundle = new Bundle();
                    String postID = postsArticleBaseBean.getObject_id();
                    String cid = postsArticleBaseBean.getTerm_id();
                    String webString = "http://thinkcmf.500-china.com/index.php?g=&m=article&a=index&id=" + postID + "&cid=" + cid;
                    bundle.putString("webString", webString);
                    bundle.putString("title", postsArticleBaseBean.getPost_title());
                    bundle.putString("postID", postID);
                    ActivityUtils.startActivity(bundle, getActivity(), WebActivity.class);
                }
            });
            recyclerview.setAdapter(animationAdapter3);
            appBarLayout_01.setVisibility(View.GONE);
        } else if (type.equals("4")) {
            animationTeslaAdaptes = new AnimationTeslaAdapter(getActivity(), dataArray, type);
            animationTeslaAdaptes.openLoadAnimation();
            animationTeslaAdaptes.setNotDoAnimationCount(3);
            recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            animationTeslaAdaptes.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("tid", postsArticleBaseBean.getTid());
                    bundle.putString("term_id", postsArticleBaseBean.getTerm_id());
                    bundle.putString("post_id", postsArticleBaseBean.getObject_id());
                    bundle.putString("post_title", postsArticleBaseBean.getPost_title());
                    bundle.putString("store_count", postsArticleBaseBean.getStore_count());
                    bundle.putString("post_authorname", postsArticleBaseBean.getPost_authorname());
                    bundle.putString("mobile", postsArticleBaseBean.getMobile());
                    bundle.putString("post_mobile", postsArticleBaseBean.getPost_mobile());
                    bundle.putString("post_area", postsArticleBaseBean.getPost_area());
                    bundle.putString("post_price", postsArticleBaseBean.getPost_price());
                    bundle.putString("post_date", postsArticleBaseBean.getPost_date());
                    bundle.putString("photos_urls", postsArticleBaseBean.getPhotos_urls());
                    bundle.putString("photos_content", postsArticleBaseBean.getPost_excerpt());
                    ActivityUtils.startActivity(bundle, getActivity(), TSShopDetailActivity.class);
                }
            });
            recyclerview.setAdapter(animationTeslaAdaptes);
            appBarLayout_01.setVisibility(View.VISIBLE);
        } else {
            animationTeslaAdapter = new AnimationTeslaAdapter(getActivity(), dataArray, type);
            animationTeslaAdapter.openLoadAnimation();
            animationTeslaAdapter.isLoadMoreEnable();
            animationTeslaAdapter.setNotDoAnimationCount(20);
            recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            animationTeslaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PostsArticleBaseBean postsArticleBaseBean = dataArray.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("tid", postsArticleBaseBean.getTid());
                    bundle.putString("term_id", postsArticleBaseBean.getTerm_id());
                    bundle.putString("post_id", postsArticleBaseBean.getObject_id());
                    bundle.putString("post_title", postsArticleBaseBean.getPost_title());
                    bundle.putString("store_count", postsArticleBaseBean.getStore_count());
                    bundle.putString("post_authorname", postsArticleBaseBean.getPost_authorname());
                    bundle.putString("mobile", postsArticleBaseBean.getMobile());
                    bundle.putString("post_mobile", postsArticleBaseBean.getPost_mobile());
                    bundle.putString("post_area", postsArticleBaseBean.getPost_area());
                    bundle.putString("post_price", postsArticleBaseBean.getPost_price());
                    bundle.putString("post_date", postsArticleBaseBean.getPost_date());
                    bundle.putString("photos_urls", postsArticleBaseBean.getPhotos_urls());
                    bundle.putString("photos_content", postsArticleBaseBean.getPost_excerpt());
                    ActivityUtils.startActivity(bundle, getActivity(), TSShopDetailActivity.class);
                }
            });
            animationTeslaAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {

//                    ToastUitl.showShort( "onLoadMoreRequested" );
                    if(loadmore){
                        if(number==4){
                            number=0;
                            count++;
                        }else{
                            number++;
                        }
                        loadQueryData();
                    }
                }
            },recyclerview);
            recyclerview.setAdapter(animationTeslaAdapter);
            appBarLayout_01.setVisibility(View.VISIBLE);
        }
    }

    private void showGiftPop(View view, String[] newString) {
        mGiftPopup.setNewData(view, newString, type);
    }

    private void showCityPop(View view, List<ProCityBean> proCityBeans, int position) {
        mCityPopup.setNewData(view, proCityBeans, position, 0);
    }

    /**
     * 默认是已付款
     */
    private int menuPosition = 0;
    private String type = "48";
    private String query = "";
    private String rule = "0";
    private String category = "0";
    private String region = "0";
    private int number = 0;
    private boolean loadmore = false;
    private int count = 0;
    private DBManager dbManager;
    private List<ProCityBean> proCityBeanList = new ArrayList<>();
    private int sposition = 0;

    public OrderListsFragment() {
        // Required empty public constructor
        LogUtil.d("allService -->", "OrderListsFragment" + rule);
    }

    public static OrderListsFragment newInstance(int param1, String param2) {
        OrderListsFragment fragment = new OrderListsFragment();
        LogUtil.d("allService -->", "newInstance" + param1);
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = String.valueOf(getArguments().getInt(ARG_PARAM1));
        }
        LogUtil.d("allService -->", "onCreate_rule" + rule);
        LogUtil.d("allService -->", "onCreate_type" + type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout.setOnRefreshListener(this);
        LogUtil.d("allService -->", "onViewCreated_rule" + rule);
        LogUtil.d("allService -->", "onViewCreated_type" + type);
        refreshLayout.setOnLoadmoreListener(this);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.autoRefresh();
        initGiftPop();
        initCityPop();
        // 设置搜索文本监听
        android_searchView.setOnQueryTextFocusChangeListener(new SearchView.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                search_btn_text.setVisibility(View.INVISIBLE);
            }
        });

        android_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                //伪搜索
                search_btn_text.setVisibility(View.INVISIBLE);

                //清除焦点，收软键盘
                //mSearchView.clearFocus();

                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //do something
                //当没有输入任何内容的时候清除结果，看实际需求
                if (TextUtils.isEmpty(newText)) {
                    search_btn_text.setVisibility(View.VISIBLE);
                } else {
                    search_btn_text.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        initanimationAdapter();
        menu_recyclerview.setVisibility(View.GONE);
        dbManager = new DBManager(getActivity());
        proCityBeanList = dbManager.getAllProCities();
    }

    public void refreshData(int position) {
        type = String.valueOf(2 + position);
        loadData();
    }

    private void loadQueryData() {
        String cidString = type;
        if (type.equals("5")) {
            cidString = "16";
        }
        LogUtil.d("allService -->", "loadQueryData_rule" + rule);
        LogUtil.d("allService -->", "loadQueryData_type" + type);
//        ToastUtils.showShort("搜索地区编码："+region);
        String page=String.valueOf(count*5+number);
        RetrofitManager.create().HomeDataQueryByPage(cidString, query, rule, category, region,page)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HomeDataUserBean>bindToLifecycle())
                .subscribe(new CommonObserver<HomeDataUserBean>(refreshLayout) {
                    @Override
                    public void onSuccess(HomeDataUserBean value) {

                        if (value.getCode() == Constant.CODE_SUCC) {
//                            ToastUtils.showShort("有效信息搜索成功");
                            if(number==0){
                                dataArray.clear();
                                dataArray = value.getReferer();
                            }else{
                                dataArray.addAll(value.getReferer());
                            }
                            if(value.getReferer().size()<20){
                                loadmore=false;
                            }else{
                                loadmore=true;
                            }
                            if (type.equals("5")) {
                                animationAdapter3.setNewData(dataArray);
                                appBarLayout_01.setVisibility(View.GONE);
                            } else if (type.equals("4")) {
                                animationTeslaAdaptes.setNewData(dataArray);
                                appBarLayout_01.setVisibility(View.VISIBLE);
                            } else {
                                animationTeslaAdapter.setNewData(dataArray);
                                appBarLayout_01.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (type.equals("5")) {
                                animationAdapter3.setNewData(dataArray);
                            } else if (type.equals("4")) {
                                animationTeslaAdaptes.setNewData(dataArray);
                            } else {
                                animationTeslaAdapter.setNewData(dataArray);
                            }
                        }
                    }
                });
    }

    private void loadData() {
//        multipleStatusView.showContent();
        String cidString = type;
        if (type.equals("5")) {
            cidString = "16";
        }
        RetrofitManager.create().HomeData("Portal", "list", "indexJson", cidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<HomeDataOtherBean>bindToLifecycle())
                .subscribe(new CommonObserver<HomeDataOtherBean>(refreshLayout) {
                    @Override
                    public void onSuccess(HomeDataOtherBean value) {
                        if (value.getCode() == Constant.CODE_SUCC) {
                            if (type.equals("5")) {
                                dataArray = value.getReferer().getPosts();
                                animationAdapter3.setNewData(dataArray);
                                appBarLayout_01.setVisibility(View.GONE);
                            } else if (type.equals("4")) {
                                dataArray = value.getReferer().getPosts();
                                animationTeslaAdaptes.setNewData(dataArray);
                                appBarLayout_01.setVisibility(View.VISIBLE);
                            } else {
                                dataArray = value.getReferer().getPosts();
                                animationTeslaAdapter.setNewData(dataArray);
                                appBarLayout_01.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("allService -->", "onDestroyView_rule" + rule);
        LogUtil.d("allService -->", "onDestroyView_type" + type);
        unbinder.unbind();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(number==4){
            number=0;
            count++;
        }else{
            number++;
        }
//        ToastUitl.showShort( "onLoadmore" );
        loadQueryData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (android_searchView != null) {
            android_searchView.setQuery("", false);
        }
        number=0;
        count=0;
//        ToastUitl.showShort( "onRefresh" );
        loadQueryData();
    }

    @Override
    @OnClick({R.id.search_btn_block, R.id.filter_layout_01, R.id.filter_layout_02, R.id.filter_layout_03})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn_block:
                number=0;
                count=0;
                LogUtil.d("allService -->", "search_btn_block_rule" + rule);
                LogUtil.d("allService -->", "search_btn_block_type" + type);
                query = String.valueOf(android_searchView.getQuery());
                if (query != null && query.length() > 0) {
                    loadQueryData();
                    KeyBordUtil.hideSoftKeyboard(view);
//                    ToastUtils.showShort(query);
                } else {
                    ToastUtils.showShort("请输入有效搜索信息");
                }
                break;
            case R.id.filter_layout_01:
                menuPosition = 0;
//                ToastUtils.showShort("发布房");
                showGiftPop(view, new String[]{"全部", "散户", "基地", "钢厂"});
//                setNewMenuData(new String[]{"全部","散户","基地","钢厂"});
                break;
            case R.id.filter_layout_02:
                menuPosition = 1;
//                ToastUtils.showShort("类型");
                showGiftPop(view, menuString);
//                setNewMenuData(new String[]{"废钢","利用废钢","机械设备","钢结构","新成品钢","废纸","废旧金属","再生塑料"});
                break;
            case R.id.filter_layout_03:
                showCityPop(view, proCityBeanList, sposition);
                break;
                default:break;
        }
    }

    private String[] menuNumber = new String[]{"0", "2", "1", "6", "7", "8", "213", "214", "215"};
    private String[] menuString = new String[]{"全部", "废钢", "利用钢材", "机械设备", "钢结构", "新成品钢", "废纸", "废旧金属", "再生塑料"};

    @Override
    public void onSelectTabItemListener(int position, String gifType) {
        type = gifType;
//        ToastUtils.showShort(String.valueOf(position));
        switch (menuPosition) {
            case 0:
                rule = String.valueOf((position == 0) ? position : position + 2);
                break;
            case 1:
                category = menuNumber[position];
                break;
            default:
                break;
        }
        number=0;
        count=0;
        LogUtil.d("allService -->", "onSelectTabItemListener_type" + type);
        LogUtil.d("allService -->", "onSelectTabItemListener_rule" + rule);
        loadQueryData();
    }

    @Override
    public void onSelectTabCityListener(String cityCode, int position) {
//        ToastUtils.showShort("0010");
        number=0;
        count=0;
        region = cityCode;
        sposition = position;
        loadQueryData();
    }

}
