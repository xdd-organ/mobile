package com.java.mobile.phone.lock.controller;

import com.java.mobile.phone.lock.service.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String lock() {
        String lock = lockService.lock();
        return lock;
    }

    @RequestMapping("unLock")
    public String unLock(@RequestParam("uid") String uid) {
        String ret = lockService.unLock(uid);
        return ret;
    }

}
