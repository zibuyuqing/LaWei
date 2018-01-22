package com.lingy.lawei.weibo.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lingy.lawei.MyApp;
import com.lingy.lawei.R;
import com.lingy.lawei.weibo.adapter.UserListAdapter;
import com.lingy.lawei.utils.Logger;
import com.lingy.lawei.weibo.api.WeiBoApi;
import com.lingy.lawei.weibo.api.WeiBoFactory;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.model.bean.User;
import com.lingy.lawei.weibo.model.bean.UserList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lingy on 2017-10-22.
 */

public class SearchActivity extends BaseActivity implements UserListAdapter.OnStateChangedListener{
    private final int ACTION_MODE_NORMAL = 0;
    private final int ACTION_MODE_MULTI_SELECT_AND_DELETE = 1;
    private String mQueryString;
    private int mPage;
    private final int PER_QUARY_COUNT = 50;
    private boolean hasMoreUsers = true;
    private UserListAdapter mAdapter;
    private UserList mUserList = new UserList();
    // #ifdef LAVA_EDIT
    // wangxijun. 2016/11/16, action
    public ActionMode mActionMode;
    private SelectActionMode mSelectActionMode;
    private TextView mSelectionAll;
    private TextView mSelectionCount;
    private boolean mIsSelectedAll = false;
    private int mActionModeType = 0;
    // #endif
    @BindView(R.id.iv_empty_view)
    ImageView mEmptyView;
    @BindView(R.id.content_list)
    XRecyclerView mXRContentList;
    @BindView(R.id.et_search_box)
    EditText mSearchText;
    @OnClick(R.id.btn_search) void search(){
        if(!TextUtils.isEmpty(mSearchText.getText())){
            search(mSearchText.getText().toString());
        } else {
            showTips("请输入要搜索的内容");
        }
    }
    @OnClick(R.id.fbtn_at_user) void atAndSendWeiBo2User(){
        if(mAdapter.isOnSelectState()) {
            String atUsers = mAdapter.getAtUserNameStr();
            SendWeiboActivity.toAtAndSendWeiBo(this,atUsers);
        } else {
            showTips("请先选择用户");
        }
    }

    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        showTips("发送失败");
    }
    private Map<String,Object> getSendMap(String token, int page){
        Map<String,Object> map = new HashMap<>();
        map.put("access_token", token);
        map.put("count", PER_QUARY_COUNT);
        map.put("page", page);
        map.put("q", mQueryString);
        return map;
    }
    @Override
    protected void init() {
        mSelectActionMode = new SelectActionMode();
        mEmptyView.setVisibility(View.VISIBLE);
        mXRContentList.setLoadingListener(new MyLoadingListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRContentList.setLayoutManager(layoutManager);
        if(mAdapter == null){
            mAdapter = new UserListAdapter(this, mUserList.getUsers());
            mAdapter.setOnActionModeChangedListener(this);
            mXRContentList.setAdapter(mAdapter);
        }
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mAdapter.isOnSelectState()){
            mAdapter.exitSelectState();
            return;
        }
        finish();
    }

    private void clearAndReplaceValue(UserList value) {
        mUserList.setNext_cursor(value.getNext_cursor());
        mUserList.getUsers().clear();
        mUserList.getUsers().addAll(value.getUsers());
        mUserList.setTotal_number(value.getTotal_number());
        mUserList.setPrevious_cursor(value.getPrevious_cursor());

    }
    @Override
    protected int providedLayoutId() {
        return R.layout.activity_user_search;
    }
    private void search(String queryString){
        mQueryString = queryString;
        mXRContentList.refresh();
    }

    @Override
    public void startSelect() {
        enterSelectMode();
    }

    @Override
    public void select() {
        updateSelectCount();
    }

    class MyLoadingListener implements XRecyclerView.LoadingListener{

        @Override
        public void onRefresh() {
            loadUsers(1,true);
        }

        @Override
        public void onLoadMore() {
            loadUsers(mPage + 1,false);
        }
    }
    private void loadUsers(int page,boolean refresh){
        WeiBoApi weiBoApi = WeiBoFactory.getWeiBoApiSingleton();
        String token = MyApp.getInstance().getAccessTokenHack();
        if(refresh) {
            Logger.logE("刷新.......");
            hasMoreUsers = true;
        } else {
            Logger.logE("加载更多.......");
        }
        mPage = page;
        weiBoApi.searchUsers(getSendMap(token,mPage))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userList -> {
                    if(refresh) {
                        mXRContentList.refreshComplete();
                    } else {
                        mXRContentList.loadMoreComplete();
                    }
                    if (userList != null && userList.getUsers().size() > 0) {
                        if(!refresh){
                            hasMoreUsers = userList.getUsers().size() > 1;
                            if (hasMoreUsers) {
                                List<User> list = userList.getUsers();
                                mUserList.getUsers().addAll(list);
                                mPage++;
                            } else {
                                hasMoreUsers = false;
                            }
                        } else {
                            clearAndReplaceValue(userList);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    mEmptyView.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
                },SearchActivity.this::loadError);
    }
    // #ifdef LAVA_EDIT
    // wangxijun. 2016/11/16, start select alarm and delete
    public void enterSelectMode() {
        mActionMode = startActionMode(mSelectActionMode);
        mActionModeType = ACTION_MODE_MULTI_SELECT_AND_DELETE;
    }
    // #endif
    // #endif
    // #ifdef LAVA_EDIT
    // wangxijun. 2016/11/16, exit select mode
    private void exitSelectMode(){
        if(mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
        mAdapter.exitSelectState();
        mActionModeType = ACTION_MODE_NORMAL;
    }
    // #endif
    // #ifdef LAVA_EDIT
    // wangxijun. 2016/11/16, add to multi select
    private class SelectActionMode implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
            ViewGroup v = (ViewGroup) LayoutInflater.from(SearchActivity.this).inflate(R.layout.select_action_bar, null);
            mode.setCustomView(v);
            mSelectionAll = (TextView) v.findViewById(R.id.select_all);
            mSelectionCount = (TextView) v.findViewById(R.id.select_count);
            mSelectionAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logger.logE("mIsSelectedAll = :" + mIsSelectedAll);
                    if(mIsSelectedAll){
                        mSelectionAll.setText("全不选");
                        mAdapter.clearAllSelectedItems();
                        mIsSelectedAll = false;
                    } else {
                        mSelectionAll.setText("全选");
                        mAdapter.selectAllItems();
                        mIsSelectedAll = true;
                    }
                    updateSelectCount();
                }
            });
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }
        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            if(mActionModeType == ACTION_MODE_MULTI_SELECT_AND_DELETE){
                exitSelectMode();
            }
        }
    }
    public void updateSelectCount() {
        int selectCount =  mAdapter.getSelectedUsers().size();
        if(selectCount == mAdapter.getItemCount()){
            mSelectionAll.setText("全不选");
            mIsSelectedAll = true;
        } else {
            mSelectionAll.setText("全选");
            mIsSelectedAll = false;
        }
        mSelectionCount.setText(selectCount+" ");
    }
    // #endif
}
