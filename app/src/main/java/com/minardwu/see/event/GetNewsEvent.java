package com.minardwu.see.event;

import com.minardwu.see.entity.NewsEntity;

/**
 * Created by Administrator on 2017/6/28.
 */
public class GetNewsEvent {

    private int type;//用来表示是否有新消息
    private NewsEntity news;

    public GetNewsEvent(int type, NewsEntity news) {
        this.type = type;
        this.news = news;
    }

    public int getType() {
        return type;
    }

    public NewsEntity getNews() {
        return news;
    }
}
