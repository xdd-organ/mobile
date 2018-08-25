package com.java.mobile.common.cache;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeferredResultCache<T> {

    private static final Map<String, DeferredResult> cache = new HashMap<>();

    public void put(String uid, DeferredResult<T> deferredResult) {
        cache.put(uid, deferredResult);
    }

    public DeferredResult<T> get(String uid) {
        return cache.remove(uid);
    }

    public DeferredResult<T> remove(String uid) {
        return cache.remove(uid);
    }
}
