package com.example.vuphu.newlaundry.Notification;

import java.io.Serializable;

public class OBNotification implements Serializable{

    private String content;
    private String time;
    private boolean unread; // true la chua doc, false la doc;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}

