package com.java.mobile.phone.home.service.impl;

import com.java.mobile.phone.home.service.HomeService;
import com.java.mobile.phone.lock.service.LockOrderService;
import com.java.mobile.phone.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HomeServiceImpl implements HomeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private LockOrderService lockOrderService;

    @Override
    public Map<String, Object> indexData() {
        Map<String, Object> result = new HashMap<>();
        result.put("total_user", userService.totalUser());
        result.put("total_order", lockOrderService.totalOrder());
        result.put("total_use_device", lockOrderService.totalUseDevice());
        result.put("total_time", lockOrderService.totalTime());
        LOGGER.info("查询首页数据返回：", result);
        return result;
    }


}
