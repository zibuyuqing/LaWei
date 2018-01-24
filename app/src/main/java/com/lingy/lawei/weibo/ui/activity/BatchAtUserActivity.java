package com.lingy.lawei.weibo.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.lingy.lawei.R;
import com.lingy.lawei.utils.ScreenUtil;
import com.lingy.lawei.weibo.adapter.BatchCommentListAdapter;
import com.lingy.lawei.weibo.base.BaseActivity;
import com.lingy.lawei.weibo.util.BatchCommentHelper;
import com.lingy.lawei.weibo.util.BatchCommentHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by Xijun.Wang on 2018/1/24.
 */

public class BatchAtUserActivity extends BaseActivity implements BatchCommentHelper.OnRequestStateChangedListener {
    public static final String WEIBO_ID = "weibo_id";
    private static final int STATE_TYPE_NORMAL = 0;
    private static final int STATE_TYPE_RESET = 1;
    private int state = STATE_TYPE_NORMAL;
    @BindView(R.id.rl_tag_layout)
    RelativeLayout llTagLayout;
    @BindView(R.id.offered_tags)
    TagGroup tgOfferedTag;
    @BindView(R.id.select_tags)
    TagGroup tgSelectTags;
    @BindView(R.id.tv_selected_tags)
    TextView tvSelectTags;
    @BindView(R.id.tv_query_tag)
    TextView tvQueryTag;
    @BindView(R.id.tv_select_count)
    TextView tvSelectedCount;
    @BindView(R.id.tv_at_count)
    TextView tvAtCount;
    @BindView(R.id.fab_start)
    FloatingActionButton fabStart;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.rv_comment_list)
    RecyclerView rvCommentsList;
    List<String> tagList = new ArrayList<>();
    List<String> selectedTagList = new ArrayList<>();
    List<String> comments = new ArrayList<>();
    private int requiredCount = 100;
    BatchCommentListAdapter adapter;
    private String weiBoId = "";
    private boolean finish = true;

    @Override
    protected void init() {
        Intent intent = getIntent();
        if (intent != null) {
            weiBoId = intent.getStringExtra(WEIBO_ID);
        }
        // 初始化一些tag
        String[] tags = {"80后", "90后", "购物狂", "运动", "吃货", "驴友", "英雄联盟", "王者荣耀", "科技", "书单", "宅男", "动漫"};
        for (String tag : tags) {
            tagList.add(tag);
        }
        tgOfferedTag.setTags(tagList);
        tgSelectTags.setTags(selectedTagList);
        tgOfferedTag.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                if (!selectedTagList.contains(tag)) {
                    selectedTagList.add(tag);
                    tgSelectTags.setTags(selectedTagList);
                }
            }
        });
        tgSelectTags.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                selectedTagList.remove(tag);
                tgSelectTags.setTags(selectedTagList);
            }
        });
        tvSelectTags.setText(buildTagsStr());
        if (adapter == null) {
            adapter = new BatchCommentListAdapter(this, comments);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rvCommentsList.setLayoutManager(manager);
            rvCommentsList.setAdapter(adapter);
        }
        fabStart.setImageResource(R.drawable.ic_progress);
    }

    private String buildTagsStr() {
        if (selectedTagList.size() <= 0) {
            return "已选择的标签：";
        }
        StringBuilder builder = new StringBuilder("已选择的标签：");
        for (String tag : selectedTagList) {
            builder.append(tag).append("  ");
        }
        return builder.toString();
    }

    @OnClick(R.id.fab_start)
    void startBatchAtUsers() {
        if (TextUtils.isEmpty(weiBoId)) {
            showTips("加载出错");
            return;
        }
        if (state == STATE_TYPE_RESET) {
            fabStart.setImageResource(R.drawable.ic_progress);
            state = STATE_TYPE_NORMAL;
            reset();
        } else {
            if (finish) {
                tvTips.setVisibility(View.GONE);
                rvCommentsList.setVisibility(View.VISIBLE);
                batchAtUser();
            }
        }
    }

    private void reset() {
        comments.clear();
        adapter.notifyDataSetChanged();
        rvCommentsList.setVisibility(View.GONE);
        tvTips.setVisibility(View.VISIBLE);
        tvQueryTag.setText("正在查询：");
        tvSelectTags.setText("已选择的标签：");
        tvAtCount.setText("At的用户数量：");
        tvSelectedCount.setText("选择的数量：");
        selectedTagList.clear();
        tgSelectTags.setTags(selectedTagList);
        requiredCount = 100;
    }

    @OnClick(R.id.btn_confirm_tag)
    void confirmTags() {
        fabStart.setVisibility(View.VISIBLE);
        llTagLayout.setVisibility(View.GONE);
        tvSelectTags.setText(buildTagsStr());
    }
    // 动画显现tag设置页面
    @OnClick(R.id.btn_set_tags)
    void setQueryTags() {
        fabStart.setVisibility(View.GONE);
        llTagLayout.setVisibility(View.VISIBLE);
        int startY = -ScreenUtil.instance(this).getScreenHeight();
        int endY = 0;
        PropertyValuesHolder transY = PropertyValuesHolder.ofFloat("translationY", startY, endY);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(llTagLayout, transY).setDuration(500);
        animator.start();
    }

    // 设置需要批量at的用户数量
    @OnClick(R.id.btn_set_count)
    void setRequireCount() {
        View etLayout = View.inflate(this, R.layout.custom_edit_text, null);
        EditText etInput = (EditText) etLayout.findViewById(R.id.et_input);
        etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("输入数量")
                .setView(etLayout)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String number = etInput.getText().toString();
                        requiredCount = Integer.parseInt(number);
                        tvSelectedCount.setText("选择的数量:" + number);
                    }
                });
        dialog.create();
        dialog.show();
    }

    // 自定义搜索tag
    @OnClick(R.id.btn_custom_tag)
    void customTag() {
        View etLayout = View.inflate(this, R.layout.custom_edit_text, null);
        EditText etInput = (EditText) etLayout.findViewById(R.id.et_input);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("输入tag")
                .setView(etLayout)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String tag = etInput.getText().toString();
                        if (!selectedTagList.contains(tag)) {
                            selectedTagList.add(tag);
                            tgSelectTags.setTags(selectedTagList);
                        }
                    }
                });
        dialog.create();
        dialog.show();
    }

    public static void toBatchAtUser(Context context, String weiboId) {
        Intent intent = new Intent(context, BatchAtUserActivity.class);
        intent.putExtra(WEIBO_ID, weiboId);
        context.startActivity(intent);
    }

    private void batchAtUser() {
        if (TextUtils.isEmpty(weiBoId)) {
            showTips("加载出错");
            return;
        }
        BatchCommentHelper helper = new BatchCommentHelper(weiBoId);
        helper.setRequestStateChangedListener(this);
        helper.setRequireCount(requiredCount);
        // 如果不自定义搜索tag，使用默认的
        if (selectedTagList.size() <= 0) {
            helper.setQueryStrings(tagList);
        } else {
            helper.setQueryStrings(selectedTagList);
        }
        helper.batchAtUser();
        finish = false;
    }

    @Override
    protected boolean canBack() {
        return true;
    }

    @Override
    protected int providedLayoutId() {
        return R.layout.activity_batch_at_user;
    }

    @Override
    public void commentFinish() {
        finishAtUsers();
    }

    @Override
    public void publishProgress(String comment, String tag, int currentUserCount) {
        comments.add(comment);
        adapter.notifyDataSetChanged();
        rvCommentsList.smoothScrollToPosition(adapter.getItemCount() - 1);
        tvQueryTag.setText("正在查询:" + tag);
        tvAtCount.setText("At的用户数量:" + currentUserCount);
    }

    @Override
    public void onError(Throwable throwable) {
        finish = true;
        if (adapter.getItemCount() > 0) {
            fabStart.setImageResource(R.drawable.ic_refresh_white_24dp);
            state = STATE_TYPE_RESET;
        } else {
            tvTips.setVisibility(View.VISIBLE);
            rvCommentsList.setVisibility(View.GONE);
        }

        showTips("加载出错");
    }


    private void finishAtUsers() {
        finish = true;
        fabStart.setImageResource(R.drawable.ic_refresh_white_24dp);
        state = STATE_TYPE_RESET;
        showTips("批量@用户完成");
    }
}
