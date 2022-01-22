package com.example.lostandfound;

public class ProfilePost extends ProfilePostId {
    public String claim_user_id;
    public String post_id;
    public String post_img_url;

    public String getClaim_user_uid() {
        return claim_user_uid;
    }

    public void setClaim_user_uid(String claim_user_uid) {
        this.claim_user_uid = claim_user_uid;
    }

    public String claim_user_uid ,desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClaim_user_id() {
        return claim_user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getPost_img_url() {
        return post_img_url;
    }

    public ProfilePost(String claim_user_id, String post_id, String post_img_url, String claim_user_uid, String desc) {
        this.claim_user_id = claim_user_id;
        this.post_id = post_id;
        this.post_img_url = post_img_url;
        this.claim_user_uid = claim_user_uid;
        this.desc = desc;
    }

    public ProfilePost(){
        //Default Constructor
    }

    public void setClaim_user_id(String claim_user_id) {
        this.claim_user_id = claim_user_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setPost_img_url(String post_img_url) {
        this.post_img_url = post_img_url;
    }



}
