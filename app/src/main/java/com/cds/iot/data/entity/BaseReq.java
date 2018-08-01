package com.cds.iot.data.entity;

public class BaseReq<T> {

    public BaseReq(T content) {
        this.content = content;
    }

    /**
     * content : {"user_id":"用户id"}
     */

    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
