package com.minardwu.see.entity;

/**
 * Created by Administrator on 2017/6/23.
 */
public class User {

    private String userid;
    private String username;
    private String password;
    private String avatar;
    private int sex;
    private int combineid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getCombineid() {
        return combineid;
    }

    public void setCombineid(int combineid) {
        this.combineid = combineid;
    }
}
