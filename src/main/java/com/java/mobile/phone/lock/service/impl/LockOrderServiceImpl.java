package com.java.mobile.phone.lock.service.impl;

import com.java.mobile.phone.lock.mapper.LockOrderMapper;
import com.java.mobile.phone.lock.service.LockOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LockOrderServiceImpl implements LockOrderService {
    @Autowired
    private LockOrderMapper lockOrderMapper;

    @Override
    public int insert(Map<String, Object> params) {
        return lockOrderMapper.insert(params);
    }

    @Override
    public int lock(Map<String, Object> params) {
        return lockOrderMapper.lock(params);
    }
}
