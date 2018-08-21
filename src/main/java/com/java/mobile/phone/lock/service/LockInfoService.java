package com.java.mobile.phone.lock.service;

public interface LockInfoService {
    String getLockState(String lockNo);

    int updateLockState(String lockNo, String state);
}
