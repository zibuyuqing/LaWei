package com.lingy.lawei.weibo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lingy.lawei.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeiBoPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> photos;
    public List<Object> paths = new ArrayList<>();
    private boolean nativeSource = true;
    public WeiBoPhotoAdapter(Context ctx, List<String> list,boolean nativeSource) {
        context = ctx;
        photos = list;
        this.nativeSource = nativeSource;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.activity_send_item_photopick,null);
        return new PhotoPickviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotoPickviewHolder pickviewHolder = (PhotoPickviewHolder) holder;
        String photoUrl = photos.get(position);
        if(nativeSource) {
            Uri uri = Uri.fromFile(new File(photoUrl));
            Glide.with(context)
                    .load(uri)
                    .centerCrop()
                    .into(pickviewHolder.iv_photo);
            getBitmapFromUri(uri, false, position);
            pickviewHolder.iv_delete.setOnClickListener(v -> {
                Glide.clear(pickviewHolder.iv_photo);
                getBitmapFromUri(null, true, position);
                pickviewHolder.iv_delete.setVisibility(View.GONE);
            });
        } else {
            Glide.with(context)
                    .load(photoUrl)
                    .centerCrop()
                    .into(pickviewHolder.iv_photo);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoPickviewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_photo)
        ImageView iv_photo;
        @BindView(R.id.iv_delete)
        ImageView iv_delete;

        public PhotoPickviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            iv_delete.setVisibility(nativeSource ? View.VISIBLE : View.GONE);
        }
    }

    private void getBitmapFromUri(Uri uri,boolean isDelete,int position){
        String path = null;
        if(uri!=null) {
            path = uri.getPath();
        }
        if(!isDelete){
            paths.add(path);
        }else {
            paths.set(position,1);
        }
    }
}
