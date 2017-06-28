package com.minardwu.see.event;

/**
 * Created by Administrator on 2017/6/28.
 */
public class SendOrReadNewsEvent {

    int result;

    public SendOrReadNewsEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
