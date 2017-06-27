package com.minardwu.see.event;

/**
 * Created by Administrator on 2017/6/27.
 */
public class RegisterEvent {

    int result;

    public RegisterEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
