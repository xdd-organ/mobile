package com.java.mobile.phone.user.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
public interface UserMapper {

    int insert(Map<String, Object> params);

    Map<String, Object> getByUsername(@Param("username") String username);
}
