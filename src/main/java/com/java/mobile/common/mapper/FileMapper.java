package com.java.mobile.common.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/7/26
 */
public interface FileMapper {

    int upload(List<Map<String, Object>> params);

    Map<String, Object> getByKey(@Param("key") String key);

    Map<String, Object> getById(@Param("id") String id);
}
