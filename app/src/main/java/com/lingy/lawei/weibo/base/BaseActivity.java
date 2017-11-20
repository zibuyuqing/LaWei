package com.lingy.lawei.weibo.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.lingy.lawei.R;

import butterknife.ButterKnife;

/**
 * Created by lingy on 2017-10-21.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected abstract void init();
    protected abstract boolean canBack();
    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.BassActivityTheme);
        super.onCreate(savedInstanceState);
        setContentView(providedLayoutId());
        ButterKnife.bind(this);
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null && mAppBar != null) {
            setSupportActionBar(mToolbar); //把Toolbar当做ActionBar给设置
            if (canBack()) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            }
            if (Build.VERSION.SDK_INT >= 21) {
                mAppBar.setElevation(10.6f);//Z轴浮动
            }
        }
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary,null));
        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void showTips(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    protected abstract int providedLayoutId();
}
