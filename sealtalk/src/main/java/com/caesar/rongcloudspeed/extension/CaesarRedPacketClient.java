package com.caesar.rongcloudspeed.extension;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.jrmf360.rylib.R.string;
import com.jrmf360.rylib.common.http.ModelHttpCallBack;
import com.jrmf360.rylib.common.util.LogUtil;
import com.jrmf360.rylib.common.util.ToastUtil;
import com.jrmf360.rylib.common.util.q;
import com.jrmf360.rylib.rp.extend.CurrentUser;
import com.jrmf360.rylib.rp.http.model.RpInfoModel;
import com.jrmf360.rylib.rp.ui.BaseActivity;
import com.jrmf360.rylib.rp.ui.PayTypeActivity;
import com.jrmf360.rylib.rp.ui.RpDetailActivity;
import com.jrmf360.rylib.rp.ui.TransPayActivity;
import com.jrmf360.rylib.wallet.c.a;
import com.jrmf360.rylib.wallet.ui.MyWalletActivity;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CaesarRedPacketClient {
    private static String mWxAppid = "";
    private static String JRMF_BASE_URL = "https://api2.jrmf360.com/";
    private static CaesarRedPacketClient.LoadListener mloadListener = new CaesarRedPacketClient.LoadListener();
    private static ArrayList<String> redPacketIds = new ArrayList();

    public CaesarRedPacketClient() {
    }

    public static void isDebug(boolean var0) {
        if (var0) {
            JRMF_BASE_URL = "https://api-pre.jrmf360.com/";
        } else {
            JRMF_BASE_URL = "https://api2.jrmf360.com/";
        }

    }

    public static String getBaseUrl() {
        return JRMF_BASE_URL;
    }

    public static void setWxAppid(String var0) {
        mWxAppid = var0;
    }

    public static String getWxAppId() {
        if (q.a(mWxAppid)) {
            throw new IllegalArgumentException("wxappid为空");
        } else {
            return mWxAppid;
        }
    }

    public static void jrmfRpWxPaySucd() {
        PayTypeActivity var0 = (PayTypeActivity)a.a().a(PayTypeActivity.class);
        if (var0 != null) {
            var0.finishWithResult();
        }

    }

    public static void jrmfTransWxPaySucd() {
        TransPayActivity var0 = (TransPayActivity)a.a().a(TransPayActivity.class);
        if (var0 != null) {
            var0.enterFinishActivity();
        }

    }

    public static String updateUserInfo(String var0, String var1, String var2) {
        String var3 = com.jrmf360.rylib.rp.http.a.d(var0, var1, var2);
        return var3;
    }

    public static void intentWallet(final Activity var0) {
        if (q.a(BaseActivity.rongCloudToken)) {
            if (RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTED)) {
                com.jrmf360.rylib.c.a.getInstance().dialogLoading(var0, var0.getString(string.loading));
                RongIMClient.getInstance().getVendorToken(new ResultCallback<String>() {
                    public void onSuccess(String var1) {
                        com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(var0);
                        LogUtil.e("getVendorToken", Thread.currentThread().getName());

                        try {
                            if (q.c(var1)) {
                                BaseActivity.rongCloudToken = URLEncoder.encode(var1, "UTF-8");
                            }
                        } catch (Exception var3) {
                            var3.printStackTrace();
                        }

                        var0.runOnUiThread(new Runnable() {
                            public void run() {
                                MyWalletActivity.intent(var0, CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon());
                            }
                        });
                    }

                    public void onError(ErrorCode var1) {
                        com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(var0);
                        LogUtil.i("获得token失败" + var1);
                        var0.runOnUiThread(new Runnable() {
                            public void run() {
                                MyWalletActivity.intent(var0, CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon());
                            }
                        });
                    }
                });
            } else {
                MyWalletActivity.intent(var0, CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon());
            }
        } else {
            MyWalletActivity.intent(var0, CurrentUser.getUserId(), BaseActivity.rongCloudToken, CurrentUser.getName(), CurrentUser.getUserIcon());
        }

    }

    public static void openGroupRp(Activity var0, String var1, String var2, String var3, String var4, String var5) {
        openRedPacket(var0, 0, var1, var2, var3, var4, var5);
    }

    public static void openSingleRp(Activity var0, String var1, String var2, String var3, String var4, String var5) {
        openRedPacket(var0, 1, var1, var2, var3, var4, var5);
    }

    private static void openRedPacket(final Activity var0, final int var1, final String var2, final String var3, String var4, String var5, final String var6) {
        com.jrmf360.rylib.c.a.getInstance().dialogLoading(var0, var0.getString(string.waiting), mloadListener);
        redPacketIds.clear();
        redPacketIds.add(var6);
        com.jrmf360.rylib.rp.http.a.a(var1, var2, var3, var6, var4, var5, new ModelHttpCallBack<RpInfoModel>() {
            public void onSuccess(RpInfoModel var1x) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(var0);
                if (!var0.isFinishing()) {
                    try {
                        if (CaesarRedPacketClient.redPacketIds.size() <= 0 || !var6.equals(CaesarRedPacketClient.redPacketIds.get(0))) {
                            return;
                        }
                    } catch (Exception var5) {
                        LogUtil.e("Changed RedPacket");
                        return;
                    }

                    if (var1x.isSuccess()) {
                        CaesarRedPacketClient.redPacketIds.clear();
                        if (var1x.envelopeStatus != 4 && var1x.envelopeStatus != 1) {
                            com.jrmf360.rylib.rp.b.a var2x = new com.jrmf360.rylib.rp.b.a();
                            Bundle var3x = new Bundle();
                            var3x.putSerializable("rpInfoModel", var1x);
                            var3x.putString("userId", var2);
                            var3x.putString("thirdToken", var3);
                            var3x.putString("envelopeId", var6);
                            var3x.putInt("key", var1);
                            var2x.setArguments(var3x);
                            FragmentTransaction var4 = var0.getFragmentManager().beginTransaction();
                            var4.add(var2x, "open_rp");
                            var4.commitAllowingStateLoss();
                        } else {
                            RpDetailActivity.intent(var0, 0, var1x, var2, var3, var6);
                        }
                    } else {
                        com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(var0);
                        ToastUtil.showToast(var0, var1x.respmsg);
                    }

                }
            }

            public void onFail(String var1x) {
                com.jrmf360.rylib.c.a.getInstance().dialogCloseLoading(var0);
                ToastUtil.showToast(var0, var0.getString(string.network_error));
            }
        });
    }

    private static class LoadListener implements com.jrmf360.rylib.common.a.c.a {
        private LoadListener() {
        }

        public void onCancel() {
            CaesarRedPacketClient.redPacketIds.clear();
        }
    }
}

