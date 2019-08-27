package com.parkbobo.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * map实现缓存辅助类
 * @vesion:wys
 * @author:1.0
 * @date:2019/4/26
 */
public class CacheUtil {
    private static CacheUtil cacheUtil;
    private static Map<String,Object> cacheMap;

    private CacheUtil(){
        cacheMap = new HashMap<String, Object>();
    }

    public static CacheUtil getInstance(){
        if (cacheUtil == null){
            cacheUtil = new CacheUtil();
        }
        return cacheUtil;
    }

    /**
     * 添加缓存
     * @param key
     * @param obj
     */
    public void addCacheData(String key,Object obj){
        cacheMap.put(key,obj);
    }

    /**
     * 取出缓存
     * @param key
     * @return
     */
    public Object getCacheData(String key){
        return cacheMap.get(key);
    }

    /**
     * 清楚缓存
     * @param key
     */
    public void removeCacheData(String key){
        cacheMap.remove(key);
    }
}
