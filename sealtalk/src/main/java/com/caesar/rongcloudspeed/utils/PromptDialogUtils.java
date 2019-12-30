package com.caesar.rongcloudspeed.utils;

import android.app.Activity;

import me.leefeng.promptlibrary.Builder;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by meijun on 17-12-30.
 */

public class PromptDialogUtils {

    public static PromptDialog getPrompDialog(Activity context) {
        Builder builder = new Builder().withAnim(false);
        PromptDialog promptDialog = new PromptDialog(builder, context) {
            @Override
            public void dismiss() {
                this.dismissImmediately();
            }

            @Override
            public void showSuccess(String msg) {
                showSuccess(msg, false);
            }

            @Override
            public void showLoading(String msg) {

                showLoading(msg, false);
            }
        };
        return promptDialog;
    }
}
