package com.java.mobile.phone.lock.service;

import com.github.pagehelper.PageInfo;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface LockOrderService {
    
    int insert(Map<String, Object> params);

    int lock(Map<String,Object> params);

    PageInfo pageByLockOrder(Map<String,Object> params);

    String unLock(Map<String, Object> params);

    String unLockV2(Map<String, Object> params);

    int deleteLockOrder(String lockNo);

    int calcLockOrderFee(String lockNo);

    int refundLockOrder(String lockNo);

    int totalOrder();

    int totalUseDevice();

    long totalTime();

    List<Map<String, Object>> unPayLockOrder(String userId);

    int update(Map<String, Object> params);

    void exportLockOrderData(Map<String,Object> params, OutputStream outputStream) throws Exception;

    Map<String,Object> sumByLockOrder(Map<String, Object> params);
}
