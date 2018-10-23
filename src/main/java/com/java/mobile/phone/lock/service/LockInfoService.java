package com.java.mobile.phone.lock.service;

import java.util.Map;

public interface LockInfoService {
    String getLockState(String lockNo);

    int updateLockState(String lockNo, String state);

    Map<String, Object> getLockInfo(String qrCodeNo);

    String getLockKey(String qrCodeNo);
}
