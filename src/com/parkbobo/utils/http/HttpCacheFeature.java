package com.parkbobo.utils.http;

/**
 * Created by free on 16-9-6.
 */
public enum HttpCacheFeature implements HttpFeature{
    hasCache(60), noCache(0);

    HttpCacheFeature(int time){
        this.cache_time=time;
    }

    private int cache_time;

    public int getCache_time() {
        return cache_time;
    }

    public void setCache_time(int cache_time) {
        this.cache_time = cache_time;
    }
}
