package com.minardwu.see.event;

import com.minardwu.see.entity.User;

/**
 * Created by Administrator on 2017/6/27.
 */
public class GetUserInfoEvent {

    private User user;

    public GetUserInfoEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
