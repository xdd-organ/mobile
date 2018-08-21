package com.java.mobile.phone.user.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
public interface TransFlowInfoMapper {

    int insert(Map<String, Object> params);

    List<Map<String, Object>> listByTrans(Map<String, Object> params);
}
