package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.caesar.rongcloudspeed.bean.AddressBean;
import com.caesar.rongcloudspeed.bean.GoodsListCartBean;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.zaaach.citypicker.db.DBManager;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import com.caesar.rongcloudspeed.R;
//import com.caesar.rongcloudspeed.bean.AddressBean;
import com.caesar.rongcloudspeed.bean.AddressItemBean;
import com.caesar.rongcloudspeed.bean.CommonResonseBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.caesar.rongcloudspeed.constants.Constant.CODE_SUCC;

public class AddressListActivity extends MultiStatusActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.addAddressBtn)
    Button addAddressBtn;
    ArrayList<AddressItemBean> addressBeans = new ArrayList<>();
    private CommonAdapter<AddressItemBean> adapter;
    private String uidString;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBarView(titlebar, "选择收货地址");
        ButterKnife.bind(this);
        initView();
        uidString = UserInfoUtils.getAppUserId(this);
        dbManager = new DBManager(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        uidString = UserInfoUtils.getAppUserId(this);
        loadData();

    }

    private void loadData() {
        multipleStatusView.showLoading();
        RetrofitManager.create().address_list(uidString)
//                .compose(this.<AddressBean>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AddressBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(AddressBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            addressBeans.clear();
                            addressBeans.addAll(value.getReferer());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

//        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().ajaxAddressJson(uidString),
//                new NetworkCallback<GoodsListCartBean>() {
//                    @Override
//                    public void onSuccess(GoodsListCartBean cartBean) {
//                        if (cartBean.getCode() == 101) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        Toast.makeText(AddressListActivity.this, "网络异常", Toast.LENGTH_LONG).show();
//                    }
//                });

    }

    private void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CommonAdapter<AddressItemBean>(this, R.layout.activity_add_address_recyclerview_item, addressBeans) {
            @Override
            protected void convert(ViewHolder holder, final AddressItemBean addressBean, final int position) {
                String provinceString = dbManager.getAreaName(addressBean.getProvince());
                String cityString = dbManager.getAreaName(addressBean.getCity());
                holder.setText(R.id.addressDetail, provinceString + "-" + cityString + "-" + addressBean.getAddress());
                holder.setText(R.id.addressTell, addressBean.getMobile());
                holder.setText(R.id.addressName, addressBean.getConsignee());
                holder.setOnClickListener(R.id.btnTop, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addressBeans.remove(position);
                        adapter.notifyDataSetChanged();
                        removeAddressItem(addressBean);
                    }
                });
                holder.setOnClickListener(R.id.addresContainer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDefaultAddressItem(addressBean);
                    }
                });
            }

        };
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                AddressItemBean addressItemBean = addressBeans.get(position);
                Intent data = getIntent();
                data.putExtra("addressItem", addressItemBean);
                setResult(RESULT_OK, data);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void removeAddressItem(AddressItemBean addressBean) {
        RetrofitManager.create().delAddressJson(addressBean.getAddress_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<CommonResonseBean>() {
                    @Override
                    public void onSuccess(CommonResonseBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            ToastUtils.showToast("地址删除成功");
                        }
                    }
                });
    }

    private void setDefaultAddressItem(AddressItemBean addressBean) {
        RetrofitManager.create().setDefaultAddressJson(uidString,addressBean.getAddress_id())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<CommonResonseBean>() {
                    @Override
                    public void onSuccess(CommonResonseBean value) {
                        if (value.getCode() == CODE_SUCC) {
                            Intent data = getIntent();
                            data.putExtra("addressItem", addressBean);
                            setResult(RESULT_OK, data);
                            ToastUtils.showToast("默认地址设置成功");
                            finish();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            AddressItemBean addressItem = data.getParcelableExtra("addressItem");
            addressBeans.add(addressItem);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public int getContentView() {
        return R.layout.activity_address_list;
    }

    @OnClick(R.id.addAddressBtn)
    public void onViewClicked() {
        AddAddressActivity.openForResult(100);
    }

    public static void open() {

    }

    /**
     * 跳转到当前界面
     *
     * @param requestCode
     */
    public static void openForResult(int requestCode) {
        ActivityUtils.
                getTopActivity().
                startActivityForResult(new Intent(ActivityUtils.getTopActivity(), AddressListActivity.class), requestCode);
    }
}
