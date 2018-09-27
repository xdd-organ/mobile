package com.java.mobile.common.weixin;

import org.apache.catalina.util.HexUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class AES2 {
	private static final Logger LOGGER = LoggerFactory.getLogger(AES2.class);

	// 加密
	public static byte[] encrypt(byte[] sSrc, byte[] sKey) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//"算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(sSrc);
			return encrypted;//此处使用BASE64做转码功能，同时能起到2次加密的作用。
		} catch (Exception ex) {
			LOGGER.error("加密异常", ex);
			return null;
		}
	}

	// 解密
	public static byte[] decrypt(byte[] sSrc, byte[] sKey) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] dncrypted = cipher.doFinal(sSrc);
			return dncrypted;
		} catch (Exception ex) {
			LOGGER.error("解密异常", ex);
			return null;
		}
	}

	@Test
	public void test() throws UnsupportedEncodingException {
//		byte[] bytes = "060101012D1A683D48271A18316E471A".getBytes();
//		byte[] bytes1 = "3A60432A5C01211F291E0F4E0C132825".getBytes();


		byte[] bytes = new byte[]{0x06, 0x01, 0x01, 0x01, 0x2D, 0x1A, 0x68, 0x3D,
								  0x48, 0x27, 0x1A, 0x18, 0x31, 0x6E, 0x47, 0x1A};
		byte[] bytes3 = new byte[]{6, 1, 1, 1, 45, 26, 104, 61,
								  72, 39, 26, 24, 49, 110, 71, 26};

		byte[] bytes1 = new byte[]{0x3A, 0x60, 0x43, 0x2A, 0x5C, 0x01, 0x21, 0x1F,
								   0x29, 0x1E, 0x0F, 0x4E, 0x0C, 0x13, 0x28, 0x25};

		byte[] bytes4 = new byte[]{58, 96, 67, 42, 92, 1, 33, 31,
								   41, 30, 15, 78, 12, 19, 40, 37};

//		byte[] bytes2 = new byte[]{165, 0x60, 0x43, 0x2A, 0x5C, 0x01, 0x21, 0x1F,
//								   0x29, 0x1E, 0x0F, 0x4E, 0x0C, 0x13, 0x28, 0x25};


		byte[] encrypt1 = encrypt(bytes, bytes1);

		System.out.println(Arrays.toString(encrypt1));
		String convert = HexUtils.convert(encrypt1);
		System.out.println(convert);
		byte[] convert2 = HexUtils.convert("ec72214cb09cd11b52fe73e901d8dc48");
		System.out.println(Arrays.toString(convert2));

		byte[] bytes2 = {-20, 114, 33, 76, -80, -100, -47, 27, 82, -2, 115, -23, 1, -40, -36, 72};
		byte[] decrypt = decrypt(bytes2, bytes1);
		System.out.println(Arrays.toString(decrypt));

	}

	@Test
	public void test2() {
		Integer a = 72;
		String hex = a.toHexString(a);
		System.out.println(hex);
	}

}