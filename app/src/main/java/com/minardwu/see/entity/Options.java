package com.minardwu.see.entity;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Options {

    private String itemTitle;
    private String itemVaule;
    private String avatarUrl;

    public Options(String itemName, String itemVaule, String avatarUrl) {
        this.itemTitle = itemName;
        this.itemVaule = itemVaule;
        this.avatarUrl = avatarUrl;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemVaule() {
        return itemVaule;
    }

    public void setItemVaule(String itemVaule) {
        this.itemVaule = itemVaule;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


}
