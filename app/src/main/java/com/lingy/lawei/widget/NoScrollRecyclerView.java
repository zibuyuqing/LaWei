package com.lingy.lawei.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Xijun.Wang on 2018/1/24.
 */
public class NoScrollRecyclerView extends RecyclerView {
    public NoScrollRecyclerView(Context context) {
        this(context,null);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
