package com.minardwu.see.entity;

/**
 * Created by MinardWu on 2017/6/22.
 */
public class UserInfoItem {

    private String itemName;
    private String itemVaule;
    private String avatarUrl;

    public UserInfoItem(String itemName, String itemVaule, String avatarUrl) {
        this.itemName = itemName;
        this.itemVaule = itemVaule;
        this.avatarUrl = avatarUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
