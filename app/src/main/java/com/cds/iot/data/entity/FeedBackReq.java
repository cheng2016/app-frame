package com.cds.iot.data.entity;

public class FeedBackReq {

    /**
     * user_id : id
     * content : 反馈内容
     * device_id : 设备id
     * imgs : 图片二进制
     */

    private String user_id;
    private String content;
    private String device_id;
    private String imgs;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }
}
