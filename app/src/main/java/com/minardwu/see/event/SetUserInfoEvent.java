package com.minardwu.see.event;

/**
 * Created by Administrator on 2017/6/28.
 */
public class SetUserInfoEvent {

    int type;
    int result;

    public SetUserInfoEvent(int type, int result) {
        this.type = type;
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public int getResult() {
        return result;
    }
}
