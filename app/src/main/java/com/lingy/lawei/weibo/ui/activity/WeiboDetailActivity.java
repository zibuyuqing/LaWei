package com.lingy.lawei.weibo.ui.activity;

import android.app.Activity;

import com.lingy.lawei.R;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.model.bean.Status;
import com.lingy.lawei.weibo.model.bean.User;

/**
 * Created by Xijun.Wang on 2018/1/22.
 */

public class WeiboDetailActivity extends BaseActivity{
    @Override
    protected void init() {

    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected int providedLayoutId() {
        return R.layout.activity_weibo_detail;
    }

    public static void displayWeiboInfo(Activity activity, User user, Status status) {

    }
}
