package com.caesar.rongcloudspeed.bean;

/**
 * Created by mac on 2018/4/5.
 */

public class GoodInfoDetail extends CommonResonseBean {
    /**
     * id : 2
     * img : {"img_one":"Public/Uploads/5abc9216607d4.png","img_two":"Public/Uploads/5abc9216680ee.png","img_three":"Public/Uploads/5abc92166c73f.png"}
     * goods_name : Apple iPhone X (A1865) 256GB 深空灰色 移动联通电信4G手机
     * money : 7788.00
     * back_money : 5000.00
     * goods_intro : &lt;p&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&lt;img src=&quot;/news/ueditor/upload/20180329/1522307568577185.jpg&quot; title=&quot;1522307568577185.jpg&quot; alt=&quot;59b860a6Nd4f9bfe9.jpg&quot;/&gt;&lt;/p&gt;
     */

    private String id;
    private ImgBean img;
    private String goods_name;
    private String money;
    private String back_money;
    private String goods_intro;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImgBean getImg() {
        return img;
    }

    public void setImg(ImgBean img) {
        this.img = img;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBack_money() {
        return back_money;
    }

    public void setBack_money(String back_money) {
        this.back_money = back_money;
    }

    public String getGoods_intro() {
        return goods_intro;
    }

    public void setGoods_intro(String goods_intro) {
        this.goods_intro = goods_intro;
    }

    public static class ImgBean {
        /**
         * img_one : Public/Uploads/5abc9216607d4.png
         * img_two : Public/Uploads/5abc9216680ee.png
         * img_three : Public/Uploads/5abc92166c73f.png
         */

        private String img_one;
        private String img_two;
        private String img_three;

        public String getImg_one() {
            return img_one;
        }

        public void setImg_one(String img_one) {
            this.img_one = img_one;
        }

        public String getImg_two() {
            return img_two;
        }

        public void setImg_two(String img_two) {
            this.img_two = img_two;
        }

        public String getImg_three() {
            return img_three;
        }

        public void setImg_three(String img_three) {
            this.img_three = img_three;
        }
    }
}
