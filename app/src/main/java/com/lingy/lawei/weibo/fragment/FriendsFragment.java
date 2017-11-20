package com.lingy.lawei.weibo.fragment;

import com.lingy.lawei.weibo.activity.UserInfoDisplayActivity;
import com.lingy.lawei.weibo.base.BaseUserInfoFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Xijun.Wang on 2017/10/23.
 */

public class FriendsFragment extends BaseUserInfoFragment {
    @Override
    protected void load() {
        if(mUid == null){
            return;
        }
        mApi.getFriendsById(getRequestMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> {
                    displayData(userList);
                }, this::loadError);
    }

    @Override
    public String getMyTag() {
        return UserInfoDisplayActivity.TAG_FRIENDS;
    }
}
