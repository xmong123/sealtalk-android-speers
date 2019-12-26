package com.caesar.rongcloudspeed.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.BookShopAdapter;
import com.caesar.rongcloudspeed.bean.GoodsDetailBean;
import com.caesar.rongcloudspeed.bean.GoodsListBaseBean;
import com.caesar.rongcloudspeed.bean.HomeDataBean;
import com.caesar.rongcloudspeed.bean.PostsArticleBaseBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.activity.SPShopDetailActivity;
import com.caesar.rongcloudspeed.widget.PagerGridLayoutManager;
import com.caesar.rongcloudspeed.widget.PagerGridSnapHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pili.pldroid.player.AVOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面子界面-发现界面
 */
public class BookStoreHomeFragment extends BaseFragment implements PagerGridLayoutManager.PageListener {
    private RecyclerView bookstoreRecyclerView;
    private BookShopAdapter bookShopAdapter;
    private View headView;
    private List<GoodsDetailBean> bookArray=new ArrayList<GoodsDetailBean>();

    @Override
    protected int getLayoutResId() {
        return R.layout.main_fragment_book_store;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        bookstoreRecyclerView = getActivity().findViewById(R.id.bookstore_recyclerView);
        headView = getLayoutInflater().inflate(R.layout.main_fragment_bookstore_header, (ViewGroup) bookstoreRecyclerView.getParent(), false);

        bookShopAdapter = new BookShopAdapter(getActivity(),bookArray);
        bookShopAdapter.openLoadAnimation();
        bookShopAdapter.setNotDoAnimationCount(4);
        bookstoreRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        bookShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsDetailBean goodsDetailBean=bookArray.get( position );
                String goods_id=goodsDetailBean.getGoods_id();
                String photo=goodsDetailBean.getPhoto();
                String goods_remark=goodsDetailBean.getGoods_remark();
                String goods_name=goodsDetailBean.getGoods_name();
                String market_price=goodsDetailBean.getMarket_price();
                String shop_price=goodsDetailBean.getShop_price();
                String goods_content=goodsDetailBean.getGoods_content();
                Intent intent = new Intent(getActivity(), SPShopDetailActivity.class);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("photo", photo);
                intent.putExtra("goods_remark", goods_remark);
                intent.putExtra("goods_name", goods_name);
                intent.putExtra("market_price", market_price);
                intent.putExtra("shop_price", shop_price);
                intent.putExtra("goods_content", goods_content);
                startActivity(intent);
            }
        });
        bookShopAdapter.addHeaderView(headView);
        bookstoreRecyclerView.setAdapter(bookShopAdapter);
        loadData();
    }

    private void loadData() {
        showLoadingDialog("");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().fetchGoodsListDataes("0"),
                new NetworkCallback<GoodsListBaseBean>() {
                    @Override
                    public void onSuccess(GoodsListBaseBean goodsListBaseBean) {
                        bookArray=goodsListBaseBean.getReferer();
                        bookArray.addAll(bookArray);
                        bookShopAdapter.setNewData(bookArray);
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onInitViewModel() {
        super.onInitViewModel();
    }

    @Override
    protected void onClick(View v, int id) {
        switch (id) {
            default:
                break;
        }
    }

    @Override
    public void onPageSizeChanged(int pageSize) {

    }

    @Override
    public void onPageSelect(int pageIndex) {

    }
}
