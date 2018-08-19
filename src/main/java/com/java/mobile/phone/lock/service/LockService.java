package com.java.mobile.phone.lock.service;

/**
 * @author xdd
 * @date 2018/8/1
 */
public interface LockService {

    String lock();

    String unLock(String uid);
}
