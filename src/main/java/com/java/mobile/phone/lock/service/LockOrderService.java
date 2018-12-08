package com.java.mobile.phone.lock.service;

import com.github.pagehelper.PageInfo;

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
}
