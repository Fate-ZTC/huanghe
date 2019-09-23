package com.system.model;

/**
 * @ClassName Result
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:47
 * @Version 1.0
 **/
public class Result<T> {
    private Boolean status;
    private Long time;
    private Integer code;
    private String properties;
    private T data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
