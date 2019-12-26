package com.caesar.rongcloudspeed.data;

public class TransferDetailUrl {

    private String id;
    private String payforid;
    private String payforname;
    private String paytoid;
    private String paytoname;
    private String payamount;
    private String payforsum;
    private String paytosum;
    private String paydatatime;
    private String dealdatatime;
    private int transferid;
    public boolean isSelf;

    public String getPayforid() {
        return payforid;
    }

    public void setPayforid(String payforid) {
        this.payforid = payforid;
    }

    public String getPayforname() {
        return payforname;
    }

    public void setPayforname(String payforname) {
        this.payforname = payforname;
    }

    public String getPaytoid() {
        return paytoid;
    }

    public void setPaytoid(String paytoid) {
        this.paytoid = paytoid;
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

    public String getPayforsum() {
        return payforsum;
    }

    public void setPayforsum(String payforsum) {
        this.payforsum = payforsum;
    }

    public String getPaytosum() {
        return paytosum;
    }

    public void setPaytosum(String paytosum) {
        this.paytosum = paytosum;
    }

    public String getPaydatatime() {
        return paydatatime;
    }

    public void setPaydatatime(String paydatatime) {
        this.paydatatime = paydatatime;
    }

    public String getDealdatatime() {
        return dealdatatime;
    }

    public void setDealdatatime(String dealdatatime) {
        this.dealdatatime = dealdatatime;
    }

    public int getTransferid() {
        return transferid;
    }

    public void setTransferid(int transferid) {
        this.transferid = transferid;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
