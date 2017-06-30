package com.minardwu.see.entity;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Photo {

    private String photoid;
    private String userid;
    private String photoUrl;
    private String photoInfo;
    private int state;

    public Photo(String photoid, String userid, String photoUrl, String photoInfo,int state) {
        this.photoid = photoid;
        this.userid = userid;
        this.photoUrl = photoUrl;
        this.photoInfo = photoInfo;
        this.state = state;
    }

    public String getPhotoid() {
        return photoid;
    }

    public void setPhotoid(String photoid) {
        this.photoid = photoid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(String photoInfo) {
        this.photoInfo = photoInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
