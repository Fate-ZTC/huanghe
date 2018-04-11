package com.parkbobo.utils.message;

import java.io.Serializable;

/**
 * 服务器单个信息bean
 * Created by free on 2017/7/27 0027.
 */
public class MessageBean<T>  extends MessageBaseBean implements Serializable {

    public MessageBean() {
        this.setTime(System.currentTimeMillis());
    }

    public MessageBean(T data) {
        this.data = data;
        this.setTime(System.currentTimeMillis());
    }

    /**
     * 被包含的消息实体
     */
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
