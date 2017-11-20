package com.lingy.lawei.weibo.bean;
public class PostComments {

    private String access_token;
    private String comment;
    private String id;

    public PostComments(String access_token, String comment, String id) {
        this.access_token = access_token;
        this.comment = comment;
        this.id = id;
    }
}
