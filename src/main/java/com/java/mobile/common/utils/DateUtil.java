package com.java.mobile.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final String DEFAULT = "yyyy-MM-dd HH:mm:ss";
    private static final String yyyyMMddHHmmssSSSS = "yyyyMMddHHmmssSSSS";
    private static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    private static final String yyyyMMdd = "yyyyMMdd";
    private static final String yyyyMM = "yyyyMM";

    /**
     * 默认格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateNow() {
        return getDateForPattern(DEFAULT);
    }

    public static String getDateyyyyMMddHHmmssSSSS() {
        return getDateForPattern(yyyyMMddHHmmssSSSS);
    }

    public static String getDateyyyyMMddHHmmss() {
        return getDateForPattern(yyyyMMddHHmmss);
    }

    public static String getDateyyyyMMdd() {
        return getDateForPattern(yyyyMMdd);
    }

    public static String getDateyyyyMM() {
        return getDateForPattern(yyyyMM);
    }

    public static Timestamp getDateFirst() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    public static Timestamp getDateLast() {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance();
        String mystrdate = myFormat.format(calendar.getTime());
        return Timestamp.valueOf(mystrdate);
    }

    public static Date parseToDate(String val) throws ParseException {
        Date date = null;
        if (null != val && val.trim().length() != 0 && !"null".equals(val.trim().toLowerCase())) {
            val = val.trim();
            if (val.length() > 10) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = sdf.parse(val);
            }
            if (val.length() <= 10) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(val);
            }
        }
        return date;
    }

    public static Date parseToDate(String dateStr, String format) throws ParseException {
        Date date = null;
        if (null != dateStr && dateStr.trim().length() != 0 && !"null".equals(dateStr.trim().toLowerCase())) {
            dateStr = dateStr.trim();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(dateStr);
        }
        return date;
    }

    public static String getDateForPattern(String pattern) {
        SimpleDateFormat myFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        return myFormat.format(calendar.getTime());
    }

    public static String getDateForPattern(String pattern, Date date) {
        if (StringUtils.isBlank(pattern)) pattern = yyyyMMddHHmmss;
        SimpleDateFormat myFormat = new SimpleDateFormat(pattern);
        return myFormat.format(date);
    }

    public static boolean validateDate(String pattern, String date) {
        if (StringUtils.isBlank(date)) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date time = sdf.parse(date);
            String newValue = sdf.format(time);
            return date.equals(newValue);
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean validateyMdHms(String date) {
        return validateDate("yyyyMMddHHmmss", date);
    }

    public static boolean validateyMdHmsWithSymbol(String date) {
        return validateDate("yyyy-MM-dd HH:mm:ss", date);
    }

    public static boolean validateyMdWithSymbol(String date) {
        return validateDate("yyyy-MM-dd", date);
    }

    public static boolean validateyMd(String date) {
        return validateDate("yyyyMMdd", date);
    }

    public static int calcHours(Date startTime, Date endTime) {
        long start = startTime.getTime();
        long end = endTime.getTime();
        long s = (end - start) / 1000;
        return (int) Math.ceil(s / 3600.0);
    }
}
