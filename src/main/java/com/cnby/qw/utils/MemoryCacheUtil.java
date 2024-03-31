package com.cnby.qw.utils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

public class MemoryCacheUtil {

    private static final TimedCache<String, Object> cache = CacheUtil.newTimedCache(60 * 1000L);

    static {
        // 启动定时检查
        cache.schedulePrune(1000);
    }

    public static void put(String key, Object value) {
        cache.put(key, value);
    }

    public static void put(String key, Object value, long timeout) {
        cache.put(key, value, timeout);
    }

    public static Object get(String key) {
        return cache.get(key, Boolean.FALSE);
    }

    public static Object getAndRefresh(String key) {
        return cache.get(key);
    }
}
