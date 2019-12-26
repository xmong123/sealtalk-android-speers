package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class FenqiBean extends CommonResonseBean {
    private List<Info> infoData;

    public List<Info> getInfoData() {
        return infoData;
    }

    public void setInfoData(List<Info> infoData) {
        this.infoData = infoData;
    }

    public static class Info {
        String time;
        String money;
        String pay_type;

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMoney_day() {
            return money;
        }

        public void setMoney_day(String money_day) {
            this.money = money_day;
        }
    }
}
