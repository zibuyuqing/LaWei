package com.lingy.lawei.weibo.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lingy.lawei.R;
import com.lingy.lawei.utils.Logger;
import com.lingy.lawei.utils.StringUtil;
import com.lingy.lawei.weibo.adapter.WeiboPhotoAdapter;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.model.bean.Status;
import com.lingy.lawei.weibo.model.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Xijun.Wang on 2018/1/22.
 */

public class WeiboDetailActivity extends BaseActivity{
    private static final String USER = "user";
    private static final String STATUS = "status";
    @BindView(R.id.civ_user_head)
    CircleImageView civUserHead;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.iv_menu_arrow)
    ImageView ivMenuArrow;

    @BindView(R.id.et_weibo_content)
    EditText etWeiBoContent;
    @BindView(R.id.et_origin_weibo_content)
    EditText etOriginWeiBoContent;
    @BindView(R.id.rv_imageList)
    RecyclerView rvImages;

    // 转发
    @BindView(R.id.tv_redirect)
    TextView tvRedirect;

    // 评论
    @BindView(R.id.bottombar_comment)
    LinearLayout llComment;

    @BindView(R.id.tv_comment)
    TextView tvComment;

    // 赞
    @BindView(R.id.tv_feedlike)
    TextView tvFeedlike;

    private User mUser;
    private Status mStatus;

    @OnClick(R.id.bottombar_comment) void comment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"普通评论","批量at用户"};
        builder.setTitle("评论")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position){
                            case 0:
                                normalComment();
                                break;
                            case 1:
                                batchAtUser();
                                break;
                        }
                    }
                });
        builder.show();
    }
    private void batchAtUser(){
        BatchAtUserActivity.toBatchAtUser(this,mStatus.getIdstr());
    }
    private void normalComment(){
        SendWeiboActivity.toPostComment(this,mStatus.getIdstr());
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                mStatus = (Status) bundle.getSerializable(STATUS);
                mUser = (User) bundle.getSerializable(USER);
                Logger.logE(mStatus.toString());
                if(mUser != null && mStatus != null){
                    initView();
                    return;
                }
            }
        }
        showTips("加载错误");
    }

    private void initView() {
        Glide.with(this).load(mUser.getAvatar_large()).into(civUserHead);
        tvUserName.setText(mUser.getScreen_name());
        tvCreateTime.setText(StringUtil.getWeiBoCreatedTime(this,mStatus.getCreated_at()));
        tvSource.setText(StringUtil.getWeiboSource(mStatus.getSource()));
        if(mStatus.getRetweeted_status() != null) {
            etOriginWeiBoContent.setVisibility(View.VISIBLE);
            etOriginWeiBoContent.setText(
                    StringUtil.getWeiboText(this, mStatus.getRetweeted_status().getText()));
            rvImages.setBackground(this.getDrawable(R.drawable.home_retweet_weiboitem_bg_auto));
        } else {
            etOriginWeiBoContent.setVisibility(View.GONE);
            rvImages.setBackground(null);
        }
        etWeiBoContent.setText(StringUtil.getWeiboText(this,mStatus.getText()));
        List<Status.ThumbnailPic> imageUrls = mStatus.getPic_urls();
        int itemImagesCount = 0;
        GridLayoutManager manager;
        if(imageUrls != null && imageUrls.size() > 0) {
            List<String> urls = new ArrayList<>();
            for(Status.ThumbnailPic pic : imageUrls){
                urls.add(pic.getLargeImg());
            }
            itemImagesCount = urls.size();
            rvImages.setVisibility(View.VISIBLE);
            if(itemImagesCount == 1){
                manager = new GridLayoutManager(this,1){
                    @Override
                    public boolean canScrollHorizontally() {
                        return false;
                    }

                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
            } else {
                manager = new GridLayoutManager(this,3){
                    @Override
                    public boolean canScrollHorizontally() {
                        return false;
                    }

                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
            }
            rvImages.setLayoutManager(manager);
            WeiboPhotoAdapter photoAdapter = new WeiboPhotoAdapter(this,urls,false);
            rvImages.setAdapter(photoAdapter);
        } else {
            rvImages.setVisibility(View.GONE);
        }
    }

    public static void displayWeiboInfo(Activity activity, User user, Status status){
        Intent intent = new Intent(activity, WeiboDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER,user);
        bundle.putSerializable(STATUS,status);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected int providedLayoutId() {
        return R.layout.activity_weibo_detail;
    }
}
