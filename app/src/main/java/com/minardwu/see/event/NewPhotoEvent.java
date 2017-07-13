package com.minardwu.see.event;

/**
 * Created by MinardWu on 2017/7/13.
 */
public class NewPhotoEvent {

    int result;

    public NewPhotoEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

}
