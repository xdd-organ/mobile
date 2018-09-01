package com.java.mbkh.phone.lock.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.common.cache.DeferredResultCache;
import com.java.common.jms.AdvancedGroupQueueSender;
import com.java.common.utils.SerialNumber;
import com.java.mbkh.common.vo.Result;
import com.java.mbkh.phone.jms.constant.ServerConstant;
import com.java.mbkh.phone.jms.vo.LockReturn;
import com.java.mbkh.phone.lock.constant.TcpConstant;
import com.java.mbkh.phone.lock.mapper.LockOrderMapper;
import com.java.mbkh.phone.lock.service.LockInfoService;
import com.java.mbkh.phone.lock.service.LockOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class LockOrderServiceImpl implements LockOrderService {
    private static final Logger logger = LoggerFactory.getLogger(LockOrderServiceImpl.class);

    @Autowired
    private LockOrderMapper lockOrderMapper;
    @Autowired
    private LockInfoService lockInfoService;
    @Autowired
    private DeferredResultCache cache;
    @Resource(name = "messageSender")
    private AdvancedGroupQueueSender messageSender;

    @Override
    public int insert(Map<String, Object> params) {
        params.put("order_no", SerialNumber.getRandomNum(32));
        logger.info("新增订单参数:{}", JSONObject.toJSONString(params));
        return lockOrderMapper.insert(params);
    }

    @Override
    public int lock(Map<String, Object> params) {
        return lockOrderMapper.lock(params);
    }

    @Override
    public PageInfo pageByLockOrder(Map<String, Object> params) {
        PageHelper.startPage(Integer.valueOf(params.get("pageNum").toString()), Integer.valueOf(params.get("pageSize").toString()));
        return new PageInfo(lockOrderMapper.listByLockOrder(params));
    }

    @Override
    public String unLock(Map<String, Object> params) {
        logger.info("解锁参数:{}", JSONObject.toJSONString(params));
        String openUid = String.valueOf(params.get("lock_no"));
        String userId = String.valueOf(params.get("user_id"));
        String state = lockInfoService.getLockState(openUid);
        logger.info("解锁设备[{}],状态[{}]", openUid, state);
        if ("0".equals(state)) {
            try {
                //入库
                params.put("user_id", userId);
                params.put("insert_author", userId);
                params.put("update_author", userId);
                this.insert(params);
                lockInfoService.updateLockState(openUid, "3");
                String rsp = new LockReturn(openUid, TcpConstant.OPEN, TcpConstant.OK).toString();
                logger.info("发送解锁消息：{}", rsp);
                messageSender.sendMsg(rsp, ServerConstant.DEFAULT);
                logger.info("发送解锁消息成功：{}", openUid);
                //关闭资源
            } catch (Exception e) {
                logger.error("异常：" + e.getMessage(), e);
                return TcpConstant.ERROR;
            }
        } else {
            DeferredResult deferredResult = cache.get(openUid);
            if (deferredResult != null) {
                logger.warn("所状态不正确，lockNo:{}，state：{}", openUid, state);
                deferredResult.setResult(new Result(100, state));
            }
        }
        return state;
    }

    @Override
    public int deleteLockOrder(String lockNo) {
        return lockOrderMapper.deleteLockOrder(lockNo);
    }
}
