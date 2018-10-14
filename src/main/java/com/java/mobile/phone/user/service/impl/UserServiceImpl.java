package com.java.mobile.phone.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        logger.info("更新用户余额：userId:{}, fee:{}", userId, fee);
        int i = userMapper.updateMoney(userId, fee);
        logger.info("更新用户余额结果：", i);
        return i;
    }

    @Override
    public int updateByOpenid(Map<String, Object> params) {
        logger.info("根据openid更新用户参数：{}", JSONObject.toJSONString(params));
        int i = userMapper.updateByOpenid(params);
        logger.info("根据openid更新用户结果：", i);
        return i;
    }

    @Override
    public int updateByUserId(Map<String, Object> params) {
        logger.info("根据userId更新用户参数:{}", JSONObject.toJSONString(params));
        int i = userMapper.updateByUserId(params);
        logger.info("根据userId更新用户参数结果：", i);
        return i;
    }

    @Override
    public int updateScore(String userId, int score) {
        logger.info("更新用户分：userId:{}, fee:{}", userId, score);
        int i = userMapper.updateScore(userId, score);
        logger.info("更新用户分结果：", i);
        return i;
    }

    @Override
    public int updateDeposit(String userId, Integer fee) {
        logger.info("更新用户押金：userId:{}, fee:{}", userId, fee);
        int i = userMapper.updateDeposit(userId, fee);
        logger.info("更新用户押金结果：", i);
        return i;
    }

    @Override
    public PageInfo pageByUser(Map<String, Object> params) {
        logger.info("分页查询用户参数：{}", params);
        PageHelper.startPage(Integer.valueOf(params.get("pageNum").toString()), Integer.valueOf(params.get("pageSize").toString()));
        PageInfo pageInfo = new PageInfo(userMapper.listByUser(params));
        logger.info("分页查询用户返回：{}", pageInfo);
        return pageInfo;
    }

    @Override
    public int totalUser() {
        int res = userMapper.totalUser();
        logger.info("查询总用户返回：{}", res);
        return res;
    }
}
