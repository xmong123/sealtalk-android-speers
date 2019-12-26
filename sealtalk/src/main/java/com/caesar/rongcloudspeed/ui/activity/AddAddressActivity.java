package com.caesar.rongcloudspeed.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.ActivityUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.AddressDefaultBean;
import com.caesar.rongcloudspeed.bean.AddressItemBean;
import com.caesar.rongcloudspeed.common.MultiStatusActivity;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.ProCityBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddAddressActivity extends MultiStatusActivity {

    @BindView(R.id.username)
    EditText usernameEdit;
    @BindView(R.id.identifyNumber)
    EditText identifyNumberEdit;
    @BindView(R.id.shixian)
    TextView shixian;
    @BindView(R.id.address)
    EditText addressEdit;
    @BindView(R.id.save)
    Button saveBtn;
    private String shiqu;
    private DBManager dbManager;
    private OptionsPickerView cityCustomOptions;
    private List<City> options1Items = new ArrayList<>();
    private List<ArrayList<City>> options2Items = new ArrayList<>();
    private int sposition = 0;//选择城市省份ID
    private int rposition = 0;//选择分类ID
    private String postRegion=null;
    private String postRegion1=null;
    private String uidString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleBarView(titlebar, "添加收货地址");
        ButterKnife.bind(this);
        getOptionData();
        uidString = UserInfoUtils.getAppUserId(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_address;
    }

    @OnClick({R.id.shixian, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shixian:
                cityCustomOptions.show();
                break;
            case R.id.save:
                if (TextUtils.isEmpty(usernameEdit.getText().toString())) {
                    ToastUtils.showShort("请输入有效的收货人姓名");
                } else if (TextUtils.isEmpty(identifyNumberEdit.getText().toString())) {
                    ToastUtils.showShort("请输入有效的收货人手机号");

                } else if (postRegion==null||postRegion1==null) {
                    ToastUtils.showShort("请选择有效的收货地址");

                } else if (TextUtils.isEmpty(addressEdit.getText().toString())) {
                    ToastUtils.showShort("请输入有效的详细地址");

                }
                submitData();
                break;
        }
    }

    private void getOptionData() {
        dbManager = new DBManager(this);
        options1Items = dbManager.getAllProvince();
        options2Items = dbManager.getAllProvinceCities();
        cityCustomOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v) -> {
            String provinceStr = options1Items.get(options1).getName();
            postRegion=options1Items.get(options1).getCode();
            postRegion1=options2Items.get(options1).get(options2).getCode();
            // 设置在你组件上的 选取后得到的内容
            String cityStr = options2Items.get(options1).get(options2).getName();
            postRegion = options2Items.get(options1).get(options2).getCode();
            shixian.setText(provinceStr + "-" + cityStr);
        })
                //* 使用系统自带的Color文件 也可用自己的值 但是这里的值要注意 颜色值=（65536*Red)+（256*Green)+(Blue) 不能用R.color的形式
                .setTitleText("城市选择")
                .setContentTextSize(20)                     //设置滚轮文字大小
                .setDividerColor(Color.GRAY)              //设置分割线的颜色
                .setSelectOptions(0, 0)                  //默认选中项
                .setBgColor(Color.WHITE)                // 设置下方选择框的背景颜色
                .setTitleBgColor(getResources().getColor(R.color.home_title_color))           // 设置标题处整个背景的背景颜色
                .setTitleColor(Color.WHITE)           // 设置标题颜色
                .setCancelColor(Color.WHITE)         // 设置左边取消按钮颜色
                .setSubmitColor(Color.WHITE)        // 设置右边确定按钮颜色
                .setTextColorCenter(getResources().getColor(R.color.home_title_color))   //  设置下方滚轮选中条目的字体颜色
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                // 只能设为三级  不管显示多少级
                .setBackgroundId(0x66000000) //设置外部遮罩颜色 只能用16进制的方式 这里设置为无颜色 只是透明度修改
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        cityCustomOptions.setPicker(options1Items, options2Items); //二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/

    }

    private void submitData() {

        multipleStatusView.showLoading();
        String consignee=usernameEdit.getText().toString();
        String mobileString=identifyNumberEdit.getText().toString();
        String addressString=addressEdit.getText().toString();
        RetrofitManager.create()
                .addAddressJson(uidString,consignee,postRegion,postRegion1,mobileString,addressString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<AddressDefaultBean>(multipleStatusView) {
                    @Override
                    public void onSuccess(AddressDefaultBean value) {
                        if (value.getCode() == 101) {
                            AddressItemBean addressItem = value.getReferer();
                            Intent data = getIntent();
                            data.putExtra("addressItem", addressItem);
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    }

                    @Override
                    public boolean onFailure(Throwable e) {

                        return super.onFailure(e);
                    }
                });


    }

    public static void openForResult(int requestCode) {
        ActivityUtils.
                getTopActivity().
                startActivityForResult(new Intent(ActivityUtils.getTopActivity(), AddAddressActivity.class), requestCode);
    }
}
