package com.lingy.lawei.weibo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lingy.lawei.R;
import com.lingy.lawei.weibo.adapter.FragmentPageAdapter;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.base.BaseFragment;
import com.lingy.lawei.weibo.bean.User;
import com.lingy.lawei.weibo.base.BaseUserInfoFragment;
import com.lingy.lawei.weibo.fragment.FansFragment;
import com.lingy.lawei.weibo.fragment.FriendsFragment;
import com.lingy.lawei.weibo.fragment.WeiBoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Xijun.Wang on 2017/10/23.
 */

public class UserInfoDisplayActivity extends BaseActivity{
    public static final String USER = "user";
    public static final String TAG_FANS = "fans";
    public static final String TAG_FRIENDS = "friends";
    public static final String TAG_WEIBOS = "weibos";
    List<BaseFragment> mFragments = new ArrayList<>();
    private FragmentPageAdapter mPageAdapter;
    private User mUser;

    @BindView(R.id.user_info_page)
    ViewPager mViewPager;

    @BindView(R.id.cover)
    ImageView ivCover;

    @BindView(R.id.civ_user_head_image)
    CircleImageView civUserHead;

    @BindView(R.id.tv_user_info_name)
    TextView tvUserName;

    @BindView(R.id.tv_user_info_description)
    TextView tvUserDescription;

    @BindView(R.id.weibo_count)
    TextView tvWeiBosCount;

    @BindView(R.id.friends_count)
    TextView tvFriendsCount;

    @BindView(R.id.fans_count)
    TextView tvFansCount;

    @BindView(R.id.topics_count)
    TextView tvTopicsCount;

    @Override
    protected void init() {
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                mUser = (User) bundle.getSerializable(USER);
                if(mUser != null){
                    initHeadView();
                }
            }
        }
        BaseFragment weibos = new WeiBoFragment();
        mFragments.add(weibos);
        BaseUserInfoFragment fans = new FansFragment();
        mFragments.add(fans);
        BaseUserInfoFragment friends = new FriendsFragment();
        mFragments.add(friends);
        mPageAdapter = new FragmentPageAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(3);
    }
    private BaseFragment getCurrentFragment(){
        if(mPageAdapter != null){
            BaseFragment fragment =
                    (BaseFragment) mPageAdapter.getItem(mViewPager.getCurrentItem());
            return fragment;
        }
        return null;
    }
    private void initHeadView() {
        tvUserName.setText(mUser.getScreen_name());
        tvUserDescription.setText(mUser.getDescription());
        tvWeiBosCount.setText(mUser.getStatuses_count());
        // mei zhe ge gong neng
        tvTopicsCount.setText("0");
        tvFansCount.setText(mUser.getFollowers_count());
        tvFriendsCount.setText(mUser.getFriends_count());
        Glide.with(this).load(mUser.getCover_image())
                .placeholder(R.drawable.cover_image)
                .centerCrop()
                .into(ivCover);

        Glide.with(this).load(mUser.getAvatar_large())
                .centerCrop()
                .into(civUserHead);
    }
    @Override
    public void onBackPressed() {
        BaseFragment fragment = getCurrentFragment();
        if(fragment != null && fragment instanceof  BaseUserInfoFragment){
            BaseUserInfoFragment userInfoFragment = (BaseUserInfoFragment) fragment;
            if(userInfoFragment.onSelectState()){
                userInfoFragment.exitSelectState();
                return;
            }
        }
        finish();
    }
    public static void displayUserInfo(Activity activity, User user){
        if(activity instanceof UserInfoDisplayActivity){
            activity.finish();
        }
        Intent intent = new Intent(activity, UserInfoDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER,user);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    public String getUserId(){
        return mUser == null ? null : mUser.getId();
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected int providedLayoutId() {
        return R.layout.activity_user_info;
    }
}
