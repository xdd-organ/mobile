package com.java.mobile.phone.lock.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.mobile.common.cache.DeferredResultCache;
import com.java.mobile.common.jms.AdvancedGroupQueueSender;
import com.java.mobile.common.sms.AliSmsService;
import com.java.mobile.common.utils.DateUtil;
import com.java.mobile.common.utils.ExcelUtil;
import com.java.mobile.common.utils.SerialNumber;
import com.java.mobile.common.utils.httpclient.HttpClientUtil;
import com.java.mobile.common.utils.httpclient.HttpResult;
import com.java.mobile.common.vo.Result;
import com.java.mobile.common.weixin.AES2;
import com.java.mobile.phone.dictionary.service.DictionaryService;
import com.java.mobile.phone.lock.constant.TcpConstant;
import com.java.mobile.phone.lock.mapper.LockOrderMapper;
import com.java.mobile.phone.lock.service.LockInfoService;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.user.service.TransFlowInfoService;
import com.java.mobile.phone.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class LockOrderServiceImpl implements LockOrderService {
    private static final String uid = "123456789";

    private static final Logger logger = LoggerFactory.getLogger(LockOrderServiceImpl.class);

    @Autowired
    private LockOrderMapper lockOrderMapper;
    @Autowired
    private LockInfoService lockInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransFlowInfoService transFlowInfoService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private DeferredResultCache cache;
    @Autowired
    private AliSmsService aliSmsService;
    @Autowired
    private AdvancedGroupQueueSender lockBeforeSmsSender;
    @Autowired
    private HttpClientUtil httpClientUtil;
    @Value("${lockUrl:http://gps.dola520.com:8888}")
    private String lockUrl;
    @Value("${lockUrl2:http://gps.nokelock.com:30008/gps/commandOrder}")
    private String lockUrl2;
    @Value("${userKey:testtest}")
    private String userKey;
    @Value("${userId:20180928test01}")
    private String userId;
    @Value("${token:3c059f186eed40309471db46169f73c2}")
    private String token;
    @Value("${companyId:217}")
    private String companyId;

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
        String fee = params.get("fee").toString();
        transFlowInfoService.insert(openUid, fee, "0", "消费", "0", userId);

        String state = lockInfoService.getLockState(openUid);
        logger.info("解锁设备[{}],状态[{}]", openUid, state);
        try  {
            //入库
            Map<String, Object> params2 = new HashMap<>();
            params2.put("lock_no", openUid);
            params2.put("user_id", userId);
            params2.put("fee", params.get("fee"));
            params2.put("insert_author", userId);
            params2.put("update_author", userId);
            this.insert(params);
            lockInfoService.updateLockState(openUid, "3");

            //发送短信
            this.sendUnLockSms(userId, params);
            DeferredResult deferredResult = cache.get(openUid);
            if (deferredResult != null) {
                logger.warn("锁状态不正确超时，lockNo:{}， state:{}", openUid, state);
                deferredResult.setResult(new Result(100, state));
            }
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage(), e);
            return TcpConstant.ERROR;
        }

        //        this.temp(params);
/*
        if ("0".equals(state)) {
            try (Socket socket = new Socket(InetAddress.getLocalHost(), 8090);// 建立TCP服务,连接本机的TCP服务器
                 InputStream inputStream = socket.getInputStream();// 获得输入流
                 OutputStream outputStream = socket.getOutputStream()) {
                //入库
                Map<String, Object> params2 = new HashMap<>();
                params2.put("lock_no", openUid);
                params2.put("user_id", userId);
                params2.put("fee", params.get("fee"));
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
        }
*/
        return state;
    }

    private void sendUnLockSms(String userId, Map<String, Object> params) {
        try {
            Map<String, Object> user = userService.getByUserId(userId);
            aliSmsService.sendSms(String.valueOf(user.get("telphone")), "SMS_149390462", null);
            logger.info("发送开锁通知完成：{}", userId);
            if (params != null && params.get("hours") != null) {
                Integer hours = Integer.valueOf(params.get("hours").toString());
                int min = (hours * 60) - 15;
                params.put("telphone", user.get("telphone"));
                lockBeforeSmsSender.sendCache(JSONObject.toJSONString(params), "default", min * 60 * 1000);
                logger.info("关锁前15分钟MQ消息发送完成：{}， {}", min, params);
            }
        } catch (Exception e) {
            logger.error("发送开锁消息失败：" + e.getMessage(), e);
        }
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
                Integer fee = Math.abs(Integer.valueOf(unLockOrder.get(0).get("fee").toString()));//已交费用
                logger.info("计算用床费用，lockNo：{}，已交费用：{}", lockNo, fee);
                String userId = unLockOrder.get(0).get("user_id").toString();//已交费用
                Date now = new Date();
                int hours = DateUtil.calcHours(date, now);
                logger.info("计算用床费用，lockNo：{}，使用时间：{}，开始时间：{}，结束时间：{}", lockNo, hours, DateUtil.getDateForPattern(null, date), DateUtil.getDateForPattern(null, now));
                res = this.calcActualFee(hours, lockInfoService.getUnitPrice(lockNo));
                logger.info("计算用床费用，lockNo：{}，实际费用：{}", lockNo, res);
                int actualFee = fee - res;
                if (actualFee > 0) {
                    //退钱
                    transFlowInfoService.insert(lockNo, actualFee + "", "1", "实际使用比计划时间少，退费到余额", "0", userId);
                } if(actualFee < 0) {
                    //余额扣钱
                    this.handlerDiffFee(actualFee, userId, unLockOrder.get(0).get("id"));
                    transFlowInfoService.insert(lockNo, actualFee + "", "0", "实际使用比计划时间多，余额扣除", "0", userId);
                }
                userService.updateScore(userId, res / 100);
                this.sendLockSms(userId);
            }
        } catch (Exception e) {
            logger.error("计算用床费用失败，lockNo：" + lockNo, e);
        }
        logger.info("计算用床费用，lockNo：{}，费用：{}", lockNo, res);
        return res;
    }

    private void handlerDiffFee(int actualFee, String userId, Object id) {
        logger.info("计算订单是否金额不足，差价：{}，用户id：{}，订单id：{}", actualFee, userId, id);
        Map<String, Object> user = userService.getByUserId(userId);
        int money = Integer.valueOf(user.get("money").toString()).intValue();
        logger.info("计算订单是否金额不足，用户余额：{}，用户id：{}", money, userId);
        int diffFee = actualFee;
        if (money > 0) {
            diffFee = money + actualFee;
        }
        logger.info("计算订单是否金额不足，差价：{}，用户id：{}", diffFee, userId);
        if (diffFee < 0) {
            Map<String, Object> feeParams = new HashMap<>();
            feeParams.put("id", id);
            feeParams.put("diff_fee", diffFee);
            lockOrderMapper.update(feeParams);
        }
    }

    private void sendLockSms(String userId) {
        try {
            Map<String, Object> user = userService.getByUserId(userId);
            aliSmsService.sendSms(String.valueOf(user.get("telphone")), "SMS_149385609", null);
            logger.info("发送关锁通知完成：{}", userId);
        } catch (Exception e) {
            logger.error("发送关锁短信失败：" + e.getMessage(), e);
        }
    }

    private int calcActualFee(int hours, int price) {
        int temp = price * 10;
        int day = hours / 24;
        int hour = hours % 24;
        int dayPrice = day * temp;
        int hourPrice = hour * price;
        if (hourPrice > temp) {
            hourPrice = temp;
        }
        return dayPrice + hourPrice;
    }

    private void temp(Map<String, Object> params) {
        String openUid = String.valueOf(params.get("lock_no"));
        String userId = String.valueOf(params.get("user_id"));
        Map<String, Object> params2 = new HashMap<>();
        params2.put("lock_no", openUid);
        params2.put("user_id", userId);
        params2.put("insert_author", userId);
        params2.put("update_author", userId);
        params2.put("fee", params.get("fee"));
        this.insert(params);
        lockInfoService.updateLockState(openUid, "3");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, Object> params3 = new HashMap<>();
        String fee = "-" + this.calcLockOrderFee(openUid);
        params3.put("lock_no", openUid);
        params3.put("fee", fee);
        params3.put("type", "1");
        this.lock(params3);
        lockInfoService.updateLockState(openUid, "0");
        DeferredResult deferredResult = cache.get(openUid);
        deferredResult.setResult(new Result(100, 0));
    }

    @Override//开锁失败，退回金额
    public int refundLockOrder(String lockNo) {
        List<Map<String, Object>> unLockOrder = lockOrderMapper.getUnLockOrder(lockNo);
        if (!CollectionUtils.isEmpty(unLockOrder)) {
            Map<String, Object> order = unLockOrder.get(0);
            Integer fee = Math.abs(Integer.valueOf(order.get("fee").toString()));
            String userId = order.get("user_id").toString();
            //退回余额
            userService.updateMoney(userId, fee);
            transFlowInfoService.insert(lockNo, fee.toString(), "1", "开锁失败退费", "0", userId);
            return 1;
        }
        return -1;
    }

    @Override
    public int totalOrder() {
        int res = lockOrderMapper.totalOrder();
        logger.info("总订单数：{}", res);
        return res;
    }

    @Override
    public int totalUseDevice() {
        int res = lockOrderMapper.totalUseDevice();
        logger.info("正在使用订单数：{}", res);
        return res;
    }

    @Override
    public long totalTime() {
        int res = lockOrderMapper.totalTime();
        logger.info("订单总时间：{}", res);
        return res;
    }

    @Override
    public String unLockV2(Map<String, Object> params) {
        try {
            logger.info("解锁参数:{}", JSONObject.toJSONString(params));
            String lockNo = params.get("lock_no").toString();
            String paramJson = this.assembleOrderParams2(lockNo);
            Map<String, String> headers = this.assembleHeaders();
            logger.info("发送GPS远程解锁数据：{}", paramJson);
            HttpResult httpResult = httpClientUtil.doPostJson(lockUrl2, paramJson, headers);
            logger.info("发送GPS远程开锁返回结果：" + httpResult.getBody() + "==" + httpResult.getCode());
            if (httpResult != null && 200 == httpResult.getCode().intValue() && httpResult.getBody().contains("\"code\":200")) {
                String openUid = String.valueOf(params.get("lock_no"));
                String userId = String.valueOf(params.get("user_id"));
                String fee = params.get("fee").toString();
                transFlowInfoService.insert(openUid, fee, "0", "消费", "0", userId);

                String state = lockInfoService.getLockState(openUid);
                logger.info("解锁设备[{}],状态[{}]", openUid, state);
                try {
                    //入库
                    Map<String, Object> params2 = new HashMap<>();
                    params2.put("lock_no", openUid);
                    params2.put("user_id", userId);
                    params2.put("fee", params.get("fee"));
                    params2.put("insert_author", userId);
                    params2.put("update_author", userId);
                    this.insert(params);
                    lockInfoService.updateLockState(openUid, "3");

                    //发送短信
                    this.sendUnLockSms(userId, params);
                    DeferredResult deferredResult = cache.get(openUid);
                    if (deferredResult != null) {
                        logger.warn("锁状态不正确超时，lockNo:{}， state:{}", openUid, state);
                        deferredResult.setResult(new Result(100, state));
                    }
                } catch (Exception e) {
                    logger.error("异常：" + e.getMessage(), e);
                    return TcpConstant.ERROR;
                }
                return state;
            }
        } catch (Exception e) {
            logger.error("开锁异常：" + e.getMessage(), e);
        }
        return TcpConstant.ERROR;
    }

    private Map<String,String> assembleHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("token", token);
        headers.put("companyId", companyId);
        return headers;
    }

    private String assembleOrderParams(String lockNo) {
        String serialNum = SerialNumber.generateRandomSerial(9);
        String cmd = "open";
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userId);
        params.put("cmd", cmd);
        params.put("deviceid", lockNo);
        params.put("serialnum", Long.valueOf(serialNum));

        String plainText = userId + cmd + lockNo + serialNum;
        String sign = null;
        try {
            sign = AES2.signMd5(plainText + userKey);
            sign = sign.toLowerCase();
        } catch (UnsupportedEncodingException e) {
            logger.error("MD5异常：" + e.getMessage(), e);
        }
        params.put("sign", sign);
        return JSONObject.toJSONString(params);
    }
    private String assembleOrderParams2(String lockNo) {
        String serialNum = SerialNumber.generateRandomSerial(9);
        Map<String, String> params = new HashMap<>();
        params.put("companyId", companyId);
        params.put("order", "open");
        params.put("deviceId", lockNo);
        params.put("buzzerType", "0");
        params.put("orderNumber", serialNum);
        return JSONObject.toJSONString(params);
    }

    @Override
    public List<Map<String, Object>> unPayLockOrder(String userId) {
        return lockOrderMapper.unPayLockOrder(userId);
    }

    @Override
    public int update(Map<String, Object> params) {
        return lockOrderMapper.update(params);
    }

    @Override
    public void exportLockOrderData(Map<String, Object> params, OutputStream outputStream) throws Exception{
        List<Map<String, Object>> list = lockOrderMapper.listByLockOrder(params);
        InputStream in = LockOrderServiceImpl.class.getResourceAsStream("/lock.xlsx");
        List<List<String>> data = this.toListData(list);
        ExcelUtil excelUtil = new ExcelUtil(in, "2007");
        excelUtil.write(0, data, true);
        excelUtil.getWorkbook().write(outputStream);
    }

    private List<List<String>> toListData(List<Map<String,Object>> list) {
        List<List<String>> res = new ArrayList<>();
        Iterator<Map<String, Object>> iterator = list.iterator();
        List<String> i;
        while (iterator.hasNext()) {
            Map<String, Object> next = iterator.next();
            i = new ArrayList<>();
            i.add(String.valueOf(next.get("order_no")));
            if (next.get("user") != null) {
                i.add(String.valueOf(((Map)next.get("user")).get("id")));
                i.add(String.valueOf(((Map)next.get("user")).get("telphone")));
            } else {
                i.add("");
                i.add("");
            }
            i.add(String.valueOf(next.get("id")));
            i.add(String.valueOf(next.get("lock_no")));
            i.add(Math.abs(Integer.valueOf(next.get("fee").toString()))/100 + "");
            i.add(String.valueOf(next.get("start_time")));
            i.add(String.valueOf(next.get("end_time")));
            i.add(Math.abs(Integer.valueOf(next.get("diff_fee").toString()))/100 + "");
            res.add(i);
        }
        return res;
    }

    @Override
    public Map<String, Object> sumByLockOrder(Map<String, Object> params) {
        return lockOrderMapper.sumByLockOrder(params);
    }
}
