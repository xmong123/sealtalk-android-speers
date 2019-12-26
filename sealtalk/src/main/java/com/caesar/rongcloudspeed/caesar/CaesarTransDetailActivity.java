package com.caesar.rongcloudspeed.caesar;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caesar.rongcloudspeed.data.TransferDetailUrl;
import com.caesar.rongcloudspeed.data.UserSumUrl;
import com.caesar.rongcloudspeed.data.result.TransferDetailResult;
import com.caesar.rongcloudspeed.data.result.UserSumResult;
import com.caesar.rongcloudspeed.extend.CaesarTransferAccountsMessage;
import com.caesar.rongcloudspeed.network.AppNetworkUtils;
import com.caesar.rongcloudspeed.network.NetworkCallback;
import com.caesar.rongcloudspeed.network.NetworkUtils;
import com.caesar.rongcloudspeed.utils.UserInfoUtils;
import com.jrmf360.rylib.R.color;
import com.jrmf360.rylib.R.drawable;
import com.jrmf360.rylib.R.id;
import com.jrmf360.rylib.R.layout;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.c.h;
import com.jrmf360.rylib.c.h.a;
import com.jrmf360.rylib.common.http.ModelHttpCallBack;
import com.jrmf360.rylib.common.model.BaseModel;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.j;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.extend.SendUser;
import com.jrmf360.rylib.rp.widget.ActionBarView;
import com.jrmf360.rylib.rp.widget.NoUnderClickableSpan;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.SendMessageCallback;
import io.rong.imlib.model.Conversation.ConversationType;

public class CaesarTransDetailActivity extends CaesarBaseActivity implements a {
    private ImageView iv_trans_state;
    private TextView tv_trans_state;
    private TextView tv_trans_money;
    private TextView tv_trans_tip;
    private TextView tv_trans_reback_tip;
    private TextView tv_trans_time;
    private TextView tv_collect_money_time;
    private LinearLayout ll_confirm_collect_money;
    private Button btn_trans_finish;
    private String mTransferOrderNo;
    private String transferReceiveUserId;
    private String transferSourceUserId;
    private h dialogFragment;
    private TransferDetailUrl transferDetUrl;
    private NoUnderClickableSpan peakTypeClick = new NoUnderClickableSpan() {
        public void onClick(View var1) {
            CaesarTransDetailActivity.this.showRebackMoneyDialog();
        }
    };

    public CaesarTransDetailActivity() {
    }

    public static void intent(Context var0, String var1, String var2, String var3) {
        Intent var4 = new Intent(var0, CaesarTransDetailActivity.class);
        Bundle var5 = new Bundle();
        var5.putString("transOrderNo", var1);
        var5.putString("transferReceiveUserId", var2);
        var5.putString("transferSourceUserId", var3);
        var4.putExtras(var5);
        var0.startActivity(var4);
    }

    public int getLayoutId() {
        return layout.jrmf_rp_activity_trans_detail;
    }

    public void initView() {
        this.actionBarView = (ActionBarView)this.findViewById(id.actionbar);
        this.tv_trans_state = (TextView)this.findViewById(id.tv_trans_state);
        this.tv_trans_money = (TextView)this.findViewById(id.tv_trans_money);
        this.tv_trans_tip = (TextView)this.findViewById(id.tv_trans_tip);
        this.tv_trans_reback_tip = (TextView)this.findViewById(id.tv_trans_reback_tip);
        this.tv_trans_time = (TextView)this.findViewById(id.tv_trans_time);
        this.tv_collect_money_time = (TextView)this.findViewById(id.tv_collect_money_time);
        this.iv_trans_state = (ImageView)this.findViewById(id.iv_trans_state);
        this.btn_trans_finish = (Button)this.findViewById(id.btn_trans_finish);
        this.ll_confirm_collect_money = (LinearLayout)this.findViewById(id.ll_confirm_collect_money);
    }

    public void initListener() {
        this.actionBarView.getIvBack().setOnClickListener(this);
        this.btn_trans_finish.setOnClickListener(this);
        String var1 = this.getString(string.jrmf_reback_tip);
        SpannableString var2 = new SpannableString(var1);
        int var3;
        if (j.c()) {
            var3 = var1.indexOf("立即退还");
        } else {
            var3 = var1.indexOf("Refund now");
        }

        var2.setSpan(this.peakTypeClick, var3, var1.length(), 33);
        var2.setSpan(new ForegroundColorSpan(this.getResources().getColor(color.title_bar_color)), var3, var1.length(), 33);
        this.tv_trans_reback_tip.setText(var2);
        this.tv_trans_reback_tip.setMovementMethod(LinkMovementMethod.getInstance());
        this.tv_trans_reback_tip.setHighlightColor(0);
    }

    protected void initData(Bundle var1) {
        if (var1 != null) {
            this.mTransferOrderNo = var1.getString("transOrderNo");
            this.transferReceiveUserId = var1.getString("transferReceiveUserId");
            this.transferSourceUserId = var1.getString("transferSourceUserId");
            this.getTransDetail(this.mTransferOrderNo);
        }

    }

    private void getTransDetail(String var1) {
//        com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.loading));
//        com.jrmf360.rylib.rp.http.a.a(this.context, CurrentUser.getUserId(), rongCloudToken, var1, new ModelHttpCallBack<k>() {
//            public void onSuccess(k var1) {
//                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransDetailActivity.this.context);
//                if (var1.isSuccess()) {
//                    CaesarTransDetailActivity.this.mTransDetailModel = var1;
//                    CaesarTransDetailActivity.this.showUI(var1);
//                } else {
//                    ToastUtil.showToast(CaesarTransDetailActivity.this.context, var1.respmsg);
//                }
//
//            }
//
//            public void onFail(String var1) {
//                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransDetailActivity.this.context);
//                ToastUtil.showToast(CaesarTransDetailActivity.this.context, CaesarTransDetailActivity.this.getString(string.net_error_l));
//            }
//        });
        String userID= UserInfoUtils.getAppUserId(CaesarTransDetailActivity.this);
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().getTransferPayDetailData(userID,var1),
                new NetworkCallback<TransferDetailResult>() {
                    @Override
                    public void onSuccess(TransferDetailResult transferDetailResult) {
                        if (transferDetailResult.getCode()==101) {
                            CaesarTransDetailActivity.this.transferDetUrl = transferDetailResult.getUrl();
                            CaesarTransDetailActivity.this.showUI(transferDetUrl);
                        } else {
                            ToastUtil.showToast(CaesarTransDetailActivity.this.context, transferDetailResult.getInfo());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(CaesarTransDetailActivity.this, "此交易异常，请稍后再试", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showUI(TransferDetailUrl var1) {
        if (var1.isSelf) {
            String var2 = CurrentUser.getNameById(this.transferReceiveUserId);
            if (q.a(var2) || var2.equals(String.format("user<%1$s>", this.transferReceiveUserId))) {
                var2 = var1.getPayforname();
            }

            String var3 = CurrentUser.getNameById(this.transferSourceUserId);
            if (q.a(var3) || var3.equals(String.format("user<%1$s>", this.transferSourceUserId))) {
                var3 = this.transferDetUrl.getPaytoname();
            }

            String var4;
            if (this.transferSourceUserId.equals(CurrentUser.getUserId())) {
                var4 = var2;
            } else {
                var4 = var3;
            }

            if (var1.getTransferid() == 0) {
                this.iv_trans_state.setImageDrawable(this.getResources().getDrawable(drawable.jrmf_ic_trans_wait));
                this.tv_trans_state.setText(String.format(this.getString(string.jrmf_trans_wait), q.o(var4)));
                this.tv_trans_tip.setText(this.getString(string.jrmf_trans_unconfirm_back));
                this.tv_trans_money.setText("￥" + var1.getPayamount());
                this.tv_trans_time.setText(String.format(this.getString(string.jrmf_trans_time), var1.getPaydatatime()));
                this.tv_collect_money_time.setVisibility(View.GONE);
            } else if (var1.getTransferid() == 1) {
                this.iv_trans_state.setImageDrawable(this.getResources().getDrawable(drawable.jrmf_ic_trans_succ));
                this.tv_trans_state.setText(String.format(this.getString(string.jrmf_trans_succ), q.o(var4)));
                this.tv_trans_money.setText("￥" + var1.getPayamount());
                this.tv_trans_time.setText(String.format(this.getString(string.jrmf_trans_time), var1.getPaydatatime()));
                this.tv_collect_money_time.setText(String.format(this.getString(string.jrmf_trans_receive_time), var1.getDealdatatime()));
            } else if (var1.getTransferid() == 2) {
                this.iv_trans_state.setImageDrawable(this.getResources().getDrawable(drawable.jrmf_ic_trans_reback));
                this.tv_trans_state.setText(String.format(this.getString(string.jrmf_trans_raback), q.o(var4)));
                this.tv_trans_money.setText("￥" + var1.getPayamount());
                this.tv_trans_tip.setText(this.getString(string.jrmf_trans_back_to_wallet));
                this.tv_trans_time.setText(String.format(this.getString(string.jrmf_trans_time), var1.getPaydatatime()));
                this.tv_collect_money_time.setText(String.format(this.getString(string.jrmf_trans_back_time), var1.getDealdatatime()));
            } else if (var1.getTransferid() == 3) {
                this.iv_trans_state.setImageDrawable(this.getResources().getDrawable(drawable.jrmf_ic_trans_fail));
                this.tv_trans_state.setText(this.getString(string.jrmf_had_back_money_timeout));
                this.tv_trans_money.setText("￥" + var1.getPayamount());
                this.tv_trans_tip.setText(this.getString(string.jrmf_trans_back_to_wallet));
                this.tv_trans_time.setText(String.format(this.getString(string.jrmf_trans_time), var1.getPaydatatime()));
                this.tv_collect_money_time.setText(String.format(this.getString(string.jrmf_trans_back_time), var1.getDealdatatime()));
            }
        } else {
            if (var1.getTransferid() == 0) {
                this.iv_trans_state.setImageDrawable(this.getResources().getDrawable(drawable.jrmf_ic_trans_wait));
                this.tv_trans_state.setText(this.getString(string.jrmf_trans_waiting_receive));
                this.tv_trans_money.setText("￥" + var1.getPayamount());
                this.ll_confirm_collect_money.setVisibility(View.VISIBLE);
                this.tv_trans_time.setText(String.format(this.getString(string.jrmf_trans_time), var1.getPaydatatime()));
                this.tv_collect_money_time.setVisibility(View.GONE);
            }

            if (var1.getTransferid() == 1) {
                this.iv_trans_state.setImageDrawable(this.getResources().getDrawable(drawable.jrmf_ic_trans_succ));
                this.tv_trans_state.setText(this.getString(string.jrmf_had_receive_money));
                this.tv_trans_money.setText("￥" + var1.getPayamount());
                this.tv_trans_tip.setText(this.getString(string.jrmf_trans_save_to_wallet));
                this.tv_trans_time.setText(String.format(this.getString(string.jrmf_trans_time), var1.getPaydatatime()));
                this.tv_collect_money_time.setText(String.format(this.getString(string.jrmf_trans_receive_time), var1.getDealdatatime()));
            }

            if (var1.getTransferid() == 2 || var1.getTransferid() == 3) {
                this.iv_trans_state.setImageDrawable(this.getResources().getDrawable(drawable.jrmf_ic_trans_reback));
                this.tv_trans_state.setText(this.getString(string.jrmf_had_back_money));
                this.tv_trans_money.setText("￥" + var1.getPayamount());
                this.tv_trans_time.setText(String.format(this.getString(string.jrmf_trans_time), var1.getPaydatatime()));
                this.tv_collect_money_time.setText(String.format(this.getString(string.jrmf_trans_back_time), var1.getDealdatatime()));
            }
        }

    }

    public void onClick(int var1) {
        if (var1 == id.iv_back) {
            this.finish();
        } else if (var1 == id.btn_trans_finish) {
            this.transReceiveMoney();
        }

    }

    private void transReceiveMoney() {
        com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.loading));
//        com.jrmf360.rylib.rp.http.a.b(this.context, CurrentUser.getUserId(), rongCloudToken, this.mTransferOrderNo, new ModelHttpCallBack<BaseModel>() {
//            public void onSuccess(BaseModel var1) {
//                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransDetailActivity.this.context);
//                if (var1.isSuccess()) {
//                    CaesarTransDetailActivity.this.sendTransBaceAndReceiveMessage("1");
//                    CaesarTransDetailActivity.this.finish();
//                } else {
//                    ToastUtil.showToast(CaesarTransDetailActivity.this.context, var1.respmsg);
//                }
//
//            }
//
//            public void onFail(String var1) {
//                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransDetailActivity.this.context);
//                ToastUtil.showToast(CaesarTransDetailActivity.this.context, CaesarTransDetailActivity.this.getString(string.net_error_l));
//            }
//        });
        String pay_id=this.mTransferOrderNo;
        String sum_foruserID=UserInfoUtils.getAppUserId(this);
        String user_sum_add=this.transferDetUrl.getPayamount();
        NetworkUtils.fetchInfo(AppNetworkUtils.initRetrofitApi().confirmCodeToUserName(pay_id,sum_foruserID,user_sum_add),
                new NetworkCallback<UserSumResult>() {
                    @Override
                    public void onSuccess(UserSumResult userSumResult) {
                        com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransDetailActivity.this.context);
                        UserSumUrl userSumUrl=userSumResult.getUrl();
                        String sumString=String.valueOf(userSumUrl.getUser_sum());
                        UserInfoUtils.setUserSum(sumString,CaesarTransDetailActivity.this);
                        CaesarTransDetailActivity.this.sendTransBaceAndReceiveMessage("1");
                        CaesarTransDetailActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransDetailActivity.this.context);
                        Toast.makeText(CaesarTransDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showRebackMoneyDialog() {
        String var1 = CurrentUser.getNameById(this.transferSourceUserId);
        if (q.a(var1) || var1.equals(String.format("user<%1$s>", this.transferSourceUserId))) {
            var1 = this.transferDetUrl.getPaytoname();
        }

        this.dialogFragment = com.jrmf360.rylib.c.a.getInstance().dialogLeftAndRight(this.context, String.format(this.getString(string.jrmf_reback_dialog), var1), this.getString(string.jrmf_quit), this.getString(string.jrmf_confirm), this);
        this.dialogFragment.show(this.getSupportFragmentManager(), this.getClass().getSimpleName());
    }

    public void onLeft() {
        if (this.dialogFragment != null) {
            this.dialogFragment.dismiss();
        }

    }

    public void onRight() {
        this.transBack();
    }

    private void transBack() {
        com.jrmf360.rylib.c.a.getInstance().dialogLoading(this.context, this.getString(string.loading));
        com.jrmf360.rylib.rp.http.a.c(this.context, CurrentUser.getUserId(), rongCloudToken, this.mTransferOrderNo, new ModelHttpCallBack<BaseModel>() {
            public void onSuccess(BaseModel var1) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransDetailActivity.this.context);
                if (var1.isSuccess()) {
                    CaesarTransDetailActivity.this.sendTransBaceAndReceiveMessage("2");
                    CaesarTransDetailActivity.this.finish();
                } else {
                    ToastUtil.showToast(CaesarTransDetailActivity.this.context, var1.respmsg);
                }

            }

            public void onFail(String var1) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(CaesarTransDetailActivity.this.context);
                ToastUtil.showToast(CaesarTransDetailActivity.this.context, CaesarTransDetailActivity.this.getString(string.net_error_l));
            }
        });
    }

    private void sendTransBaceAndReceiveMessage(String var1) {
        String var2 = this.transferDetUrl.getPayamount();
        String var3 = SendUser.sendUserId;
        String var4 = CurrentUser.getUserId();
        String var5 = "1".equals(var1) ? this.getString(string.jrmf_had_receive_money) : this.getString(string.jrmf_had_back_money);
        CaesarTransferAccountsMessage var6 = CaesarTransferAccountsMessage.obtain(this.mTransferOrderNo, var2, var3, var4, var5, var1);
        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance().getRongIMClient().sendMessage(ConversationType.PRIVATE, var3, var6, this.getString(string.jrmf_receive_message_tip), (String)null, new SendMessageCallback() {
                public void onError(Integer var1, ErrorCode var2) {
                    LogUtil.e("发送转账消息失败:" + var2.toString());
                }

                public void onSuccess(Integer var1) {
                }
            });
            this.finish();
        }

    }
}

