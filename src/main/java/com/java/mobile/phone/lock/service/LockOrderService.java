package com.java.mobile.phone.lock.service;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface LockOrderService {
    
    int insert(Map<String, Object> params);

    int lock(Map<String,Object> params);

    PageInfo pageByLockOrder(Map<String,Object> params);

    String unLock(Map<String, Object> params);
}
