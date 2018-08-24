package com.java.mobile.common.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SerialNumber {

    private static final Random RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 32;
    private static ThreadLocal<SimpleDateFormat> dateFormatFactory = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }
    };

    private static final char[] mm = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 长度32位
     *
     * @return
     */
    public static String getRandomNum() {
        StringBuilder sb = new StringBuilder();
        sb.append(dateFormatFactory.get().format(new Date()))
                .append(generateRandomSerial(DEFAULT_LENGTH - sb.length()));
        return sb.toString();
    }

    /**
     * 最少17位
     *
     * @param length 长度，min 17
     * @return
     */
    public static String getRandomNum(int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateFormatFactory.get().format(new Date()))
                .append(generateRandomSerial(length - sb.length()));
        return sb.toString();
    }

    /**
     * 最少17位
     *
     * @param prefix 前缀
     * @param length 长度，min 17
     * @return
     */
    public static String getRandomNum(String prefix, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(dateFormatFactory.get().format(new Date()))
                .append(generateRandomSerial(length - sb.length()));
        return sb.toString();
    }

    public static String generateRandomSerial(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(mm[RANDOM.nextInt(mm.length)]);
        }
        return sb.toString();
    }

}