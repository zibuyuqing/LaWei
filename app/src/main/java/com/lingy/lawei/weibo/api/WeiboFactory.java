package com.lingy.lawei.weibo.api;
import com.lingy.lawei.MyApp;
import com.lingy.lawei.utils.StateUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public class WeiboFactory {

    protected static final Object monitor = new Object();
    static WeiboApi weiBoApiSingleton = null;

    //return Singleton
    public static WeiboApi getWeiBoApiSingleton() {
        synchronized (monitor) {
            if (weiBoApiSingleton == null) {
                weiBoApiSingleton = new weiBoApiRetrofit().getAPiService();
            }
            return weiBoApiSingleton;
        }
    }

    //retrofit2.0
    static class weiBoApiRetrofit {
        public WeiboApi WeiBoApiService;
        public static final String BASE_URL = "https://api.weibo.com/2/";

        public WeiboApi getAPiService() {
            return WeiBoApiService;
        }

        weiBoApiRetrofit() {

            //cache url
            File httpCacheDirectory = new File(MyApp.mContext.getCacheDir(), "responses");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                    .cache(cache).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            WeiBoApiService = retrofit.create(WeiboApi.class);
        }

        //cache
        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {

            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365,TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();

            Request request = chain.request();
            if(!StateUtils.isNetworkAvailable(MyApp.mContext)){
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();

            }
            Response originalResponse = chain.proceed(request);
            if (StateUtils.isNetworkAvailable(MyApp.mContext)) {
                int maxAge = 0; // read from cache
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        };
    }


}
