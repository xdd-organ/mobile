package com.java.mobile.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class DeferredResultCache<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeferredResultCache.class);

    private Map<String, DeferredResult<T>> resultMap;
    private ScheduledExecutorService executor;

    @Value("${timeOut:10000}")
    private Long timeOut;


    public DeferredResultCache() {
        this(2000);
    }

    public DeferredResultCache(int capacity) {
        this.executor = Executors.newScheduledThreadPool(5);
        this.resultMap = new ConcurrentHashMap<>(capacity);
    }

    public void put(String key, DeferredResult<T> deferredResult) {
        deferredResult.onCompletion(new DeferredResultCache.CompletionCallBack(key, this));
        this.resultMap.put(key, deferredResult);
        this.executor.schedule(new DeferredResultCache.TimeoutCleanTask(key, this), timeOut + 5000L, TimeUnit.MILLISECONDS);
    }

    public DeferredResult<T> get(String key) {
        return this.resultMap.remove(key);
    }

    @PreDestroy
    public void destroy() {
        this.resultMap.clear();
        this.resultMap = null;
        this.executor.shutdown();
    }

    private class CompletionCallBack implements Runnable {
        String key;
        DeferredResultCache deferredResultCache;
        public CompletionCallBack(String key, DeferredResultCache deferredResultMiniCache) {
            this.key = key;
            this.deferredResultCache = deferredResultMiniCache;
        }

        public void run() {
            DeferredResult<T> deferredResult = this.deferredResultCache.get(this.key);
            if (deferredResult != null) {
                LOGGER.info(String.format("CompletionCallBack:删除无效的deferredResult,Key为：%s", this.key));
            }

        }
    }

    class TimeoutCleanTask implements Callable<String> {
        String key;
        DeferredResultCache deferredResultCache;
        public TimeoutCleanTask(String key, DeferredResultCache deferredResultMiniCache) {
            this.key = key;
            this.deferredResultCache = deferredResultMiniCache;
        }

        public String call() throws Exception {
            DeferredResult<T> deferredResult = this.deferredResultCache.get(this.key);
            if (deferredResult != null) {
                LOGGER.info(String.format("TimeoutCleanTask:删除无效的deferredResult,Key为：%s", this.key));
            }

            return "Ready!";
        }
    }
}
