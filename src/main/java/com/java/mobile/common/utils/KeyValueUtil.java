package com.java.mobile.common.utils;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class KeyValueUtil {

    private static final Pattern PATTERN = Pattern.compile("^(\\S+?=(.|\\n)*&)+\\S+=(.|\\n)*$");

    public static boolean isKeyValueString(String str) {
        return  PATTERN.matcher(str).matches();
    }

    private static int STATUS_KEY = 1;
    private static int STATUS_SIMPLEVALUE = 2;
    private static int STATUS_COMPLEXVALUE = 4;

    public static SortedMap<String, String> keyValueStringToMap(String keyValueString) {
        if (!StringUtils.hasText(keyValueString)) {
            return null;
        }

        StringBuilder sb = new StringBuilder(keyValueString.trim());
        if (sb.charAt(0) == '{') {
            sb.deleteCharAt(0);
        }
        if (sb.charAt(sb.length() - 1) == '}') {
            sb.deleteCharAt(sb.length() - 1);
        }

        SortedMap<String, String> map = new TreeMap<String, String>();

        int currentIndex = 0;
        String key = null;
        String value = null;

        int status = STATUS_KEY;

        for (int i = 0; i < sb.length(); ++i) {
            char c = sb.charAt(i);
            // 状态转换
            if (status == STATUS_KEY && c == '=') {
                status = STATUS_SIMPLEVALUE;
                key = sb.substring(currentIndex, i);
                currentIndex = i + 1;
            } else if (status == STATUS_SIMPLEVALUE && c == '&') {
                status = STATUS_KEY;
                value = sb.substring(currentIndex, i);
                map.put(key, value);
                currentIndex = i + 1;
            } else if (status == STATUS_SIMPLEVALUE && c == '{') {
                status = STATUS_COMPLEXVALUE;
            } else if (status == STATUS_COMPLEXVALUE && c == '}') {
                status = STATUS_SIMPLEVALUE;
            }
        }
        value = sb.substring(currentIndex, sb.length());
        map.put(key, value);

        return map;
    }

    /**
     * 将Map中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式
     * 
     * @param map
     * @return
     */
    public static String mapToString(Map<String, String> map) {
        SortedMap<String, String> sortedMap = new TreeMap<String, String>(map);

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (!StringUtils.hasText(entry.getValue())) {
                continue;
            }
            sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.length() == 0 ? "" : sb.toString();
    }

}
