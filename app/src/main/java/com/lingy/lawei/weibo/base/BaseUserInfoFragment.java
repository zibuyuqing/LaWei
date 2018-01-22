package com.lingy.lawei.weibo.base;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lingy.lawei.weibo.adapter.UserListAdapter;
import com.lingy.lawei.weibo.model.bean.User;
import com.lingy.lawei.weibo.model.bean.UserList;

import java.util.List;

/**
 * Created by Xijun.Wang on 2017/10/23.
 */

public abstract class BaseUserInfoFragment extends BaseDisplayInfoFragment {
    protected UserListAdapter mAdapter;
    private UserList mUserList = new UserList();
    @Override
    protected void init() {
        super.init();
        if(mAdapter == null) {
            mAdapter = new UserListAdapter(mActivity, mUserList.getUsers());
        }
        mXRContentList.setAdapter(mAdapter);
        Log.e(TAG,"mUid------------------- =:" + mUid);
        if(TextUtils.isEmpty(mUid)){
            return;
        }
        mXRContentList.refresh();
    }
    private void clearAndReplaceValue(UserList value) {
        mUserList.setNext_cursor(value.getNext_cursor());
        mUserList.getUsers().clear();
        mUserList.getUsers().addAll(value.getUsers());
        mUserList.setTotal_number(value.getTotal_number());
        mUserList.setPrevious_cursor(value.getPrevious_cursor());

    }
    public boolean onSelectState(){
        return mAdapter.isOnSelectState();
    }
    public void exitSelectState(){
        mAdapter.exitSelectState();
    }
    public void displayData(UserList userList) {
        if(refresh) {
            mXRContentList.refreshComplete();
        } else {
            mXRContentList.loadMoreComplete();
        }
        if (userList != null && userList.getUsers().size() > 0) {
            if(!refresh){
                hasMore = userList.getUsers().size() > 1;
                if (hasMore) {
                    List<User> list = userList.getUsers();
                    mUserList.getUsers().addAll(list);
                    mPage++;
                } else {
                    hasMore = false;
                }
            } else {
                clearAndReplaceValue(userList);
            }
        }
        mAdapter.notifyDataSetChanged();
        mEmptyView.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

}
