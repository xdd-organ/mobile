package com.java.mobile.phone.lock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.mobile.phone.lock.mapper.LockInfoMapper;
import com.java.mobile.phone.lock.service.LockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    public List<List<String>> importLockInfoData(List<List<String>> res, Object userId) {
        List<List<String>> ret = new ArrayList<>();
        for (int i = 0; i < res.size(); i++) {
            if (i == 0 || i == 1) {
                continue;
            } else {
                try {
                    lockInfoMapper.insertOne(this.assembleParams(res.get(i), userId));
                } catch (Exception e) {
                    logger.error("锁信息录入失败：" + e.getMessage(), e);
                    ret.add(res.get(i));
                }
            }
        }
        return ret;
    }

    private Map<String,Object> assembleParams(List<String> strings, Object userId) {
        Map<String, Object> res = new HashMap<>();
        res.put("lock_no", strings.get(0));
        res.put("qr_code_no", strings.get(1));
        res.put("lock_mac", strings.get(2));
        res.put("device_no", strings.get(2));
        res.put("address", strings.get(3));
        res.put("lock_pwd", strings.get(4));
        res.put("lock_key", strings.get(5));
        res.put("user_id", userId);
        return res;
    }

    @Override
    public PageInfo<Map<String, Object>> pageByLockInfo(Map<String, Object> params) {
        PageHelper.startPage(Integer.valueOf(params.get("pageNum").toString()), Integer.valueOf(params.get("pageSize").toString()));
        return new PageInfo(lockInfoMapper.listByLockInfo(params));
    }

    @Override
    public int update(Map<String, Object> params) {
        return lockInfoMapper.update(params);
    }
}
