package com.example.utacommunityapp.Models;

import com.google.firebase.database.ServerValue;

public class Forum {

    private String Title;
    private String Community;
    private String Post;
    private String userID;
    private Object time;
    private String postID;

    public Forum(String title, String community, String post, String userID) {
        Title = title;
        Community = community;
        Post = post;
        this.userID = userID;
        this.time = ServerValue.TIMESTAMP;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getTitle() {
        return Title;
    }

    public String getCommunity() {
        return Community;
    }

    public String getPost() {
        return Post;
    }

    public String getUserID() {
        return userID;
    }

    public Object getTime() {
        return time;
    }

    public Forum() {

    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setCommunity(String community) {
        Community = community;
    }

    public void setPost(String post) {
        Post = post;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTime(Object time) {
        this.time = time;
    }
}
