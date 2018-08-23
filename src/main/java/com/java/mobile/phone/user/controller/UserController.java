package com.java.mobile.phone.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.user.service.TransFlowInfoService;
import com.java.mobile.phone.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
@RequestMapping("user")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TransFlowInfoService transFlowInfoService;
    @Autowired
    private UserService userService;

    @RequestMapping("pageByTransFlowInfo")
    public Result pageByTransFlowInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("分页查询流水参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        PageInfo pageInfo = transFlowInfoService.pageByTransFlowInfo(params);
        logger.info("分页查询流水返回：{},userId:{}", JSONObject.toJSONString(pageInfo));
        return new Result(100, pageInfo);
    }

    @RequestMapping("recharge")//充值
    public Result recharge(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("充值参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        return new Result(100, null);
    }

    @RequestMapping("deposit")//押金
    public Result deposit(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("缴纳押金参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        return new Result(100, null);
    }

    @RequestMapping("getUserInfo")//押金
    public Result getUserInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("获取用户信息参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        Map<String, Object> user = userService.getByUserId(userId.toString());
        logger.info("获取用户信息返回：{}", JSONObject.toJSONString(user));
        return new Result(100, user);
    }

    @RequestMapping("bindPhone")//绑定手机
    public Result bindPhone(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("绑定手机参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        params.put("user_id", userId);
        int i = userService.updateByUserId(params);
        logger.info("绑定手机返回：{}", i);
        return new Result(100, null);
    }


}
