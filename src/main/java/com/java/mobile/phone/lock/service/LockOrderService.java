package com.java.mobile.phone.lock.service;

import java.util.Map;

public interface LockOrderService {
    
    int insert(Map<String, Object> params);

    int lock(Map<String,Object> params);
}
