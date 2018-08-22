package com.java.mobile.phone.user.service.impl;

import com.java.mobile.phone.user.mapper.UserMapper;
import com.java.mobile.phone.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> insert(Map<String, Object> params) {
        userMapper.insert(params);
        return params;
    }

    @Override
    public Map<String, Object> getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    @Override
    public Map<String, Object> getByUserId(String userId) {
        Map<String, Object> user = userMapper.getByUserId(userId);
        if (user != null) user.remove("password");
        return user;
    }

    @Override
    public Map<String, Object> getByPrimaryKey(Map<String, Object> params) {
        Map<String, Object> user = userMapper.getByPrimaryKey(params);
        if (user != null) user.remove("password");
        return user;
    }

    @Transactional
    @Override
    public int updateMoney(String userId, Integer fee) {
        return userMapper.updateMoney(userId, fee);
    }

    @Override
    public int updateByOpenid(Map<String, Object> params) {
        return userMapper.updateByOpenid(params);
    }

    @Override
    public int updateByUserId(Map<String, Object> params) {
        return userMapper.updateByUserId(params);
    }
}
