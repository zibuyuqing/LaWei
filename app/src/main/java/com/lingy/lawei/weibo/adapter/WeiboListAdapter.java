package com.lingy.lawei.weibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lingy.lawei.R;
import com.lingy.lawei.utils.StringUtil;
import com.lingy.lawei.weibo.ui.activity.WeiboDetailActivity;
import com.lingy.lawei.weibo.model.bean.Status;
import com.lingy.lawei.weibo.model.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;



/**
 * Created by lingy on 2017-10-23.
 */

public class WeiboListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Status> data;
    private Activity activity;
    private LayoutInflater inflater;
    public WeiboListAdapter(Activity activity, List<Status> data){
        this.activity = activity;
        this.data = data;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new WeiBoViewHolder(inflater.inflate(R.layout.weibo_list_item_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        WeiBoViewHolder holder = (WeiBoViewHolder) viewHolder;
        holder.bindData(activity,data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class WeiBoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.weibo_layout)
        LinearLayout llWeiboLayout;
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
        public WeiBoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void bindData(Context context,Status status){
            User user = status.getUser();
            Glide.with(activity).load(user.getAvatar_large()).into(civUserHead);
            tvUserName.setText(user.getScreen_name());
            tvCreateTime.setText(StringUtil.getWeiBoCreatedTime(context,status.getCreated_at()));
            tvSource.setText(StringUtil.getWeiboSource(status.getSource()));
            tvComment.setText(status.getComments_count());
            tvFeedlike.setText(status.getAttitudes_count());
            if(status.getRetweeted_status() != null) {
                etOriginWeiBoContent.setVisibility(View.VISIBLE);
                etOriginWeiBoContent.setText(
                        StringUtil.getWeiboText(context, status.getRetweeted_status().getText()));
                rvImages.setBackground(context.getDrawable(R.drawable.home_retweet_weiboitem_bg_auto));
            } else {
                etOriginWeiBoContent.setVisibility(View.GONE);
                rvImages.setBackground(null);
            }
            etWeiBoContent.setText(StringUtil.getWeiboText(context,status.getText()));
            List<Status.ThumbnailPic> imageUrls = status.getPic_urls();
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
                    manager = new GridLayoutManager(context,1){
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
                    manager = new GridLayoutManager(context,3){
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
                WeiboPhotoAdapter photoAdapter = new WeiboPhotoAdapter(context,urls,false);
                rvImages.setAdapter(photoAdapter);
            } else {
                rvImages.setVisibility(View.GONE);
            }
            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WeiboDetailActivity.displayWeiboInfo(activity,user,status);
                }
            });
            llWeiboLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WeiboDetailActivity.displayWeiboInfo(activity,user,status);
                }
            });
        }
    }
}
