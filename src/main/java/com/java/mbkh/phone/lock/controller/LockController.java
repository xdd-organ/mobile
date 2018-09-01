package com.java.mbkh.phone.lock.controller;

import com.java.common.cache.DeferredResultCache;
import com.java.mbkh.phone.lock.service.LockService;
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

    private static final Logger logger = LoggerFactory.getLogger(LockController.class);

    @Autowired
    private LockService lockService;
    @Autowired
    private DeferredResultCache cache;

    @RequestMapping("lock")
    public String lock(@RequestParam("UID") String uid) {
        logger.info("上锁接收参数UID：{}", uid);
        String lock = lockService.lock(uid);
        logger.info("上锁返回结果UID：{}，res:{}", uid, lock);
        return lock;
    }

    @RequestMapping("unLock")
    public String unLock(@RequestParam("uid") String uid, HttpServletRequest request) {
        Object userId = request.getSession().getAttribute("userId");
        logger.info("解锁接收参数UID：{}，userId:{}", uid, userId);
        String ret = lockService.unLock(uid, userId);
        logger.info("解锁返回结果UID：{}，res:{}", uid, ret);
        return ret;
    }

    @RequestMapping("status")
    public String status(@RequestParam("UID") String uid
            , @RequestParam(value = "TYPE", required = false) String type
            , @RequestParam(value = "STATUS", required = false) String status
            , @RequestParam(value = "RET", required = false) String ret) {
        logger.info("上传状态接收参数UID：{},TYPE:{},TET:{},status:{}", uid, type, ret,status);
        String res = lockService.status(uid, type, ret, status);
        logger.info("上传状态返回结果UID：{}，res:{}", uid, res);
        return res;
    }

}
