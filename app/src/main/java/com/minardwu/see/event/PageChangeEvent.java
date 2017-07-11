package com.minardwu.see.event;

/**
 * Created by Administrator on 2017/6/29.
 */
public class PageChangeEvent {

    int result;

    public PageChangeEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
