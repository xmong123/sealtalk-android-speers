package com.caesar.rongcloudspeed.ui.activity;

/**
 * Created by cenxiaozhong on 2017/5/22.
 *  <p>
 *
 */

public class WebActivity extends BaseWebActivity {

    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();



        //测试Cookies
        /*
        try {

            String targetUrl="http://www.han80.com/mobile/user.php";
            Log.i("Info","cookies:"+ AgentWebConfig.getCookiesByUrl(targetUrl="http://www.han80.com/mobile/user.php"));
            AgentWebConfig.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Log.i("Info","onResume():"+value);
                }
            });

            String tagInfo=AgentWebConfig.getCookiesByUrl(targetUrl);
            Log.i("Info","tag:"+tagInfo);
            AgentWebConfig.syncCookie("http://www.han80.com/mobile/user.php","ECS[history]=21860%2C32626%2C33752%2C32627%2C33659; UM_distinctid=16535c7b8c21a6-0a360888eeefed-5b193413-1fa400-16535c7b8c316e; CNZZDATA1261666818=1638936239-1534201584-%7C1534201584; ECSCP_ID=dc6c5d7770a4d59ac9443b564afb84e79d652486; ECS[username]=15358801339; ECS[user_id]=60; ECS[password]=000a8b50eae59ccd557def940bc2fb59; real_ipd=114.225.46.172; ECS_ID=4eabcdb4a1cab01525bba1da1e1559ca962459bf");
            String tag=AgentWebConfig.getCookiesByUrl(targetUrl);
            Log.i("Info","tag:"+tag);
            AgentWebConfig.removeSessionCookies();
            Log.i("Info","removeSessionCookies:"+AgentWebConfig.getCookiesByUrl(targetUrl));
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }
}
