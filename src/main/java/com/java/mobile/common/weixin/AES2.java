package com.java.mobile.common.weixin;

import org.apache.catalina.util.HexUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class AES2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(AES2.class);

    public static byte[] key = {32,87,47,82,54,75,63,71,48,80,65,88,17,99,45,43};
//    public static byte[] key = new byte[]{0x3A, 0x60, 0x43, 0x2A, 0x5C, 0x01, 0x21, 0x1F,
//            0x29, 0x1E, 0x0F, 0x4E, 0x0C, 0x13, 0x28, 0x25};

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
        byte[] convert3 = HexUtils.convert("ec72214cb09cd11b52fe73e901d8dc48");
        byte[] convert2 = HexUtils.convert("b8c892046c380b3f1fab893b88de6a3a00000000");
        System.out.println(Arrays.toString(convert2));

        byte[] bytes2 = {-20, 114, 33, 76, -80, -100, -47, 27, 82, -2, 115, -23, 1, -40, -36, 72};
//        byte[] bytes211 = {0x07, 0x9e, 0xf6, 0x01, 0x23, 0xb2, 0xe3,
//                0x2f, 0x6f, 0x9b, 0x19, 0xce, 0x29, 0x34, 0x0d, 0x02, 0x00, 0x00, 0x00, 0x00};
        byte[] decrypt = decrypt(bytes2, bytes1);
        System.out.println(Arrays.toString(decrypt));

    }

    @Test
    public void testtt() {
        byte[] convert2 = HexUtils.convert("b8");
        System.out.println(Arrays.toString(convert2));

        int a = 184;
        String s = Integer.toHexString(a);
        System.out.println(s);
    }

    @Test
    public void test2() {
        byte[] a = {-20, 114, 33, 76, -80, -100, -47, 27, 82, -2, 115, -23, 1, -40, -36, 72};
        String s = bytes2HexString(a);
        System.out.println(s);

        byte b = 72;
        String hex = Integer.toHexString(b);
        System.out.println(hex);

        String aa = "fe";
        int i = Integer.parseInt(aa, 16);
        System.out.println(i);
    }

    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        } return ret;
    }

    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
    public static byte[] toBytes2(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 10);
        }

        return bytes;
    }

    /**
     * 方法一：
     * byte[] to hex string
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexFun1(byte[] bytes) {
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};



    public static boolean verify(String plainText, String userKey, String signature) {
        try {
            LOGGER.info("加密内容：{}, userKey:{}, signature:{}", plainText, userKey, signature);
            String signedStr = signMd5(plainText + userKey);
            LOGGER.info("加密结果：{}", signedStr);
            return signature.equalsIgnoreCase(signedStr);
        } catch (Exception e) {
            LOGGER.error("验签异常", e);
            return false;
        }
    }

    public static boolean verifyByMap(Map<String, Object> plainTextMap, String userKey, String signature) {
        try {
            String plainText = mapToString(plainTextMap);
            boolean signedStr = verify(plainText, userKey, signature);
            return signedStr;
        } catch (Exception e) {
            LOGGER.error("验签异常", e);
            return false;
        }
    }

    public static String signMd5(String plainText) throws UnsupportedEncodingException {
        byte[] inputByteArr = plainText.getBytes("UTF-8");
        String signedStr = DigestUtils.md5Hex(inputByteArr).toUpperCase();
        return signedStr;
    }

    public static String mapToString(Map<String, Object> plainTextMap)  {
        final StringBuilder sb = new StringBuilder();
        Collection<Object> values = plainTextMap.values();
        for (Object value : values) {
            sb.append(value);
        }
        return sb.toString();
    }

}