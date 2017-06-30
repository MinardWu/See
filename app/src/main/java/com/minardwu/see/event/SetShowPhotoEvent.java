package com.minardwu.see.event;

/**
 * Created by Administrator on 2017/6/30.
 */
public class SetShowPhotoEvent {
    int result;
    String photoid;

    public SetShowPhotoEvent(int result, String photoid) {
        this.result = result;
        this.photoid = photoid;
    }

    public int getResult() {
        return result;
    }

    public String getPhotoid() {
        return photoid;
    }
}
