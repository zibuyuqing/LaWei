package com.lingy.lawei.weibo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lingy.lawei.MainActivity;
import com.lingy.lawei.R;
import com.lingy.lawei.weibo.util.AccessTokenKeeper;

import java.util.Timer;
import java.util.TimerTask;
public class WelcomeActivity extends Activity {
    private Intent mStartIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            mStartIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        } else {
            mStartIntent = new Intent(WelcomeActivity.this, UnLoginActivity.class);
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendMessage(Message.obtain());
            }
        }, 500);
    }


    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startActivity(mStartIntent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
