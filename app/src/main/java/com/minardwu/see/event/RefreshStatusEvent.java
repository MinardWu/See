package com.minardwu.see.event;

/**
 * Created by Administrator on 2017/6/29.
 */
public class RefreshStatusEvent {

    int result;

    public RefreshStatusEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
