package com.java.mobile.phone.dictionary.service;

import java.util.List;
import java.util.Map;

public interface DictionaryService {
    Long insert(Map<String,Object> params);

    Map<String,Object> getByKey(String key);

    int getPrice(String lockNo);

    int updateByKey(Map<String,Object> params);

    List<Map<String,Object>> listByDictionary(Map<String,Object> params);
}
