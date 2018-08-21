package com.java.mobile.phone.lock.controller;

import com.java.mobile.phone.lock.service.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xdd
 * @date 2018/8/1
 */
@RestController
public class LockController {

    private Logger logger = LoggerFactory.getLogger(LockController.class);

    @Autowired
    private LockService lockService;

    @RequestMapping("lock")
    public String lock(@RequestParam("UID") String uid) {
        String lock = lockService.lock(uid);
        return lock;
    }

    @RequestMapping("unLock")
    public String unLock(@RequestParam("uid") String uid, HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("userId");
        String ret = lockService.unLock(uid, userId);
        return ret;
    }

    @RequestMapping("status")
    public String status(@RequestParam("UID") String uid
            , @RequestParam(value = "TYPE", required = false) String status
            , @RequestParam(value = "RET", required = false) String type) {
        String ret = lockService.status(uid, status, type);
        return ret;
    }

}
