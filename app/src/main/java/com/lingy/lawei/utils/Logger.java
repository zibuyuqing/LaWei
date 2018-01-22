package com.lingy.lawei.utils;

import android.util.Log;

/**
 * Created by lingy on 2017-10-22.
 */

public class Logger {
    public static final boolean ENABLE = true;
    private static final String TAG ="LaWei";
    public static void logE(String msg){
        if(ENABLE) {
            logE(TAG, msg);
        }
    }
    public static void logE(String tag,String msg){
        if(ENABLE) {
            Log.e(tag, msg);
        }
    }
    public static void logD(String msg){
        if(ENABLE) {
            logD(TAG, msg);
        }
    }
    public static void logD(String tag,String msg){
        if(ENABLE) {
            Log.d(tag, msg);
        }
    }
}
