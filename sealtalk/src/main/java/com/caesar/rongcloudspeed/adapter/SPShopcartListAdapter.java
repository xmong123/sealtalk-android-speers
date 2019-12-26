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
 * Date: @date 2015年10月30日 下午10:03:56
 * Description: 购物车 adapter
 *
 * @version V1.0
 */
package com.caesar.rongcloudspeed.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.GoodsCartBean;
import com.caesar.rongcloudspeed.constants.Constant;

import java.util.List;

/**
 * @author 飞龙
 *
 */
public class SPShopcartListAdapter extends BaseAdapter implements View.OnClickListener {

    private String TAG = "SPShopcartListAdapter";
    private List<GoodsCartBean> mProducts;
    private Context mContext;
    private ShopCarClickListener mShopCarClickListener;
    private Handler mHandler;

    public SPShopcartListAdapter(Context context, ShopCarClickListener shopCarClickListener) {
        this.mContext = context;
        this.mShopCarClickListener = shopCarClickListener;
    }


    public void setData(List<GoodsCartBean> products) {
        if (products == null) return;
        this.mProducts = products;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mProducts == null) return 0;
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        if (mProducts == null) return null;
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mProducts == null) return -1;
        return Integer.valueOf(mProducts.get(position).getGoods_id());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shopcart_list_item, parent, false);

            //使用减少findView的次数
            holder.nameTxtv = ((TextView) convertView.findViewById(R.id.name_txtv));
            holder.picImgv = ((ImageView) convertView.findViewById(R.id.pic_imgv));
            holder.shopPriceTxtv = ((TextView) convertView.findViewById(R.id.shop_price_txtv));
//			holder.marketPriceTxtv = ((TextView) convertView.findViewById(R.id.market_price_txtv));
            holder.cartCountTxtv = ((EditText) convertView.findViewById(R.id.cart_count_dtxtv));
            holder.minusBtn = ((Button) convertView.findViewById(R.id.cart_minus_btn));
            holder.plusBtn = ((Button) convertView.findViewById(R.id.cart_plus_btn));
            holder.checkBtn = ((Button) convertView.findViewById(R.id.check_btn));
            holder.deleteBtn = ((ImageView) convertView.findViewById(R.id.shop_del_btn));
            holder.volume_number = ((TextView) convertView.findViewById(R.id.volume_number));
            holder.volume_price = ((TextView) convertView.findViewById(R.id.volume_price));
            holder.cart_layout_volume = ((LinearLayout) convertView.findViewById(R.id.cart_layout_volume));
            convertView.setTag(holder);//设置标记

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.minusBtn.setTag(position);
        holder.plusBtn.setTag(position);
        holder.checkBtn.setTag(position);

        holder.minusBtn.setOnClickListener(this);
        holder.plusBtn.setOnClickListener(this);
        holder.checkBtn.setOnClickListener(this);
        holder.deleteBtn.setOnClickListener(this);
        /** 选中的行: 背景白色, 否则灰色  */
        //获取该行数据
        GoodsCartBean product = (GoodsCartBean) mProducts.get(position);

        //设置数据到View
        holder.nameTxtv.setText(product.getGoods_name());
        holder.shopPriceTxtv.setText("￥" + product.getGoods_price()+"元");
//		holder.marketPriceTxtv.setText("¥"+product.getGoods_price());
        holder.cartCountTxtv.setText(product.getGoods_num());

//		holder.marketPriceTxtv.getPaint().setFlags( Paint.STRIKE_THRU_TEXT_FLAG);

        if (product.getSelected().equals("1")) {
            holder.checkBtn.setBackgroundResource(R.drawable.icon_checkyes);
        } else {
            holder.checkBtn.setBackgroundResource(R.drawable.icon_checkno);
        }
        String goods_img = product.getGoods_img();

        if (!goods_img.startsWith( "http://" )) {
            goods_img = Constant.THINKCMF_PATH + goods_img;
        }
        if(goods_img!=null&&goods_img.length()>32){
            Glide.with( mContext ).load( goods_img ).into( holder.picImgv );
        }
//        Glide.with(mContext).load(Constant.IMAGE_PATHS + goods_img).placeholder(R.drawable.product_default).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(holder.picImgv);

		/*swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
			@Override
			public void onDoubleClick(SwipeLayout layout, boolean surface) {
				Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
			}
		});
		*/
        return convertView;
    }


    class ViewHolder {
        TextView nameTxtv;
        TextView shopPriceTxtv;
        //		TextView marketPriceTxtv;
        EditText cartCountTxtv;
        ImageView picImgv;
        Button checkBtn;
        Button minusBtn;
        Button plusBtn;
        LinearLayout cart_layout_volume;
        TextView volume_number;
        TextView volume_price;
        ImageView deleteBtn;
    }

    public interface ShopCarClickListener {
        public void minuProductFromCart(GoodsCartBean product);

        public void plusProductFromCart(GoodsCartBean product);

        public void checkProductFromCart(GoodsCartBean product, boolean checked);

        public void deleteProductFromCart(GoodsCartBean product);
    }

    @Override
    public void onClick(View v) {
        int position = 0;
        GoodsCartBean product = null;
        if (v.getTag() != null && v.getTag().toString() != null) {
            position = Integer.valueOf(v.getTag().toString());
        }
        if (mProducts != null && position < mProducts.size() && position >= 0) {
            product = mProducts.get(position);
        }
        switch (v.getId()) {
            case R.id.cart_minus_btn:
                if (mShopCarClickListener != null)
                    mShopCarClickListener.minuProductFromCart(product);
                break;
            case R.id.cart_plus_btn:
                if (mShopCarClickListener != null)
                    mShopCarClickListener.plusProductFromCart(product);
                break;
            case R.id.check_btn:
                boolean checked = product.getSelected().equals("1") ? false : true;
                if (mShopCarClickListener != null)
                    mShopCarClickListener.checkProductFromCart(product, checked);
                break;
			case R.id.shop_del_btn:
				if (mShopCarClickListener!=null)mShopCarClickListener.deleteProductFromCart(product);
				break;
        }
    }

}
