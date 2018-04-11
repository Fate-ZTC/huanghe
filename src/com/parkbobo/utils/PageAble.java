package com.parkbobo.utils;

import java.util.List;

/**
 * Created by lijunhong on 17/11/6.
 */
public class PageAble<T> {

    private final int page;

    private final long start;

    private final int pageSize;

    //总条数
    private long totalCount;

    /**
     * 总页数
     */
    private long totalPage;
    private String text;

    private T reqData;

    private List<T> list;

    public PageAble(int page, int pageSize) {
        this.pageSize = pageSize;

        if (page > 0 && pageSize > 0) {
            this.start = pageSize * (page - 1);
            this.page = page;
        } else {
            this.start = 0L;
            this.page = 1;
        }
    }

    public void setTotalCount(long totalCount) {
        if (totalCount == 0) {
            totalPage = 1;
        } else{
            if (totalCount % pageSize == 0) {
                totalPage = totalCount / pageSize;
            } else {
                totalPage = (totalCount / pageSize) + 1;
            }
        }
        this.totalCount = totalCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public T getReqData() {
        return reqData;
    }

    public void setReqData(T reqData) {
        this.reqData = reqData;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public int getPage() {
        return page;
    }

    public long getStart() {
        return start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
