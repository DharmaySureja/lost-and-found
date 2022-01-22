package com.example.lostandfound;

import java.util.Date;

public class HistoryPost extends HistoryPostId {

   public String post_id,deliverby,desc,deliverMail,receiverMail,receiverUid,post_img;
   public Date timestamp;

    public HistoryPost(String post_id, String deliverby, String desc, String deliverMail, String receiverMail, String receiverUid, String post_img, Date timestamp) {
        this.post_id = post_id;
        this.deliverby = deliverby;
        this.desc = desc;
        this.deliverMail = deliverMail;
        this.receiverMail = receiverMail;
        this.receiverUid = receiverUid;
        this.post_img = post_img;
        this.timestamp = timestamp;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getDeliverby() {
        return deliverby;
    }

    public void setDeliverby(String deliverby) {
        this.deliverby = deliverby;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDeliverMail() {
        return deliverMail;
    }

    public void setDeliverMail(String deliverMail) {
        this.deliverMail = deliverMail;
    }

    public String getReceiverMail() {
        return receiverMail;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getPost_img() {
        return post_img;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public HistoryPost(){
       //Default...
   }
}
