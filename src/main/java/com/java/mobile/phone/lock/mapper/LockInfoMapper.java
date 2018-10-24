package com.java.mobile.phone.lock.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/20
 */
public interface LockInfoMapper {

    int insert(List<Map<String, Object>> params);

    int insertOne(Map<String, Object> params);

    int update(Map<String, Object> params);

    List<Map<String, Object>> listByLockInfo(Map<String, Object> params);

    String getLockState(@Param("lock_no") String lockNo);

    int updateLockState(@Param("lock_no") String lockNo, @Param("state") String state);

    int updateLockStatus(@Param("lock_no")String lockNo, @Param("status")String status);

    Map<String, Object> getLockInfo(@Param("qr_code_no") String qrCodeNo);

    String getLockKey(@Param("qr_code_no") String qrCodeNo);

}
