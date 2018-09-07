package com.java.mobile.phone.lock.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.mobile.common.cache.DeferredResultCache;
import com.java.mobile.common.utils.DateUtil;
import com.java.mobile.common.utils.SerialNumber;
import com.java.mobile.common.vo.Result;
import com.java.mobile.phone.lock.constant.TcpConstant;
import com.java.mobile.phone.lock.mapper.LockOrderMapper;
import com.java.mobile.phone.lock.service.LockInfoService;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.user.service.TransFlowInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LockOrderServiceImpl implements LockOrderService {
    private static final String uid = "123456789";

    private static final Logger logger = LoggerFactory.getLogger(LockOrderServiceImpl.class);

    @Autowired
    private LockOrderMapper lockOrderMapper;
    @Autowired
    private LockInfoService lockInfoService;
    @Autowired
    private DeferredResultCache cache;
    private static final int PRICE = 300;

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


        this.temp(params);
        String openUid = String.valueOf(params.get("lock_no"));
        String userId = String.valueOf(params.get("user_id"));
        String state = lockInfoService.getLockState(openUid);
        /*logger.info("解锁设备[{}],状态[{}]", openUid, state);
        if ("0".equals(state)) {
            try (Socket socket = new Socket(InetAddress.getLocalHost(), 8090);// 建立TCP服务,连接本机的TCP服务器
                 InputStream inputStream = socket.getInputStream();// 获得输入流
                 OutputStream outputStream = socket.getOutputStream()) {
                //入库
                Map<String, Object> params2 = new HashMap<>();
                params2.put("lock_no", openUid);
                params2.put("user_id", userId);
                params2.put("insert_author", userId);
                params2.put("update_author", userId);
                this.insert(params);
                lockInfoService.updateLockState(openUid, "3");

                // 写入数据
                outputStream.write(("{\"TYPE\":\"OPEN\",\"UID\":\"" + uid + "\",\"OPEN_UID\":\"" + openUid + "\"}").getBytes());
                byte[] buf = new byte[1024];
                int len = inputStream.read(buf);
                String ret = new String(buf, 0, len);
                logger.info("远程服务器返回：{}", ret);
                //关闭资源
            } catch (Exception e) {
                logger.error("异常：" + e.getMessage(), e);
                return TcpConstant.ERROR;
            }
        } else {
            DeferredResult deferredResult = cache.get(openUid);
            if (deferredResult != null) {
                logger.warn("锁状态不正确超时，lockNo:{}， state:{}", openUid, state);
                deferredResult.setResult(new Result(302, state));
            }
        }*/
        return state;
    }

    @Override
    public int deleteLockOrder(String lockNo) {
        return lockOrderMapper.deleteLockOrder(lockNo);
    }

    @Override
    public int calcLockOrderFee(String lockNo) {
        int res = 0;
        logger.info("计算用床费用，lockNo：{}", lockNo);
        try {
            List<Map<String, Object>> unLockOrder = lockOrderMapper.getUnLockOrder(lockNo);
            if (!CollectionUtils.isEmpty(unLockOrder)) {
                Date date = (Date) unLockOrder.get(0).get("start_time");
                Date now = new Date();
                int hours = DateUtil.calcHours(date, now);
                res = hours * PRICE;
            }
        } catch (Exception e) {
            logger.error("计算用床费用失败，lockNo：" + lockNo, e);
        }
        logger.info("计算用床费用，lockNo：{}，费用：{}", lockNo, res);
        return res;
    }

    @Autowired
    private LockOrderService lockOrderService;
    @Autowired
    private TransFlowInfoService transFlowInfoService;

    private void temp(Map<String, Object> params) {
        String openUid = String.valueOf(params.get("lock_no"));
        String userId = String.valueOf(params.get("user_id"));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("lock_no", openUid);
        params2.put("user_id", userId);
        params2.put("insert_author", userId);
        params2.put("update_author", userId);
        this.insert(params);
        lockInfoService.updateLockState(openUid, "3");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, Object> params3 = new HashMap<>();
        String fee = "-" + lockOrderService.calcLockOrderFee(openUid);
        params3.put("lock_no", openUid);
        params3.put("fee", fee);
        params3.put("type", "1");
        transFlowInfoService.saveTrans(openUid, fee, "0", "消费", "0");
        lockOrderService.lock(params3);
        lockInfoService.updateLockState(openUid, "0");

        DeferredResult deferredResult = cache.get(openUid);
        deferredResult.setResult(new Result(100, 0));
    }
}
