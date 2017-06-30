package com.minardwu.see.event;

import com.avos.avoscloud.AVObject;
import com.minardwu.see.entity.Photo;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public class GetUserPhotoEvent {

    private List<Photo> list;
    private String userid;

    public GetUserPhotoEvent(String userid,List<Photo> list) {
        this.list = list;
        this.userid = userid;
    }

    public List<Photo> getList() {
        return list;
    }

    public void setList(List<Photo> list) {
        this.list = list;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
