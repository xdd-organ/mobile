package com.java.mobile.phone.user.service;

import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
public interface UserService {

    Map<String, Object> insert(Map<String, Object> params);

    Map<String, Object> getByUsername(String username);

    Map<String, Object> getByUserId(String userId);

    Map<String, Object> getByPrimaryKey(Map<String, Object> params);

    int updateMoney(String userId, Integer fee);

    int updateByOpenid(Map<String,Object> params);

    int updateByUserId(Map<String,Object> params);

    int updateDeposit(String userId, Integer integer);

    int updateScore(String userId, int score);

    PageInfo pageByUser(Map<String,Object> params);

    int totalUser();
}
