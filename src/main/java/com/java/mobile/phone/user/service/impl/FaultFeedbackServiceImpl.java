package com.java.mobile.phone.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.java.mobile.phone.user.mapper.FaultFeedbackMapper;
import com.java.mobile.phone.user.service.FaultFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FaultFeedbackServiceImpl implements FaultFeedbackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaultFeedbackServiceImpl.class);

    @Autowired
    private FaultFeedbackMapper faultFeedbackMapper;


    @Override
    public Long insert(Map<String, Object> params) {
        int insert = faultFeedbackMapper.insert(params);
        return Long.valueOf(params.get("id").toString());
    }

    @Override
    public int update(Map<String, Object> params) {
        return faultFeedbackMapper.update(params);
    }

    @Override
    public PageInfo<Map<String, Object>> pageByFaultFeedback(Map<String, Object> params) {
        PageHelper.startPage(Integer.valueOf(params.get("pageNum").toString()), Integer.valueOf(params.get("pageSize").toString()));
        return new PageInfo(faultFeedbackMapper.listByFaultFeedback(params));
    }
}
