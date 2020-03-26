package com.caesar.rongcloudspeed.bean;

/**
 * Created by mac on 2018/4/5.
 */

public class PersonalInvoiceBean extends CommonResonseBean {
    /**
     * result : suc
     * msg : 获取成功
     * code : 101
     * quota_present : 0.00
     * info : [{"id":"2","img":"Public/Uploads/5abc9216607d4.png","goods_name":"Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机","money":"7788.00"},{"id":"1","img":"Public/Uploads/5abc8eb10ec51.png","goods_name":"Apple iPhone 8 Plus (A1864) 64GB 金色 移动联通电信4G手机","money":"6688.00"}]
     */
    private String state;

    private InvoiceBean url;

    private InvoiceBean referer;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public InvoiceBean getUrl() {
        return url;
    }

    public void setUrl(InvoiceBean url) {
        this.url = url;
    }

    public InvoiceBean getReferer() {
        return referer;
    }

    public void setReferer(InvoiceBean referer) {
        this.referer = referer;
    }

    public static class  InvoiceBean {
        private String invoice_id;
        private String user_id;
        private String invoice_name;
        private String invoice_identifi;
        private String invoice_address;
        private String invoice_phone;
        private String invoice_bank;
        private String invoice_account;

        public String getInvoice_id() {
            return invoice_id;
        }

        public void setInvoice_id(String invoice_id) {
            this.invoice_id = invoice_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getInvoice_name() {
            return invoice_name;
        }

        public void setInvoice_name(String invoice_name) {
            this.invoice_name = invoice_name;
        }

        public String getInvoice_identifi() {
            return invoice_identifi;
        }

        public void setInvoice_identifi(String invoice_identifi) {
            this.invoice_identifi = invoice_identifi;
        }

        public String getInvoice_address() {
            return invoice_address;
        }

        public void setInvoice_address(String invoice_address) {
            this.invoice_address = invoice_address;
        }

        public String getInvoice_phone() {
            return invoice_phone;
        }

        public void setInvoice_phone(String invoice_phone) {
            this.invoice_phone = invoice_phone;
        }

        public String getInvoice_bank() {
            return invoice_bank;
        }

        public void setInvoice_bank(String invoice_bank) {
            this.invoice_bank = invoice_bank;
        }

        public String getInvoice_account() {
            return invoice_account;
        }

        public void setInvoice_account(String invoice_account) {
            this.invoice_account = invoice_account;
        }
    }

}
