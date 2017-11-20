package com.lingy.lawei.weibo.info;

/**
 * Created by lingy on 2017-10-21.
 */

public class WeiBoConstants {
    public static final String APP_KEY = "211160679";
    public static final String REDIRECT_URL = "http://oauth.weico.cc";
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";

    public static final String AppSecret = "1e6e33db08f9192306c4afa0a61ad56c";
    public static final String PackageName = "com.eico.weico";

    //图片形状
    public static final String IMGSIZE_HORIZONTAL = "1"; //水平方向的长方形尺寸
    public static final String IMGSIZE_VERTICAL = "2";//竖直方向的长方形尺寸
    public static final String IMGSIZE_SQUARE = "3";//正方形尺寸

    //首页分组
    public static final long GROUP_TYPE_ALL = 0;//全部微博
    public static final long GROUP_TYPE_FRIENDS_CIRCLE = 1;//好友圈


    //评论页分组
    public static final int GROUP_COMMENT_TYPE_ALL = 0x12;//全部评论
    public static final int GROUP_COMMENT_TYPE_FRIENDS = 0x13;//关注的人
    public static final int GROUP_COMMENT_TYPE_BYME = 0x14;//我发出的

    //@我的分组
    public static final int GROUP_RETWEET_TYPE_ALL = 0x15;//所有微博
    public static final int GROUP_RETWEET_TYPE_FRIENDS = 0x16;//关注人的微博
    public static final int GROUP_RETWEET_TYPE_ORIGINWEIBO = 0x17;//原创微博
    public static final int GROUP_RETWEET_TYPE_ALLCOMMENT = 0x18;//所有评论
    public static final int GROUP_RETWEET_TYPE_FRIEDNSCOMMENT = 0x19;//关注人的评论

    //我的微博
    public static final int GROUP_MYWEIBO_TYPE_ALL = 0;
    public static final int GROUP_MYWEIBO_TYPE_ORIGIN = 1;
    public static final int GROUP_MYWEIBO_TYPE_PICWEIBO = 2;

    //删除微博的类型
    public static final String DELETE_WEIBO_TYPE1 = "全部微博";

    public static final String DELETE_WEIBO_TYPE2 = "我的全部微博";
    public static final String DELETE_WEIBO_TYPE3 = "我的原创微博";
    public static final String DELETE_WEIBO_TYPE4 = "我的图片微博";
    public static final String DELETE_WEIBO_TYPE5 = "好友圈";
    public static final String DELETE_WEIBO_TYPE6 = "我的收藏";
}
