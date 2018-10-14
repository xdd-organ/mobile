package com.java.mobile.phone.user.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
public interface UserMapper {

    int insert(Map<String, Object> params);

    Map<String, Object> getByUsername(@Param("username") String username);

    Map<String,Object> getByUserId(@Param("user_id") String userId);

    Map<String,Object> getByPrimaryKey(Map<String,Object> params);

    int updateByOpenid(Map<String,Object> params);

    int updateByUserId(Map<String,Object> params);

    int updateMoney(@Param("user_id") String userId, @Param("money") Integer money);

    int updateScore(@Param("user_id") String userId, @Param("score") Integer score);

    int updateDeposit(@Param("user_id") String userId, @Param("deposit") Integer deposit);

    List<Map<String,Object>> listByUser(Map<String,Object> params);

    int totalUser();

}
