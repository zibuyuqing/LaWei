package com.lingy.lawei.utils;

import android.util.Log;

/**
 * Created by lingy on 2017-10-22.
 */

public class Logger {
    private static final String TAG ="LaWei";
    public static void logE(String msg){
        logE(TAG,msg);
    }
    public static void logE(String tag,String msg){
        Log.e(tag,msg);
    }
    public static void logD(String msg){
        logD(TAG,msg);
    }
    public static void logD(String tag,String msg){
        Log.d(tag,msg);
    }
}
