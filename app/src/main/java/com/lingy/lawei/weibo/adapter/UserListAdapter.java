package com.lingy.lawei.weibo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lingy.lawei.R;
import com.lingy.lawei.utils.Logger;
import com.lingy.lawei.weibo.ui.activity.UserInfoDisplayActivity;
import com.lingy.lawei.weibo.model.bean.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingy on 2017-10-22.
 */

public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_SELECT = 1;
    private int mState = STATE_NORMAL;
    private List<User> data;
    private Activity activity;
    private LayoutInflater inflater;
    private Set<User> selectedUsers = new HashSet<>();
    private boolean onSelectState = false;
    private int firstSelectItem = -1;
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private OnStateChangedListener mListener;
    public UserListAdapter(Activity activity,List<User> data){
        this.data = data;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(inflater.inflate(R.layout.user_listview_item_layout,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder viewHolder = (MyHolder) holder;
        bindView(viewHolder, position);
    }
    public void setOnActionModeChangedListener(OnStateChangedListener listener){
        mListener = listener;
    }
    private void bindView(MyHolder viewHolder, int position) {
        User user = data.get(position);
        viewHolder.username.setText(user.getName());
        viewHolder.content.setText(user.getDescription());
        Glide.with(activity).load(user.getAvatar_large()).into(viewHolder.userhead);
        if(onSelectState){
            viewHolder.select.setVisibility(View.VISIBLE);
            Logger.logE("position =:" + position + ",isItemChecked(position) =:" + isItemChecked(position));
            if(isItemChecked(position)){
                viewHolder.select.setChecked(true);
            } else {
                viewHolder.select.setChecked(false);
            }
        } else {
            viewHolder.select.setChecked(false);
            viewHolder.select.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mState = STATE_SELECT;
                onSelectState = true;
                firstSelectItem = position;
                mListener.startSelect();
                setItemChecked(firstSelectItem,true);
                notifyDataSetChanged();
                return true;
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onSelectState){
                    Logger.logE("onClick position =:" + position);
                    CheckBox select = viewHolder.select;
                    if(select.isChecked()){
                        select.setChecked(false);
                        setItemChecked(position,false);
                    } else {
                        select.setChecked(true);
                        setItemChecked(position,true);
                    }
                } else {
                    UserInfoDisplayActivity.displayUserInfo(activity,data.get(position));
                }
            }
        });
    }
    public void setItemChecked(int position, boolean isChecked) {
        Logger.logE("setItemChecked :: position = :" + position + ",isChecked =:" + isChecked);
        mSelectedPositions.put(position, isChecked);
        selectUser(position,isChecked);
        mListener.select();
    }
    private boolean isItemChecked(int position) {
        return mSelectedPositions.get(position);
    }
    public void selectAllItems(){
        for (int i = 0; i <getItemCount(); i++) {
            setItemChecked(i,true);
        }
        notifyDataSetChanged();
    }
    public void clearAllSelectedItems(){
        mSelectedPositions = new SparseBooleanArray();
        selectedUsers.clear();
        firstSelectItem = -1;
        notifyDataSetChanged();
    }
    public void exitSelectState(){
        mState = STATE_NORMAL;
        onSelectState = false;
        clearAllSelectedItems();
        notifyDataSetChanged();
    }
    public boolean isOnSelectState(){
        return onSelectState;
    }
    public Set<User> getSelectedUsers(){
        return selectedUsers;
    }
    public String getAtUserNameStr(){
        if(selectedUsers.size() <= 0){
            return null;
        }
        StringBuilder strBuild = new StringBuilder();
        for(User user : selectedUsers){
            strBuild.append("@").append(user.getScreen_name()).append(" ");
        }
        return strBuild.toString();
    }
    public void selectUser(int position,boolean checked){
        User user = data.get(position);
        if(checked){
            selectedUsers.add(user);
        } else {
            if(selectedUsers.contains(user)){
                selectedUsers.remove(user);
            }
        }
        Logger.logE("selectUser :: selectedUsers =:" + selectedUsers.size());
    }
    public int getState(){
        return mState;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_username)
        TextView username;
        @BindView(R.id.tv_content)
        TextView content;
        @BindView(R.id.civ_user_head)
        CircleImageView userhead;
        @BindView(R.id.cb_select)
        CheckBox select;
        int position = 0;
        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            position = getAdapterPosition();
        }
    }
    public interface OnStateChangedListener{
        void startSelect();
        void select();
    }
}
