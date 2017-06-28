package com.minardwu.see.entity;

public class MultipleView {

    int type;
    private String itemTitle;
    private String itemVaule;
    private String avatarUrl;

    public MultipleView(int type, String itemTitle, String itemVaule, String avatarUrl) {
        this.type = type;
        this.itemTitle = itemTitle;
        this.itemVaule = itemVaule;
        this.avatarUrl = avatarUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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