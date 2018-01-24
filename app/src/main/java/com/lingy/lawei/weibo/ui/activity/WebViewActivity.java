package com.lingy.lawei.weibo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lingy.lawei.MainActivity;
import com.lingy.lawei.R;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.api.info.WeiboConstants;
import com.lingy.lawei.weibo.util.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import butterknife.BindView;

/**
 * Created by lingy on 2017-10-21.
 */

public class WebViewActivity extends BaseActivity {
    public static final String TAG ="WebViewActivity";
    @BindView(R.id.webview)
    WebView mWeb;
    private Context mContext;
    private String sRedirectUri;
    private String mLoginURL;
    private boolean mComeFromAccoutActivity;
    @Override
    protected void init() {
        mContext = this;
        mLoginURL = getIntent().getStringExtra("url");
        Log.e(TAG,mLoginURL);
        mComeFromAccoutActivity = getIntent().getBooleanExtra("comeFromAccoutActivity", false);
        sRedirectUri = WeiboConstants.REDIRECT_URL;
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWeb.setWebViewClient(new MyWebViewClient());
        mWeb.loadUrl(mLoginURL);
        mWeb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                    if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                        if (mWeb.canGoBack()) {
                            mWeb.goBack();
                        } else {
                            if (!mComeFromAccoutActivity) {
                                Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                finish();
                            }

                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    public void startMainActivity() {
        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        intent.putExtra("fisrtstart", true);
        if (mComeFromAccoutActivity) {
            intent.putExtra("comeFromAccoutActivity", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
        finish();
    }
    @Override
    protected int providedLayoutId() {
        return R.layout.webview_layout;
    }
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isUrlRedirected(url)) {
                view.stopLoading();
                handleRedirectedUrl(mContext, url);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!url.equals("about:blank") && isUrlRedirected(url)) {
                view.stopLoading();
                handleRedirectedUrl(mContext, url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }
    }

    public void handleRedirectedUrl(Context context, String url) {
        if (!url.contains("error")) {
            int tokenIndex = url.indexOf("access_token=");
            int expiresIndex = url.indexOf("expires_in=");
            int refresh_token_Index = url.indexOf("refresh_token=");
            int uid_Index = url.indexOf("uid=");

            String token = url.substring(tokenIndex + 13, url.indexOf("&", tokenIndex));
            String expiresIn = url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex));
            String refresh_token = url.substring(refresh_token_Index + 14, url.indexOf("&", refresh_token_Index));
            String uid = new String();
            if (url.contains("scope=")) {
                uid = url.substring(uid_Index + 4, url.indexOf("&", uid_Index));
            } else {
                uid = url.substring(uid_Index + 4);
            }
            Oauth2AccessToken accessToken = new Oauth2AccessToken();
            accessToken.setToken(token);
            accessToken.setExpiresIn(expiresIn);
            accessToken.setRefreshToken(refresh_token);
            accessToken.setUid(uid);
            AccessTokenKeeper.writeAccessToken(context,accessToken);
            Log.d(TAG,"uid= " + uid);
            startMainActivity();
        }
    }
    public boolean isUrlRedirected(String url) {
        return url.startsWith(sRedirectUri);
    }
}
