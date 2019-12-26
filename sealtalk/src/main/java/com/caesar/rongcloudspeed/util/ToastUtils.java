package com.caesar.rongcloudspeed.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 43053 on 2016/6/15.
 */
public class ToastUtils {

    private static Toast toast;

    public ToastUtils(Context context){
        if (toast == null){
            synchronized (Toast.class){
                if (toast == null){
                    toast = Toast.makeText(context.getApplicationContext(),"",Toast.LENGTH_SHORT);
                }
            }
        }
    }

    public void show(String message){
        if (toast == null){
            return;
        }
        toast.setText(message);
        toast.show();
    }

}
