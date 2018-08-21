package com.java.mobile.phone.lock.service.impl;

import com.java.mobile.phone.lock.mapper.LockInfoMapper;
import com.java.mobile.phone.lock.service.LockInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockInfoServiceImpl implements LockInfoService {
    @Autowired
    private LockInfoMapper lockInfoMapper;

    @Override
    public String getLockState(String lockNo) {
        return lockInfoMapper.getLockState(lockNo);
    }

    @Override
    public int updateLockState(String lockNo, String state) {
        return lockInfoMapper.updateLockState(lockNo, state);
    }
}
