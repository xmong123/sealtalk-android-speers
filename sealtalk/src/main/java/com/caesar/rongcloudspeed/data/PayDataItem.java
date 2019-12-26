package com.caesar.rongcloudspeed.data;

public class PayDataItem {

    private String payforname;

    public String getPayforname() {
        return payforname;
    }

    public void setPayforname(String payforname) {
        this.payforname = payforname;
    }

    public String getPaytoname() {
        return paytoname;
    }

    public void setPaytoname(String paytoname) {
        this.paytoname = paytoname;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getPaydatatime() {
        return paydatatime;
    }

    public void setPaydatatime(String paydatatime) {
        this.paydatatime = paydatatime;
    }

    private String paytoname;
    private String payamount;
    private String paydatatime;

}
