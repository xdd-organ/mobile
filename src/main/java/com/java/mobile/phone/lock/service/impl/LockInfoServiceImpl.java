package com.java.mobile.phone.lock.service.impl;

import com.java.mobile.phone.lock.mapper.LockInfoMapper;
import com.java.mobile.phone.lock.service.LockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LockInfoServiceImpl implements LockInfoService {

    private static final Logger logger = LoggerFactory.getLogger(LockInfoServiceImpl.class);

    @Autowired
    private LockInfoMapper lockInfoMapper;

    @Override
    public String getLockState(String lockNo) {
        logger.info("获取锁状态:lockNo：{}", lockNo);
        String lockState = lockInfoMapper.getLockState(lockNo);
        logger.info("获取锁状态返回:{}", lockNo);
        return lockState;
    }

    @Override
    public int updateLockState(String lockNo, String state) {
        logger.info("更新锁状态：lockNo:{},state:{}", lockNo, state);
        int i = lockInfoMapper.updateLockState(lockNo, state);
        logger.info("更新锁状态结果:{}", i);
        return i;
    }
}
