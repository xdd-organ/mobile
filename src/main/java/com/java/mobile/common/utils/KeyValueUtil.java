package com.java.mobile.common.utils;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * {@code KeyValutUtil}主要用于处理类似"key=value&key=value"的字符串
 * 
 * <p>
 * {@code KeyValueUtil}主要提供将key-value字符串转换成{@link Map}的功能{@link #keyValueStringToMap(String)
 * keyValueStringToMap} 和将{@link Map}转换成key-value的功能{@link #mapToString(Map)
 * mapToString}
 * </p>
 * 
 * <p>
 * <Strong>设计思路：</Strong>Map转换成key-value字符串时，一次取出每个实体(Entity)，将key与value用“=”连接，每个实体间用“&”连接，
 * 组成如：key1=value1&key2=value2的字符串。 ​
 * key-value转字符串的时候需要区分含有value子串的形式：key1=value1&key2={key21=value21&key22=value22}，
 * 设计思路对key-value字符串逐个字符进行处理，利用状态机判断当前状态为key还是value。
 * </p>
 * 
 * @author wuhang
 * @since xpay-common 0.0.1
 */
public class KeyValueUtil {

    private static final Pattern PATTERN = Pattern.compile("^(\\S+?=(.|\\n)*&)+\\S+=(.|\\n)*$");

    public static boolean isKeyValueString(String str) {
        return  PATTERN.matcher(str).matches();
    }

    /**
     * 识别字符串状态机转换：<br/>
     * STATUS_KEY --[=]--> STATUS_SIMPLEVALUE <br/>
     * STATUS_SIMPLEVALUE --[&]--> STATUS_KEY <br/>
     * STATUS_SIMPLEVALUE --[{]--> STATUS_COMPLEXVALUE <br/>
     * STATUS_COMPLEXVALUE --[}]--> STATUS_SIMPLEVALUE <br/>
     * STATUS_COMPLEXVALUE --[=]--> STATUS_COMPLEXVALUE <br/>
     * STATUS_COMPLEXVALUE --[&]--> STATUS_COMPLEXVALUE
     */
    private static int STATUS_KEY = 1;
    private static int STATUS_SIMPLEVALUE = 2;
    private static int STATUS_COMPLEXVALUE = 4;

    /**
     * 将key1=value1&key2=value2形式的字符串转转换为一个排序的map<br>
     * 此方法忽略字符串前后可能存在的"{}"字符<br>
     * 样例字符串：{accessType=0&bizType=000201&currencyCode=156&encoding=UTF-8&
     * issuerIdentifyMode=0&merId=777290058110048&orderId=20160317150838&
     * origRespCode=00&origRespMsg=成功[0000000]&payCardType=01&queryId=
     * 201603171508382661928&reqReserved={a=aaa&b=bbb&c=ccc}&respCode=00&respMsg
     * =成功[0000000]&settleAmt=10000&settleCurrencyCode=156&settleDate=0317&
     * signMethod=01&traceNo=266192&traceTime=0317150838&txnAmt=10000&txnSubType
     * =01&txnTime=20160317150838&txnType=01&version=5.0.0&certId=68759585097&
     * signature=EpwPj3OIQgCmr9FfdJIs/dYG+
     * CVnYOm9JwoC4dyaEjtgdSCzRNyWGOCbToHs5sAbVfjqSUi/o3ctqAaOJEyMEIdbZt+
     * hVQcWDmUovQs6ruQM5VN0tNdRsR+QANo1f1LYNs6q89UhGo+OIpFMMB+jdb2Sg54XFH++
     * ywqXoL0WCWWwtzeu2Haqq8LM5P1j4p0FqrAYuEI58zy40g/T4S+
     * eTBrZZx8MGGNcAQDMsk2IEsuEa1IVzzAIW5ZvsG2Ypf74DJpPEGMgzInKUyC1+BblJ/
     * oYGIRQyeYan0jd/7nZuvTB5nmoTdSgSsPZlnuSsPvHP+BK48MyrvsWRJXH983VFw==}
     * 
     * @param keyValueString
     * @return
     */
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
