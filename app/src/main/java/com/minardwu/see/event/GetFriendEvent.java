package com.minardwu.see.event;

/**
 * Created by Administrator on 2017/6/27.
 */
public class GetFriendEvent {

    String result;

    public GetFriendEvent(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
