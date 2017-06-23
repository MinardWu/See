package com.minardwu.see.entity;

/**
 * Created by Administrator on 2017/6/23.
 */
public class News {

    private int newsid;
    private int user1id;
    private int user2id;
    private int state;

    public News(int newsid, int user1id, int user2id, int state) {
        this.newsid = newsid;
        this.user1id = user1id;
        this.user2id = user2id;
        this.state = state;
    }

    public int getNewsid() {
        return newsid;
    }

    public void setNewsid(int newsid) {
        this.newsid = newsid;
    }

    public int getUser1id() {
        return user1id;
    }

    public void setUser1id(int user1id) {
        this.user1id = user1id;
    }

    public int getUser2id() {
        return user2id;
    }

    public void setUser2id(int user2id) {
        this.user2id = user2id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
