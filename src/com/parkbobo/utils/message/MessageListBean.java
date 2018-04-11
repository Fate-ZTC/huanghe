package com.parkbobo.utils.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器多个信息bean
 * Created by free on 2017/7/27 0027.
 */
public class MessageListBean<T> extends MessageBaseBean implements Serializable {

    public MessageListBean() {
        this.setTime(System.currentTimeMillis());
    }

    public MessageListBean(List<T> data) {
        this.data = data;

        this.setTime(System.currentTimeMillis());
    }

    /**
     * 被包含的多个消息实体
     */
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 增加一个数据
     * @param data 数据
     */
    public void addData(T data){
        if (this.data==null){
            this.data=new ArrayList<T>(5);
        }
        this.data.add(data);
    }
}
