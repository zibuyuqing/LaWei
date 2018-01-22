package com.lingy.lawei.weibo.ui.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.lingy.lawei.R;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.info.WeiBoConstants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import butterknife.OnClick;

/**
 * Created by lingy on 2017-10-21.
 */

public class UnLoginActivity extends BaseActivity{
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
        String authurl = "https://open.weibo.cn/oauth2/authorize" + "?" + "client_id=" + WeiBoConstants.APP_KEY
                + "&response_type=token&redirect_uri=" + WeiBoConstants.REDIRECT_URL
                + "&key_hash=" + WeiBoConstants.AppSecret + (TextUtils.isEmpty(WeiBoConstants.PackageName) ? "" : "&packagename=" + WeiBoConstants.PackageName)
                + "&display=mobile" + "&scope=" + WeiBoConstants.SCOPE;

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", authurl);
        startActivity(intent);
        finish();
    }
}
