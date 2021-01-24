package com.example.utacommunityapp.Models;

import com.google.firebase.database.ServerValue;

public class Comment_Add {

    public Comment_Add() {
    }

    public Comment_Add(String content, String userid, String username) {
        this.content = content;
        this.userid = userid;
        this.username = username;
        this.time = ServerValue.TIMESTAMP;
    }

    public Comment_Add(String content, String userid, String username, Object time) {
        this.content = content;
        this.userid = userid;
        this.username = username;
        this.time = time;
    }

    private String content, userid, username;
    private Object time;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }
}
