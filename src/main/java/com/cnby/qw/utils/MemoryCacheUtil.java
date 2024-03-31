package com.cnby.qw.utils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

public class MemoryCacheUtil {

    public static class TIME_CACHE {
        /**
         * 计算请求时长cache
         */
        private static final TimedCache<String, Object> TIME_CACHE = CacheUtil.newTimedCache(60 * 1000L);

        static {
            // 启动定时检查
            TIME_CACHE.schedulePrune(1000);
        }

        public static void put(String key, Object value) {
            TIME_CACHE.put(key, value);
        }

        public static void put(String key, Object value, long timeout) {
            TIME_CACHE.put(key, value, timeout);
        }

        public static Object get(String key) {
            return TIME_CACHE.get(key, Boolean.FALSE);
        }

        public static Object getAndRefresh(String key) {
            return TIME_CACHE.get(key);
        }
    }

    public static class ERROR_CACHE {
        /**
         * 计算请求时长cache
         */
        private static final TimedCache<String, Object> CACHE = CacheUtil.newTimedCache(10 * 1000L);

        static {
            // 启动定时检查
            CACHE.schedulePrune(1000);
        }

        public static void put(String key, Object value) {
            CACHE.put(key, value);
        }

        public static void put(String key, Object value, long timeout) {
            CACHE.put(key, value, timeout);
        }

        public static Object get(String key) {
            return CACHE.get(key, Boolean.FALSE);
        }

        public static Boolean contains(String key) {
            return CACHE.containsKey(key);
        }

        public static Object getAndRefresh(String key) {
            return CACHE.get(key);
        }
    }


    public static class ERROR_TIMES_CACHE {
        /**
         * 一分钟内错误的次数
         */
        private static final TimedCache<String, Object> CACHE = CacheUtil.newTimedCache(60 * 1000L);

        static {
            // 启动定时检查
            CACHE.schedulePrune(1000);
        }

        public static Integer getAntSet(String key) {
            Integer count = 1;
            if (contains(key)) {
                count = (Integer) CACHE.get(key, Boolean.FALSE);
                CACHE.put(key, ++count);
                return count;
            }
            CACHE.put(key, count);
            return count;
        }

        public static Boolean contains(String key) {
            return CACHE.containsKey(key);
        }
    }
}
