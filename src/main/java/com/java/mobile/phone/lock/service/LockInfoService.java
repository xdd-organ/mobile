package com.java.mobile.phone.lock.service;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface LockInfoService {
    String getLockState(String lockNo);

    int updateLockState(String lockNo, String state);

    Map<String, Object> getLockInfo(String qrCodeNo);

    String getLockKey(String qrCodeNo);

    List<List<String>> importLockInfoData(List<List<String>> res, Object userId);

    PageInfo<Map<String,Object>> pageByLockInfo(Map<String, Object> params);

    int update(Map<String,Object> params);
}
