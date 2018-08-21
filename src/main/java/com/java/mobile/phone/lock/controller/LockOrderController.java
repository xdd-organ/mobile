package com.java.mobile.phone.lock.controller;

import com.github.pagehelper.PageInfo;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.lock.service.LockService;
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
 * @date 2018/8/1
 */
@RestController
@RequestMapping("lockOrder")
public class LockOrderController {

    private Logger logger = LoggerFactory.getLogger(LockOrderController.class);

    @Autowired
    private LockOrderService lockOrderService;
    @Autowired
    private LockService lockService;

    @RequestMapping("pageByLockOrder")
    public Result pageByLockOrder(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        PageInfo pageInfo = lockOrderService.pageByLockOrder(params);
        return new Result(100, pageInfo);
    }

    @RequestMapping("unLock")
    public Result unLock(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        String pageInfo = lockOrderService.unLock(params);
        return new Result(100, pageInfo);
    }



}
