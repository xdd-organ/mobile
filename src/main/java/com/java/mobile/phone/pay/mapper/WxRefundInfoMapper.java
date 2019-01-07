package com.java.mobile.phone.pay.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author xdd
 * @date 2018/8/20
 */
public interface WxRefundInfoMapper {

    int insert(Map<String, Object> params);

    int updateByRefundNo(Map<String, Object> params);

    Map<String, Object> getByRefundNo(@Param("out_refund_no") String refundNo);

}
