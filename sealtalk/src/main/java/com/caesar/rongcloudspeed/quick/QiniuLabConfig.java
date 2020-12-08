package com.caesar.rongcloudspeed.quick;

public class QiniuLabConfig {
    public static final String WXAPPID = "wx87a9e4787773477d";
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String ALIAPPID = "2018012502066010";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "2088921623910711";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "";

    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCG+0/LC0xemvEwBchsKnUmgHD+v2gY+3zTTdDda0YZkwjOf6YUbB/KdpJ50lPR9tls+zwExH5JEX02LUS2S4XaM/5EI6xRel6KdGqTUENTUUGPqlCci3MDmiuZQ5ERihUQQfKhv4GG99k7lP6NTld5IwiYXrdMcvyXGfepXTISElVlfqyQ8Ta8K13yN/ZCYsIONolvNyMKvD1iFZ+6aacOb9NJ2hI4bUDOarhRhNJ1m3xY2+qmwKZML/wZgpm7VtTWnQyfFOFSzSiuC5mo5HP67ssFMyz9ejgNTa+YUoS+s2diaEuMGsjh34l6Yz/08qDDDoqROzW2cDdlbSLICuBbAgMBAAECggEAMQLHMj5j+GXh9UHkSAISj2xLmvP85DVMWZ0DnaH5zgdpRzFqKDgqQuVSwhDwhmhPAhcm78Q45mcHQ8ikDlzVuSjvxeX+B4ebFEK4/8xA5du3Jmb85hpsJH6yHfA+mFO+2ixX4QscCNmtcsjitIQhlsZ1tH2PqJ6HwIWZO1GvgIfIVfAwDH6kbp4wXbkiI+0OBbXTRhHuElrs5PmHcnIAIvEVJmS4l4FMXQGWUKTuU8ZAicPKZNhhZ7SDLF9ej7PaPMK/NjLjv0o9Siea9nfG1tfLGZO68f12sG6BUdBBIziH1/8dUTyLPWkMDhKfsIFEL4Xb+sArRWbA1s2FAf8WIQKBgQDGzNw0XBz4Ja3KPdlpSBLOYbAoBOal1BaCDh273ovqiDdzH2Bd0Cxh7fogxbKZqMGX2DJenAHLAr2fjA9KgT77rcDnINeNN/m2voVwddcxPTMrnuOkyTMvXPYqAVAHNq4CMVOZQUeQwLIHTrhkhb2NPQ3jAN1zNKUvabbtldlXtwKBgQCt0buqSe3WvKzWZ+JyVE1DTBdCmu2ZWoaVlvGaURIqSjhJ4Xjl5GG0rh0ozLjjB2l9DWr55Z8pFrDbUQxuLV9j7sFscYW7L+P2mbUu8YbmPHpoWK6VyLElBt4FvLBwU3XkY9PKFeAJrTPGg/v0UQgDjMeZUEoSfMeQgimqdvVUfQKBgQC+j78BPc7TGyMU+uZ9ogXM6R03r0L8cGnG7wsXfAM4pKwqYUg5hQQWN00NvxQtcPNpv2bBA+BAHndwCYzayTt7exmvM+8LQj3PK5+zpnHJlDWJubvSyL6UxxJ8P1fcIlD4bxoIjOex3NSKZq4gRYbmbUDzeW/NMAirgzVDVuHIzwKBgAMYW/4hSOHtBCrhOLAOfj23iiZ+ecKkUQcy4+R6kxc7sRrnr+5wDalY/F3wn04zoJZAeeovQ5FIvSd6nv0LQ8qUws76ioYVyTpn7EkEtgE9jHR1N86k/zqIj8WMYZvucIf7mxbbIt0pP2XGjeMA2mKtzBQX0OnZXPheyP0nFyFFAoGAHkQjmGJWdv/BfPD1BO4qB/GXX2X0vglhK404mtTV1hOsVHHybELuildNc4ecSixIZnq176vk3RGFZdez+U/ASwgGGPXGIDlb74c3e/V8DcZqqgiOGlDJzrfiprllZrHiwpaO8rxUmaVVGTIgzKzwHV7ZERb1EA6pc4l+8W3wE/w=";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    public final static String LOG_TAG = "QiniuLab";
    public static final String TESTSERVER = "http://thinkcmf.91bim.net";
    public final static String REMOTE_SERVICE_SERVER = "http://qiniu.500-china.com";
    public final static String ADVERT_SERVICE_VIDEO = "http://qiniu.500-china.com/banner/acc777cc-c689-46f2-859f-77caa425629a.mp4";
    //quick start
    public final static String QUICK_START_IMAGE_DEMO_PATH = "/api/quick_start/simple_image_example_token.php";
    public final static String QUICK_START_VIDEO_DEMO_PATH = "/portal/index/get_QiniuAuthJSON";

    // simple upload
    public final static String SIMPLE_UPLOAD_WITHOUT_KEY_PATH = "/api/simple_upload/without_key_upload_token.php";
    public final static String SIMPLE_UPLOAD_WITH_KEY_PATH = "/api/simple_upload/with_key_upload_token.php";
    public final static String SIMPLE_UPLOAD_USE_SAVE_KEY_PATH = "/api/simple_upload/use_save_key_upload_token.php";
    public final static String SIMPLE_UPLOAD_USE_SAVE_KEY_FROM_XPARAM_PATH = "/api/simple_upload/use_save_key_from_xparam_upload_token.php";
    public final static String SIMPLE_UPLOAD_USE_RETURN_BODY_PATH = "/api/simple_upload/use_return_body_upload_token.php";
    public final static String SIMPLE_UPLOAD_OVERWRITE_EXISTING_FILE_PATH = "/api/simple_upload/overwrite_existing_file_upload_token.php";
    public final static String SIMPLE_UPLOAD_USE_FSIZE_LIMIT_PATH = "/api/simple_upload/use_fsize_limit_upload_token.php";
    public final static String SIMPLE_UPLOAD_USE_MIME_LIMIT_PATH = "/api/simple_upload/use_mime_limit_upload_token.php";
    public final static String SIMPLE_UPLOAD_WITH_MIMETYPE_PATH = "/api/simple_upload/with_mimetype_upload_token.php";
    public final static String SIMPLE_UPLOAD_ENABLE_CRC32_CHECK_PATH = "/api/simple_upload/enable_crc32_check_upload_token.php";
    public final static String SIMPLE_UPLOAD_USE_ENDUSER_PATH = "/api/simple_upload/use_enduser_upload_token.php";

    // resumable upload
    public final static String RESUMABLE_UPLOAD_WITHOUT_KEY_PATH = "/api/resumable_upload/without_key_upload_token.php";
    public final static String RESUMABLE_UPLOAD_WITH_KEY_PATH = "/api/resumable_upload/with_key_upload_token.php";

    // callback upload
    public final static String CALLBACK_UPLOAD_WITH_KEY_IN_URL_FORMAT_PATH = "/api/callback_upload/with_key_in_url_format_upload_token.php";
    public final static String CALLBACK_UPLOAD_WITH_KEY_IN_JSON_FORMAT_PATH = "/api/callback_upload/with_key_in_json_format_upload_token.php";

    public final static String PUBLIC_IMAGE_VIEW_LIST_PATH = "/api/image_view/public_image_view_list.php";
    public final static String PUBLIC_VIDEO_PLAY_LIST_PATH = "/api/video_play/public_video_play_list.php";
    public final static String QUERY_PFOP_RESULT_PATH = "/service/query_pfop_result.php";

    public static String makeUrl(String remoteServer, String reqPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(remoteServer);
        sb.append(reqPath);
        return sb.toString();
    }
}
