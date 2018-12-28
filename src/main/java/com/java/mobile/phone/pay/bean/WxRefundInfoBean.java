package com.java.mobile.phone.pay.bean;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 微信退款参数实体bean
 *
 * */
public class WxRefundInfoBean implements Serializable{

	private static final long serialVersionUID = 7971964792351542253L;
	/**
	 * 应用ID
	 * */
	private String appId;
	/**
	 * 商户号
	 * */
	private String mchId;
	/**
	 * 随机字符串
	 * */
	private String nonceStr;
	/**
	 * 微信生成的订单号，在支付通知中有返回
	 * */
	private String transactionId;
	/**
	 * 商户侧传给微信的订单号
	 * */
	private String outTradeNo;
	/**
	 * 商户退款单号
	 * */
	private String outRefundNo;
	/**
	 * 总金额
	 * */
	private String totalFee;
	/**
	 * 退款金额
	 * */
	private String refundFee;
	/**
	 * 操作员
	 * */
	private String opUserId;
	/**
	 * 返回状态码
	 * */
	private String returnCode;
	/**
	 * 返回信息
	 * */
	private String returnMsg;
	/**
	 * 业务结果
	 * */
	private String resultCode;
	/**
	 * 错误代码
	 * */
	private String errCode;
	/**
	 * 错误代码描述
	 * */
	private String errCodeDes;
	/**
	 * 微信退款单号
	 * */
	private String refundId;
	/**
	 * 现金支付金额，单位为分，只能为整数，详见支付金额
	 * */
	private String cashFee;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public String getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(String opUserId) {
		this.opUserId = opUserId;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getCashFee() {
		return cashFee;
	}

	public void setCashFee(String cashFee) {
		this.cashFee = cashFee;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ":" + JSONObject.toJSONString(this);
	}

}
