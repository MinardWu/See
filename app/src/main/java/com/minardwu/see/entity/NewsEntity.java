package com.minardwu.see.entity;

/**
 * Created by Administrator on 2017/6/23.
 */
public class NewsEntity {

    private String newsid;
    private String userid;
    private String username;
    private String useravatar;

    public NewsEntity(String newsid, String userid, String username, String useravatar) {
        this.newsid = newsid;
        this.userid = userid;
        this.username = username;
        this.useravatar = useravatar;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseravatar() {
        return useravatar;
    }

    public void setUseravatar(String useravatar) {
        this.useravatar = useravatar;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NewsEntity other = (NewsEntity) obj;
        if(this.getNewsid()!=other.getNewsid())
            return false;
        return true;
    }
}
