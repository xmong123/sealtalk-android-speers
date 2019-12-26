package com.caesar.rongcloudspeed.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 43053 on 2016/8/4.
 */
public class ActivityStack {

    public static List<Activity> activities;

    public static void push(Activity appCompatActivity) {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        activities.add(appCompatActivity);
    }

    public static void pop(Activity appCompatActivity) {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        appCompatActivity.finish();
        activities.remove(appCompatActivity);
        appCompatActivity = null;
    }

    public static void finishAll() {
        if (activities == null) {
            activities = new ArrayList<>();
        }

        for (Activity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 获取第一个Activity
     */
    public static Activity getFirstActivity() {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        if (activities.size() == 0) {
            return null;
        } else {
            return activities.get(0);
        }
    }

    /**
     * 获取栈顶Activity
     */
    public static Activity getTopActivity() {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        if (activities.size() == 0) {
            return null;
        } else {
            return activities.get(activities.size() - 1);
        }
    }
}
