package com.minardwu.see.event;

/**
 * Created by MinardWu on 2017/7/18.
 */
public class AddFriendEvent {

    int result;

    public AddFriendEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
