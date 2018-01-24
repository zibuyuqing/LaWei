package com.lingy.lawei.weibo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lingy.lawei.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Xijun.Wang on 2018/1/24.
 */

public class BatchCommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<String> comments;
    private Context context;
    private LayoutInflater inflater;
    public BatchCommentListAdapter(Context context,List<String> comments){
        this.context = context;
        this.comments = comments;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(inflater.inflate(R.layout.batch_comment_list_item,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder viewHolder = (MyHolder) holder;
        viewHolder.content.setText(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_comment_content)
        TextView content;
        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
