package com.java.mobile.phone.lock.service.impl;

import com.github.pagehelper.PageInfo;
import com.java.mobile.phone.lock.mapper.LockInfoMapper;
import com.java.mobile.phone.lock.service.LockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> getLockInfo(String qrCodeNo) {
        Map<String, Object> res = lockInfoMapper.getLockInfo(qrCodeNo);
        logger.info("获取锁密码结果:{}，{}", qrCodeNo, res);
        return res;
    }

    @Override
    public String getLockKey(String qrCodeNo) {
        String lockKey = lockInfoMapper.getLockKey(qrCodeNo);
        logger.info("获取锁秘钥结果:{}，{}", qrCodeNo, lockKey);
        return lockKey;
    }

    @Override
    public List<List<String>> importLockInfoData(List<List<String>> res) {
        return null;
    }

    @Override
    public PageInfo<Map<String, Object>> pageByLockInfo(Map<String, Object> params) {
        return null;
    }
}
