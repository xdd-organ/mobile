package com.java.mobile.phone.user.controller;

import com.github.pagehelper.PageInfo;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.user.service.TransFlowInfoService;
import com.java.mobile.phone.user.service.UserService;
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

    @Autowired
    private TransFlowInfoService transFlowInfoService;
    @Autowired
    private UserService userService;

    @RequestMapping("pageByTransFlowInfo")
    public Result pageByTransFlowInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        PageInfo pageInfo = transFlowInfoService.pageByTransFlowInfo(params);
        return new Result(100, pageInfo);
    }

    @RequestMapping("recharge")//充值
    public Result recharge(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        return new Result(100, null);
    }

    @RequestMapping("deposit")//押金
    public Result deposit(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        return new Result(100, null);
    }

    @RequestMapping("getUserInfo")//押金
    public Result getUserInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        Map<String, Object> user = userService.getByUserId(userId.toString());
        return new Result(100, user);
    }

    @RequestMapping("bindPhone")//绑定手机
    public Result bindPhone(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        userService.updateByUserId(params);
        return new Result(100, null);
    }


}
