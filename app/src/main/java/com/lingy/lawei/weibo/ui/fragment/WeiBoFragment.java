package com.lingy.lawei.weibo.ui.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lingy.lawei.utils.Logger;
import com.lingy.lawei.weibo.ui.activity.UserInfoDisplayActivity;
import com.lingy.lawei.weibo.adapter.WeiboListAdapter;
import com.lingy.lawei.weibo.base.BaseDisplayInfoFragment;
import com.lingy.lawei.weibo.model.bean.Status;
import com.lingy.lawei.weibo.model.bean.StatusList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lingy on 2017-10-24.
 */

public class WeiBoFragment extends BaseDisplayInfoFragment {
    protected static final String TAG = "WeiBoFragment";
    private StatusList mStatusList = new StatusList();
    WeiboListAdapter mAdapter;
    protected void init() {
        super.init();

        if(mAdapter == null) {
            mAdapter = new WeiboListAdapter(mActivity, mStatusList.getStatuses());
        }
        mXRContentList.setAdapter(mAdapter);
        if(TextUtils.isEmpty(mUid)){
            return;
        }
        mXRContentList.refresh();
    }
    @Override
    public String getMyTag() {
        return UserInfoDisplayActivity.TAG_WEIBOS;
    }
    private void clearAndReplaceValue(StatusList value) {
        Logger.logE("clearAndReplaceValue :: value =:" + value.getStatuses().size());
        mStatusList.setNext_cursor(value.getNext_cursor());
        mStatusList.getStatuses().clear();
        mStatusList.getStatuses().addAll(value.getStatuses());
        mStatusList.setTotal_number(value.getTotal_number());
        mStatusList.setPrevious_cursor(value.getPrevious_cursor());
    }
    @Override
    protected void load() {
        if(mUid == null){
            return;
        }
        mApi.getUserTimeLine(getRequestMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statusList -> {
                    displayData(statusList);
                }, this::loadError);
    }
    public void displayData(StatusList contentList) {
        if(mRefresh) {
            mXRContentList.refreshComplete();
        } else {
            mXRContentList.loadMoreComplete();
        }
        if (contentList != null && contentList.getStatuses().size() > 0) {
            if(!mRefresh){
                mHasMore = contentList.getStatuses().size() > 1;
                if (mHasMore) {
                    List<Status> list = contentList.getStatuses();
                    mStatusList.getStatuses().addAll(list);
                    mPage++;
                } else {
                    mHasMore = false;
                }
            } else {
                clearAndReplaceValue(contentList);
            }
        }
        mAdapter.notifyDataSetChanged();
        mEmptyView.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }
}
