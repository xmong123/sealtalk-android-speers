package com.caesar.rongcloudspeed.bean;

/**
 * Created by mac on 2018/4/11.
 */

public class EventBusBaseBean {
    /**
     *
     */
    int type;

    /**
     * 必须指定当前bean类的类型
     *
     * @param type
     */
    public EventBusBaseBean(int type) {
        this.type = type;
    }


    public static final class EventBusType {
        public static final int UPDATE_HEADER = 1;

    }
}
