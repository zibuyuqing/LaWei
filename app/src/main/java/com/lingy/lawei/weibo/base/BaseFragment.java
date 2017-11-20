package com.lingy.lawei.weibo.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lingy.lawei.utils.Logger;

import butterknife.ButterKnife;

/**
 * Created by lingy on 2017-10-22.
 */

public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected View mContains;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContains = inflater.inflate(providedLayoutId(),container,false);
        ButterKnife.bind(this,mContains);
        return mContains;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Logger.logE("mContains =:" + mContains);
        init();
    }

    public void showTips(String msg){
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }
    protected abstract void init();
    protected abstract int providedLayoutId();
    public abstract String getMyTag();
}
