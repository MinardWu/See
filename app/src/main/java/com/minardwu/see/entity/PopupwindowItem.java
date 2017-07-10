package com.minardwu.see.entity;

/**
 * Created by MinardWu on 2017/7/10.
 */
public class PopupwindowItem {

    private int icon;
    private String item;

    public PopupwindowItem(int icon, String item) {
        this.icon = icon;
        this.item = item;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
