package com.minardwu.see.entity;

/**
 * Created by Administrator on 2017/6/23.
 */
public class NewsEntity {

    private String newsid;
    private String username;
    private String useravatar;

    public NewsEntity(String newsid, String username, String useravatar) {
        this.newsid = newsid;
        this.username = username;
        this.useravatar = useravatar;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }
}
