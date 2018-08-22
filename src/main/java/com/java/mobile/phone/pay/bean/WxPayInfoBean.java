package com.java.mobile.phone.pay.bean;

import java.io.Serializable;

/**
 * 微信支付参数信息
 * 
 *
 */
public class WxPayInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 交易请求日期 YYYYMMDD
	 */
	private String reqDate;
	/**
	 * 外部商户提交给统一支付的订单号
	 */
	private String orderNo;
	/**
	 * 统一支付内部流水 
	 */
	private String upayTransId;

	/**
	 * 随机字符串
	 */
	private String nonceStr;
	

	/**
	 * 公众账号ID
	 */
	private String appId;
	/**
	 * 微信商户号
	 */
	private String mchId;
	/**
	 * 商品描述
	 */
	private String body;
	/**
	 * 商品详情
	 */
	private String detail;
	/**
	 * 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	 */
	private String attach;
	/**
	 * 订单总金额，单位为分
	 */
	private String totalFee;
	/**
	 * 终端IP，APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
	 */
	private String spbillCreateIp;
	/**
	 * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	 */
	private String notifyUrl;
	/**
	 * 交易类型 JSAPI、NATIVE、APP、MICROPAY
	 */
	private String tradeType;
	/**
	 * 商品ID
	 */
	private String productId;
	/**
	 * 指定支付方式
	 */
	private String limitPay;
	/**
	 * 用户在商户appid下的唯一标识 trade_type=JSAPI，此参数必传
	 */
	private String openId;

	private String transactionId;
	/**
	 * 刷卡支付授权码
	 */
	private String authCode;
	
	/**
	 * mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付,mweb_url的有效期为5分钟
	 */
	private String mwebUrl;
	/**
	 * 场景信息
	 */
	private String sceneInfo;

	/**
	 * 返回状态码SUCCESS/FAIL 
	 */
	private String returnCode;
	/**
	 * 返回信息，如非空，为错误原因
	 */
	private String returnMsg;
	/**
	 * 业务结果 SUCCESS/FAIL
	 */
	private String resultCode;
	/**
	 * 错误代码
	 */
	private String errCode;
	/**
	 * 错误代码描述
	 */
	private String errCodeDes;

	/**
	 * 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	 */
	private String prepayId;
	/**
	 * 二维码链接
	 */
	private String codeUrl;

	/**
	 * 账期日，对账时候需要
	 */
	private String settleDate;
	
	/**
	 * 订单失效时间，格式为yyyyMMddHHmmss
	 */
	private String timeExpire;
	

	public String gainActivityCode() {
		return null;
	}
	
	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String gainReqSys() {
		return null;
	}
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransActionId(String transActionId) {
		this.transactionId = transActionId;
	}
	public String getReqDate() {
		return reqDate;
	}

	public void setReqDate(String reqDate) {
		this.reqDate = reqDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUpayTransId() {
		return upayTransId;
	}

	public void setUpayTransId(String upayTransId) {
		this.upayTransId = upayTransId;
	}

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

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getLimitPay() {
		return limitPay;
	}

	public void setLimitPay(String limitPay) {
		this.limitPay = limitPay;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public String getSettleDate() {
		return settleDate;
	}

	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}

	public String getMwebUrl() {
		return mwebUrl;
	}

	public void setMwebUrl(String mwebUrl) {
		this.mwebUrl = mwebUrl;
	}

	public String getSceneInfo() {
		return sceneInfo;
	}

	public void setSceneInfo(String sceneInfo) {
		this.sceneInfo = sceneInfo;
	}

	public void packStatus() {
		if (!"SUCCESS".equals(returnCode)) {
			returnCode = "FAIL";
			resultCode = null;
		}
	}

	public String getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}

}
