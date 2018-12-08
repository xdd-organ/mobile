package com.java.mobile.phone.lock.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.java.mobile.common.cache.DeferredResultCache;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.jms.service.LockBeforeSmsService;
import com.java.mobile.phone.lock.service.LockInfoService;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.lock.service.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/1
 */
@RestController
@RequestMapping("lockOrder")
@CrossOrigin
public class LockOrderController {

    private static final Logger logger = LoggerFactory.getLogger(LockOrderController.class);

    @Autowired
    private LockOrderService lockOrderService;
    @Autowired
    private DeferredResultCache cache;
    @Autowired
    private LockInfoService lockInfoService;
    @Value("${timeout:10000}")
    private Long timeout;

    @RequestMapping("pageByLockOrder")
    public Result pageByLockOrder(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        logger.info("分页查询订单参数：{},userId:{}", JSONObject.toJSONString(params), userId);
//        params.put("user_id", userId);
        PageInfo pageInfo = lockOrderService.pageByLockOrder(params);
        logger.info("分页查询订单返回：{}", JSONObject.toJSONString(pageInfo));
        return new Result(100, pageInfo);
    }

    @RequestMapping("unLock")
    public DeferredResult unLock(@RequestBody final Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        final Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        logger.info("解锁参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        final DeferredResult deferredResult = new DeferredResult(timeout);
        final String lockNo = String.valueOf(params.get("lock_no"));
        cache.put(lockNo, deferredResult);
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                DeferredResult deferredResult1 = cache.get(lockNo);
                if (deferredResult1 != null) {
                    logger.warn("解锁超时，lockNo:{}", lockNo);
                    deferredResult1.setResult(new Result(408));
                    lockInfoService.updateLockState(lockNo, "0");
                    lockOrderService.deleteLockOrder(lockNo);
                    lockOrderService.refundLockOrder(lockNo);
                }
            }
        });
        String state = lockOrderService.unLock(params);
        logger.info("解锁返回：{}", state);
        return deferredResult;
    }
    @RequestMapping("unLockV2")
    public DeferredResult unLockV2(@RequestBody final Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        final Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        logger.info("解锁参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        final DeferredResult deferredResult = new DeferredResult(timeout);
        final String lockNo = String.valueOf(params.get("lock_no"));
        cache.put(lockNo, deferredResult);
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                DeferredResult deferredResult1 = cache.get(lockNo);
                if (deferredResult1 != null) {
                    logger.warn("解锁超时，lockNo:{}", lockNo);
                    deferredResult1.setResult(new Result(408));
                    lockInfoService.updateLockState(lockNo, "0");
                    lockOrderService.deleteLockOrder(lockNo);
                    lockOrderService.refundLockOrder(lockNo);
                }
            }
        });
        cache.put(lockNo, deferredResult);
        String state = lockOrderService.unLockV2(params);
        logger.info("解锁返回：{}", state);
        return deferredResult;
    }

    @RequestMapping("getLockInfo")
    public Result getLockInfo(@RequestBody final Map<String, Object> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        final Object userId = session.getAttribute("userId");
        params.put("user_id", userId);
        logger.info("获取锁密码参数：{},userId:{}", JSONObject.toJSONString(params), userId);
        Map<String, Object> res = lockInfoService.getLockInfo(String.valueOf(params.get("qr_code_no")));
        logger.info("获取锁密码返回：{}", res);
        return new Result(100, res);
    }

}
