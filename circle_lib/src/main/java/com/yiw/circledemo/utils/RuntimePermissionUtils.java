package com.yiw.circledemo.utils;

import android.content.Context;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Created by 43053 on 2016/6/21.
 */
public class RuntimePermissionUtils {

    public void getPermission(Context context, final PermissionCallback callback, String... permission) {
/*        RxPermissions.getInstance(context.getApplicationContext())
                .request(permission)
                .subscribe(granted -> {
                    if (callback != null){
                        callback.onPermissionCallback(granted);
                    }
                });*/

        RxPermissions.getInstance(context.getApplicationContext())
                .request(permission)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (callback != null) {
                            callback.onPermissionCallback(granted);
                        }
                    }
                });
    }
}
