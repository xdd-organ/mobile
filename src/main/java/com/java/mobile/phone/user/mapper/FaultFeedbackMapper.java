package com.java.mobile.phone.user.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/21
 */
public interface FaultFeedbackMapper {

    int insert(Map<String, Object> params);

    int update(Map<String, Object> params);

    List<Map<String, Object>> listByFaultFeedback(Map<String, Object> params);

}
