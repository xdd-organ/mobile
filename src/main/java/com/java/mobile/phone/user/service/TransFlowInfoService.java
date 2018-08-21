package com.java.mobile.phone.user.service;

import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface TransFlowInfoService {

    Long saveTrans(String uid, String fee, String type, String desc, String status);

    Long insert(String uid, String fee, String type, String desc, String status, String userId);

    PageInfo pageByTransFlowInfo(Map<String,Object> params);
}
