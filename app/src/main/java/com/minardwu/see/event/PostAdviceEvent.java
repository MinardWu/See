package com.minardwu.see.event;

/**
 * Created by Administrator on 2017/6/29.
 */
public class PostAdviceEvent {

    int result;

    public PostAdviceEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
