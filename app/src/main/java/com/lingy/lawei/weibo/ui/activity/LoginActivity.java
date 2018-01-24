package com.lingy.lawei.weibo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.lingy.lawei.R;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.api.info.WeiboConstants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import butterknife.OnClick;

/**
 * Created by lingy on 2017-10-21.
 */

public class LoginActivity extends BaseActivity{
    private Oauth2AccessToken mAccessToken;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private static final String TAG = "UnLoginActivity";
    @Override
    protected void init() {
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected int providedLayoutId() {
        return R.layout.activty_unlogin;
    }
    @OnClick(R.id.register) void register(){
        startAuth();
    }
    @OnClick(R.id.login) void login(){
        startAuth();
    }
    void startAuth(){
        String authurl = "https://open.weibo.cn/oauth2/authorize" + "?" + "client_id=" + WeiboConstants.APP_KEY
                + "&response_type=token&redirect_uri=" + WeiboConstants.REDIRECT_URL
                + "&key_hash=" + WeiboConstants.AppSecret + (TextUtils.isEmpty(WeiboConstants.PackageName) ? "" : "&packagename=" + WeiboConstants.PackageName)
                + "&display=mobile" + "&scope=" + WeiboConstants.SCOPE;

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", authurl);
        startActivity(intent);
        finish();
    }
    public static void toLogin(Context context){
        context.startActivity(new Intent(context,LoginActivity.class));
    }
}
