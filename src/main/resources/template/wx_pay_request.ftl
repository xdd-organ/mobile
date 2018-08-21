<?xml version="1.0" encoding="UTF-8"?>
<xml>	
	<#if ((appId)?? && (appId)!='')>
		<appid>${(appId)!}</appid>
	</#if>
	<#if ((mchId)?? && (mchId)!='')>
		<mch_id>${(mchId)!}</mch_id>
	</#if>
	<#if ((nonceStr)?? && (nonceStr)!='')>
		<nonce_str>${(nonceStr)!}</nonce_str>
	</#if>
	<sign>${(sign)!}</sign>
	<#if ((body)?? && (body)!='')>
		<body>${(body)!}</body>
	</#if>
	<#if ((detail)?? && (detail)!='')>
		<detail>${(detail)!}</detail>
	</#if>
	<#if ((attach)?? && (attach)!='')>
		<attach>${(attach)!}</attach>
	</#if>
	<#if ((orderNo)?? && (orderNo)!='')>
		<out_trade_no>${(orderNo)!}</out_trade_no>
	</#if>
	<total_fee>${(totalFee)!}</total_fee>
	<#if ((spbillCreateIp)?? && (spbillCreateIp)!='')>
		<spbill_create_ip>${(spbillCreateIp)!}</spbill_create_ip>
	</#if>
	<#if ((notifyUrl)?? && (notifyUrl)!='')>
		<notify_url>${(notifyUrl)!}</notify_url>
	</#if>
	<#if ((tradeType)?? && (tradeType)!='')>
		<trade_type>${(tradeType)!}</trade_type>
	</#if>
		<#if ((prepayId)?? && (prepayId)!='')>
		<product_id>${(prepayId)!}</product_id>
	</#if>
	<#if ((limitPay)?? && (limitPay)!='')>
		<limit_pay>${(limitPay)!}</limit_pay>
	</#if>
	<#if ((openId)?? && (openId)!='')>
		<openid>${(openId)!}</openid>
	</#if>
	<#if ((authCode)?? && (authCode)!='')>
		<auth_code>${(authCode)!}</auth_code>
	</#if>
	<#if ((sceneInfo)?? && (sceneInfo)!='')>
		<scene_info>${(sceneInfo)!}</scene_info>
	</#if>
	<#if ((timeExpire)?? && (timeExpire)!='')>
		<time_expire>${(timeExpire)!}</time_expire>
	</#if>
</xml>