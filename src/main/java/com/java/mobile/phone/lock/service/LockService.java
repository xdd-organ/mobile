package com.java.mobile.phone.lock.service;

/**
 * @author xdd
 * @date 2018/8/1
 */
public interface LockService {

    String lock(String uid);

    String unLock(String uid, Object userId);

    String status(String uid, String type, String ret, String status);
}
