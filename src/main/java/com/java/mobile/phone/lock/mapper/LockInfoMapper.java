package com.java.mobile.phone.lock.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/20
 */
public interface LockInfoMapper {

    int insert(List<Map<String, Object>> params);

    int update(Map<String, Object> params);

    List<Map<String, Object>> listByLockInfo(Map<String, Object> params);

}
