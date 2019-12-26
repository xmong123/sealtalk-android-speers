package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.id;
import com.jrmf360.rylib.R.layout;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.a.b;
import com.jrmf360.rylib.a.d;
import com.jrmf360.rylib.common.http.ModelHttpCallBack;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.f;
import com.jrmf360.rylib.common.util.h;
import com.jrmf360.rylib.common.util.j;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.a.a;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.extend.SendUser;
import com.jrmf360.rylib.rp.http.model.RpInfoModel;
import com.jrmf360.rylib.rp.http.model.RpInfoModel.RpItemModel;
import com.jrmf360.rylib.rp.ui.MyRpActivity;
import com.jrmf360.rylib.rp.widget.ActionBarView;
import java.util.ArrayList;
import java.util.List;

public class CaesarRpDetailActivity extends CaesarBaseActivity implements OnScrollListener {
    private ListView listView;
    private ImageView iv_avatar;
    private TextView tv_look_rp_history;
    private TextView tv_username;
    private TextView tv_bless;
    private TextView tv_rec_amount;
    private TextView tv_rp_num;
    private CaesarRpDetailActivity.RpDetailAdapter rpDetailAdapter;
    private int mListViewHeight;
    private int page = 2;
    private int pageCount;
    private boolean isScroll = false;
    private a buttomBean;
    private List<Object> rpItemModelList;
    private String userId;
    private String thirdToken;
    private String rpId;
    private String userName;
    private String userIcon;
    private int hasLeft;
    private boolean loadingDataByPage = false;

    public CaesarRpDetailActivity() {
    }

    public static void intent(Context var0, int var1, RpInfoModel var2, String var3, String var4, String var5) {
        Intent var6 = new Intent(var0, CaesarRpDetailActivity.class);
        Bundle var7 = new Bundle();
        var7.putInt("fromKey", var1);
        var7.putString("userId", var3);
        var7.putString("thirdToken", var4);
        var7.putString("rpId", var5);
        var7.putSerializable("rpInfoModel", var2);
        var6.putExtras(var7);
        var0.startActivity(var6);
    }

    public static void intent(Activity var0, int var1, String var2, String var3, String var4, String var5, String var6) {
        Intent var7 = new Intent(var0, CaesarRpDetailActivity.class);
        Bundle var8 = new Bundle();
        var8.putInt("fromKey", var1);
        var8.putString("userId", var2);
        var8.putString("thirdToken", var3);
        var8.putString("rpId", var4);
        var8.putString("userName", var5);
        var8.putString("userIcon", var6);
        var7.putExtras(var8);
        var0.startActivity(var7);
    }

    public static void intent(Context var0, int var1, String var2, String var3, String var4, String var5, String var6) {
        Intent var7 = new Intent(var0, CaesarRpDetailActivity.class);
        Bundle var8 = new Bundle();
        var8.putInt("fromKey", var1);
        var8.putString("userId", var2);
        var8.putString("thirdToken", var3);
        var8.putString("rpId", var4);
        var8.putString("userName", var5);
        var8.putString("userIcon", var6);
        var7.putExtras(var8);
        var0.startActivity(var7);
    }

    public int getLayoutId() {
        return layout._activity_rp_detail;
    }

    public void initView() {
        this.actionBarView = (ActionBarView)this.findViewById(id.actionbar);
        this.listView = (ListView)this.findViewById(id.listView);
        this.tv_look_rp_history = (TextView)this.findViewById(id.tv_look_rp_history);
        View var1 = View.inflate(this, layout._header_rp_detail, (ViewGroup)null);
        this.iv_avatar = (ImageView)var1.findViewById(id.iv_avatar);
        this.tv_username = (TextView)var1.findViewById(id.tv_username);
        this.tv_bless = (TextView)var1.findViewById(id.tv_bless);
        this.tv_rec_amount = (TextView)var1.findViewById(id.tv_rec_amount);
        this.tv_rp_num = (TextView)var1.findViewById(id.tv_rp_num);
        this.listView.addHeaderView(var1);
    }

    public void initListener() {
        this.actionBarView.getIvBack().setOnClickListener(this);
        this.tv_look_rp_history.setOnClickListener(this);
        this.listView.setOnScrollListener(this);
        this.listView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressLint({"NewApi"})
            public void onGlobalLayout() {
                CaesarRpDetailActivity.this.mListViewHeight = CaesarRpDetailActivity.this.listView.getHeight();
                if (VERSION.SDK_INT > 16) {
                    CaesarRpDetailActivity.this.listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    CaesarRpDetailActivity.this.listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

            }
        });
    }

    protected void initData(Bundle var1) {
        this.actionBarView.setTitle(this.getString(string.jrmf_send_rp_title));
        if (var1 != null) {
            int var2 = var1.getInt("fromKey");
            this.userId = var1.getString("userId");
            this.thirdToken = var1.getString("thirdToken");
            this.rpId = var1.getString("rpId");
            this.rpItemModelList = new ArrayList();
            this.rpDetailAdapter = new CaesarRpDetailActivity.RpDetailAdapter(this, this.rpItemModelList);
            this.listView.setAdapter(this.rpDetailAdapter);
            if (var2 == 0) {
                this.showData(var1);
            } else {
                this.userName = var1.getString("userName");
                this.userIcon = var1.getString("userIcon");
                this.loadRpDetail(this.userId, this.thirdToken, this.rpId, this.userName, this.userIcon);
            }
        }

    }

    private void showData(Bundle var1) {
        RpInfoModel var2 = (RpInfoModel)var1.getSerializable("rpInfoModel");
        this.hasLeft = var2.hasLeft;
        String var3 = CurrentUser.getNameById(SendUser.sendUserId);
        if (q.a(var3)) {
            this.tv_username.setText(String.format(this.getString(string.who_rp), var2.username));
        } else {
            this.tv_username.setText(String.format(this.getString(string.who_rp), var3));
        }

        if (q.b(CurrentUser.getUserIconById(SendUser.sendUserId))) {
            h.a().a(this.iv_avatar, CurrentUser.getUserIconById(SendUser.sendUserId));
        } else if (q.b(var2.avatar)) {
            h.a().a(this.iv_avatar, var2.avatar);
        }

        this.tv_bless.setText(var2.content);
        if (var2.type == 1) {
            f.a(this.context, this.tv_username, drawable._ic_pin, true);
        } else {
            f.a(this.context, this.tv_username, drawable._ic_pin, false);
        }

        if (q.c(var2.grabMoney) && q.i(var2.grabMoney) > 0.0D) {
            SpannableString var4 = new SpannableString(String.format(this.getString(string.jrmf_money_yuan), var2.grabMoney));
            int var5 = var2.grabMoney.length();
            var4.setSpan(new AbsoluteSizeSpan(100), 0, var5, 18);
            this.tv_rec_amount.setText(var4);
        } else {
            this.tv_rec_amount.setVisibility(View.GONE);
        }

        if (var2.isSelf == 1) {
            if (var2.hasLeft == 0) {
                this.tv_rp_num.setText(String.format(this.getString(string.self_rp_no_left), var2.receTotal, var2.total));
            } else {
                this.tv_rp_num.setText(String.format(this.getString(string.self_rp_has_left), var2.receTotal, var2.total, var2.recTotalMoney, var2.totalMoney));
            }
        } else if (var2.hasLeft == 0) {
            this.tv_rp_num.setText(String.format(this.getString(string.other_rp_no_left), var2.receTotal, var2.total));
        } else {
            this.tv_rp_num.setText(String.format(this.getString(string.other_rp_has_left), var2.receTotal, var2.total));
        }

        if (var2.receiveHistory != null && var2.receiveHistory.size() > 0) {
            this.rpItemModelList.addAll(var2.receiveHistory);
            this.buttomBean = new a();
            if (var2.receiveHistory.size() >= 10) {
                this.buttomBean.a = true;
                this.pageCount = 2;
            } else {
                this.buttomBean.a = false;
            }

            this.rpItemModelList.add(this.buttomBean);
        }

        this.rpDetailAdapter.notifyDataSetChanged();
    }

    private void showHeader(RpInfoModel var1) {
        if (q.b(CurrentUser.getUserIconById(SendUser.sendUserId))) {
            h.a().a(this.iv_avatar, CurrentUser.getUserIconById(SendUser.sendUserId));
        } else if (q.b(var1.avatar)) {
            h.a().a(this.iv_avatar, var1.avatar);
        }

        String var2 = CurrentUser.getNameById(SendUser.sendUserId);
        if (q.a(var2)) {
            this.tv_username.setText(String.format(this.getString(string.who_rp), var1.username));
        } else {
            this.tv_username.setText(String.format(this.getString(string.who_rp), var2));
        }

        this.tv_bless.setText(var1.content);
        if (var1.type == 1) {
            f.a(this.context, this.tv_username, drawable._ic_pin, true);
        } else {
            f.a(this.context, this.tv_username, drawable._ic_pin, false);
        }

        if (var1.envelopeStatus == 4) {
        }

        if (q.c(var1.grabMoney) && q.i(var1.grabMoney) > 0.0D) {
            SpannableString var3 = new SpannableString(String.format(this.getString(string.jrmf_money_yuan), var1.grabMoney));
            int var4 = j.b() ? var3.length() - 3 : var3.length() - 1;
            var3.setSpan(new AbsoluteSizeSpan(100), 0, var4, 18);
            this.tv_rec_amount.setText(var3);
        } else {
            this.tv_rec_amount.setVisibility(View.GONE);
        }

        if (var1.isSelf == 1) {
            if (var1.hasLeft == 0) {
                this.tv_rp_num.setText(String.format(this.getString(string.self_rp_no_left), var1.receTotal, var1.total));
            } else {
                this.tv_rp_num.setText(String.format(this.getString(string.self_rp_has_left), var1.receTotal, var1.total, q.h(var1.recTotalMoney), var1.totalMoney));
            }
        } else if (var1.hasLeft == 0) {
            this.tv_rp_num.setText(String.format(this.getString(string.other_rp_no_left), var1.receTotal, var1.total));
        } else {
            this.tv_rp_num.setText(String.format(this.getString(string.other_rp_has_left), var1.receTotal, var1.total));
        }

    }

    private void loadRpDetail(String var1, String var2, String var3, String var4, String var5) {
        com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.loading));
        com.jrmf360.rylib.rp.http.a.c(var1, var2, var3, var4, var5, new ModelHttpCallBack<RpInfoModel>() {
            public void onSuccess(RpInfoModel var1) {
                if (!CaesarRpDetailActivity.this.context.isFinishing()) {
                    com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarRpDetailActivity.this.context);
                    if (var1 == null) {
                        ToastUtil.showToast(CaesarRpDetailActivity.this.context, CaesarRpDetailActivity.this.getString(string.network_error));
                    } else {
                        if (var1.isSuccess()) {
                            CaesarRpDetailActivity.this.hasLeft = var1.hasLeft;
                            CaesarRpDetailActivity.this.showHeader(var1);
                            if (var1.receiveHistory != null && var1.receiveHistory.size() > 0) {
                                CaesarRpDetailActivity.this.rpItemModelList.addAll(var1.receiveHistory);
                                CaesarRpDetailActivity.this.buttomBean = new a();
                                if (var1.receiveHistory.size() > 9) {
                                    CaesarRpDetailActivity.this.buttomBean.a = true;
                                    CaesarRpDetailActivity.this.pageCount = 2;
                                } else {
                                    CaesarRpDetailActivity.this.buttomBean.a = false;
                                }

                                CaesarRpDetailActivity.this.rpItemModelList.add(CaesarRpDetailActivity.this.buttomBean);
                            }

                            CaesarRpDetailActivity.this.rpDetailAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }

            public void onFail(String var1) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarRpDetailActivity.this.context);
                ToastUtil.showToast(CaesarRpDetailActivity.this.context, CaesarRpDetailActivity.this.getString(string.network_error));
            }
        });
    }

    private void loadNextPage() {
        this.loadingDataByPage = true;
        com.jrmf360.rylib.rp.http.a.a(this.userId, this.thirdToken, this.rpId, this.userName, this.usericon, this.page, new ModelHttpCallBack<RpInfoModel>() {
            public void onSuccess(RpInfoModel var1) {
                if (!CaesarRpDetailActivity.this.context.isFinishing()) {
                    if (var1 == null) {
                        CaesarRpDetailActivity.this.loadingDataByPage = false;
                        ToastUtil.showToast(CaesarRpDetailActivity.this.context, CaesarRpDetailActivity.this.getString(string.net_error_l));
                    } else {
                        if (var1.isSuccess()) {
                            CaesarRpDetailActivity.this.pageCount = var1.pageCount;
                            CaesarRpDetailActivity.this.page++;
                            if (var1.receiveHistory != null && var1.receiveHistory.size() > 0) {
                                if (var1.receiveHistory.size() >= 10) {
                                    CaesarRpDetailActivity.this.buttomBean.a = true;
                                } else {
                                    CaesarRpDetailActivity.this.buttomBean.a = false;
                                }

                                CaesarRpDetailActivity.this.rpItemModelList.addAll(CaesarRpDetailActivity.this.rpItemModelList.size() - 1, var1.receiveHistory);
                                CaesarRpDetailActivity.this.rpDetailAdapter.notifyDataSetChanged();
                            } else if (CaesarRpDetailActivity.this.buttomBean != null) {
                                CaesarRpDetailActivity.this.buttomBean.a = false;
                                CaesarRpDetailActivity.this.rpDetailAdapter.notifyDataSetChanged();
                            }
                        }

                        CaesarRpDetailActivity.this.loadingDataByPage = false;
                    }
                }
            }

            public void onFail(String var1) {
                ToastUtil.showToast(CaesarRpDetailActivity.this.context, CaesarRpDetailActivity.this.getString(string.net_error_l));
                CaesarRpDetailActivity.this.loadingDataByPage = false;
            }
        });
    }

    public void onClick(int var1) {
        if (var1 == id.iv_back) {
            this.finish();
        } else if (var1 == id.tv_look_rp_history) {
            MyRpActivity.intent(this, this.userId, this.thirdToken);
        }

    }

    public void onScrollStateChanged(AbsListView var1, int var2) {
        if (var2 == 0) {
            this.isScroll = false;
        } else {
            this.isScroll = true;
        }

    }

    public void onScroll(AbsListView var1, int var2, int var3, int var4) {
        if (var2 + var3 == var4 && this.isScroll) {
            View var5 = this.listView.getChildAt(this.listView.getChildCount() - 1);
            if (var5 != null && var5.getBottom() == this.mListViewHeight) {
                if (this.page <= this.pageCount) {
                    if (!this.loadingDataByPage) {
                        this.loadNextPage();
                    }
                } else if (this.buttomBean != null && this.buttomBean.a) {
                    this.buttomBean.a = false;
                    this.rpDetailAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    class RpDetailAdapter extends b<Object> {
        public RpDetailAdapter(Context var2, List<Object> var3) {
            super(var2, var3);
        }

        public int getItemViewType(int var1) {
            return CaesarRpDetailActivity.this.rpItemModelList.get(var1) instanceof a ? 1 : 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public View getView(int var1, View var2, ViewGroup var3) {
            int var4 = this.getItemViewType(var1);
            TextView var8;
            if (var4 == 0) {
                RpItemModel var11 = (RpItemModel)CaesarRpDetailActivity.this.rpItemModelList.get(var1);
                d var12 = d.a(CaesarRpDetailActivity.this.context, var2, var3, layout._item_rp_detail, var1);
                TextView var13 = (TextView)var12.a(id.tv_name);
                var8 = (TextView)var12.a(id.tv_time);
                TextView var14 = (TextView)var12.a(id.tv_amount);
                TextView var10 = (TextView)var12.a(id.tv_best);
                var13.setText(var11.nickname);
                var8.setText(var11.activateTime);
                var14.setText(String.format(CaesarRpDetailActivity.this.getString(string.jrmf_money_yuan3), var11.moneyYuan));
                if (var11.isBLuck == 1) {
                    if (CaesarRpDetailActivity.this.hasLeft != 0) {
                        var10.setVisibility(View.INVISIBLE);
                    } else {
                        var10.setVisibility(View.VISIBLE);
                    }
                } else {
                    var10.setVisibility(View.INVISIBLE);
                }

                return var12.a();
            } else {
                d var5 = d.a(CaesarRpDetailActivity.this.context, var2, var3, layout._item_list_buttom, var1);
                a var6 = (a)CaesarRpDetailActivity.this.rpItemModelList.get(var1);
                ImageView var7 = (ImageView)var5.a(id.imageview_progress_spinner);
                var8 = (TextView)var5.a(id.tv_title);
                if (var6.a) {
                    var7.setVisibility(View.VISIBLE);
                    var8.setText(CaesarRpDetailActivity.this.getString(string.loading));
                    AnimationDrawable var9 = (AnimationDrawable)var7.getDrawable();
                    var9.start();
                } else {
                    var7.setVisibility(View.GONE);
                    if (CaesarRpDetailActivity.this.rpItemModelList != null && CaesarRpDetailActivity.this.rpItemModelList.size() >= 10) {
                        var8.setText(CaesarRpDetailActivity.this.getString(string.to_buttom));
                    } else {
                        var8.setText("");
                    }
                }

                return var5.a();
            }
        }
    }
}

