package com.java.mobile.phone.user.service;

import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface FaultFeedbackService {

    Long insert(Map<String, Object> params);

    int update(Map<String, Object> params);

    PageInfo<Map<String, Object>> pageByFaultFeedback(Map<String, Object> params);


}
