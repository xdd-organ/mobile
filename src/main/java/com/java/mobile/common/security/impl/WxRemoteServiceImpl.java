package com.java.mobile.common.security.impl;

import com.java.mobile.common.security.WxRemoteService;
import com.java.mobile.common.utils.Base64Util;
import com.java.mobile.common.utils.KeyValueUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

/**
 * 微信签名验签实现 给予MD5方式
 */
@Component("wxRemoteServiceImpl")
public class WxRemoteServiceImpl implements WxRemoteService {

	private static final Logger logger = LoggerFactory.getLogger(WxRemoteServiceImpl.class);
	@Value("${weixin.key}")
	private String key;


	public String signMd5(final String mchId, final String plainText) throws IllegalArgumentException {
		logger.info(String.format("调用微信MD5签名,mchId [%s],原文：[%s]", mchId, plainText));
		Map<String, String> dataMap = KeyValueUtil.keyValueStringToMap(plainText);
		String signedStr = signMd5ByMap(mchId, dataMap);
		return signedStr;
	}

	@Override
	public boolean verifyMd5(String mchId, String plainText, String signature) throws IllegalArgumentException {
		logger.info(
				String.format("调用微信MD5验签,mchId [%s],原文：[%s],签名:[%s]", mchId, plainText, signature));
		return signMd5(mchId, plainText).equals(signature);
	}

	@Override
	public String signMd5ByMap(String mchId, Map<String, String> plainText) throws IllegalArgumentException {
		logger.info(String.format("WxRemoteServiceImpl|调用微信MD5签名,mchId [%s],原文：[%s]", mchId, plainText));
//		WxAccount account = wxAccountContainer.getWxAccountMap().get(mchId);
//		if (account == null) {
//			logger.warn(String.format("WxRemoteServiceImpl|参数：mchId [%s]非法,plainText [%s]", mchId, plainText), null,
//					null);
//			throw new IllegalArgumentException("mchId(" + mchId + ")非法");
//		}
//		if (plainText == null) {
//			logger.warn(String.format("WxRemoteServiceImpl|参数：mchId [%s],plainText [%s]非法", mchId, plainText), null,
//					null);
//			throw new IllegalArgumentException("plainText(" + plainText + ")非法");
//		}
		plainText.remove("sign");
		plainText.remove("sign_type");
		String sortedStr = KeyValueUtil.mapToString(plainText);
//		String conbimedStr = sortedStr + "&key=" + account.getMd5Key();
		String conbimedStr = sortedStr + "&key=" + key;
		String signedStr = "";
		try {
			byte[] inputByteArr = conbimedStr.getBytes("UTF-8");
			signedStr = DigestUtils.md5Hex(inputByteArr).toUpperCase();
			logger.info(String.format("微信MD5签名完成,mchId [%s],原文：[%s],签名:[%s]", mchId, plainText,
					signedStr));
		} catch (Exception e) {
			logger.error(String.format("WxRemoteServiceImpl|参数：mchId [%s],plainText [%s]MD5签名失败", mchId, plainText), e);
		}
		return signedStr;
	}

	@Override
	public boolean verifyMd5ByMap(String mchId, Map<String, String> plainText, String signature)
			throws IllegalArgumentException {
		logger.info(
				String.format("调用微信MD5验签,mchId [%s],原文：[%s],签名:[%s]", mchId, plainText, signature));
		return signMd5ByMap(mchId, plainText).equals(signature);
	}


	/**
	 * 对密文ciphertext进行base64解码,再进行SHA-256-EBC解密取得明文
	 */
	@Override
	public String decryption(String ciphertext, String mchId) throws Exception {
		byte[] b = Base64Util.decode(ciphertext);
//		WxAccount account = wxAccountContainer.getWxAccountMap().get(mchId);
//		String md5Key=DigestUtils.md5Hex(account.getMd5Key()).toLowerCase();
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return new String(cipher.doFinal(b),"UTF-8").trim();
	}

	/**
	 * 对明文plaintext进行SHA-256-EBC加密再进行base64编码
	 */
	@Override
	public String encrypt(String plaintext, String mchId) throws Exception {
//		WxAccount account = wxAccountContainer.getWxAccountMap().get(mchId);
//		String md5Key=DigestUtils.md5Hex(account.getMd5Key()).toLowerCase();
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
		return Base64Util.encode(ciphertext);
	}

}
