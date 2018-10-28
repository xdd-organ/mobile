package com.java.mobile.phone.lock.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/20
 */
public interface LockOrderMapper {

    int insert(Map<String, Object> params);

    int update(Map<String, Object> params);

    List<Map<String, Object>> listByLockOrder(Map<String, Object> params);

    int lock(Map<String, Object> params);

    List<Map<String, Object>> getUnLockOrder(String uid);

    int deleteLockOrder(String lockNo);

    int totalOrder();

    int totalUseDevice();

    int totalTime();

    Map<String, Object> getByLockOrder(Map<String, Object> params);

}
