package com.java.mobile.common.security;

import java.util.Map;

public interface WxRemoteService {

	String signMd5(String mchId, String plainText) throws IllegalArgumentException;

	boolean verifyMd5(String mchId, String plainText, String signature) throws IllegalArgumentException;
	
	String signMd5ByMap(String mchId, Map<String, String> plainText) throws IllegalArgumentException;

	boolean verifyMd5ByMap(String mchId, Map<String, String> plainText, String signature) throws IllegalArgumentException;

	String decryption(String ciphertext, String mchId) throws Exception;
	
	String encrypt(String plaintext, String mchId) throws Exception;
}
