package com.java.mobile.phone.pay.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/20
 */
public interface WxPayInfoMapper {

    int insert(Map<String, Object> params);

    int updateByOrderNo(Map<String, Object> params);

    Map<String, Object> getByOrderNo(@Param("out_trade_no") String orderNo);

    List<Map<String, Object>> listByWxPayInfo(Map<String, Object> params);
}
