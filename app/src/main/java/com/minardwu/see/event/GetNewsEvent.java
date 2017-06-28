package com.minardwu.see.event;

import com.minardwu.see.entity.NewsEntity;

/**
 * Created by Administrator on 2017/6/28.
 */
public class GetNewsEvent {

    private NewsEntity news;

    public GetNewsEvent(NewsEntity news) {
        this.news = news;
    }

    public NewsEntity getNews() {
        return news;
    }
}
