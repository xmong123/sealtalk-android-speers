package com.caesar.rongcloudspeed.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.allen.library.SuperTextView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.LogUtils;
import com.caesar.rongcloudspeed.R;
import com.caesar.rongcloudspeed.bean.HomeDataUserBean;
import com.caesar.rongcloudspeed.bean.RecruitItemBean;
import com.caesar.rongcloudspeed.bean.UserRecruitMessageBean;
import com.caesar.rongcloudspeed.constants.Constant;
import com.caesar.rongcloudspeed.manager.RetrofitManager;
import com.caesar.rongcloudspeed.oberver.CommonObserver;
import com.caesar.rongcloudspeed.rxlife.RxFragment;
import com.caesar.rongcloudspeed.ui.activity.AccountRecruitEduActivity;
import com.caesar.rongcloudspeed.ui.activity.AccountRecruitJobActivity;
import com.caesar.rongcloudspeed.ui.activity.AccountRecruitMessageActivity;
import com.caesar.rongcloudspeed.utils.ImageLoaderUtils;
import com.caesar.rongcloudspeed.utils.ToastUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PersonalRecruitTwoFragment extends RxFragment implements OnRefreshListener, View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_container)
    RelativeLayout empty_container;
    @BindView(R.id.recruit_empty_btn)
    Button recruit_empty_btn;
    @BindView(R.id.recruit_container)
    LinearLayout recruit_container;
    @BindView(R.id.two_recruit_card1)
    CardView two_recruit_card1;
    @BindView(R.id.two_recruit_card2)
    CardView two_recruit_card2;
    @BindView(R.id.two_recruit_card3)
    CardView two_recruit_card3;
    @BindView(R.id.two_recruit_supert)
    SuperTextView two_recruit_supert;
    @BindView(R.id.two_recruit_supert1)
    SuperTextView two_recruit_supert1;
    @BindView(R.id.two_recruit_supert2)
    SuperTextView two_recruit_supert2;
    @BindView(R.id.two_recruit_supert3)
    SuperTextView two_recruit_supert3;
    @BindView(R.id.two_recruit_supert4)
    SuperTextView two_recruit_supert4;
    @BindView(R.id.two_recruit_supert5)
    SuperTextView two_recruit_supert5;
    @BindView(R.id.two_recruit_supert6)
    SuperTextView two_recruit_supert6;
    @BindView(R.id.two_recruit_supert7)
    SuperTextView two_recruit_supert7;
    @BindView(R.id.two_recruit_supert8)
    SuperTextView two_recruit_supert8;
    private String uidString;
    private DBManager dbManager;
    private OptionsPickerView cityCustomOptions;
    private List<City> options1Items = new ArrayList<>();
    private List<ArrayList<City>> options2Items = new ArrayList<>();
    private String postRegion;
    private RecruitItemBean recruitItemBean;
    private String[] salaryItems = new String[]{"2000元/月以下", "2000～3000元/月", "3000～4000元/月", "4000～5000元/月", "5000～8000元/月", "8000元/月以上", "面议"};
    private String[] gradeItems = new String[]{"高职高中及以下", "大专院校", "全日制本科", "硕士研究生", "MBA", "博士及以上", "保密"};
    private String[] jobItems = new String[]{"1年及以下工作经验", "2年工作经验", "3年工作经验", "4年工作经验", "5年工作经验", "6年工作经验", "7年工作经验", "8年及以上工作经验"};
    private String[] stateItems = new String[]{"目前在职", "目前正在找工作", "观望更多的工作机会"};

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uidString = UserInfoUtils.getAppUserId(getActivity());
        LogUtils.e("PersonalRecruitTwoFragment");
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOptionData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_recruit_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadPersonalRecruitData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPersonalRecruitData();
    }

    @Override
    @OnClick({
            R.id.recruit_empty_btn,
            R.id.two_recruit_supert,
            R.id.two_recruit_supert1,
            R.id.two_recruit_supert2,
            R.id.two_recruit_supert3,
            R.id.two_recruit_supert4,
            R.id.two_recruit_supert5,
            R.id.two_recruit_supert6,
            R.id.two_recruit_supert7,
            R.id.two_recruit_supert8,
            R.id.two_recruit_card1,
            R.id.two_recruit_head,
            R.id.two_recruit_head1,
            R.id.two_recruit_head2,
            R.id.two_recruit_card2,
            R.id.two_recruit_card3})
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), AccountRecruitMessageActivity.class);
        switch (view.getId()) {
            case R.id.recruit_empty_btn:
                startActivity(intent);
                break;
            case R.id.two_recruit_card1:
            case R.id.two_recruit_head:
            case R.id.two_recruit_supert:
            case R.id.two_recruit_supert1:
            case R.id.two_recruit_supert2:
            case R.id.two_recruit_supert3:
                intent.putExtra("recruitItemBean", recruitItemBean);
                startActivity(intent);
                break;
            case R.id.two_recruit_card2:
            case R.id.two_recruit_head1:
            case R.id.two_recruit_supert4:
            case R.id.two_recruit_supert5:
            case R.id.two_recruit_supert6:
                intent = new Intent(getActivity(), AccountRecruitJobActivity.class);
                intent.putExtra("recruitItemBean", recruitItemBean);
                startActivity(intent);
                break;
            case R.id.two_recruit_card3:
            case R.id.two_recruit_head2:
            case R.id.two_recruit_supert7:
            case R.id.two_recruit_supert8:
                intent = new Intent(getActivity(), AccountRecruitEduActivity.class);
                intent.putExtra("recruitItemBean", recruitItemBean);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void loadPersonalRecruitData() {
//        ToastUtils.showToast("loadPersonalRecruitData==>");
        RetrofitManager.create().LoadPersonalRecruitData(uidString)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<UserRecruitMessageBean>bindToLifecycle())
                .subscribe(new CommonObserver<UserRecruitMessageBean>(refreshLayout) {
                    @Override
                    public void onSuccess(UserRecruitMessageBean value) {
                        if (value.getCode() == Constant.CODE_SUCC) {
                            recruitItemBean = value.getReferer();
                            int jobInt=Integer.valueOf(recruitItemBean.getRecruit_workingyears());
                            int stateInt=Integer.valueOf(recruitItemBean.getRecruit_state());
                            int salaryInt=Integer.valueOf(recruitItemBean.getRecruit_salary());
                            int gradeInt = Integer.valueOf(recruitItemBean.getRecruit_grade());
                            String recruit_place = recruitItemBean.getRecruit_place();
                            String placeString = dbManager.getAreaNames(recruit_place);
                            String cityString = dbManager.getAreaNames(recruitItemBean.getRecruit_native());
                            two_recruit_supert.setLeftString(recruitItemBean.getRecruit_name());
                            two_recruit_supert.setLeftBottomString(jobItems[jobInt]);
                            two_recruit_supert.setRightString(cityString);
                            ImageLoaderUtils.displayUserPortraitImage(recruitItemBean.getRecruit_avatar(), two_recruit_supert.getLeftIconIV());
                            two_recruit_supert1.setLeftString(recruitItemBean.getRecruit_mobile());
                            two_recruit_supert2.setLeftString(recruitItemBean.getRecruit_email());
                            two_recruit_supert3.setLeftString(stateItems[stateInt]);
                            two_recruit_supert4.setCenterString(recruitItemBean.getRecruit_job());
                            two_recruit_supert5.setCenterString(placeString);
                            two_recruit_supert6.setCenterString(salaryItems[salaryInt]);
                            two_recruit_supert7.setCenterString(gradeItems[gradeInt]);
                            two_recruit_supert8.setCenterString(jobItems[jobInt]);
                            empty_container.setVisibility(View.GONE);
                            recruit_container.setVisibility(View.VISIBLE);
                        } else {
                            empty_container.setVisibility(View.VISIBLE);
                            recruit_container.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "网络异常,请稍后再试...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void showEditDialog(int position) {
        String title = "请填写求职职位";
        final EditText editText = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle(title)
                .setIcon(R.drawable.recruit_message)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        two_recruit_supert4.setCenterString(editText.getText().toString());
                    }
                }).setNegativeButton("取消", null).show();
    }

    public void showSelectDialog(int position) {
        String title = "请选择简历信息";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.recruit_message);
        builder.setTitle(title);
        String[] finalItems = salaryItems;
        if (position == 7) {
            finalItems = gradeItems;
        } else if (position == 8) {
            finalItems = jobItems;
        }
        builder.setSingleChoiceItems(finalItems, -1, (dialog, which) -> {
            if (position == 6) {
                two_recruit_supert6.setCenterString(salaryItems[which]);
            } else if (position == 7) {
                two_recruit_supert7.setCenterString(gradeItems[which]);
            } else {
                two_recruit_supert8.setCenterString(jobItems[which]);
            }
            dialog.dismiss();
        });
        builder.show();
    }

    private void getOptionData() {
        dbManager = new DBManager(getActivity());
        options1Items = dbManager.getAllProvince();
        options2Items = dbManager.getAllProvinceCities();
        cityCustomOptions = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String provinceStr = options1Items.get(options1).getName();
                // 设置在你组件上的 选取后得到的内容
                String cityStr = options2Items.get(options1).get(option2).getName();
                postRegion = options2Items.get(options1).get(option2).getCode();
                two_recruit_supert5.setCenterString(provinceStr + "-" + cityStr);
            }
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
}
