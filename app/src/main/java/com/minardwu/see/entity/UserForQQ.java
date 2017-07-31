package com.minardwu.see.entity;

/**
 * Created by MinardWu on 2017/7/24.
 */
public class UserForQQ {

    String qqID;
    String username;
    String avatarUrl;
    String sex;

    public UserForQQ(){}

    public UserForQQ(String qqID, String username, String avatarUrl, String sex) {
        this.qqID = qqID;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.sex = sex;
    }

    public String getQqID() {
        return qqID;
    }

    public void setQqID(String qqID) {
        this.qqID = qqID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
