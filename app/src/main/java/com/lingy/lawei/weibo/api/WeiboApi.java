package com.lingy.lawei.weibo.api;
import com.lingy.lawei.weibo.model.bean.PostComments;
import com.lingy.lawei.weibo.model.bean.Status;
import com.lingy.lawei.weibo.model.bean.StatusList;
import com.lingy.lawei.weibo.model.bean.User;
import com.lingy.lawei.weibo.model.bean.UserList;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;
public interface WeiboApi {
    // 根据用户ID获取微博列表
    @GET("statuses/user_timeline.json")
    Observable<StatusList> getUserTimeLine(@QueryMap Map<String,Object> params);

    // 根据用户ID获取用户信息
    @GET("users/show.json")
    Observable<User> getUserInfo(@QueryMap Map<String, String> params);

    // 获取用户的关注
    @GET("friendships/friends.json")
    Observable<UserList> getFriendsById(@QueryMap Map<String, Object> params);

    // 获取用户粉丝
    @GET("friendships/followers.json")
    Observable<UserList> getFollowersById(@QueryMap Map<String, Object> params);

    // 查询用户
    @FormUrlEncoded
    @POST("search/users.json")
    Observable<UserList> searchUsers(@FieldMap Map<String,Object> params);

    // 发布评论
    @FormUrlEncoded
    @POST("comments/create.json")
    Observable<PostComments> setComment(@FieldMap Map<String, Object> params);

    // 发布带图片的微博
    @Multipart
    @POST("statuses/upload.json")
    Observable<Status> sendWeiBoWithImg(
            @Part("access_token") RequestBody accessToken,
            @Part("status") RequestBody context,
            @Part("pic\";filename=\"file") RequestBody requestBody);

    // 发布普通微博
    @FormUrlEncoded
    @POST("statuses/update.json")
    Observable<Status> sendWeiBoWithText(@FieldMap Map<String, Object> params);
}
