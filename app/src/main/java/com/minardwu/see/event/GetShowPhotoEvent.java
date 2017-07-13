package com.minardwu.see.event;

import com.minardwu.see.entity.Photo;

/**
 * Created by MinardWu on 2017/7/13.
 */
public class GetShowPhotoEvent {

    Photo photo;

    public GetShowPhotoEvent(Photo photo) {
        this.photo = photo;
    }

    public Photo getPhoto() {
        return photo;
    }
}
