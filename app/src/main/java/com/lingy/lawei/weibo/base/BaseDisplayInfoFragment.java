package com.lingy.lawei.weibo.base;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lingy.lawei.MyApp;
import com.lingy.lawei.R;
import com.lingy.lawei.utils.Logger;
import com.lingy.lawei.weibo.api.WeiboApi;
import com.lingy.lawei.weibo.api.WeiboFactory;
import com.lingy.lawei.weibo.ui.activity.UserInfoDisplayActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Xijun.Wang on 2018/1/23.
 */

public abstract class BaseDisplayInfoFragment  extends BaseFragment{
    public static final String TAG = "BaseDisplayInfoFragment";
    protected String mUid;
    protected boolean mRefresh;
    protected boolean mHasMore;
    protected int mPage;
    protected String mToken;
    protected WeiboApi mApi = WeiboFactory.getWeiBoApiSingleton();
    @BindView(R.id.xr_info_list)
    protected XRecyclerView mXRContentList;
    @BindView(R.id.iv_empty_view)
    protected ImageView mEmptyView;
    @Override
    protected void init() {
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mXRContentList.setLayoutManager(manager);
        mXRContentList.setLoadingListener(new MyLoadingListener());
        mToken = MyApp.getInstance().getAccessTokenHack();
        mUid = ((UserInfoDisplayActivity)mActivity).getUserId();
    }

    @Override
    protected int providedLayoutId() {
        return R.layout.fragment_user_info;
    }

    protected Map<String,Object> getRequestMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("access_token", mToken);
        map.put("uid", mUid);
        return map;
    }
    protected abstract void load();
    private class MyLoadingListener implements XRecyclerView.LoadingListener {
        @Override
        public void onRefresh() {
            mRefresh = true;
            mHasMore = true;
            Logger.logE("刷新.......");
            load();
        }

        @Override
        public void onLoadMore() {
            mRefresh = false;
            Logger.logE("加载更多.......");
            load();
        }
    }

    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        showTips("加载出错");
    }
}
