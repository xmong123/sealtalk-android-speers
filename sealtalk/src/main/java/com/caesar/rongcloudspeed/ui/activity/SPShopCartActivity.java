/**
 * shopmobile for tpshop
 * ============================================================================
 * 版权所有 2015-2099 深圳搜豹网络科技有限公司，并保留所有权利。
 * 网站地址: http://www.tp-shop.cn
 * ——————————————————————————————————————
 * 这不是一个自由软件！您只能在不用于商业目的的前提下对程序代码进行修改和使用 .
 * 不允许对程序代码以任何形式任何目的的再发布。
 * ============================================================================
 * Author: 飞龙  wangqh01292@163.com
 * Date: @date 2015年10月20日 下午7:19:26
 * Description:购物车Fragment
 *
 * @version V1.0
 */
package com.caesar.rongcloudspeed.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.caesar.rongcloudspeed.bean.GoodsCartBean;
import com.caesar.rongcloudspeed.bean.GoodsListCartBean;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.ui.SPBaseActivity;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.classic.common.MultipleStatusView;

import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.adapter.SPShopcartListAdapter;
import com.caesar.rongcloudspeed.utils.SPConfirmDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 *  首页 -> 购物车
 *
 */
public class SPShopCartActivity extends SPBaseActivity implements View.OnClickListener, SPShopcartListAdapter.ShopCarClickListener, SPConfirmDialog.ConfirmDialogListener {

    private String TAG = "SPShopCartFragment";
    private Context mContext;

    private ListView shopcartListv;
    private TextView totalfeeTxtv;
    private TextView cutfeeTxtv;
    private Button checkallBtn;
    private Button buyBtn;

    private RelativeLayout shopcartPcf;
    private List<GoodsCartBean> products;
    private GoodsCartBean currentProduct;
    private JSONArray formDataArray;
    private SPShopcartListAdapter mAdapter;

    private double totalFee;
    private double cutFee;
    boolean isAllCheck;

    private String uidString;
    private String orderName;
    private String orderTotal="100.00";
    private String orderImage;

    protected MultipleStatusView multipleStatusView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcart_fragment);
        shopcartListv = (ListView) findViewById(R.id.shopcart_listv);
        shopcartPcf = (RelativeLayout) findViewById(R.id.shopcart_pcf);
        multipleStatusView = (MultipleStatusView) findViewById(R.id.multiple_status_view);

        totalfeeTxtv = (TextView) findViewById(R.id.totalfee_txtv);
        cutfeeTxtv = (TextView) findViewById(R.id.cutfee_txtv);
        checkallBtn = (Button) findViewById(R.id.checkall_btn);
        buyBtn = (Button) findViewById(R.id.buy_btn);
        View emptyView = findViewById(R.id.empty_lstv);
        shopcartListv.setEmptyView(emptyView);
        checkallBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        shopcartListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsCartBean cartBean = (GoodsCartBean) mAdapter.getItem(position);
                String goods_id = cartBean.getGoods_id();
                String photo = cartBean.getPhoto();
                String goods_remark = cartBean.getGoods_remark();
                String goods_name = cartBean.getGoods_name();
                String market_price = cartBean.getMarket_price();
                String shop_price = cartBean.getShop_price();
                Intent intent = new Intent(SPShopCartActivity.this, SPShopDetailActivity.class);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("photo", photo);
                intent.putExtra("goods_remark", goods_remark);
                intent.putExtra("goods_name", goods_name);
                intent.putExtra("market_price", market_price);
                intent.putExtra("shop_price", shop_price);
                startActivity(intent);

            }
        });
        uidString = UserInfoUtils.getAppUserId(this);
        formDataArray = new JSONArray();
        mAdapter = new SPShopcartListAdapter(SPShopCartActivity.this, this);
        shopcartListv.setAdapter(mAdapter);
        isAllCheck = false;
        refreshData();
    }

    @Override
    public void initSubViews() {

    }

    @Override
    public void initEvent() {


    }

    @Override
    public void initData() {

    }

    public void refreshData() {

        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().ajaxAddCart(uidString),
                new NetworkCallback<GoodsListCartBean>() {
                    @Override
                    public void onSuccess(GoodsListCartBean cartBean) {
                        if (cartBean.getCode() == 101) {
                            orderTotal=cartBean.getInfo();
                            totalfeeTxtv.setText("合计: ¥" + cartBean.getInfo());
                            products = cartBean.getReferer();
                            mAdapter.setData(products);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(SPShopCartActivity.this, "暂无数据，请稍后再试", Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     *  将每一个购物车商品生产对应的表单数据缓存起来, 以便表单提交试用
     *
     */
    public void dealModels(List<GoodsCartBean> products) {

        formDataArray = new JSONArray();
        isAllCheck = true;
        if (products == null || products.size() < 1) {
            totalFee = 0.00;
            cutFee = 0.00;
            refreshFeeView();
            ;
            checkallBtn.setBackgroundResource(R.drawable.icon_checkno);
            return;
        }
        try {
            for (GoodsCartBean product : products) {
                JSONObject formJson = new JSONObject();
                formJson.put("cartID", product.getGoods_id());
                formJson.put("goodsNum", product.getGoods_num());
                formJson.put("selected", product.getSelected());
                formJson.put("storeCount", product.getGoods_num());

                if ("0".equals(product.getSelected())) {
                    isAllCheck = false;
                }
                formDataArray.put(formJson);
            }

            //设置全选状态
            if (isAllCheck) {
                checkallBtn.setBackgroundResource(R.drawable.icon_checkyes);
            } else {
                checkallBtn.setBackgroundResource(R.drawable.icon_checkno);
            }
            if (mDataJson.has("totalFee")) {
                totalFee = mDataJson.getDouble("totalFee");
            }
            if (mDataJson.has("cutFee")) {
                cutFee = mDataJson.getDouble("cutFee");
            }
            refreshFeeView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新商品总价
     */
    public void refreshFeeView() {

        String totalFeeFmt = "合计:¥" + totalFee;
        String cutFeeFmt = "共节省:¥" + cutFee;

        int startIndex = 3;
        int endIndex = totalFeeFmt.length();
        SpannableString totalFeeSpanStr = new SpannableString(totalFeeFmt);
        totalFeeSpanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_red)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

        totalfeeTxtv.setText(totalFeeSpanStr);
        cutfeeTxtv.setText(cutFeeFmt);

        if (isAllCheck) {
            checkallBtn.setBackgroundResource(R.drawable.icon_checkyes);
        } else {
            checkallBtn.setBackgroundResource(R.drawable.icon_checkno);
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.checkall_btn) {
            //全选或全不选
            checkAllOrNo();
        } else if (v.getId() == R.id.buy_btn) {
            Intent confirmIntent = new Intent(SPShopCartActivity.this, ConfirmOrderActivity.class);
            confirmIntent.putExtra("orderName","");
            confirmIntent.putExtra("orderTotal",orderTotal);
            confirmIntent.putExtra("orderImage","");
            SPShopCartActivity.this.startActivityForResult(confirmIntent,100);
        }
    }


    @Override
    public void minuProductFromCart(GoodsCartBean product) {
        String cart_id = product.getId();
        int goodsNO=Integer.parseInt(product.getGoods_num());
        if(goodsNO>1){
            NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().ajaxUpdateCart(cart_id,String.valueOf(--goodsNO)),
                new NetworkCallback<GoodsListCartBean>() {
                    @Override
                    public void onSuccess(GoodsListCartBean cartBean) {
                        if (cartBean.getCode() == 101) {
                            orderTotal=cartBean.getInfo();
                            totalfeeTxtv.setText("合计: ¥" + cartBean.getInfo());
                            products = cartBean.getReferer();
                            mAdapter.setData(products);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(SPShopCartActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
        }
    }

    @Override
    public void plusProductFromCart(GoodsCartBean product) {
        String cart_id = product.getId();
        int goodsNO=Integer.parseInt(product.getGoods_num());
        if(goodsNO<10) {
            NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().ajaxUpdateCart(cart_id,String.valueOf(++goodsNO)),
                    new NetworkCallback<GoodsListCartBean>() {
                        @Override
                        public void onSuccess(GoodsListCartBean cartBean) {
                            if (cartBean.getCode() == 101) {
                                orderTotal=cartBean.getInfo();
                                totalfeeTxtv.setText("合计: ¥" + cartBean.getInfo());
                                products = cartBean.getReferer();
                                mAdapter.setData(products);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(SPShopCartActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    public void checkProductFromCart(GoodsCartBean product, boolean checked) {
        String cart_id = product.getId();
        String selected=checked?"1":"0";
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().ajaxUpdateCartChecked(cart_id,selected),
                new NetworkCallback<GoodsListCartBean>() {
                    @Override
                    public void onSuccess(GoodsListCartBean cartBean) {
                        if (cartBean.getCode() == 101) {
                            orderTotal=cartBean.getInfo();
                            totalfeeTxtv.setText("合计: ¥" + cartBean.getInfo());
                            products = cartBean.getReferer();
                            mAdapter.setData(products);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(SPShopCartActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void deleteProductFromCart(GoodsCartBean product) {
        String cart_id=product.getId();
        int actionType=Integer.parseInt(cart_id);
        showConfirmDialog("确定删除该商品", "删除提醒", this, actionType);
    }


    public void checkAllOrNo() {
        boolean needCheckAll = false;//是否需要全选
        try {
            //1. 判断是否需要全选
            int length = formDataArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject cartJson = formDataArray.getJSONObject(i);
                if (cartJson.getString("selected").equals("0")) {
                    needCheckAll = true;
                    break;
                }
            }
            //2. 全选或反选
            String selected = needCheckAll ? "1" : "0";
            int length2 = formDataArray.length();
            for (int j = 0; j < length2; j++) {
                JSONObject cartJson2 = formDataArray.getJSONObject(j);
                cartJson2.put("selected", selected);
            }
            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshData();
    }

    /**
     *  从购物车删除商品
     *
     *  @param
     */
    public void deleteProductFromCart(String string) {
        showToast("正在删除");

    }

    @Override
    public void clickOk(int actionType) {
        showToast("正在删除");
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().ajaxDeleteCart(String.valueOf(actionType)),
                new NetworkCallback<GoodsListCartBean>() {
                    @Override
                    public void onSuccess(GoodsListCartBean cartBean) {
                        if (cartBean.getCode() == 101) {
                            orderTotal=cartBean.getInfo();
                            totalfeeTxtv.setText("合计: ¥" + cartBean.getInfo());
                            products = cartBean.getReferer();
                            mAdapter.setData(products);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(SPShopCartActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            finish();
        }
    }
}
