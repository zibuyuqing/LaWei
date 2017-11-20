package com.lingy.lawei;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.lingy.lawei.weibo.info.WeiBoConstants;
import com.lingy.lawei.weibo.util.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by lingy on 2017-10-21.
 */

public class MyApp extends Application {
    public static MyApp instance = null;
    public static Context mContext;
    private Activity mCurrentAcivity;
    @Override
    public void onCreate() {
        instance = this;
        mContext = getApplicationContext();
        super.onCreate();
    }
    public static MyApp getInstance(){
        return instance;
    }
    public void setCurrentActivity(Activity activity){
        mCurrentAcivity = activity;
    }
    public Activity getCurrentAcivity(){
        return mCurrentAcivity;
    }
    public void destroyActivity(){
        mCurrentAcivity = null;
    }
    public String getAccessTokenHack() {
        return AccessTokenKeeper.readAccessToken(this).getToken();
    }
    public Oauth2AccessToken getAccessToken() {
        return AccessTokenKeeper.readAccessToken(this);
    }
    public String getMyId() {
        return AccessTokenKeeper.readAccessToken(this).getUid();
    }
}
