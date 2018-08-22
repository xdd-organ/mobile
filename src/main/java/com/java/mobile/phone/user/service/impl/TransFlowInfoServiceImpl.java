package com.java.mobile.phone.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.mobile.phone.lock.mapper.LockOrderMapper;
import com.java.mobile.phone.user.mapper.TransFlowInfoMapper;
import com.java.mobile.phone.user.service.TransFlowInfoService;
import com.java.mobile.phone.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransFlowInfoServiceImpl implements TransFlowInfoService {
    @Autowired
    private TransFlowInfoMapper transFlowInfoMapper;
    @Autowired
    private LockOrderMapper lockOrderMapper;
    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public Long saveTrans(String uid, String fee, String type, String desc, String status) {
        List<Map<String, Object>> unLockOrder = lockOrderMapper.getUnLockOrder(uid);
        if (CollectionUtils.isEmpty(unLockOrder)) {
            return null;
        } else {
            Object userId = unLockOrder.get(0).get("user_id");
            Long ret = this.insert(uid, fee, type, desc, status, userId.toString());
            return ret;
        }
    }

    @Transactional
    @Override
    public Long insert(String uid, String fee, String type, String desc, String status, String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", userId);
        params.put("fee", type);
        params.put("desc", desc);
        params.put("status", status);
        params.put("user_id", userId);
        params.put("insert_author", userId);
        params.put("update_author", userId);
        userService.updateMoney(userId.toString(), Integer.valueOf(fee));
        transFlowInfoMapper.insert(params);
        return Long.valueOf(params.get("id").toString());
    }

    @Override
    public PageInfo pageByTransFlowInfo(Map<String, Object> params) {
        PageHelper.startPage(Integer.valueOf(params.get("pageNum").toString()), Integer.valueOf(params.get("pageSize").toString()));
        return new PageInfo(transFlowInfoMapper.listByTrans(params));
    }
}