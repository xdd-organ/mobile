package com.java.mobile.phone.lock.controller;

import com.java.mobile.common.cache.DeferredResultCache;
import com.java.mobile.common.weixin.AES2;
import com.java.mobile.phone.lock.service.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/1
 */
@RestController
@CrossOrigin
public class LockController {

    private static final Logger logger = LoggerFactory.getLogger(LockController.class);

    @Autowired
    private LockService lockService;
    @Autowired
    private DeferredResultCache cache;
    @Value("${userKey:testtest}")
    private String userKey;

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

    @RequestMapping("location")
    public String location(@RequestBody Map<String, Object> body) {
        logger.info("锁上传位置信息:{}", body);
        String sign = (String) body.remove("sign");
        boolean b = AES2.verifyByMap(body, userKey, sign);
        String cmd = String.valueOf(body.get("cmd"));
        String lockStatus = String.valueOf(body.get("lockstatus"));
        if ("close".equals(cmd) && "0".equals(lockStatus)) {
            //关锁
            logger.info("关锁");
            String deviceId = String.valueOf(body.get("deviceid"));
            String lock = lockService.lock(deviceId);
            logger.info("关锁返回：{}", lock);
        }
        logger.info("锁上传位置信息验签结果:{}", b);
        return body.toString();
    }

    @RequestMapping("order")
    public String order(@RequestBody Map<String, Object> body) {
        logger.info("锁接收指令信息:{}", body);
        String sign = (String) body.remove("sign");
        boolean b = AES2.verifyByMap(body, userKey, sign);
        logger.info("锁上传指令信息验签结果:{}", b);
        return body.toString();
    }

}
